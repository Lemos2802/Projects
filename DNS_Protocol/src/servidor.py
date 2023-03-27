#######################################
#   Comunicações por Computador       #
#   Servidor                          #
#   Simão, Hugo e António             #
#######################################

import socket
import sys
from threading import Thread, Lock
from DNS_binEncoder import dnsDecode, dnsEncode
from queryHandler import handleQuery, printDB
from log import time_now
from servidor_principal import ServidorPrincipal
from servidor_secundario import ServidorSecundario
import time

# def query_recursiva(servidor, query, client_add):
#     s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
#     cache = servidor.cache.search_cache(query)
#     if cache is not None:
#         response = '0'
#     else:
#         i = 0
#         servers_to_send = servidor.SDT
#         response = '-1'
#         servidor.STcomm(query, s, 0)
#     while response != '0':
#         try:
#             s.settimeout(1)
#             msg_recebida, add = s.recvfrom(1024)
#             s.settimeout(None)
#             msg_recebida = dnsDecode(msg_recebida)
#             response = servidor.tratarResposta(msg_recebida)
#             if response == '1':
#                 servers_to_send = servidor.server_to_send(msg_recebida)
#                 i = 0
#                 servidor.SDTcomm(query, s, servers_to_send[i])
#         except socket.timeout:
#             s.settimeout(None)
#             if i != len(servers_to_send)-1:
#                 i += 1
#                 servidor.SDTcomm(query, s, servers_to_send[i])
#             else:
#                 # nenhum dos ST e alcançavel
#                 msg_recebida = "DNS Error, erro de conexao com servidores de topo"
#                 response = 0
#         except:
#             response = 0
#             msg_recebida = "DNS Error"
#     print(msg_recebida)
#     s.sendto(dnsEncode(msg_recebida), client_add)
#     s.close()

def processamento_udp(servidor, msg, add):
    r = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    msg_resposta = handleQuery(msg, servidor.db)
    time_RP = time_now()
    r.sendto(dnsEncode(msg_resposta), add)
    servidor.logs.rp(time_RP, add, msg_resposta)
    r.close()

def processamento_udp_ss(servidor, msg, add):
    r = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    with servidor.lock:
        msg_resposta = handleQuery(msg, servidor.bd)
    time_RP = time_now()
    r.sendto(dnsEncode(msg_resposta), add)
    servidor.logs.rp(time_RP, add, msg_resposta)
    r.close()

# conexao udp para responder a queries efetuadas pelo cliente
def udp_connection(servidor):
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        porta = 5353
        s.bind((servidor.host_ip, porta))

        print(f"Estou a escuta no {servidor.host_ip}:{porta}")

        while True:
            msg, add = s.recvfrom(1024)
            time_QR = time_now()
            servidor.logs.qr(time_QR, add, msg)
            msg = dnsDecode(msg)
            print(f"Recebi uma mensagem do cliente {add}")
            Thread(target=processamento_udp, args=(servidor, msg, add)).start()

# conexao udp para responder a queries efetuadas pelo cliente
def udp_connection_ss(servidor, l):
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        porta = 5353
        s.bind((servidor.host_ip, porta))

        print(f"Estou a escuta no {servidor.host_ip}:{porta}")

        while True:
            with l:
                msg, add = s.recvfrom(1024)
                time_QR = time_now()
                servidor.logs.qr(time_QR, add, msg)
                msg = dnsDecode(msg)
                print(f"Recebi uma mensagem do cliente {add}")
                Thread(target=processamento_udp_ss, args=(servidor, msg, add)).start()

def exec_SP(config, bd, debug, root):
    time_inicial = time_now()
    sp = ServidorPrincipal(time_inicial, config, bd, debug)
    if not root:
        Thread(target=sp.tcp_connection, args=()).start()
    udp_connection(sp)

def exec_SS(config, debug):
    time_inicial = time_now()
    ss = ServidorSecundario(time_inicial, config, debug)
    while True:
        try:
            ss.tzSS()
            break
        except:
            time_ez = time_now()
            [ip,porta] = ss.config["SP"].split(":")
            ss.logs.ez(time_ez, f"{ip}:{porta}", "SS")
            time.sleep(60)
    if debug:
        printDB(ss.bd)
    l = Lock()
    Thread(target=ss.timeout, args=[l]).start()
    udp_connection_ss(ss, l)

def main():
    i = len(sys.argv)
    if i > 3:
        try:
            i = sys.argv.index('-c')
            config = sys.argv[i+1]
            if sys.argv.count('-d'):
                debug = True
            else:
                debug = False
            if sys.argv.count('-s') == 1:
                exec_SS(config, debug)
            elif sys.argv.count('-p') == 1:
                i = sys.argv.index('-bd')
                bd = sys.argv[i+1]
                if sys.argv.count('-root') == 1: # e importante saber quando e um servidor root para pois nao existe transferencia de zona
                    exec_SP(config,bd,debug,True)
                exec_SP(config,bd,debug,False)
            else:
                raise Exception("'Nao foi especificado o tipo de servidor de maneira correta",)
        except Exception as e:
            x = e.args
            x = x[0]
            x = x.split("'")
            x = x[1]
            if x == '-c':
                print("Erro, deve especificar o caminho do ficheiro de configuracao ('-c <pathtoconfig>')")
            elif x == '-bd':
                print("Erro, deve especificar o caminho da Base de dados ('-bd <pathtobd>')")
            else:
                print(f"Erro, {x}")
    else:
        print("O servidor, se for servidor principal, deve receber o caminho da Base de dados ('-bd <pathtobd>'), ", 
                "o caminho do ficheiro de configuracao ('-c <pathtoconfig>') e ", 
                "uma flag a indicar se e servidor principal ou secundario ('-p' or '-s').")

if __name__ == "__main__":
    main()
