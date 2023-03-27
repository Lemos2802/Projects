#######################################
#   Comunicações por Computador       #
#   Servidor Principal                #
#   Simão, Hugo e António             #
#######################################

import socket
import sys
import time
from DNS_binEncoder import dnsDecode, dnsEncode
from DNS_parser import parseConfig, parseDB
from threading import Thread

import netifaces as ni

from log import Logs, time_now
from queryHandler import protocolo_tz, handleQuery


class ServidorPrincipal():

    def __init__(self, time_inicial):
        time_read_config = time_now()
        self.db_path = sys.argv[1]
        self.config = parseConfig(sys.argv[2])
        self.host_ip = str(ni.ifaddresses('eth0')[ni.AF_INET][0]['addr'])
        self.debug = True
        self.logs = Logs(self.config["LG"], self.host_ip, 0, time_inicial, time_read_config, self.debug)  # falta adicionar timeout para servidor
        time_bd = time_now()
        self.db = parseDB(self.db_path)
        self.logs.ev(time_bd, self.db_path)

    def __init__(self, time_inicial, config_path, bd_path, debug):
        time_read_config = time_now()
        self.db_path = bd_path
        self.config = parseConfig(config_path)
        self.host_ip = str(ni.ifaddresses('eth0')[ni.AF_INET][0]['addr'])
        self.debug = debug
        self.logs = Logs(self.config["LG"], self.host_ip, 0, time_inicial, time_read_config, self.debug)  # falta adicionar timeout para servidor
        time_bd = time_now()
        self.db = parseDB(bd_path)
        self.logs.ev(time_bd, bd_path)

    def readDB(self):
        f = open(self.db_path,'r')
        texto = f.readlines()
        
        newtexto = list()
        for line in texto:
            if line[0] not in ["\n", "#"]:
                newtexto.append(line)
        return newtexto

    def tzSP(self, connection, msg):
        lines = self.readDB()
        bytes_sended = 0
        if msg[0] == '0':
            msg = protocolo_tz(1, 0, str(len(lines)))
            bytes_sended = connection.send(msg.encode('utf-8'))
        elif msg[0] == '1':
            x = 0
            for line in lines:
                msg = protocolo_tz(2, x, line)
                bytes_sended = connection.send(msg.encode('utf-8'))
                x += 1
                time.sleep(0.1)
        else:
            print("erro, algo de errado nao ta certo")
        return bytes_sended

    def processamento_tcp(self, connection, add):
        msg = connection.recv(1024)
        inicial_time = time_now()
        duration_time = int(round(time.time()*1000))
        bytes_sended = int()
        while msg:
            msg = msg.decode('UTF-8')
            bytes_sended += self.tzSP(connection, msg.split(';'))
            msg = connection.recv(1024)
        duration_time = duration_time - int(round(time.time()*1000))
        self.logs.zt(inicial_time, add, f"SP; ZT duration time in milliseconds:{duration_time}; bytes sended:{bytes_sended}")
        connection.close()

    def tcp_connection(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        porta = 5353
        s.bind((self.host_ip, porta))
        while True:
            s.listen()

            print(f"Estou à escuta no {self.host_ip}:{porta}")

            connection, address = s.accept()
            connection.send

            Thread(target=self.processamento_tcp, args=(connection, address)).start()

    def processamento_udp(self, msg, add):
        r = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        msg_resposta = handleQuery(msg, self.db)
        time_RP = time_now()
        r.sendto(dnsEncode(msg_resposta), add)
        self.logs.rp(time_RP, add, msg_resposta)
        r.close()

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
            Thread(target=self.processamento_udp, args=(msg,add)).start()

        s.close()

def main():
    time_inicial = time_now()
    sp = ServidorPrincipal(time_inicial)
    Thread(target=sp.tcp_connection, args=()).start()
    sp.udp_connection()

if __name__ == "__main__":
    main()

#singleton estrutura para o multithreading
