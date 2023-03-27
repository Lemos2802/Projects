#######################################
#   Comunicações por Computador       #
#   Cliente                           #
#   Criado a  26 de outubro           #
#   Ultima versão 23 de Novembro      #
#   Simão, Hugo e António             #
#######################################

# Os vários imports das bibliotecas que vamos utilizando neste ficheiro
import random  # para termos o id das queries
import socket  # permite a conexão entre o Cliente e o Servidor
import sys  # para acedermos aos argumentos passados no arranque do programa
import time  # para utilizar o time out
import signal
from DNS_binEncoder import dnsDecode, dnsEncode

from queryHandler import *

timeinicio = time.time()

def timeout_handler(signum, frame):
    raise TimeoutError("TO")


def main():
    # controlo do SIGALARM para tratar do timeout
    signal.signal(signal.SIGALRM, timeout_handler)

    # Abrir o socket para a comunicação servidor cliente e vice-versa
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # exemplo: python3 cliente.py 10.3.3.3 example.com MX 5353 1000

    # o primeiro argumento passado no terminal é o endereço do servidor a contactar
    endereco = sys.argv[1]

    # o segundo é o dominio do qual queremos obter informações
    dominio = sys.argv[2]

    # o terceiro é tipo de query que queremos (se é MX, A, etc)
    type = sys.argv[3]

    # o quarto é a porta da qual abrimos o socket (é a 5353 por default)
    portaAux = sys.argv[4]

    # o valor da porta precisa de estar em int
    if (portaAux == "-1"):
        porta = 5353
    else: porta = int(portaAux)


    # para o id da query ser um número random (segundo o enunciado precisa de estar entre 1 e 65535
    id = str(random.randint(1,65535))

    # junta a mensagem a ser enviada numa única string (algo a rever aquando da implementação da codificação binária)
    msg_enviada = id + ",Q,0,0,0,0;" + dominio + "," + type + ";"

    #dá print da mensagem enviada (usado para efeitos de debug)
    print("Query enviada:")
    print(msg_enviada)


    #guarda o tempo de envio para efeitos de timout
    # timeenvio = time.time()

    #envia a mensagem para o sítio especificado
    s.sendto(dnsEncode(msg_enviada), (endereco,porta))

    # print de para quem a querie foi enviada
    print(f"\nEstou à escuta no {endereco}:{porta}")

    # timeout de 20 segundos iniciado
    signal.alarm(20)

    # a resposta à querie (se o timeout tiver ultrapassado é descartada)
    try:
        msg_recebida, add = s.recvfrom(1024)
        signal.alarm(0)
        # prints da mensagem recebida para debug
        print(f"Recebi uma resposta do servidor {add}")
        msg = dnsDecode(msg_recebida)
        displayReply(msg)
    except IndexError as ie:
        print(msg_recebida.decode('utf-8'))
    except TimeoutError:
        print("Nao foi possivel obter resposta do servidor.\nPor favor tente de novo.")
    finally:
        # cancelar a contagem do timeout
        signal.alarm(0)


    #fechamos o socket e acabamos com o programa
    s.close()


if __name__ == "__main__":
    main()
