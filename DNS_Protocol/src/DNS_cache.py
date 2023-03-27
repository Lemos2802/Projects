from threading import Lock
from time import time

class DNS_cache:

    def __init__(self):
        # self.cache = {'DOMAIN':[],'TTL':[],'SOASP':[],'SOAADMIN':[],'SOASERIAL':[],'SOAREFRESH':[],'SOARETRY':[],'SOAEXPIRE':[],'NS':[],'MX':[],'A':[],'CNAME':[], 'PTR':[]}
        self.cache = {}
        self.lock = Lock()

    def cacheEmpty(self):
        with self.lock:
            if (self.cache == {}):
                return True
            else: False

    def search_cache(self, query):
        query = query.split(';')
        query = query[1]
        try:
            with self.lock:
                try:
                    return self.cache[query]
                except KeyError:
                    return None
        except NameError:
            return None

    # para adicionar as mensagens à cache
    def addCache(self, resposta, query):
        # resposta = resposta.split(';')
        # resposta = resposta[:-1][2:]
        # time_ = time()
        # with self.lock:
        #     for line in resposta:
        #         parts = line.split(' ')
        #         self.cache[parts[1]].append((parts,time_))
        query = query.split(';')
        query = query[1]
        self.cache[query] = resposta
