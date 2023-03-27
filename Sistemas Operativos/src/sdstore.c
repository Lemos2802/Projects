#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "./includes/pedido.h"

void fazer_pedido(PEDIDO_ *pedido)
{
	int pipe = open(PIPE_CLIENT_SRV, O_WRONLY);
	write(pipe, pedido, sizeof(*pedido));
	close(pipe);
}

PEDIDO_ parse_pedido(int argc, char const *argv[])
{
	PEDIDO_ pedido;
	pedido.id = getpid();
	pedido.n_transf = 0;
	for (int i = 0; i < 64; i++)	pedido.input_file[i] = pedido.output_file[i] = 0;

	if (!strcmp(argv[2], "-p"))
	{
		pedido.priority = atoi(argv[3]);
		strcpy(pedido.input_file, argv[4]);
		strcpy(pedido.output_file, argv[5]);
		for (int i = 6; i < argc; i++)
		{
			if (!strcmp(argv[i], "nop"))
			{
				pedido.id_tranf[pedido.n_transf] = nop;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "bcompress"))
			{
				pedido.id_tranf[pedido.n_transf] = bcompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "bdecompress"))
			{
				pedido.id_tranf[pedido.n_transf] = bdecompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "gcompress"))
			{
				pedido.id_tranf[pedido.n_transf] = gcompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "gdecompress"))
			{
				pedido.id_tranf[pedido.n_transf] = gdecompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "encrypt"))
			{
				pedido.id_tranf[pedido.n_transf] = encrypt;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "decrypt"))
			{
				pedido.id_tranf[pedido.n_transf] = decrypt;
				pedido.n_transf++;
			}
			else
			{
				pedido.id = -1;
				i = argc;
			}
		}
	}
	else
	{
		pedido.priority = 0;
		strcpy(pedido.input_file, argv[2]);
		strcpy(pedido.output_file, argv[3]);
		
		for (int i = 4; i < argc; i++)
		{
			if (!strcmp(argv[i], "nop"))
			{
				pedido.id_tranf[pedido.n_transf] = nop;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "bcompress"))
			{
				pedido.id_tranf[pedido.n_transf] = bcompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "bdecompress"))
			{
				pedido.id_tranf[pedido.n_transf] = bdecompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "gcompress"))
			{
				pedido.id_tranf[pedido.n_transf] = gcompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "gdecompress"))
			{
				pedido.id_tranf[pedido.n_transf] = gdecompress;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "encrypt"))
			{
				pedido.id_tranf[pedido.n_transf] = encrypt;
				pedido.n_transf++;
			}
			else if (!strcmp(argv[i], "decrypt"))
			{
				pedido.id_tranf[pedido.n_transf] = decrypt;
				pedido.n_transf++;
			}
			else
			{
				pedido.id = -1;
				i = argc;
			}
		}
	}
	return pedido;
}

int main(int argc, char const *argv[])
{
	char pipe_srv_client[32] = "";

	sprintf(pipe_srv_client, "./pipes/%d", (int)getpid());

	mkfifo(pipe_srv_client, 0666);

	if (argc > 1)
	{
		if (argc == 2 && !strcmp(argv[1], "status"))
		{
			PEDIDO_ pedido_;
			pedido_.id = getpid();
			pedido_.priority = -1;
			pedido_.n_transf = 0;
			fazer_pedido(&pedido_);
			char buffer[512] = "";
			int read_from_srv = open(pipe_srv_client, O_RDONLY, 0666);
			while (read(read_from_srv, buffer, sizeof(buffer)))
				write(1, buffer, sizeof(buffer));
			close(read_from_srv);
		}
		else if (!strcmp(argv[1], "proc-file"))
		{
			PEDIDO_ pedido = parse_pedido(argc, argv);

			if (pedido.id != -1)
			{
				fazer_pedido(&pedido);
				for (int i = 0; i < 3; i++)
				{
					char buffer[128] = "";
					int read_from_srv = open(pipe_srv_client, O_RDONLY, 0666);
					read(read_from_srv, buffer, sizeof(buffer));
					if (strcmp(buffer, "nao e possivel processar o pedido por falta de recursos\n") == 0)	i = 3;
					write(1, buffer, sizeof(buffer));
					close(read_from_srv);
				}
			}
			else
				write(1, "Invalid commands\n", sizeof("Invalid commands\n"));
		}
	}
	else
	{
		char c_status[] = "./sdstore status\n";
		char c_proc[] = "./sdstore proc-file -p priority input-filename output-filename transformation-id-1 transformation-id-2 ...\n";
		write(1, c_status, sizeof(c_status));
		write(1, c_proc, sizeof(c_proc));
	}
	unlink(pipe_srv_client);
	return 0;
}
