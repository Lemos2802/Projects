#######################################
#   Comunicações por Computador       #
#   Servidor Secundario               #
#   Simão, Hugo e António             #
#######################################

import socket
import sys
from DNS_binEncoder import dnsDecode, dnsEncode
from DNS_parser import parseConfig, parseDB2
from threading import Thread, Lock
import time

import netifaces as ni

from queryHandler import printDB, protocolo_tz, handleQuery
from log import Logs, time_now

class ServidorSecundario():

    def __init__(self, time_inicial):
        time_read_config = time_now()
        self.config = parseConfig(sys.argv[1])
        self.host_ip = str(ni.ifaddresses('eth0')[ni.AF_INET][0]['addr'])
        self.debug = True
        self.logs = Logs(self.config["LG"], self.host_ip, 0, time_inicial, time_read_config, self.debug)  # falta adicionar timeout para servidor
    
    def __init__(self, time_inicial, config, debug):
        time_read_config = time_now()
        self.config = parseConfig(config)
        self.host_ip = str(ni.ifaddresses('eth0')[ni.AF_INET][0]['addr'])
        self.debug = debug
        self.logs = Logs(self.config["LG"], self.host_ip, 0, time_inicial, time_read_config, self.debug)  # falta adicionar timeout para servidor
        self.lock = Lock()

    # transferencia de zona entre SS e SP para rececao da base de dados
    def tzSS(self):

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        [ip,porta] = self.config["SP"].split(":")
        s.connect((ip,int(porta)))

        dominio = self.config["DOMAIN"][0]
        msg = protocolo_tz(0,0,dominio)
        s.send(msg.encode('utf-8'))

        msg, add = s.recvfrom(1024)
        msg = msg.decode('utf-8')

        resposta = msg.split(';')
        if resposta[0] == '1':
            msg = protocolo_tz(1, int(resposta[1]) + 1, resposta[2])
            s.send(msg.encode('utf-8'))
        else:
            print("erro, algo de errado nao ta certo")	# nao sei como fazer um erro
        bd = ""
        for x in range(int(resposta[2])):
            msg = s.recv(1024)
            msg = msg.decode('utf-8')
            resposta = msg.split(";")
            if resposta[0] == '2':
                bd = bd + resposta[2]
            else:
                print("erro, algo de errado nao ta certo_2!")
        s.close()
        time_tz = time_now()
        self.bd = parseDB2(bd)
        self.logs.zt(time_tz, f"{ip}:{porta}", "")

    def processamento_udp(self, msg, add, r):
        msg_resposta = handleQuery(msg, self.db)
        time_RP = time_now()
        r.sendto(dnsEncode(msg_resposta), add)
        self.logs.rp(time_RP, add, msg_resposta)
        r.close()

    # conexao udp para responder a queries efetuadas pelo cliente
    def udp_connection(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        porta = 5353
        s.bind((self.host_ip, porta))

        print(f"Estou Ã  escuta no {self.host_ip}:{porta}")

        while True:
            msg, add = s.recvfrom(1024)
            time_QR = time_now()
            self.logs.qr(time_QR, add, msg) #rever o add
            msg = dnsDecode(msg)
            print(f"Recebi uma mensagem do cliente {add}")
            r = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            Thread(target=self.processamento_udp, args=(msg, add, r)).start()

        s.close()

    def timeout(self, l):
        refresh = int(self.bd['SOAREFRESH'][0][2])
        retry = int(self.bd['SOARETRY'][0][2])
        expire = int(self.bd['SOAEXPIRE'][0][2])
        inicial_time = time.time()
        while True:
            time.sleep(refresh)
            while True and time.time()-(inicial_time+expire) < 0:
                try:
                    self.tzSS()
                    break
                except:
                    time_ez = time_now()
                    [ip,porta] = self.config["SP"].split(":")
                    self.logs.ez(time_ez, f"{ip}:{porta}", "SS")
                time.sleep(retry)
            if time.time()-(inicial_time+expire) >= 0:
                with l:
                    while True:
                        try:
                            self.tzSS()
                            break
                        except:
                            time_ez = time_now()
                            [ip,porta] = self.config["SP"].split(":")
                            self.logs.ez(time_ez, f"{ip}:{porta}", "SS")

def main():
    time_inicial = time_now()
    ss = ServidorSecundario(time_inicial)
    try:
        ss.tzSS()
        print("base de dados recebida") 
        printDB(ss.bd)
    except:
        time_ez = time_now()
        [ip,porta] = ss.config["SP"].split(":")
        ss.logs.ez(time_ez, f"{ip}:{porta}", "SS")

    ss.udp_connection()

if __name__ == "__main__":
    main()
