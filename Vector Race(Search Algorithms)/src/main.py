import itertools
from Graph import Grafo
from Node import Node
from parser_1 import parser
from corrida import Corrida
import pygame
from pygame.locals import *
import sys
import os
import re

BLACK = (0,0,0)
WHITE = (200,200,200)
GREEN = (0,200,0)
BLUE = (0,0,200)
ROSA = (255,0,127)
ORANGE = (255,128,0)
GREY = (96,96,96)
RED = (255,0,0)
PURPLE = (204,0,204)
WINDOW_HEIGHT = 600
WINDOW_WIDTH = 800
MARGIN = 5

def drawGrid(percurso,caminho,caminho2,custo1,custo2,tipo):
    block_sizex = (WINDOW_WIDTH - (5 * len(percurso))) / len(percurso[0])
    block_sizey = (WINDOW_HEIGHT - (5 * len(percurso))) / len(percurso)
    font = pygame.font.Font('freesansbold.ttf',10)
    carro1 = "carro1:" + str(custo1)
    carro2 = "carro2:" + str(custo2)
    text1 = font.render(carro1,True,PURPLE,BLACK)
    text2 = font.render(carro2,True,BLUE,BLACK)
    textrect1 = text1.get_rect()
    textrect2 = text2.get_rect
    #textrect1.center = (WINDOW_WIDTH - 20,1)
    #textrect2.center = (WINDOW_WIDTH - 20,3)
    for y in range(len(percurso)):
        for x in range(len(percurso[0])):
            color = WHITE
            
            if percurso[y][x] == 4:
                color = GREY
            elif percurso[y][x] == 2:
                color = GREEN
    
            pygame.draw.rect(SCREEN,color,[(MARGIN + block_sizex) * x + MARGIN, (MARGIN + block_sizey) * y + MARGIN , block_sizex , block_sizey])
    SCREEN.blit(text1,(WINDOW_WIDTH - 70, 1))
    SCREEN.blit(text2,(WINDOW_WIDTH -70,10))
    for (c1,c2) in itertools.zip_longest(caminho,caminho2):
        if c1 == c2:
            color1 = color2 = ORANGE
        else:
            color1 = PURPLE
            color2 = BLUE
        if c1 is not None and tipo != 2:
            pygame.draw.rect(SCREEN,color1,[(MARGIN + block_sizex) * c1[1] + MARGIN, (MARGIN + block_sizey) * c1[0] + MARGIN , block_sizex , block_sizey])
        if c2 is not None and tipo != 1:
            pygame.draw.rect(SCREEN,color2,[(MARGIN + block_sizex) * c2[1] + MARGIN, (MARGIN + block_sizey) * c2[0] + MARGIN , block_sizex , block_sizey])
        pygame.time.wait(200)
        pygame.display.update()
        pygame.event.pump()

def grid_pygame(percurso,caminho,caminho2,custo1,custo2):
    global SCREEN,CLOCK
    pygame.init()
    SCREEN = pygame.display.set_mode((WINDOW_WIDTH,WINDOW_HEIGHT))
    CLOCK = pygame.time.Clock()
    SCREEN.fill(BLACK)
    while True:
        pygame.event.pump()
        tipo = 0
        drawGrid(percurso,caminho,caminho2,custo1,custo2, tipo)
        #print(pygame.key.get_pressed())
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

def caminho_to_coord(caminho):
    #string = caminho[0]
    res = []
    for x in caminho[0]:
        lista = re.split(r'[ \s(),;]',x)
        a = int(lista[1])
        b = int(lista[2])
        res.append((a,b))
    return res
   

def menu_inicial(carro):
    if carro == 1:
        print("ESCOLHA O TIPO DE PROCURA PARA O CARRO" + str(carro))
    elif carro == 2:
        print("ESCOLHA O TIPO DE PROCURA PARA O CARRO" + str(carro))
    print("1- BFS")
    print("2- DFS")
    print("3- GREEDY")
    print("4- Astar")
    


def main():

    inicio,final,percurso,inicio2 = parser()
    final_str = []

    
    race = Corrida(inicio,percurso,final)
    race.cria_grafo()
    race2 = Corrida(inicio2,percurso,final)
    race2.cria_grafo()


    for c in final :
        y = str(c).replace(' ', '') 
        final_str.append(y)
    
    inicio_1 = str(inicio).replace(' ', '')+";(0,0)"
    inicio_2 = str(inicio2).replace(' ', '')+";(0,0)"
    

    
    caminho = ([],1)
    caminho_carro2 = ([],1) 
    
    menu_inicial(1)
    
    l = input()
    
    match l:
        case "1":
            caminho = race.solucaoBFS(inicio_1,final_str,["(0,0)"])
        
        case "2":
            caminho = race.solucaoDFS(inicio_1,final_str,["(0,0)"])
            
        case "3":
            caminho = race.solucaoGreedy(inicio_1,final_str,["(0,0)"])
        
        case "4":
            caminho = race.solucao_Astar(inicio_1,final_str,["(0,0)"])

    #ordem de expansão dos nodos
    print(caminho[2])
    caminho2 = caminho_to_coord(caminho)
    
    

    menu_inicial(2)
    
    m = input()
    
    match m:
        case "1":
            caminho_carro2 = race2.solucaoBFS(inicio_2,final_str,caminho2)
        
        case "2":
            caminho_carro2 = race2.solucaoDFS(inicio_2,final_str,caminho2)

        case "3":
            caminho_carro2 = race2.solucaoGreedy(inicio_2,final_str,caminho2)
        
        case "4":
            caminho_carro2 = race2.solucao_Astar(inicio_2,final_str,caminho2)

    print(caminho_carro2[2])
    caminho2_carro2 = caminho_to_coord(caminho_carro2)    


    #os.system('clear')
    print("Deseja visualizar o caminho encontrado?")
    print("S")
    print("N")
    resposta = input()

    if resposta == 'S' or resposta == 's':
        grid_pygame(percurso,caminho2,caminho2_carro2,caminho[1],caminho_carro2[1])


if __name__ == "__main__":
    main()
