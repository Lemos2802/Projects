from Node import Node
import math
from queue import Queue
import math
import re 


# Biblioteca de tratamento de grafos necessária para desenhar graficamente o grafo
import networkx as nx
# Biblioteca de tratamento de grafos necessária para desenhar graficamente o grafo
import matplotlib.pyplot as plt


class Grafo:

    def __init__(self, directed=False):
        self.m_nodes = []
        self.m_directed = directed
        self.m_graph = {}  # dicionario para armazenar os nodos e arestas
        self.m_h = {}  # dicionario para posterirmente armazenar as heuristicas para cada nodo -< pesquisa informada

  
    def __str__(self):
        out = ""
        for key in self.m_graph.keys():
            out = out + "node" + str(key) + ": " + \
                str(self.m_graph[key]) + "\n"
            return out

 

    def get_node_by_name(self, name,coord,vel):
        search_node = Node(name,coord,vel)
        for node in self.m_nodes:
            if node == search_node:
                return node
            else:
                return None


    def imprime_aresta(self):
        listaA = ""
        lista = self.m_graph.keys()
        for nodo in lista:
            for (nodo2, custo) in self.m_graph[nodo]:
                listaA = listaA + nodo + " ->" + \
                    nodo2 + " custo:" + str(custo) + "\n"
        return listaA


    def nome_form(self,coord,vel):
        return "(" + str(coord[0]) + "," + str(coord[1]) + ")" + ";" + "(" + str(vel[0]) + "," + str(vel[1]) + ")" 

    def add_edge(self, node1, node2,coord1,coord2,vel1,vel2,weight):
        n1 = Node(node1,coord1,vel1)
        node2 = self.nome_form(coord2,vel2)
        n2 = Node(node2,coord2,vel2)
        if (n1 not in self.m_nodes):
            self.m_nodes.append(n1)
            self.m_graph[node1] = list()
        
        if (n2 not in self.m_nodes):
            self.m_nodes.append(n2)
            self.m_graph[node2] = list()
        
        if (node2,weight) not in self.m_graph[node1]:
            self.m_graph[node1].append((node2, weight))



    def getNodes(self):
        return self.m_nodes



    def get_arc_cost(self, node1, node2):
        custoT = math.inf
        a = self.m_graph[node1]  # lista de arestas para aquele nodo
        for (nodo, custo) in a:
            if nodo == node2:
                custoT = custo

        return custoT



    def calcula_custo(self, caminho):
        # caminho é uma lista de nodos
        teste = caminho
        custo = 0
        i = 0
        while i + 1 < len(teste):
            custo = custo + self.get_arc_cost(teste[i], teste[i + 1])
            # print(teste[i])
            i = i + 1
        return custo

    def in_caminho(self,coord_nodo,caminho,nivel):
        #coord_nodo = '(' + coord_nodo[0] + ',' + coord_nodo[1] + ')'
        #print(coord_nodo,caminho)

        if len(caminho) <= nivel:
            return False
        if coord_nodo == caminho[nivel]:
            print("ok")
            return True    
        else: 
            return False
    
    def procura_DFS(self, start, end, path, visited,expansion,caminho ,nivel = 0):
        path.append(start)
        visited.add(start)
        
        expansion.append(start)
        x = start.split(";")
        lista = re.split(r'[ \s(),;]',x[0])
                    #print(lista)
        a = int(lista[1])
        b = int(lista[2])
        

        if x[0] in end:
            # calcular o custo do caminho funçao calcula custo.
            custoT = self.calcula_custo(path)
            return (path, custoT,expansion)
        
        for (adjacente, peso) in self.m_graph[start]:
            if adjacente not in visited and self.in_caminho((a,b),caminho,nivel) == False:
                resultado = self.procura_DFS(adjacente, end, path, visited,expansion,caminho,nivel + 1)
                if resultado is not None:
                    return resultado

        path.pop()  # se nao encontra remover o que está no caminho......
        return None


    def procura_BFS(self, start, end,caminho):
        # definir nodos visitados para evitar ciclos
        visited = set()
        fila = Queue()
        expansion = list()
        # adicionar o nodo inicial à fila e aos visitados
        fila.put(start)
        visited.add(start)
        
        # garantir que o start node nao tem pais...
        parent = dict()
        parent[start] = None

        nivel = dict()
        nivel[start] = 0
        path_found = False


        while not fila.empty() and path_found == False:
            nodo_atual = fila.get()
            x = nodo_atual.split(";") 
            expansion.append(nodo_atual)
            for estado in end:
                if x[0] == estado :
                    path_found = True
                    nodo_final = x[0] + ";" + x[1]
            
            #i += 1
            if path_found == False:
                for (adjacente,peso) in self.m_graph[nodo_atual]:
                    i = nivel[nodo_atual] + 1
                    y = adjacente.split(";")
                    lista = re.split(r'[ \s(),;]',y[0])
                    a = int(lista[1])
                    b = int(lista[2])
                    if adjacente not in visited and self.in_caminho((a,b),caminho,i) == False:  
                        fila.put(adjacente)
                        parent[adjacente] = nodo_atual
                        nivel[adjacente] = i
                        visited.add(adjacente)
              

        # Reconstruir o caminho

        path = []
        if path_found:
            path.append(nodo_final)
            while parent[nodo_final] is not None:
                
                path.append(parent[nodo_final])
                nodo_final = parent[nodo_final]

            
            path.reverse()

            # funçao calcula custo caminho
            custo = self.calcula_custo(path)
            return (path, custo,expansion)


    def getNeighbours(self, nodo):
        lista = []
        for (adjacente, peso) in self.m_graph[nodo]:
            lista.append((adjacente, peso))
        return lista



    def add_heuristica(self, name, coord, vel,value):
        n1 = Node(name,coord,vel)
        #if n1 in self.m_nodes:
        self.m_h[name] = value

    
    def heuristica(self):
        nodos = self.m_graph.keys
        for n in nodos:
            self.m_h[n] = 1
        return (True)

    

    def calcula_est(self, estima):
        l = list(estima.keys())
        min_estima = estima[l[0]]
        node = l[0]
        for k, v in estima.items():
            if v < min_estima:
                min_estima = v
                node = k
        return node


    def procura_aStar(self, start, end,caminho):
        # open_list is a list of nodes which have been visited, but who's neighbors
        # haven't all been inspected, starts off with the start node
        # closed_list is a list of nodes which have been visited
        # and who's neighbors have been inspected
        open_list = {start}
        closed_list = set([])

        # g contains current distances from start_node to all other nodes
        # the default value (if it's not found in the map) is +infinity
        g = {}  # g é apra substiruir pelo peso  ???

        g[start] = 0

        # parents contains an adjacency map of all nodes
        nivel = dict()
        nivel[start] = 0
        parents = {}
        parents[start] = start
        n = None
        expansion = list()
        while len(open_list) > 0:
            # find a node with the lowest value of f() - evaluation function
            calc_heurist = {}
            flag = 0
            for v in open_list:
                if n == None:
                    n = v
                else:
                    flag = 1
                    calc_heurist[v] = g[v] + self.getH(v)
            if flag == 1:
                min_estima = self.calcula_est(calc_heurist)
                n = min_estima
            if n == None:
                print('Path does not exist!')
                return None

            # if the current node is the stop_node
            # then we begin reconstructin the path from it to the start_node
            x = n.split(";")
            expansion.append(n)

            if x[0] in end:
                reconst_path = []

                while parents[n] != n:
                    reconst_path.append(n)
                    n = parents[n]

                reconst_path.append(start)

                reconst_path.reverse()

                #print('Path found: {}'.format(reconst_path))
                return (reconst_path, self.calcula_custo(reconst_path),expansion)

            # for all neighbors of the current node do
            # definir função getneighbours  tem de ter um par nodo peso
            for (m, weight) in self.getNeighbours(n):
                # if the current node isn't in both open_list and closed_list
                # add it to open_list and note n as it's parent
                #i = nivel[start] + 1
                #y = m.split(";")
                #lista = re.split(r'[ \s(),;]',y[0])
                ##print(lista)
                #a = int(lista[1])
                #b = int(lista[2])
                if m not in open_list and m not in closed_list:
                    open_list.add(m)
                    parents[m] = n
                    g[m] = g[n] + weight

                # otherwise, check if it's quicker to first visit n, then m
                # and if it is, update parent data and g data
                # and if the node was in the closed_list, move it to open_list
                else:
                    if g[m] > g[n] + weight:
                        g[m] = g[n] + weight
                        parents[m] = n

                        if m in closed_list:
                            closed_list.remove(m)
                            open_list.add(m)

            # remove n from the open_list, and add it to closed_list
            # because all of his neighbors were inspected
            open_list.remove(n)
            closed_list.add(n)

        print('Path does not exist!')
        return None


    def getH(self, nodo):
        if nodo not in self.m_h.keys():
            return 1000
        else:
            return (self.m_h[nodo])

    def greedy(self, start, end,caminho):
        # open_list é uma lista de nodos visitados, mas com vizinhos
        # que ainda não foram todos visitados, começa com o  start
        # closed_list é uma lista de nodos visitados
        # e todos os seus vizinhos também já o foram
        open_list = set([start])
        closed_list = set([])
        expansion = list()
        # parents é um dicionário que mantém o antecessor de um nodo
        # começa com start
        nivel = dict()
        nivel[start] = 0
        parents = {}
        parents[start] = start

        while len(open_list) > 0:
            n = None

            # encontrar nodo com a menor heuristica
            for v in open_list:
                if n == None or self.m_h[v] < self.m_h[n]:
                    n = v

            if n == None:
                print('Path does not exist!')
                return None

            # se o nodo corrente é o destino
            # reconstruir o caminho a partir desse nodo até ao start
            # seguindo o antecessor
            x = n.split(";")
            expansion.append(n)
        
            if x[0] in end:
                reconst_path = []

                while parents[n] != n:
                    reconst_path.append(n)
                    n = parents[n]

                reconst_path.append(start)

                reconst_path.reverse()

                return (reconst_path, self.calcula_custo(reconst_path),expansion)

            # para todos os vizinhos  do nodo corrente
            for (m, weight) in self.getNeighbours(n):
                # Se o nodo corrente nao esta na open nem na closed list
                # adiciona-lo à open_list e marcar o antecessor
                i = nivel[n] + 1
                y = m.split(";")
                lista = re.split(r'[ \s(),;]',y[0])
                #print(lista)
                a = int(lista[1])
                b = int(lista[2])
                if m not in open_list and m not in closed_list and self.in_caminho((a,b),caminho,i) == False:
                    open_list.add(m)
                    parents[m] = n
                    nivel[m] = i

            # remover n da open_list e adiciona-lo à closed_list
            # porque todos os seus vizinhos foram inspecionados
            open_list.remove(n)
            closed_list.add(n)

        print('Path does not exist!')
        return None
    
    