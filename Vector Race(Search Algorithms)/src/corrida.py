from Graph import Grafo
from Node import Node
import math


class Corrida():
    
    def __init__(self,start,percurso,end):
            self.g = Grafo(directed=True)
            self.start = start
            self.percurso = percurso
            self.end = end
            

    def cria_grafo(self):
        startg = self.nome_form(self.start,(0,0))
        start1 = Node(self.nome_form(self.start,(0,0)),self.start,(0,0))
        self.g.m_nodes.append(start1)
        self.g.m_graph[startg] = list()
        for parent in self.g.m_nodes:
            for nodo in self.expande(parent.getCoord(),parent.getVel()):
                #add heuristica.
                self.g.add_heuristica(nodo.getName(),nodo.getCoord(),nodo.getVel(),self.shorter_distance(nodo.getCoord(),self.end)) 
                coord = nodo.getCoord()
                if coord[0] >= 0 and coord[1] >= 0 and coord[0] < len(self.percurso) and coord[1] < len(self.percurso[0]) :                 
                    if self.percurso[coord[0]][coord[1]] == 4:
                            
                            self.g.add_edge(parent.getName(),parent.getName(),parent.getCoord(),parent.getCoord(),parent.getVel(),(0,0),25)
                    elif self.percurso[coord[0]][coord[1]] == 1 or self.percurso[coord[0]][coord[1]] == 2:
                        x = self.verifica_paredes(parent.getCoord(),nodo.getCoord(),nodo.getVel())
                        if x == 0:
                            self.g.add_edge(parent.getName(),nodo.getName(),parent.getCoord(),nodo.getCoord(),parent.getVel(),nodo.getVel(),1)

                        
                        
                        


    def in_percurso(self,coordy,coordx):
        if coordx >= 0 and coordy >= 0 and coordy < len(self.percurso) and coordx < len(self.percurso[0]) :
            return True
        else:
            return False
                    
    def expande(self,coord,vel):
        lista = set()
        
        lista.add(Node(self.nome_form(coord,vel),coord,vel))

        x = self.acel_vel(vel,(0,1))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(0,-1))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(1,0))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(1,1))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(1,-1))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(-1,-1))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(-1,0))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        x = self.acel_vel(vel,(-1,1))
        y = self.acel_pos(coord,x)
        lista.add(Node(self.nome_form(y,x),y,x))
        

        return lista


    def acel_vel(self,vel,acel):
        
        vel = (vel[0]+ acel[0],vel[1]+acel[1])
        return vel
    
    def acel_pos(self,coord,vel):    
        coord = (coord[0]+vel[0],coord[1]+vel[1])
        
        return coord
     
    def nome_form(self,coord,vel):
        return "(" + str(coord[0]) + "," + str(coord[1]) + ")" + ";" + "(" + str(vel[0]) + "," + str(vel[1]) + ")" 

    def verifica_paredes(self,coordInicial,coord,vel):
        flag = 0

        if vel[0] == 0 :
            if vel[1] > 0:
                x = coordInicial[1] + 1
                y = coordInicial[0]
                while x != coord[1] and x < len(self.percurso[0]) and x >= 0:
                    if self.percurso[y][x] == 4:
                        flag = -1 
                    x = x + 1
            if vel[1] < 0:
                x = coordInicial[1] - 1
                y = coordInicial[0]
                while x != coord[1] and x < len(self.percurso[0]) and x >= 0:
                    if self.percurso[y][x] == 4:
                        flag = -1 
                    x = x - 1
        
        elif vel[1] == 0 and vel[0] != 0:
            x = coordInicial[1]
            if vel[0] < 0:
                
                y = coordInicial[0] - 1
                while y != coord[0] and self.in_percurso(y,x):
                    if self.percurso[y][x] == 4:
                        flag = -1 
                    y = y - 1

            elif vel[0] > 0:
                
                y = coordInicial[0] + 1
                while y != coord[0] and self.in_percurso(y,x):
                    if self.percurso[y][x] == 4:
                        flag = -1 
                    y = y + 1

        elif abs(vel[0]) == abs(vel[1]) and abs(vel[0]) > 1 and abs(vel[1]) > 1:
            coord_y = coordInicial[0]
            coord_x = coordInicial[1] 


            if vel[0] < 0:
                if vel[1] > 0:
                    while coord_y != coord[0] and coord_x != coord[1] and self.in_percurso(coord_y,coord_x):
                        coord_y = coord_y - 1
                        coord_x = coord_x + 1
                        #print(coord_x)
                        if self.percurso[coord_y][coord_x] == 4:
                            flag = -1
                
                elif vel[1] < 0:
                    while coord_y != coord[0] and coord_x != coord[1] and self.in_percurso(coord_y,coord_x):
                        coord_y = coord_y - 1
                        coord_x = coord_x - 1
                        if self.percurso[coord_y][coord_x] == 4:
                            flag = -1
            
            elif vel[0] > 0:
                if vel[1] > 0:
                    while (coord_y,coord_x) != coord and self.in_percurso(coord_y,coord_x):
                        coord_y = coord_y + 1
                        coord_x = coord_x + 1
                        
                        if self.percurso[coord_y][coord_x] == 4:
                            flag = -1
                
                elif vel[1] < 0:
                    while (coord_y,coord_x) != coord and self.in_percurso(coord_y,coord_x):
                        coord_y = coord_y + 1
                        coord_x = coord_x - 1
                        if self.percurso[coord_y][coord_x] == 4:
                            flag = -1            

        elif abs(vel[0]) > abs(vel[1]):     # aumentar o x --> verificar criterios de pesquisa
            conta = 0
            if vel[0] > 0:
                if vel[1] > 0:
                    y = coordInicial[0] + 1
                    x = coordInicial[1] 
                    while x != coord[1] and self.in_percurso(y,x):
                        while y != coord[0] and self.in_percurso(y,x) :
                            if self.percurso[y][x] == 4:
                                flag = -1

                            y = y + 1
                        x = x + 1
                        conta = conta + 1
                        y = coordInicial[0] + 1 + conta
                
                elif vel[1] < 0:
                    y = coordInicial[0] - 1
                    x = coordInicial[1] 
                    while x != coord[1] and self.in_percurso(y,x):
                        while y != coord[0] and self.in_percurso(y,x) :
                            if self.percurso[y][x] == 4:
                                flag = -1
                            
                            y = y + 1
                        x = x - 1
                        conta = conta + 1
                        y = coordInicial[0] + 1 + conta
            
            elif vel[0] < 0:
                if vel[1] > 0:
                    y = coordInicial[0] + 1
                    x = coordInicial[1] 
                    while x != coord[1] and self.in_percurso(y,x):
                        while y != coord[0] and self.in_percurso(y,x) :
                            if self.percurso[y][x] == 4:
                                flag = -1

                            y = y - 1
                        x = x + 1
                        conta = conta + 1
                        y = coordInicial[0] - 1 - conta
                
                elif vel[1] < 0:
                    y = coordInicial[0] 
                    x = coordInicial[1] - 1
                    while x >= coord[1] and self.in_percurso(y,x):
                        while y != coord[0] and self.in_percurso(y,x) :
                            
                            if self.percurso[y][x] == 4:
                                flag = -1
                            
                            y = y - 1
                        x = x - 1
                        conta = conta + 1
                        y = coordInicial[0] - 1 - conta
        
        elif abs(vel[0]) < abs(vel[1]):
            y = coordInicial[0]
            x = coordInicial[1] + 1
            conta = 0

            if vel[0] > 0:
                if vel[1] > 0:
                    
                    while y <= coord[0] and self.in_percurso(y,x): ## aumentar o y e pesquisar, verificar criterios de pesquisa outra vez
                        if y == coordInicial[0]:
                            if self.percurso[y][x] == 4:
                                flag = -1
                        while x != coord[1] and y != coordInicial[0] and self.in_percurso(y,x):
                            if self.percurso[y][x] == 4:
                                flag = -1

                            x = x + 1
                        y = y + 1
                        conta = conta + 1
                        if conta > 1:
                            x = coordInicial[1] + 1 + conta - 1
                
                elif vel[1] < 0:
                    x = coordInicial[1] - 1
                    while y <= coord[0] and self.in_percurso(y,x): ## aumentar o y e pesquisar, verificar criterios de pesquisa outra vez
                        if y == coordInicial[0]:
                            if self.percurso[y][x] == 4:
                                flag = -1
                        while x != coord[1] and y != coordInicial[0] and self.in_percurso(y,x):
                            if self.percurso[y][x] == 4:
                                flag = -1

                            x = x - 1
                        y = y + 1
                        conta = conta + 1
                        if conta > 1:
                            x = coordInicial[1] - 1 - conta + 1
            
            elif vel[0] < 0 :
                if vel[1] > 0:
                    while y >= coord[0] and self.in_percurso(y,x): ## aumentar o y e pesquisar, verificar criterios de pesquisa outra vez
                        if y == coordInicial[0]:
                            if self.percurso[y][x] == 4:
                                flag = -1
                        while x != coord[1] and y != coordInicial[0] and self.in_percurso(y,x):
                            if self.percurso[y][x] == 4:
                                flag = -1

                            x = x + 1
                        y = y - 1
                        conta = conta + 1
                        if conta > 1:
                            x = coordInicial[1] + 1 + conta - 1
                
                elif vel[1] < 0:
                    x = coordInicial[1] - 1
                    while y >= coord[0] and self.in_percurso(y,x): ## aumentar o y e pesquisar, verificar criterios de pesquisa outra vez
                        if y == coordInicial[0]:
                            if self.percurso[y][x] == 4:
                                flag = -1
                        while x != coord[1] and y != coordInicial[0] and self.in_percurso(y,x):
                            if self.percurso[y][x] == 4:
                                flag = -1
                        
                            x = x - 1
                        y = y - 1
                        conta = conta + 1
                        if conta > 1:
                            x = coordInicial[1] - 1 - conta + 1
        return flag

    
    def shorter_distance(self,nodo,final):
        shorter = 1000
        for fim in final:
            x = math.dist(nodo,fim)
            if x < shorter:
                shorter = x
        return shorter

    def solucaoBFS(self, start, goal,caminho):
        return self.g.procura_BFS(start, goal,caminho)
    
    def solucaoDFS(self,start,goal,caminho):
        path = []
        visited = set()
        expansion = list()
        return self.g.procura_DFS(start,goal,path,visited,expansion,caminho,nivel = 0)
    
    def solucaoGreedy(self,start,end,caminho):
        return self.g.greedy(start,end,caminho)

    def solucao_Astar(self,start,end,caminho):
        return self.g.procura_aStar(start,end,caminho)