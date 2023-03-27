#######################################
#   Comunicações por Computador       #
#   SR                                #
#   Criado a  12 de dezembro          #
#   Ultima versão 2 de Janeiro        #
#   Simão, Hugo e António             #
#######################################
import socket
# ficheiro de código relativo ao Servidor de Resolução (SR)

# Os vários imports que utilizamos a desenvolver
import sys # vai receber como argumento a lista de servidores de topo e uma flag sobre o modo debug
import time
from DNS_binEncoder import dnsDecode, dnsEncode # para ttimeout e ttl
from DNS_parser import * # importar a nossa biblioteca de parser (modularidade)
import netifaces as ni
from threading import Thread 
from log import Logs, time_now
from DNS_cache import DNS_cache
from threading import Thread


# python3 servidor_resolucao.py ./ListaST/st.txt -debug

# classe do servidor de resolução
class ServidorResolucao:

    def __init__(self, modoDebug):
        time_inicial = time_now()
        self.debug = modoDebug
        self.SDT = parseSDT(sys.argv[1])
        self.host_ip = str(ni.ifaddresses('eth0')[ni.AF_INET][0]['addr'])
        self.logs = Logs("./log/all-log.txt", self.host_ip, 0, time_inicial, None, self.debug)
        self.cache = DNS_cache()

    def processamento_query(self, query, client_add):
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        cache = self.cache.search_cache(query)
        if cache is not None:
            response = '-1'
            msg_recebida = cache
        else:
            i = 0
            servers_to_send = self.SDT
            response = '1'
            self.STcomm(query, s, 0)
        while response == '1':
            try:
                s.settimeout(1)
                msg_recebida, add = s.recvfrom(1024)
                s.settimeout(None)
                msg_recebida = dnsDecode(msg_recebida)
                self.logs.rr(time_now(),add,msg_recebida)
                response = self.tratarResposta(msg_recebida)
                if response == '1':
                    servers_to_send = self.server_to_send(msg_recebida, servers_to_send)
                    i = 0
                    self.SDTcomm(query, s, servers_to_send[i])
            except socket.timeout:
                s.settimeout(None)
                if i != len(servers_to_send)-1:
                    i += 1
                    self.SDTcomm(query, s, servers_to_send[i])
                else:
                    # nenhum dos Servidores e alcançavel
                    msg_recebida = "DNS Error, erro de conexao com os servidores"
                    response = '-1'
            except Exception as e:
                if e.args[0] == "loop error":
                    msg_recebida = "DNS Error, DNS loop"
                else:
                    msg_recebida = "DNS Error"
                response = '-1'
        if cache == None:
            self.cache.addCache(msg_recebida, query)
        if response == '-1':
            s.sendto(msg_recebida.encode('utf-8'), client_add)
        else:
            s.sendto(dnsEncode(msg_recebida), client_add)
        s.close()

    # recebe a comunicação do cliente
    def Clicomm(self):
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            porta = 5353
            s.bind((self.host_ip, porta))

            print(f"Estou a escuta no {self.host_ip}:{porta}")

            while True:
                msg, add = s.recvfrom(1024)
                time_QR = time_now()
                self.logs.qr(time_QR, add, msg)
                msg = dnsDecode(msg)
                print(f"Recebi uma mensagem do cliente {add}")
                Thread(target=self.processamento_query, args=(msg, add)).start()
            s.close()


    # por enquanto só comunica com um servidor de topo, fazer CENA DO TIMEOUT PARA COMUNICAR COM MAIS DO QUE UM
    def STcomm(self, msg, s, i):
        time_QE = time_now()
        self.logs.qe(time_QE, self.SDT[i]+":5353", msg)
        s.sendto(dnsEncode(msg), (self.SDT[i], 5353))

    def SDTcomm(self, msg, s, i):
        time_QE = time_now()
        self.logs.qe(time_QE, i+":5353", msg)
        s.sendto(dnsEncode(msg), (i, 5353))

    def AScomm(self):
        pass

    #verfica se a resposta recebida é a resposta final (temos de comparar dominios)
    def verificaRF(self):
        pass

    # devolve o ip dos servidores em extra_values
    def server_to_send(self, msg, servers_antigos):
        msg = msg.split(';')
        extra_values = msg[4].split(',')
        r = []
        for x in extra_values:
            x = x.split(' ')
            if x[2] in servers_antigos:
                raise Exception(("loop error",))
            r.append(x[2])
        return r

    # tratar da resposta recebida
    def tratarResposta(self, msg_recebida):
        msg = msg_recebida.split(';')
        header = msg[0].split(',')
        return header[2]
        

def main():
    if(len(sys.argv) > 2):
        modoDebug = True
    else:
        modoDebug = False
    sr = ServidorResolucao(modoDebug)
    sr.Clicomm()


if __name__ == "__main__":
    main()

