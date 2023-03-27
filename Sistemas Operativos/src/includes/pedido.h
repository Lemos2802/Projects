#ifndef PEDIDO
#define PEDIDO

#define PIPE_CLIENT_SRV "/tmp/pipe_client_srv"

char types_transform[7][16] = 
						{"nop",
						"bcompress",
						"bdecompress",
						"gcompress",
						"gdecompress",
						"encrypt",
						"decrypt"};

typedef enum
{
	nop = 0,
	bcompress = 1,
	bdecompress = 2,
	gcompress = 3,
	gdecompress = 4,
	encrypt = 5,
	decrypt = 6
}TRANSF_ID;

typedef struct pedido_
{
	int id;					//	pid do cliente
	int priority;			//	prioridade do pedido
	int n_transf;			//	nº de transformacoes a fazer
	char input_file[64];	//	path para o ficheiro de input
	char output_file[64];	//	path para onde o ficheiro de output deve ir
	TRANSF_ID id_tranf[32];	//	tipo de transformacoes a fazer por ordem
}PEDIDO_;

#endif
