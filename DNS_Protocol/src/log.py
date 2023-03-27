import sys
from threading import Lock
from datetime import datetime

def time_now():
    time = datetime.now()
    return f"{time.day}:{time.month}:{time.year}.{time.hour}:{time.minute}:{time.second}:{time.microsecond}"

class Logs():

    def __init__(self, ficheiro, localhost_ip, timeout, tempo_inicial, tempo_config, debug):
        self.lock = Lock()
        self.localhost_ip = localhost_ip
        self.ficheiro = ficheiro
        self.debug = debug
        timeout = str(timeout)
        time_log = time_now()
        try:
            f = open(ficheiro, "x")
            if self.debug == True:
                put = self.estrutra(tempo_inicial, "ST", localhost_ip, f"{timeout} debug")
            else:
                put = self.estrutra(tempo_inicial, "ST", localhost_ip, f"{timeout}")
            f.write(put)
            if self.debug:
                print(put)
            if tempo_config != None:
                put = self.estrutra(tempo_config, "EV", "@", f"conf-file-read {sys.argv[1]}")
            f.write(put)
            if self.debug:
                print(put)
            put = self.estrutra(time_log, "EV", "@", f"log-file-create {self.ficheiro}")
            f.write(put)
            if self.debug:
                print(put)
            f.close()
        except:
            f = open(ficheiro, "a")
            if self.debug == True:
                put = self.estrutra(tempo_inicial, "ST", localhost_ip, f"{timeout} debug")
            else:
                put = self.estrutra(tempo_inicial, "ST", localhost_ip, f"{timeout}")
            f.write(put)
            if self.debug:
                print(put)
            if tempo_config != None:
                put = self.estrutra(tempo_config, "EV", "@", f"conf-file-read {sys.argv[1]}")
            f.write(put)
            if self.debug:
                print(put)
            put = self.estrutra(time_log, "EV", "@", f"log-file-read {self.ficheiro}")
            f.write(put)
            if self.debug:
                print(put)
            f.close()

    def estrutra(self, tempo, tipo, ip_porta, dados):
        return f"{tempo} {tipo} {ip_porta} {dados}\n"

    def put(self, tempo, tipo, ip_porta, dados):
        with self.lock:
            with open(self.ficheiro, "a") as f:
                log = self.estrutra(tempo, tipo, ip_porta, dados)
                if self.debug:
                    print(log)
                f.write(log)

    def qr(self, tempo, ip, dados):
        self.put(tempo, "QR", ip, dados)

    def qe(self, tempo, ip, dados):
        self.put(tempo, "QE", ip, dados)

    def rp(self, tempo, ip, dados):
        self.put(tempo, "RP", ip, dados)

    def rr(self, tempo, ip, dados):
        self.put(tempo, "RR", ip, dados)

    def zt(self, tempo, ip, dados):
        self.put(tempo, "ZT", ip, dados)


    def ev(self, tempo, dados):
        self.put(tempo, "EV", "@", dados)


    def er(self, tempo, ip, dados):
        self.put(tempo, "ER", ip, dados)


    def ez(self, tempo, ip, dados):
        self.put(tempo, "EZ", ip, dados)


    def fl(self, tempo, ip, dados):
        self.put(tempo, "FL", ip, dados)


    def to(self, tempo, ip, dados):
        self.put(tempo, "TO", ip, dados)


    def sp(self, tempo, ip, dados):
        self.put(tempo, "SP", ip, dados)

