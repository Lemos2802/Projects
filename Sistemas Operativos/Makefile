FLAGS = -O2 -g -w
COMPILER=gcc
INCLUDES=src/includes
SRC=src
OBJ=obj

all: sdstored sdstore

server: sdstored

client: sdstore

sdstore:$(SRC)/sdstore.c $(INCLUDES)/pedido.h
	$(COMPILER) $(FLAGS) $(SRC)/sdstore.c -o sdstore

sdstored:$(SRC)/sdstored.c $(INCLUDES)/pedido.h
	$(COMPILER) $(FLAGS) $(SRC)/sdstored.c -o sdstored

clean:
		rm sdstore sdstored pipes/*