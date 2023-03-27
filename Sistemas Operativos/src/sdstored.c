#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include "./includes/pedido.h"

int flag_sigterm = 1;
char transf_folder[128] = "";
int task_number = 0;
pid_t pid_le_pedidos;

typedef struct pedido_queue PEDIDO_QUEUE;
typedef struct tasks TASKS;

struct pedido_queue
{
	PEDIDO_ pedido;
	PEDIDO_QUEUE *next;
};

typedef struct processos_
{
	TASKS *tasks;
	PEDIDO_QUEUE *pedido_queue[6]; // pedido_queue :: queue de pedidos
	char bin_transforms[7][64];	   // executaveis onde andam estao as trasnformacoes
	int n_procs[7];				   // n_proc :: numero de processos a correr atualmente
	int max_proc[7];			   // max_proc :: maximo de processos de cada tipo
} PROCESSOS_;

struct tasks
{
	int task_number;
	int id;
	int pid;
	int priority;
	char in_output[128];
	char trasnf[128];
	int task[7];
	TASKS *next;
};

void signal_handler()
{
	flag_sigterm = -1;
	if (pid_le_pedidos != 0)
		kill(pid_le_pedidos, SIGTERM);
}

void parse_config_file(const char *filename, int *max_proc)
{
	char config_file[256] = "";
	char *temp = config_file, *token;
	int file = open(filename, O_RDONLY, 0666);
	read(file, config_file, sizeof(config_file));
	for (int i = 0; i < 7; i++)
	{
		while ((*temp) < '0' || (*temp) > '9')
			temp++;
		token = temp;
		while (*temp != '\n')
			temp++;
		*temp = '\0';
		temp++;
		max_proc[i] = atoi(token);
	}
	close(file);
}

PROCESSOS_ parse_trasnf_file(const char *filename, PROCESSOS_ processos_)
{
	for (int i = 0; i < 7; i++)
		sprintf(processos_.bin_transforms[i], "%s/%s", filename, types_transform[i]);
	return processos_;
}

TASKS *add_task(TASKS task_add, TASKS *tasks)
{
	TASKS *tmp = tasks;
	if (tmp != NULL)
	{
		while (tmp->next != NULL)	tmp = tmp->next;
		TASKS *task_ = (TASKS*)malloc(sizeof(TASKS));
		*task_ = task_add;
		task_->next = NULL;
		tmp->next = task_;
	}
	else
	{
		TASKS *task_ = (TASKS*)malloc(sizeof(TASKS));
		*task_ = task_add;
		task_->next = NULL;
		tasks = task_;
	}
	return tasks;
}

TASKS *remove_pedido_task(int id, TASKS *tasks)
{
	TASKS *prev = tasks;
	TASKS *tmp = tasks;
	while (tmp && tmp->id != id)
	{
		prev = tmp;
		tmp = tmp->next;
	}

	if (tmp)
	{
		if (prev == tmp)
			tasks = tmp->next;
		else
			prev->next = tmp->next;
		free(tmp);
	}
	return tasks;
}

void remove_all_tasks(TASKS *tasks)
{
	TASKS *prev = tasks;
	TASKS *tmp;
	if (prev != NULL)
	{
		if (prev->next != NULL)
		{
			tmp = prev->next;
			while (tmp->next)
			{
				free(prev);
				prev = tmp;
				tmp = tmp->next;
			}
			free(prev);
			free(tmp);
		}
		else
			free(prev);
	}
}

PROCESSOS_ add_pedido_queue(PEDIDO_ pedido, PROCESSOS_ processos_)
{
	PEDIDO_QUEUE *tmp = processos_.pedido_queue[pedido.priority];
	if (tmp != NULL)
	{
		while (tmp->next != NULL) tmp = tmp->next;

		PEDIDO_QUEUE *tmp_p_q = (PEDIDO_QUEUE*)malloc(sizeof(PEDIDO_QUEUE));
		tmp_p_q->next = NULL;
		tmp_p_q->pedido = pedido;
		tmp->next = tmp_p_q;
	}
	else
	{
		PEDIDO_QUEUE *tmp_p_q = (PEDIDO_QUEUE*)malloc(sizeof(PEDIDO_QUEUE));
		tmp_p_q->next = NULL;
		tmp_p_q->pedido = pedido;
		processos_.pedido_queue[pedido.priority] = tmp_p_q;
	}
	char srv_to_cliente[128] = "";
	sprintf(srv_to_cliente, "./pipes/%d", pedido.id);
	int write_to_client = open(srv_to_cliente, O_WRONLY, 0666);
	strcpy(srv_to_cliente, "pending\n");
	write(write_to_client, srv_to_cliente, sizeof(srv_to_cliente));
	close(write_to_client);
	return processos_;
}

// PEDIDO_QUEUE *remove_pedido_queue(PEDIDO_ *pedido, PEDIDO_QUEUE *pedido_queue)
// {
// 	PEDIDO_QUEUE *tmp = pedido_queue;
// 	if (tmp)
// 	{
// 		*pedido = tmp->pedido;
// 		pedido_queue = tmp->next;
// 		free(tmp);
// 	}
// 	else
// 		return NULL;
// 	return pedido_queue;
// }

PEDIDO_QUEUE *remove_pedido_queue(PEDIDO_ *pedido, PEDIDO_QUEUE *pedido_queue, int *i)
{
	PEDIDO_QUEUE *tmp = pedido_queue, *prev = pedido_queue;
	while (*i && tmp)
	{
		prev = tmp;
		tmp = tmp->next;
		(*i)--;
	}

	if (tmp && tmp == prev)
	{
		*pedido = tmp->pedido;
		pedido_queue = tmp->next;
		free(tmp);
	}
	else if (!tmp)
		(*i) = -1;
	else
	{
		*pedido = tmp->pedido;
		prev->next = tmp->next;
		free(tmp);
	}

	return pedido_queue;
}

PEDIDO_QUEUE **remove_all(int i, PEDIDO_QUEUE *pedido_queue[])
{
	PEDIDO_QUEUE *prev = pedido_queue[i];
	pedido_queue[i] = NULL;
	PEDIDO_QUEUE *tmp;
	if (prev != NULL)
	{
		if (prev->next != NULL)
		{
			tmp = prev->next;
			while (tmp->next)
			{
				free(prev);
				prev = tmp;
				tmp = tmp->next;
			}
			free(prev);
			free(tmp);
		}
		else
			free(prev);
	}
	return pedido_queue;
}

void le_pedidos(int pipe_read_client_server[2], int max_procs[7])
{
	close(pipe_read_client_server[0]);
	PEDIDO_ pedido;
	int pipe_client_server = open(PIPE_CLIENT_SRV, O_RDONLY, 0666); // pipe_client_server :: pipe para comunicar com os clientes
	while (flag_sigterm != -1)
	{
		int read_bytes = read(pipe_client_server, &pedido, sizeof(pedido));
		if (read_bytes)
		{
			int i;
			int n_procs_tmp[7] = {0, 0, 0, 0, 0, 0, 0};
			for (i = 0; i < pedido.n_transf; i++)
				n_procs_tmp[pedido.id_tranf[i]]++;
			for (i = 0; i < 7; i++)
			{
				if (n_procs_tmp[i] > max_procs[i])
				{
					char buffer[128] = "";
					sprintf(buffer, "./pipes/%d", pedido.id);
					int write_to_client = open(buffer, O_WRONLY, 0666);
					strcpy(buffer, "nao e possivel processar o pedido por falta de recursos\n");
					write(write_to_client, buffer, sizeof(buffer));
					close(write_to_client);
					i = 10;
				}
			}
			if (i < 10)
				write(pipe_read_client_server[1], &pedido, sizeof(pedido));
		}
		else
		{
			pedido.id = -1;
			pedido.priority = 0;
			write(pipe_read_client_server[1], &pedido, sizeof(pedido));
		}
	}
	close(pipe_client_server);
	close(pipe_read_client_server[1]);
	exit(0);
}

PROCESSOS_ libertar_recursos(PROCESSOS_ processos_, TASKS *task_tmp)
{
	for (int i = 0; i < 7; i++)
		processos_.n_procs[i] -= task_tmp->task[i];
	processos_.tasks = remove_pedido_task(task_tmp->id, processos_.tasks);
	return processos_;
}

PROCESSOS_ atualiza_estado(PROCESSOS_ processos_)
{
	int status;
	int pid = waitpid(-1, &status, WNOHANG);
	while (pid > 0)
	{
		TASKS *task_tmp = processos_.tasks;
		while (task_tmp && (task_tmp->id != pid))
			task_tmp = task_tmp->next;

		if (task_tmp != NULL)
			processos_ = libertar_recursos(processos_, task_tmp);
		pid = waitpid(-1, &status, WNOHANG);
	}
	return processos_;
}

void executa_pedidos(PEDIDO_ pedido, char bin_transforms[7][64])
{
	char buffer[128] = "";
	sprintf(buffer, "./pipes/%d", pedido.id);
	int write_to_client = open(buffer, O_WRONLY, 0666);
	strcpy(buffer, "processing\n");
	write(write_to_client, buffer, sizeof(buffer));
	close(write_to_client);

	int fd_input_file = open(pedido.input_file, O_RDONLY, 0666);
	int fd_ouput_file = open(pedido.output_file, O_CREAT | O_TRUNC | O_WRONLY, 0666);

	dup2(fd_input_file, 0);
	dup2(fd_ouput_file, 1);

	char pipe_char[2] = "|";
	int i;
	char *argv[pedido.n_transf * 2];
	for (i = 0; i < pedido.n_transf * 2 - 1; i++)
	{
		if (i % 3 == 2)
			argv[i] = bin_transforms[pedido.id_tranf[i / 3]];
		else if (i % 3 == 0)
			argv[i] = types_transform[pedido.id_tranf[i / 3]];
		else if (i % 3 == 1)
			argv[i] = pipe_char;
	}
	argv[i] = NULL;

	pid_t pid = fork();
	if (pid == 0)
		execv(bin_transforms[pedido.id_tranf[0]], argv);
	wait(NULL);

	long bytes_read = lseek(fd_input_file, 0, SEEK_END);
	long bytes_write = lseek(fd_ouput_file, 0, SEEK_END);

	sprintf(buffer, "./pipes/%d", pedido.id);
	write_to_client = open(buffer, O_WRONLY, 0666);
	sprintf(buffer, "concluded (bytes-input: %ld, bytes-output: %ld)\n", bytes_read, bytes_write);
	write(write_to_client, buffer, sizeof(buffer));
	close(write_to_client);
	_exit(0);
}

TASKS parse_for_status(TASKS task, PEDIDO_ pedido)
{
	for (int i = 0; i < 128; i++)	task.in_output[i] = task.trasnf[i] = 0;

	sprintf(task.in_output, "%s %s", pedido.input_file, pedido.output_file);

	char *tmp = task.trasnf;
	for (int i = 0; i < pedido.n_transf; i++)
	{
		sprintf(tmp, "%s ", types_transform[pedido.id_tranf[i]]);
		while (*tmp)	tmp++;
	}
	return task;
}

int verifica_viabilidade(PROCESSOS_ processos_, PEDIDO_ pedido)
{
	int r = 0, i = 0;
	PEDIDO_QUEUE *tmp = processos_.pedido_queue[pedido.priority];
	while (tmp)
	{
		pedido = tmp->pedido;
		TASKS tasks;
		for (int i = 0; i < 7; i++)
			tasks.task[i] = 0;
		for (int i = 0; i < pedido.n_transf; i++)
			tasks.task[pedido.id_tranf[i]]++;
		for (int i = 0; i < 7; i++)
		{
			if (tasks.task[i] > processos_.max_proc[i] - processos_.n_procs[i])
			{
				i = 7;
				r++;
			}
		}
		tmp = tmp->next;
		if (i == r)
			tmp = NULL;
		i++;
	}
	if (i == r)
		r = -1;
	
	return r;
}

PROCESSOS_ asd(PROCESSOS_ processos_, int j) // ja n sei q chamar as funcoes
{
	processos_ = atualiza_estado(processos_);
	int n = 0;
	PEDIDO_ pedido;
	pedido.priority = j;

	n = verifica_viabilidade(processos_, pedido);
	if (n != -1)
	{
		processos_.pedido_queue[j] = remove_pedido_queue(&pedido, processos_.pedido_queue[j], &n);
		TASKS tasks;
		for (int i = 0; i < 7; i++)
			tasks.task[i] = 0;
		for (int i = 0; i < pedido.n_transf; i++)
			tasks.task[pedido.id_tranf[i]]++;
		for (int i = 0; i < 7; i++)
			processos_.n_procs[i] += tasks.task[i];
		tasks.task_number = task_number++;
		tasks.pid = pedido.id;
		tasks.next = NULL;
		tasks.priority = pedido.priority;
		tasks = parse_for_status(tasks, pedido);
		pid_t pid = fork();
		if (pid == 0)
			executa_pedidos(pedido, processos_.bin_transforms);
		else
			tasks.id = pid;
		processos_.tasks = add_task(tasks, processos_.tasks);
	}
	return processos_;
}

// PROCESSOS_ asd(PROCESSOS_ processos_, int j) // ja n sei q chamar as funcoes
// {
// 	PEDIDO_ pedido;
// 	pedido.priority = j;
// 	processos_.pedido_queue[j] = remove_pedido_queue(&pedido, processos_.pedido_queue[j]);
	
// 	TASKS tasks;
// 	for (int i = 0; i < 7; i++)
// 		tasks.task[i] = 0;
// 	for (int i = 0; i < pedido.n_transf; i++)
// 		tasks.task[pedido.id_tranf[i]]++;
// 	for (int i = 0; i < 7; i++)
// 	{
// 		while (tasks.task[i] > processos_.max_proc[i] - processos_.n_procs[i])
// 			processos_ = atualiza_estado(processos_);
// 		processos_.n_procs[i] += tasks.task[i];
// 	}
// 	tasks.task_number = task_number++;
// 	tasks.pid = pedido.id;
// 	tasks.next = NULL;
// 	tasks.priority = pedido.priority;
// 	tasks = parse_for_status(tasks, pedido);
// 	pid_t pid = fork();
// 	if (pid == 0)
// 		executa_pedidos(pedido, processos_.bin_transforms);
// 	else
// 		tasks.id = pid;
// 	processos_.tasks = add_task(tasks, processos_.tasks);
// 	return processos_;
// }

PROCESSOS_ processa_pedidos(PROCESSOS_ processos_)
{
	if (processos_.pedido_queue[5])
	{
		processos_ = asd(processos_, 5);
	}
	else if (processos_.pedido_queue[4])
	{
		processos_ = asd(processos_, 4);
	}
	else if (processos_.pedido_queue[3])
	{
		processos_ = asd(processos_, 3);
	}
	else if (processos_.pedido_queue[2])
	{
		processos_ = asd(processos_, 2);
	}
	else if (processos_.pedido_queue[1])
	{
		processos_ = asd(processos_, 1);
	}
	else if (processos_.pedido_queue[0])
	{
		processos_ = asd(processos_, 0);
	}
	else
	{
		if (flag_sigterm == -1)
			flag_sigterm = 0;
	}
	return processos_;
}

PROCESSOS_ status(PROCESSOS_ processos_, PEDIDO_ pedido)
{
	processos_ = atualiza_estado(processos_);
	TASKS *task_tmp = processos_.tasks;
	char buffer[512] = "";
	sprintf(buffer, "./pipes/%d", pedido.id);
	int write_to_client = open(buffer, O_WRONLY, 0666);
	while (task_tmp)
	{
		char tmp[512] = "";
		sprintf(tmp, "task #%d: proc-file %d %s %s\n", task_tmp->task_number, task_tmp->priority, task_tmp->in_output, task_tmp->trasnf);
		write(write_to_client, tmp, sizeof(tmp));
		task_tmp = task_tmp->next;
	}
	for (int i = 0; i < 7; i++)
	{
		char tmp[512] = "";
		sprintf(tmp, "transf %s: %d/%d (running/max)\n", types_transform[i], processos_.n_procs[i], processos_.max_proc[i]);
		write(write_to_client, tmp, sizeof(tmp));
	}
	close(write_to_client);
	return processos_;
}

PROCESSOS_ init_processos()
{
	PROCESSOS_ processos_;
	for (int i = 0; i < 6; i++)
		processos_.pedido_queue[i] = NULL;
	for (int i = 0; i < 7; i++)
		processos_.n_procs[i] = 0;
	for (int i = 0; i < 7; i++)
	{
		for (int j = 0; j < 64; j++)
			processos_.bin_transforms[i][j] = 0;
	}
	processos_.tasks = NULL;
	return processos_;
}

int main(int argc, char const *argv[])
{
	// signal(SIGINT, signal_handler);
	signal(SIGTERM, signal_handler);
	if (mkfifo(PIPE_CLIENT_SRV, 0666) == -1)
	{
		// perror("mkfifo");
		// return -1;
	}
	int pipe_read_client_server[2]; // pipe_read_client_server :: pipe para comunicar entre filho(recebe pedido) e pai
	if (pipe(pipe_read_client_server) < 0)
	{
		perror("pipe_read_client_server");
		return -1;
	}
	PROCESSOS_ processos_ = init_processos();
	parse_config_file(argv[1], processos_.max_proc);	 // max_proc :: maximo de processos de cada tipo
	processos_ = parse_trasnf_file(argv[2], processos_); // transf_folder :: pasta onde estao os executaveis para as trasnformacoes

	pid_le_pedidos = fork();
	if (pid_le_pedidos == 0)
		le_pedidos(pipe_read_client_server, processos_.max_proc); // filho que le_pedidos
	close(pipe_read_client_server[1]);

	PEDIDO_ pedido;
	while (flag_sigterm != 0)
	{
		processos_ = atualiza_estado(processos_);
		read(pipe_read_client_server[0], &pedido, sizeof(pedido));
		if (pedido.id != -1)
		{
			if (pedido.priority >= 0)
				processos_ = add_pedido_queue(pedido, processos_);
			else
				processos_ = status(processos_, pedido);
		}
		processos_ = atualiza_estado(processos_);
		processos_ = processa_pedidos(processos_);
	}
	unlink(PIPE_CLIENT_SRV);
	return 0;
}
