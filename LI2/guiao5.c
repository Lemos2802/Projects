#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <math.h>

/**
*\brief Definir a união de tipos dos elemento da stack 
*/
typedef enum TYPE { /**< Enum para os tipos de input */
/* inteiros */
INT = 1, 
/* floats */
FLOAT = 2, 
/* carateres */
CHAR = 4, 
/* strings */
STRING = 8,
/* arrays */
ARR = 10,
/* blocks */
BLOCK = 20
}TYPE;/**<tipos*/

/**
 *\brief definir a struct com os valores em data (long,char,double,string) para depois ser usado na stack.
 */
typedef struct data
{
    TYPE type; /**<tipo do elemento*/
    union tipos /**<união de tipos*/
    {
        long i; /**<valores do tipo long*/
        double f; /**<valores do tipo double*/
        char c; /**<valores do tipo char*/
        char* s; /**<valores do tipo string*/
        struct stack *arr; /**<valores do tipo array*/
        char* block; /**<valores do tipo block*/
    }types; /**<uniao dos tipos*/
}DATA;

/**
 *\brief definir a struct stack para a colocar a guardar dados que sejam relevantes como e o caso da stack em si definida mais abaixo,
 *o seu numero de elementos e o  numero maximo de elementos.
 */
typedef struct stack
{
    DATA *stack; /**<apontador para stack*/
    int max_elems; /**<numero maximo de elementos*/
    int n_elems; /**<numero de elementos*/
}STACK;

/**
 *\brief Função que é responsavel por construir a stack onde irao
 *estar presentes elementos do tipo DATA.
 *
 *@returns a stack que foi construida.
 */
STACK *create_stack() {
  STACK *s = (STACK *) calloc(1, sizeof(STACK));
  s->max_elems = 500;
  s->stack = (DATA *) calloc(s->max_elems, sizeof(DATA));
  return s;
}

/**
 *\brief Função que é responsável por converter um long para o tipo DATA para ser possivel coloca-lo dentro da stack.
 *
 *@param val_i valor int que queremos converter.
 *
 *@returns o valor convertido.
 */
DATA convertI (long val_i)
{
    DATA LONG;
    LONG.types.i = val_i;
    LONG.type = INT;
    return LONG;
}

/**
 *\brief Função que é responsável por converter um float para o tipo DATA para ser possivel coloca-lo dentro da stack.
 *
 *@param val_f double que queremos converter.
 *
 *@returns o valor convertido.
 */
DATA convertF (double val_f)
{
    DATA DOUBLE;
    DOUBLE.type = FLOAT;
    DOUBLE.types.f = val_f;
    return DOUBLE;
}

/**
 *\brief Função que é responsável por converter um caracter para o tipo DATA para ser possivel coloca-lo dentro da stack.
 *
 *@param val_c char que queremos converter.
 *
 *@returns o valor convertido.
 */
DATA convertC (char val_c)
{
    DATA CH;
    CH.type = CHAR;
    CH.types.c = val_c;
    return CH;
}

/**
 *\brief Função que é responsável por converter uma string para o tipo DATA para ser possivel coloca-lo dentro da stack.
 *
 *@param val_s string que queremos converter.
 *
 *@returns o valor convertido.
 */
DATA convertS (char *val_s)
{
    DATA STR;
    STR.types.s = val_s;
    STR.type = STRING;
    return STR;
}

/**
 *\brief Função que é responsável por converter um array para o tipo DATA para ser possivel coloca-lo dentro da stack.
 *
 *@param val_arr array que queremos converter.
 *
 *@returns o valor convertido.
 */
DATA convertARR (STACK* val_arr)
{
    DATA a;
    a.types.arr = val_arr;
    a.type = ARR;
    return a;
}

/**
 *\brief Função que é responsável por converter um bloco para o tipo DATA para ser possivel coloca-lo dentro da stack.
 *
 *@param val_b block que queremos converter.
 *
 *@returns o valor convertido.
 */
DATA convertBLOCK (char *val_b)
{
    DATA B;
    B.types.block = val_b;
    B.type = BLOCK;
    return B;
}

/**
 *\brief Função que é responsável por retornar o topo da stack
 *
 *@param s apontador para a stack
 *
 *@returns o valor do topo
 */
int TOP(STACK *s)
{
    return (s->n_elems-1);
}

/**
 *\brief Função que retira o elemento do topo da stack
 *
 *@param s stack de onde queremos retirar o valor do seu topo.
 *
 */
void POP(STACK *s)
{
    s->n_elems--;
}

/**
 *\brief Função que introduz elemento no topo da stack.
 *
 *@param s stack onde queremos colocar
 *@param elem valor que quer colocar no topo da stack.
 *
 */
void PUSH (STACK *s, DATA elem)
{
    if(s->max_elems == s->n_elems)
    {
        s->max_elems += 100;
        s->stack = (DATA *) realloc(s->stack, s->max_elems * sizeof(DATA));
    }
    if (elem.type == STRING)
        s->stack[s->n_elems].types.s = malloc(sizeof(char*) * 3);
    s->stack[s->n_elems] = elem;
    s->n_elems++;
}

/**
 *\brief Esta é a função que é responsavel por separar por casos as diferentes operacoes segundo os tipos.
 *Por exemplo, operacao de double com double e double com int e retorna numeros diferentes consoante os operadores.
 *
 *@param s função recebe a stack
 *@param N o seu numero de elementos.
 *
 *@returns o valor 0, 1 ou -1.
 */
int operation_type (STACK *s, int N)
{
    int x = 0;
    int i;
    if(s->n_elems == 1)
        N = 1;
    for (i = TOP(s); i >= s->n_elems-N; i--)
    {
        int t = s->stack[i].type;
        if (t == INT){
          if (x != INT)
            x += t;
        }
        else
        if (t == FLOAT){
          if (x != FLOAT)
            x += t;
        }
        else
        if (t == CHAR){
            if (x != CHAR)
            x += t;
        }
        else
        if (t == STRING){
            if (x != STRING)
            x += t;
        }
        else
        if (x != ARR)
          x += t;
    }
    return x;
}

/**
 *\brief Função que é responsável por iniciar as variáveis
 *
 *@param vars endereço das variáveis
 *
 */
void var_init(DATA* vars)
{
    long variaveis_num[9]= {10,11,12,13,14,15,0,1,2};
    char variaveis_char[2]="\n ";
    int i;

    for (i = 0; i < 9; i++)
        *(vars+i) = convertI(variaveis_num[i]);
    for (i = 9; i < 13; i++)
        *(vars+i) = convertC('\0');
    *(vars+13) = convertC(variaveis_char[0]);
    for (i = 14; i < 18; i++)
        *(vars+i) = convertC('\0');
    *(vars+18) = convertC(variaveis_char[1]);
    for (i = 19; i < 23; i++)
        *(vars+i) = convertC('\0');
    for (i = 23; i < 26; i++)
        *(vars+i) = convertI(variaveis_num[i-17]);
}

/**
 *\brief Esta é a função  que é responsavel por verificar os numeros no input e se for necessario
 *faz a conversao dos numeros para outros tipos.
 *
 *@param line o input do utilizador
 *@param s stack onde iremos colocar os valores
 *@param N o numero responsavel por indicar se converte em float ou int.
 *
 */
void strtonum (char *line, STACK *s, int N){
    if(*line >= '0' && *line <= '9') {

        int i = atoi(line);
        float f = atof(line);

        if (f - (int)f == 0)
        {
            if (N == 0)
                PUSH (s, convertI(i));
            else  if(N == 3)
                PUSH (s, convertI(i));
            else
                PUSH (s, convertF(i));

        }
        else
        {
            if (N == 1)
                PUSH (s, convertF(f));
            else  if(N == 3)
                PUSH (s, convertF(f));
            else
                PUSH (s, convertI(f));
        }
    }
    else {
        PUSH(s, convertS(line));
    }
}

/**
 *\brief Esta é a função que é responsavel por realizar a soma dos dois numeros que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void somaNum(STACK *s) 
{
    double x,y;
	int op = operation_type(s,2);
    
    if (op == INT)
		{
			x = s->stack[TOP(s)].types.i;
            y = s->stack[TOP(s)-1].types.i;
            POP(s);
            POP(s);
            PUSH (s, convertI(y+x));
		}
		else
		{
			if (operation_type(s,1) == INT)
				x = s->stack[TOP(s)].types.i;
			else
				x = s->stack[TOP(s)].types.f;
			POP(s);
			if (operation_type(s,1) == INT)
				y = s->stack[TOP(s)].types.i;
			else
				y = s->stack[TOP(s)].types.f;
			POP(s);
			PUSH (s, convertF((double) y+x));
        }
}

/**
 *\brief Esta é a função que é responsavel por realizar a diferença dos dois numeros que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void subNum(STACK *s)
{
    double x,y;
	int op = operation_type(s,2);
    
    if (op == INT)
		{
			x = s->stack[TOP(s)].types.i;
            y = s->stack[TOP(s)-1].types.i;
            POP(s);
            POP(s);
            PUSH (s, convertI(y-x));
		}
		else
		{
			if (operation_type(s,1) == INT)
				x = s->stack[TOP(s)].types.i;
			else
				x = s->stack[TOP(s)].types.f;
			POP(s);
			if (operation_type(s,1) == INT)
				y = s->stack[TOP(s)].types.i;
			else
				y = s->stack[TOP(s)].types.f;
			POP(s);
			PUSH (s, convertF((double) y-x));
        }
}

/**
 *\brief Esta é a função que é responsavel por realizar o produto dos dois numeros que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void multNum(STACK *s)
{
    double x,y;
	int op = operation_type(s,2);    
    
    if (op == INT)
	{
		x = s->stack[TOP(s)].types.i;
        y = s->stack[TOP(s)-1].types.i;
        POP(s);
        POP(s);
        PUSH (s, convertI(y*x));
	}
	else
	{
		if (operation_type(s,1) == INT)
			x = s->stack[TOP(s)].types.i;
		else
			x = s->stack[TOP(s)].types.f;
		POP(s);
		if (operation_type(s,1) == INT)
			y = s->stack[TOP(s)].types.i;
		else
			y = s->stack[TOP(s)].types.f;
		POP(s);
		PUSH (s, convertF((double) y*x));
    }
}

/**
 *\brief Esta é a função que é responsavel por realizar o quociente dos dois numeros que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void divNum(STACK *s)
{
    double x,y;
	int op = operation_type(s,2);

    if (op == INT)
		{
			x = s->stack[TOP(s)].types.i;
            y = s->stack[TOP(s)-1].types.i;
            POP(s);
            POP(s);
            PUSH (s, convertI(y/x));
		}
		else
		{
			if (operation_type(s,1) == INT)
				x = s->stack[TOP(s)].types.i;
			else
				x = s->stack[TOP(s)].types.f;
			POP(s);
			if (operation_type(s,1) == INT)
				y = s->stack[TOP(s)].types.i;
			else
				y = s->stack[TOP(s)].types.f;
			POP(s);
			PUSH (s, convertF((double) y/x));
		}
}

/**
 *\brief Esta é a função que é responsavel por calcular o resto da divisão inteira dos dois numeros que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void modNum(STACK *s)
{
    long a, b;
	int op = operation_type(s,2);

    if (op == INT)
	{
        a = s->stack[TOP(s)].types.i;
        b = s->stack[TOP(s)-1].types.i;
        POP(s);
        POP(s);
        PUSH (s, convertI(b%a));
	}
}

/**
 *\brief Esta é a função que é responsavel por realizar a exponenciação dos dois numeros que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void expNum(STACK *s)
{
    double x,y;
	int op = operation_type(s,2);

    if (op == INT)
		{
			x = s->stack[TOP(s)].types.i;
            y = s->stack[TOP(s)-1].types.i;
            POP(s);
            POP(s);
            PUSH (s, convertI(pow(y,x)));
		}
		else
		{
			if (operation_type(s,1) == INT)
				x = s->stack[TOP(s)].types.i;
			else
				x = s->stack[TOP(s)].types.f;
			POP(s);
			if (operation_type(s,1) == INT)
				y = s->stack[TOP(s)].types.i;
			else
				y = s->stack[TOP(s)].types.f;
			POP(s);
			PUSH (s, convertF(pow(y,x)));
		}
}

/**
 *\brief Esta é a função que é responsavel por realizar a descrementação do elemento que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void decNum(STACK *s)
{
    double y;
	int op = operation_type(s,1);

	if (op == INT){
		y = s->stack[TOP(s)].types.i;
		POP(s);
		PUSH (s, convertI(y-1));
	}
	else
	if (op == FLOAT){
		y = s->stack[TOP(s)].types.f;
		POP(s);
		PUSH (s, convertF(y-1));
	}
    else
	if (op == CHAR){
		char y = s->stack[TOP(s)].types.c;
		POP(s);
		char Z = y-1;
		PUSH (s, convertC(Z));
	}
}

/**
 *\brief Esta é a função que é responsavel por realizar a incrementação do elemento que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void incNum(STACK *s)
{
    double y;
	int op = operation_type(s,1);
	if (op == CHAR){
		char y = s->stack[TOP(s)].types.c;
		POP(s);
		char Z = y+1;
		PUSH (s, convertC(Z));
	}
	else
	if (op == INT){
		y = s->stack[TOP(s)].types.i;
		POP(s);
		PUSH (s, convertI(y+1));
	}
	else
	if (op == FLOAT){
		y = s->stack[TOP(s)].types.f;
		POP(s);
		PUSH (s, convertF(y+1));
	}
}

/**
 *\brief Esta é a função que é responsavel por calcular o range do elemento que se encontram no topo da Stack.
 *
 *@param s stack onde retiramos os valores e colocamos o resultado
 *
 */
void criaArrayNum(STACK *s)
{
    double x;
    int op = operation_type(s,1);

    if (op == INT)  {
        x = s->stack[TOP(s)].types.i;
        POP(s);
        s->stack[TOP(s)+1].type = ARR;
        s->stack[TOP(s)+1].types.arr = create_stack();
        s->n_elems++;
        for(int i = 0; i < x; i++)
            PUSH(s->stack[TOP(s)].types.arr, convertI(i));
    }
}

/**
 *\brief Função que é responsável por realizar operações matemáticas correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void opAritmeticas_num(STACK *s, int n)
{
    switch(n) {
        case 0:
            somaNum(s);
            break;
        case 1:
            subNum(s);
            break;
        case 2:
            multNum(s);
            break;
        case 3:
            divNum(s);
            break;
        case 4:
            modNum(s);
            break;
        case 5:
            expNum(s);
            break;
        case 6:
            decNum(s);
            break;
        case 7:
            incNum(s);
            break;
        case 8:
            criaArrayNum(s);
            break;
    }
}

/**
 *\brief Esta é a função que é responsavel por remover o topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void popOp(STACK *s)
{
    POP(s);
}

/**
 *\brief Esta é a função que é responsavel por duplicar o topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void dupTop(STACK *s)
{
    DATA x = s->stack[TOP(s)];
    PUSH(s, x);
}

/**
 *\brief Esta é a função que é responsavel por trocar os dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void trocaTop(STACK *s)
{
    DATA x;
	x = s->stack[TOP(s)];
    s->stack[TOP(s)] = s->stack[TOP(s)-1];
    s->stack[TOP(s)-1] = x;
}

/**
 *\brief Esta é a função que é responsavel por rodar os três elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void arroba(STACK *s)
{
    DATA x, y;
    x = s->stack[TOP(s)];
    y = s->stack[TOP(s)-2];
    s->stack[TOP(s)-2] = s->stack[TOP(s)-1];
    s->stack[TOP(s)] = y;
    s->stack[TOP(s)-1] = x;
}

/**
 *\brief Esta é a função que é responsavel por calcular o resultado da operação if-then-else dos três elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void ifthenelse(STACK *s)
{
    double x, y, z;
    
    if (operation_type(s,1) == INT)   // int
        x = s->stack[TOP(s)].types.i;
    else
    if (operation_type(s,1) == FLOAT)                            // float
        x = s->stack[TOP(s)].types.f;
    else
        x = s->stack[TOP(s)].types.arr->n_elems;
    POP(s);
    if (operation_type(s,1) == INT)   // int
        y = s->stack[TOP(s)].types.i;
    else
    if (operation_type(s,1) == FLOAT)                            // float
        y = s->stack[TOP(s)].types.f;
    else
        y = s->stack[TOP(s)].types.arr->n_elems;
    POP(s);
    if (operation_type(s,1) == INT)   // int
        z = s->stack[TOP(s)].types.i;
    else
    if (operation_type(s,1) == FLOAT)                            // float
        z = s->stack[TOP(s)].types.f;
    else
        z = s->stack[TOP(s)].types.arr->n_elems;
    POP(s);
    if (z)
        PUSH (s, convertF(y));
    else
        PUSH (s, convertF(x));
}

/**
 *\brief Esta é a função que é responsavel por copiar o n-ésimo elemento para o topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void copiaParaTopo(STACK *s)
{
    DATA a;
	int i = s->stack[s->n_elems-1].types.i;
    POP(s);
    a = s->stack[s->n_elems-(i+1)];
    PUSH(s, a);
}

/**
 *\brief Esta é a função que é responsavel por calcular a negação do elemento que se encontra no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void nao(STACK *s)
{
    double x;
    
    if (operation_type(s,1) == INT) // int
        x = s->stack[s->n_elems-1].types.i;
    else
    if (operation_type(s,1) == CHAR) // char
        x = s->stack[s->n_elems-1].types.c;
    else{
        x = s->stack[s->n_elems-1].types.f; // float
    }
    POP (s);
    if (x==0)
        PUSH (s,convertI (1));
    else
        PUSH (s,convertI (0));
}

/**
 *\brief Função que é responsável por realizar operações de stack correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void opStack(STACK *s, int n)
{
	switch (n)
    {
    case 0:
        popOp(s);
        break;
    case 1:
        dupTop(s);
        break;
    case 2:
        trocaTop(s);
        break;
    case 3:
        arroba(s);
        break;
    case 4:
        ifthenelse(s);
        break;
    case 5:
        copiaParaTopo(s);
        break;
    case 6:
        nao(s);
        break;
    }
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica & dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void eLogico(STACK *s)
{
    int op = operation_type(s, 2);
    if (op == INT)
    {
        long a = s->stack[TOP(s)].types.i;
        long b = s->stack[TOP(s)-1].types.i;
        POP(s);
        POP(s);
        PUSH (s, convertI(b&a));
    }
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica | dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void ouLogico(STACK *s)
{
    int op = operation_type(s, 2);
    
    if (op == INT)
	{
        long a = s->stack[TOP(s)].types.i;
        long b = s->stack[TOP(s)-1].types.i;
        POP(s);
        POP(s);
        PUSH (s, convertI(b|a));
	}
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica < dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void menorNum(STACK *s)
{
    double x, y;
    
    if (operation_type(s,1) == INT)       // int
        x = s->stack[TOP(s)].types.i;
    else                                // char
    if (operation_type(s,1) == CHAR) {
        char z = s->stack[s-> n_elems-1].types.c;
        x = (long) z;
    }
    else                                // float
        x = s->stack[TOP(s)].types.f;
    POP(s);
    if (operation_type(s,1) == INT)       // int
        y = s->stack[TOP(s)].types.i;
    else                                // char
    if (operation_type(s,1) == CHAR) {
        char z = s->stack[s-> n_elems-1].types.c;
        y = (int)z;
    }
    else                                //float
        y = s->stack[TOP(s)].types.f;
    POP(s);
    if (y < x)
        PUSH (s, convertI(1));
    else
        PUSH (s, convertI(0));
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica > dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void maiorNum(STACK *s)
{
    double x, y;
    
    if (operation_type(s,1) == INT)       // int
        x = s->stack[TOP(s)].types.i;
    else                                // char
    if (operation_type(s,1) == CHAR) {
        char z = s->stack[s-> n_elems-1].types.c;
        x = (long) z;
    }
    else                                // float
        x = s->stack[TOP(s)].types.f;
    POP(s);
    if (operation_type(s,1) == INT)       // int
        y = s->stack[TOP(s)].types.i;
    else                                // char
    if (operation_type(s,1) == CHAR) {
        char z = s->stack[s-> n_elems-1].types.c;
        y = (int)z;
    }
    else                                //float
        y = s->stack[TOP(s)].types.f;
    POP(s);
    if (y > x)
        PUSH (s, convertI(1));
    else
        PUSH (s, convertI(0));
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica ^ dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void ouExlusivo(STACK *s)
{
    if (operation_type(s, 2) == INT)
	{
		long a = s->stack[TOP(s)].types.i;
        long b = s->stack[TOP(s)-1].types.i;
        POP(s);
        POP(s);
        PUSH (s, convertI(b^a));
	}
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica ~ dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void complemento(STACK *s)
{
    if (operation_type(s,1) == INT)
	{
		long a = s->stack[TOP(s)].types.i;
        POP(s);
        PUSH (s, convertI(~a));
	}
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica = dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void igualNum(STACK *s)
{
    double x, y;
    int op = operation_type(s, 2);

    if (op == INT)
    {
        x = s->stack[TOP(s)].types.i;
        y = s->stack[TOP(s)-1].types.i;
        POP(s);
        POP(s);
        if (x==y)
            PUSH (s, convertI(1));
        else
            PUSH (s, convertI(0));
    }
    else
    {
        if (operation_type(s,1) == INT)
            x = s->stack[TOP(s)].types.i;
        else
            x = s->stack[TOP(s)].types.f;
        POP(s);
        if (operation_type(s,1) == INT)
            y = s->stack[TOP(s)].types.i;
        else
            y = s->stack[TOP(s)].types.f;
        POP(s);
        if (x==y)
            PUSH (s, convertI(1));
        else
            PUSH (s, convertI (0));
    }
}

/**
 *\brief Função que é responsável por realizar operações de comparacao correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void opcomparacoes_num(STACK *s, int n)
{
    switch (n)
    {
    case 0:
        eLogico(s);
        break;
    case 1:
        ouLogico(s);
        break;
    case 2:
        menorNum(s);
        break;
    case 3:
        maiorNum(s);
        break;
    case 4:
        ouExlusivo(s);
        break;
    case 5:
        complemento(s);
        break;
    case 6:
        igualNum(s);
        break;
    }
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica e com shortcut dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void eShortcut(STACK *s)
{
    double x, y;
    
    if (operation_type(s,1) == INT)
        x = s->stack[TOP(s)].types.i;
    else
        x = s->stack[TOP(s)].types.f;
    POP(s);
    if (operation_type(s,1) == INT)
        y = s->stack[TOP(s)].types.i;
    else
        y = s->stack[TOP(s)].types.f;
    POP(s);
    if (x==0 || y==0)
        PUSH (s, convertI(0));
    else
    if (x!=0)
        PUSH (s, convertI(x));
    else
        PUSH (s,convertI (y));
}

/**
 *\brief Esta é a função que é responsavel por realizar a operação lógica ou com shortcut dos dois elementos que se encontram no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void ouShortcut(STACK *s)
{
    double x, y;

    if (operation_type(s,1) == INT)
        x = s->stack[TOP(s)].types.i;
    else
        x = s->stack[TOP(s)].types.f;
    POP(s);
    if (operation_type(s,1) == INT)
        y = s->stack[TOP(s)].types.i;
    else
        y = s->stack[TOP(s)].types.f;
    POP(s);
    if (y!=0)
        PUSH (s, convertF(y));
    else
    if (x!=0 && y==0 )
        PUSH (s, convertF(x));
    else
        PUSH (s,convertF (0));
}

/**
 *\brief Esta é a função que é responsavel por colocar o menor dos dois elementos no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void menorE(STACK *s)
{
    double x, y;
    
    if (operation_type(s,1) == INT)   // int
        x = s->stack[TOP(s)].types.i;
    else                            // float
        x = s->stack[TOP(s)].types.f;
    POP(s);
    if (operation_type(s,1) == INT)   // int
        y = s->stack[TOP(s)].types.i;
    else                            // float
        y = s->stack[TOP(s)].types.f;
    POP(s);
    if (x < y)
        PUSH (s, convertF(x));
    else
        PUSH (s, convertF(y));
}

/**
 *\brief Esta é a função que é responsavel por colocar o maoir dos dois elementos no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void maiorE(STACK *s)
{
    double x, y;

    if (operation_type(s,1) == INT)   // int
        x = s->stack[TOP(s)].types.i;
    else                            // float
        x = s->stack[TOP(s)].types.f;
    POP(s);
    if (operation_type(s,1) == INT)   // int
        y = s->stack[TOP(s)].types.i;
    else                            // float
        y = s->stack[TOP(s)].types.f;
    POP(s);
    if (x > y)
        PUSH (s, convertF(x));
    else
        PUSH (s, convertF(y));
}

/**
 *\brief Função que é responsável por realizar operações de comparacao correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void opcomparacoes2_num(STACK *s, int n)
{
    switch (n)
    {
    case 0:
        eShortcut(s);
        break;
    case 1:
        ouShortcut(s);
        break;
    case 2:
        menorE(s);
        break;
    case 3:
        maiorE(s);
        break;
    }
}

/**
 *\brief Função que é responsável por realizar operações de variaveis correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void opvar(STACK *s, DATA* var)
{
    PUSH(s, *(var));
}

/**
 *\brief Função que é responsável por realizar operações de variaveis correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void opcopyvar(STACK *s, DATA* var)
{
    *var = s->stack[TOP(s)];
}

/**
 *\brief Função que é responsável por realizar operações de leitura de uma linha, correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void leitura(STACK *s, int n)
{    
    char* line;
    char* x = malloc(sizeof(char)*1000);
    switch (n)
    {
    case 0:
        assert (fgets (x,500,stdin)!= NULL);
        if(*x >= '0' && *x <= '9')
            strtonum(x, s, 3);
        else
            PUSH(s, convertS(x));
        break;
    
    case 1:
        line = x;
        line = fgets (line,500,stdin);
        while (line != NULL)
        {
            line = x + strlen(x);
            line= fgets (line,500,stdin);
        }
        PUSH (s, convertS(x));
        break;
    }
}

/**
 *\brief Função que é responsável por realizar operações de conversão de elementos da stack, correspondentes a um determinado token.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void convert(STACK *s, int n)
{
    int op;
    double x;
    op = operation_type(s,1);
    switch(n) {
        case 0:
            if (op == CHAR)
            {
                char x = s->stack[TOP(s)].types.c;
                s->stack[TOP(s)].types.i =(int) x;
                s->stack[TOP(s)].type = INT;
            }
            else
            if (op == FLOAT){
                x = s->stack[TOP(s)].types.f;
                s->stack[TOP(s)].types.i =(int) x;
                s->stack[TOP(s)].type = INT;
            }
            else
            if (op == STRING){
                char *str = s->stack[TOP(s)].types.s;
                POP(s);
                strtonum(str, s, 2);
            }
            break;
        case 1:
            if (op == INT){
                x = s->stack[TOP(s)].types.i;
                s->stack[TOP(s)].types.f =(double) x;
                s->stack[TOP(s)].type = FLOAT;
            }
            if (op == STRING){
                char *str = s->stack[TOP(s)].types.s;
                POP(s);
                strtonum(str, s, 1);
            }
            break;
        case 2:
            if(op == INT){
                char c = s->stack[TOP(s)].types.i;
                s->stack[TOP(s)].types.c = c;
                s->stack[TOP(s)].type = CHAR;
            }
            break;
    }
}

/**
 *\brief Esta é a função que é responsavel por concatenar duas strings.
 *
 *@param s stack que iremos alterar.
 *
 */
void concatArrS(STACK *s)
{
    char* str1;
    char* str2;

    str2 = s->stack[TOP(s)].types.s;
    str1 = s->stack[TOP(s)-1].types.s;
    POP(s);
    POP(s);
    str1 = strcat (str1,str2);
    PUSH (s, convertS(str1));
}

/**
 *\brief Esta é a função que é responsavel por concatenar dois arrays.
 *
 *@param s stack que iremos alterar.
 *
 */
void concatArrA(STACK *s)
{
    STACK *z;
    STACK *a;
    a= s->stack[TOP(s)].types.arr;
    z = s->stack[TOP(s)-1].types.arr;
    POP(s);
    POP(s);
    int y = a->n_elems,b;
    for (b=0; b<y; b++)
        {
            PUSH(z,a->stack[b]);
        }
    free (a);
    PUSH(s,convertARR(z));
}

/**
 *\brief Esta é a função que é responsavel por concatenar um array e um inteiro.
 *
 *@param s stack que iremos alterar.
 *
 */
void concatArrAI(STACK *s)
{
    STACK *z;
    STACK *b=create_stack();
    int a;
    if (s->stack[TOP(s)].type==INT) {
         
        a= s->stack[TOP(s)].types.i;
        z= s->stack[TOP(s)-1].types.arr;
    }
    else { 
    z= s->stack[TOP(s)].types.arr;
    a= s->stack[TOP(s)-1].types.i;
    }
        
    if (s->stack[TOP(s)].type==INT) {
        PUSH(z,convertI (a));
        free (b);
        POP(s);
        POP(s);
        PUSH(s,convertARR(z));
    } else {
        PUSH (b,convertI (a));
        for (int i=0;i<z->n_elems;i++)
            PUSH (b,z->stack[i]);
    POP(s);
    POP(s);
    free (z);
    PUSH(s,convertARR(b));
    }
}

/**
 *\brief Esta é a função que é responsavel por concatenar dois arrays ou strings.
 *
 *@param s stack que iremos alterar.
 *
 */
void concatArr(STACK *s)
{
    int op = operation_type(s,2);
    
    if (op == STRING) concatArrS(s);
	else if (op == (ARR)) concatArrA(s);
    else if (op == (ARR+INT)) concatArrAI(s);
		
}

/**
 *\brief Esta é a função que é responsavel por concatenar múltiplas vezes dois arrays ou strings.
 *
 *@param s stack que iremos alterar.
 *
 */
void multConcArr(STACK *s)
{
    int op = operation_type(s,2);
    
    if (op == STRING+INT)
        {
            char *str;
            int x = s->stack[TOP(s)].types.i;
            str = s->stack[TOP(s)-1].types.s;
            int  y=strlen (str);
            char *str2=malloc(y*sizeof (char));
            strcpy(str2,str);
            POP(s);
            POP(s);
            for (int i=1;i<x;i++){
                str = strcat (str,str2);
            }
            PUSH(s, convertS(str));
            free(str2);
        }
    
        else
		if (op == ARR+INT)
        {
            STACK *z;
            int x = s->stack[TOP(s)].types.i;
            z = s->stack[TOP(s)-1].types.arr;
            POP(s);
            POP(s);
            int y = z->n_elems;
            for (; x > 1; x--)
            {
                for (int i = 0; i < y; i++)
                {
                    PUSH(z,z->stack[i]);
                }
            }
            PUSH(s,convertARR(z));
        }
}

/**
 *\brief Esta é a função que é responsavel por calcular o tamanho de uma string ou array.
 *
 *@param s stack que iremos alterar.
 *
 */
void lengthArr(STACK *s)
{
    int op = operation_type(s,1);
    
    if (op == STRING)
    {
        int x = strlen(s->stack[TOP(s)].types.s);
        POP(s);
        PUSH(s, convertI(x));
    }
    else
    if (op == INT)
    {
        STACK *z=create_stack();
        int i;
        int x = s->stack[TOP(s)].types.i;
        POP (s);
        for (i=0; (i<x) && (x>0); i++){
            PUSH (z,convertI(i));
        }
        PUSH(s,convertARR(z));
    }
    else
    if (op == ARR)
    {
        STACK *z;
        z = s->stack[TOP(s)].types.arr;
        POP(s);
        
        PUSH(s,convertI (z->n_elems));
    }
}

/**
 *\brief Esta é a função que é responsavel por colocar todos os elementos do array na stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void tranfArr(STACK *s)
{
    int op = operation_type(s,1);
    
    if (op == ARR)
	{
        STACK *z;
        z = s->stack[TOP(s)].types.arr;
        POP(s);
        int y = z->n_elems;
        int a=0;
            for (; a<y; a++)
            {
                PUSH(s,z->stack[a]);
            }
        free (z);
    }
}

/**
 *\brief Esta é a função que é responsavel por colocar o primeiro elemento do array no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void primElemArr(STACK *s)
{
    char *str1;
    int op = operation_type(s,1);
    
    if (op == STRING){
        str1 = (s->stack[TOP(s)].types.s);
        POP(s);
        PUSH(s, convertS(str1+1));
        PUSH(s, convertC(str1[0]));
    }
    else
    if (op == ARR)
	{
        STACK *z;
        STACK *b = create_stack();
        z = s->stack[TOP(s)].types.arr;
        POP(s);
        int y = z->n_elems;
        int a=1;
            for (; a<y; a++)
            {
                PUSH(b,z->stack[a]);
            }
        PUSH(s,convertARR(b));
        PUSH(s,z->stack[0]);
        free (z);
	}
}

/**
 *\brief Esta é a função que é responsavel por colocar o último elemento do array no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void ultElemArr(STACK *s)
{
    char *str1;
    int op = operation_type(s,1);
    
    if (op == STRING)
    {
        str1 = (s->stack[TOP(s)].types.s);
        POP(s);
        char y= str1[strlen(str1)-1];
        str1[strlen(str1)-1]='\0';
        PUSH(s, convertS(str1));
        PUSH(s, convertC(y));
    } 
    else
    if (op == ARR)
		{
            STACK *z= s->stack[TOP(s)].types.arr;
            POP(s);
            int y = z->n_elems;
            POP (z);
            PUSH(s,convertARR(z));
            PUSH(s,z->stack[y-1]);
	    }
}

/**
 *\brief Função que é responsável por realizar operações em elementos do tipo array
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void op_arr(STACK *s, int n) 
{
    switch (n)
    {
    case 0:
        concatArr(s);
        break;
    case 1:
        multConcArr(s);
        break;
    case 2:
        lengthArr(s);
        break;
    case 3:
        tranfArr(s);
        break;
    case 4:
        primElemArr(s);
        break;
    case 5:
        ultElemArr(s);
        break;
    }
}

/**
 *\brief Esta é a função que é responsavel por comparar duas strings ou obter o valor de um determinado indice caso o elemento do topo da stack seja um inteiro.
 *
 *@param s stack que iremos alterar.
 *
 */
void igualArr(STACK *s)
{
    char* str1;
    char* str2;
    int op = operation_type(s,2);
    
    if (op == STRING)
	{
        str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        if (strcmp (str1,str2)==0) 
            PUSH (s,convertI(1));
        else
            PUSH (s,convertI(0));
	}
	else
    if (op == (STRING+INT))
	{
		long x = s->stack[TOP(s)].types.i;
        POP(s);
        char y = s->stack[TOP(s)].types.s[x];
        POP(s);
        PUSH (s, convertC(y));
	}
    else
    {
        long x = s->stack[TOP(s)].types.i;
        POP(s);
        STACK *z = s->stack[TOP(s)].types.arr;
        POP(s);
        PUSH(s,z->stack[x]);
    }
}

/**
 *\brief Esta é a função que é responsavel por comparar duas strings ou obter os n primeiros valores do array/string caso o elemento do topo da stack seja um inteiro.
 *
 *@param s stack que iremos alterar.
 *
 */
void menorArr(STACK *s)
{
	char* str1;
    char* str2;
    int op = operation_type(s,2);
    if (op == STRING)
	{
		str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        if (strcmp (str1,str2) < 0) 
            PUSH (s,convertI(1));
        else
             PUSH (s,convertI(0));
	}
	else
    if (op == (STRING+INT))
	{
		int x = s->stack[TOP(s)].types.i;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        for(int y = 0; y < x; y++)
            PUSH(s, convertC(str1[y]));
	}
    else
    {
        int x = s->stack[TOP(s)].types.i;
        POP(s);
        x = s->stack[TOP(s)].types.arr->n_elems-x;
        s->stack[TOP(s)].types.arr->n_elems -= x;
    }
}

/**
 *\brief Esta é a função que é responsavel por comparar duas strings ou obter os n ultimos valores do array/string caso o elemento do topo da stack seja um inteiro.
 *
 *@param s stack que iremos alterar.
 *
 */
void maiorArr(STACK *s)
{
    char* str1;
    char* str2;
    int op = operation_type(s,2);
    
    if (op == STRING)
	{
		str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        if (strcmp (str1,str2) > 0) 
            PUSH (s,convertI(1));
        else
             PUSH (s,convertI(0));
	}
	else
    if(op == (STRING+INT))
	{
		int x = s->stack[TOP(s)].types.i;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        int y = strlen(str1) - x;
        int t = strlen(str1);
        for(; y < t; y++)
            PUSH(s, convertC(str1[y]));
	}
    else
    {
        int x = s->stack[TOP(s)].types.i-1;
        POP(s);
        STACK* z = s->stack[TOP(s)].types.arr;
        POP(s);
        STACK* y = create_stack();
        for (int i = TOP(z)-x; i <= TOP(z); i++)
        {
            PUSH(y, z->stack[i]);
        }
        PUSH(s, convertARR(y));
    }
}

/**
 *\brief Esta é a função que é responsavel por procurar uma substring devolvendo o indice do seu inicio e -1 caso nao encontre.
 *
 *@param s stack que iremos alterar.
 *
 */
void substring(STACK *s)
{
    char* str1;
    char* str2;
    int op = operation_type(s,2);
    
    if(op == STRING)
    {
        str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        char *str3= strstr(str1,str2);
        if (str3==NULL) PUSH (s,convertI(-1));
        else {
        int x= str3-str1;
        PUSH (s,convertI(x));
        }
        free(str2);
        free(str1);
    }
}

/**
 *\brief Esta é a função que é responsavel por separar uma string em substrings.
 *
 *@param s stack que iremos alterar.
 *
 */
void separastring(STACK *s)
{
    char* str1;
    char* str2;
    int op = operation_type(s,2);
   
    if(op == STRING)
    {
        str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        char *str3 = malloc(sizeof(char*)*5);
        POP(s);
        POP(s);
        int j, t = strlen(str2);
        if(strstr(str1, str2))
        {
            for(int i = 0; str1[i];) {
                for(j = 0; strncmp(str1+j+i, str2, t-1) != 0  && str1[i+j]; j++) {
                    str3[j] = str1[j+i];
                }
                i += j + t;
                str3[j] = '\0';
                PUSH(s, convertS(str3));
            }
        }
    }
}

/**
 *\brief Esta é a função que é responsável por colocar o menor dos dois arrays no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void menordosArr(STACK *s)
{
    int op = operation_type(s,2);
    char* str1;
    char* str2;

    if (op == STRING)
	{
		str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        if (strcmp (str1,str2) < 0)
            PUSH (s,convertS(str1));
        else
            PUSH (s,convertS(str2));
	}
}

/**
 *\brief Esta é a função que é responsável por colocar o maior dos dois arrays no topo da stack.
 *
 *@param s stack que iremos alterar.
 *
 */
void maiordosArr(STACK *s)
{
    int op = operation_type(s,2);
    char* str1;
    char* str2;
    
    if (op == STRING)
    {
        str2 = s->stack[TOP(s)].types.s;
        str1 = s->stack[TOP(s)-1].types.s;
        POP(s);
        POP(s);
        if (strcmp (str1,str2) > 0)
            PUSH (s,convertS(str1));
        else
            PUSH (s,convertS(str2));
    }
}

/**
 *\brief Função que é responsável por realizar operações de comparacao correspondentes a um determinado token em elementos do tipo array.
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica qual a operação a ser realizada
 *
 */
void comparacoes_arr(STACK *s, int n)
{
    switch (n)
    {
    case 0:
        igualArr(s);
        break;
    case 1:
        menorArr(s);
        break;
    case 2:
        maiorArr(s);
        break;
    case 3:
        substring(s);
        break;
    case 4:
        separastring(s);
        break;
    case 7:
        menordosArr(s);
        break;
    default:
        maiordosArr(s);
        break;
    }
}

/**
 *\brief Função que é responsável por separar um array quando certos tokens ocorrem
 *
 *@param s stack onde vamos realizar as operações
 *@param n indica o elemento onde irá ocorrer
 *
 */
void separar_arr(STACK *s, int n)
{
    if (n==0) {
        char *delims = " ";
        char *str = s->stack [TOP(s)].types.s;
        POP(s);
        STACK *arr = create_stack();
        char *token = strtok (str,delims);
        for (;token != NULL; token = strtok (NULL,delims)){
            PUSH (arr,convertS(token));
        }
        PUSH (s,convertARR(arr));
    }
    else
    if (n==2) {
        char *delims = "\n";
        char *str= s->stack [TOP(s)].types.s;
        POP(s);
        STACK *ARR=create_stack();
        char *token = strtok (str,delims);

        for (;token != NULL; token = strtok (NULL,delims)) {
            PUSH (ARR,convertS(token));
        }
        PUSH (s,convertARR(ARR));
    }
}

/**
 *\brief Função que é responsável ver que tipo de operação vai realizar
 *
 *@param token apontador para a string
 *@param s stack onde vamos realizar as operações
 *
 */
void _num(char *token, STACK *s)
{
	char aritmeticas[9] = "+-*/%#(),";
	char a[7] = "&|<>^~=";
    int i;

	for(i = 0; (i < 9) && (*token != aritmeticas[i]); i++);
	if(i != 9) opAritmeticas_num(s, i);

	for(i = 0; (i < 7) && (*token != a[i]); i++);
	if(i != 7) opcomparacoes_num(s, i);

	for(i = 0; (i < 4) && (*(token+1) != a[i]); i++);
	if(i != 4) opcomparacoes2_num(s, i);
}

/**
 *\brief Função que é responsável ver que tipo de operação em arrays vai realizar
 *
 *@param token apontador para a string
 *@param s stack onde vamos realizar as operações
 *
 */
void _arr(char *token, STACK *s)
{
    int i;
	char a[6] = "+*,~()";
    char b[5] = "=<>#/";
	char c[4] = "S/N/";

	for(i = 0; (i < 6) && (*token != a[i]); i++);
	if(i != 6) op_arr(s, i);

    	for(i = 0; (i < 5) && (*token != b[i]); i++){}
	if(i != 5) comparacoes_arr(s, i);
	
	for(i = 1; (i < 3) && (*(token+1) != b[i]); i++){}
	if(i != 3) comparacoes_arr(s, i+6);

	for(i = 0; (i < 4) && (*token != c[i]) && (*(token+1) != c[i+1]); i += 2);
	if(i != 4)
    {
        separar_arr(s, i);
        *(token+2) = ' ';
        token = strtok(token, " \n\t");
    }

}

/**
 *\brief Função que é responsavel por decidir o tipo de operação globar (stack, converter tipos, leitura)irá realizar com base no token.
 *
 *@param token apontador para a string
 *@param s stack onde vamos colocar os valores
 *
 */
void decideGlobal(char *token, STACK *s, int *x)
{
    int i;
	char c[7] = ";_\\@?$!";
	char d[4] = "ifcs";
	char e[3] = "ltp";

    for(i = 0; (i < 7) && (*token != c[i]); i++){}
	if(i != 7) opStack(s, i), *x = 1;

	for(i = 0; (i < 4) && (*token != d[i]); i++);
	if(i != 4) convert(s, i), *x = 1;

	for(i = 0; (i < 3) && (*token != e[i]); i++);
	if(i != 3) leitura(s, i), *x = 1;
}

/**
 *\brief Função que é responsavel por decidir o tipo de operação irá realizar com base no token.
 *
 *@param token apontador para a string
 *@param s stack onde vamos colocar os valores
 *
 */
void decide(char *token, STACK *s, DATA* vars)
{
	int i; int x = 0;
	char var[27] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ:";

    decideGlobal(token, s, &x);

	for(i = 0; (i < 27) && (*token != var[i]); i++);
    if (*(token+1) != '/')
    {
        if(i < 26)
        {
            opvar(s, vars+i);
            x = 1;
        }
        else if (i == 26)
        {
            for(i = 0; (i < 26) && (*(token+1) != var[i]); i++);
            opcopyvar(s, vars+i);
            x = 1;
        }
    }
	if (x == 0)
	{
		x++;
        if (s->n_elems > 1)
            x++;
        if (operation_type(s,2) >= STRING)  {
			_arr (token, s);
        }
		else
			_num (token, s);
	}
}

/**
 *\brief Função que é responsavel por decidir o tipo de operação irá realizar com base no token.
 *
 *@param token apontador para a string
 *@param s stack onde vamos colocar os valores
 *
 *@returns string responsavel pela operaçao do bloco
 */
char* _block(char* token, STACK* s)
{
    char* str = malloc(sizeof(char)*500);
    strcpy(str, s->stack[TOP(s)].types.block);
    POP(s);
    str++;
    str[strlen(str)-1] = '\0';
    *(token+1) = ' ';
    char* str2 = malloc(sizeof(char)*500);
    if (*token == '~')
    {
        strcpy (str2, token+2);
        strcat (token, str);
        strcat (token, str2);
    }
    if (*token == '%')
    {
        if (operation_type(s,1) == STRING)
        {
            char* str3 = malloc(sizeof(char)*500);
            str3 = s->stack[TOP(s)].types.s;
            int x = strlen(str3);
            strcpy (str2, token+2);
            for (int i = 0; i < x; i++)
            {
                strcat (token, "( \\ :A ;");
                strcat (token, str);
                strcat (token, "A ");
            }
            strcat (token, str2);
        }
        else
        {
            STACK* x;
            x = s->stack[TOP(s)].types.arr;
            strcpy (str2, token+2);
            for (int i = 0; i < x->n_elems; i++)
            {
                strcat (token, "( \\ :A ;");
                strcat (token, str);
                strcat (token, "A ");
            }
            strcat (token, str2);
        }
    }

    return token;
}

/**
 *\brief Função que é responsavel por criar uma string 
 *
 *@param token apontador para a string
 *@param sobra apontador para string que sobra
 *
 */
char *_str(char *token,char *sobra)
{
    char *str;
    str = malloc(sizeof(char*)*200);
    token++;
    sobra++;
    while (*token != '\"')
	{
		if (*token == '\0')
        	*token = ' ';
		token++;
	}
	*token = '\0';
    strcpy(str, sobra);
	*token = '\"';
    if (*(token+1) == '\0')
    {
        *(token+1) = ' ';
    }
	token = strtok(token," \t\n");
	return str;
}

/**
 *\brief Função que é responsavel por separar o conteudo do array
 *
 *@param token apontador para a string
 *@param sobra apontador para string que sobra
 *
 */
char *_str2(char *token,char *sobra)
{
    char *str;
    str = malloc(sizeof(char*)*1000);
    token++;
    sobra++;
    while (*token != ']')
	{
		if (*token == '\0')
        	*token = ' ';
		token++;
	}
	*token = '\0';
    strcpy(str, sobra);
	*token = ']';
    if (*(token+1) == '\0'){
        *(token+1) = ' ';
    }
	return str;
}

/**
 *\brief Função que é responsavel por separar o conteudo do bloco
 *
 *@param token apontador para a string
 *@param sobra apontador para string que sobra
 *
 */
char* _str3(char* token)
{
    while (*token != '}')
    {
        if (*token == '\0')
            *token = ' ';
        token++;
    }
    return token;
}

/**
 *\brief Função que identifica o tipo dos elementos e e responsavel por chamar a funcao decide.
 *
 *@param line linha da qual se vai fazer o parse
 *@param s stack que sera utilizada para as funcoes.
 *
 *@returns Esta função nao retorna nada.
 */
void parse(char *line, STACK *s, DATA *vars);

/**
 *\brief Função que verifica se o token é um numero, se for irá dar push para a stack.
 *
 *@param token token que sera analisado.
 *@param s stack que sera utilizada para as funcoes.
 *@param sobra resto que sera analizado depois
 *
 *@returns Esta função nao retorna nada.
 */
void pushNum(STACK *s, char *token, char *sobra)
{
    if (*sobra != '.')
    {
        long val_i = strtol (token, &sobra, 10);
        if (strlen (sobra)==0){
            PUSH (s, convertI(val_i));
        }
    }
    else
    {
        double val_f = strtod (token,&sobra);
        if (strlen (sobra)==0)
            PUSH (s, convertF(val_f));
    }
}

/**
 *\brief Função responsável por criar um array.
 *
 *@param token token que sera analisado.
 *@param s stack que sera utilizada para as funcoes.
 *@param sobra resto que sera analizado depois
 *@param vars variaveis globais.
 *
 *@returns Esta função nao retorna nada.
 */
void array(STACK *s, char *token, char *sobra, DATA *vars)
{
    char *str;
    str = _str2(token, sobra);
    char* str2 = malloc(sizeof(char*)*1000);
    strcpy(str2,str);
    s->stack[s->n_elems].types.arr = create_stack();
    s->stack[s->n_elems].type = ARR;
    s->n_elems++;
    parse(str, s->stack[TOP(s)].types.arr,vars);
    token = strtok(token, str2);
    token = strtok(NULL, str2);
    free(str2);
}
/**
 *\brief Função responsável por criar um bloco.
 *
 *@param token token que sera analisado.
 *@param s stack que sera utilizada para as funcoes.
 *@param sobra resto que sera analizado depois
 *
 *@returns Esta função nao retorna nada.
 */
void block(STACK *s, char *token, char *sobra)
{
    char *str = malloc(sizeof(char)*100);
    token = _str3(token);
    *(token+1) = '\0';
    strcpy(str,sobra);
    PUSH(s, convertBLOCK(str));
    *(token+1) = ' ';
    token = strtok(token, " \n\t");
}

/**
 *\brief Função responsável por efetuar uma operaçãp sobre um bloco.
 *
 *@param token token que sera analisado.
 *@param s stack que sera utilizada para as funcoes.
 *@param sobra resto que sera analizado depois
 *@param vars variaveis globais.
 *
 *@returns Esta função nao retorna nada.
 */
void opBlock(STACK *s, char *token, char *sobra)
{
    char* str;
    sobra++;
    *(token-2) = '\0';
    str = malloc(sizeof(char)*100);
    strcpy(str, sobra);
    token = _block (token, s);
    free(str);
    *(token-2) = '~';
    *(token-1) = ' ';
    token -= 2;
}

/**
 *\brief Função que identifica o tipo dos elementos e e responsavel por chamar a funcao decide.
 *
 *@param line linha da qual se vai fazer o parse
 *@param s stack que sera utilizada para as funcoes.
 *
 *@returns Esta função nao retorna nada.
 */
void parse(char *line, STACK *s, DATA *vars){
    char *delims = " \t\n";
    char *token = strtok (line,delims);

    for (;token != NULL; token = strtok (NULL,delims)) {
        char *sobra;  
        strtol (token, &sobra, 10);

        pushNum(s, token, sobra);

        if (*token < '0' || *token > '9') 
        {
            if(*token == '-' && (*(token+1) >= '0' && *(token+1) <= '9'));
            else
            if (*token == '\"') // quando e introduzido o carater "
            {
                char *str;
                str = _str(token, sobra);
                PUSH(s, convertS(str));
            }
            else
            if (*token == '[') // quando e introduzido o carater [
            {
                array(s, token, sobra, vars);
            }
            else
            if (*token == '{') // quando e introduzido o carater {
            {
                block(s, token, sobra);
            }
            else
            if ((operation_type(s,1) == BLOCK) && (*token == '~' || *token == '%' || *token == '*' || *token == ',' || *token == '$' || *token == 'w'))
            {
                opBlock(s, token, sobra);
            }
            else
                decide(token, s, vars);
        }
    }
}

/**
 *\brief Esta é a função que é responsável por imprimir toda a stack de forma a permitir ao utilizador ver o output esperado.
 *
 *@param s stack que vamos imprimir.
 *
 *@returns Esta função nao retorna nenhum valor.
 */
void print_stack(STACK *s)
{
    for (int i = 0; i < s->n_elems; i++)
    {
        DATA elem = s->stack[i];
        switch (elem.type)
        {
        case INT:
            printf("%ld", elem.types.i);
            break;
        case FLOAT:
            printf("%g",elem.types.f);
            break;
        case CHAR:
            printf("%c",elem.types.c);
            break;
        case STRING:
            printf("%s",elem.types.s);
            break;
        case ARR:
            print_stack(s->stack[i].types.arr);
            break;
        case BLOCK:
            printf("%s",elem.types.block);
        }
    }
}

/**
 *\brief Esta e a função  que e responsavel por tratar dos inputs e chamar as respetivas auxiliares de forma a colocar os elementos da stack e dar o output final esperado.
 *
 *@returns o valor 0.
 */
int main()
{
    char line [10240];
    DATA vars[26];
    var_init (vars);
    assert (fgets (line,10240,stdin)!= NULL);
    assert (line [strlen (line)-1]=='\n');
    STACK *s = create_stack();
    parse(line, s,vars);
    print_stack(s);
    putchar('\n');
    return 0;
}
