{- | 

= Introdução

A tarefa 5 foi a tarefa em que tratamos da movimentação dos fantasmas em relação
ao pacman sendo estas movimentações diferentes consoante o modo do fantasma.

= Objetivo  

O objetivo desta tarefa era definir três funções, a "chaseMode" sendo esta para 
movimentar os fantasmas quando estes estão vivos e a função "scatterMode" para fazer
o mesmo mas para quando os fantasmas estão mortos e por fim a função "ghostPLay" que 
utiliza as duas funções anteriores e devolve a jogada do fantasma.


= Estratégia para resolução do problema 

Para a definição desta funções não seguimos totalmente as indicações do guião visto 
que para estas utilizamos types diferentes. A movimentação dos nossos fantasmas 
basea-se nas coordenadas em que o pacman se encontra, sendo que os fantasmas optam 
pelo caminho mais curto para irem de encontro ao pacman.


= Conclusão

Acreditamos que a tarefa foi concluida com sucesso mas achamos que talvez os fantasmas 
estarão num modo muito dificil e por isso talvez a implementação de vários niveis de 
diferente dificuldade tivesse sido uma boa opção para esta tarefa.

-}

module Tarefa5 where 

import Types
import Tarefa2


ghostPlay :: State 
          -> [Play]

ghostPlay s@(State m x n) = playsGhost s (pacoord x)

-- | Segundo um state devolve a melhor jogada para cada um dos ghosts

playsGhost :: State
          -> Coords
          -> [Play]

playsGhost s@(State m [] n) (x1,y1) = []

playsGhost s@(State m ((Ghost (GhoState (i,c@(x,y),v,o,p,l) gm)):xs) n) (x1,y1) = if gm == Alive then (Move i ori0) : (playsGhost (State m xs n) (x1,y1))
                                                                                                 else (Move i ori1) : (playsGhost (State m xs n) (x1,y1))
 where ori0 = menordist c (x1,y1) (getOrientations m c o) (-1) U
       ori1 = maiordist c (x1,y1) (getOrientations m c o) (-1) U

playsGhost (State m (x:xs) n) c = playsGhost (State m xs n) c

-- | Devolve a orientação do ghost que minimiza a sua distância ao alvo tendo em conta as jogadas que lhe são permitidas

menordist :: Coords
          -> Coords
          -> [Orientation]
          -> Int
          -> Orientation
          -> Orientation

menordist c pac [] a z = z
menordist c@(x,y) pac (o:os) a z = case o of U -> if dist (x-1,y) (pac) <= a || a == (-1) then menordist c pac os (dist (x-1,y) (pac)) U
                                                                                          else menordist c pac os a z
                                             
                                             D -> if dist (x+1,y) (pac) <= a || a == (-1) then menordist c pac os (dist (x+1,y) (pac)) D
                                                                                          else menordist c pac os a z
                                             
                                             R -> if dist (x,y+1) (pac) <= a || a == (-1) then menordist c pac os (dist (x,y+1) (pac)) R
                                                                                          else menordist c pac os a z
                                             
                                             L -> if dist (x,y-1) (pac) <= a || a == (-1) then menordist c pac os (dist (x,y-1) (pac)) L
                                                                                          else menordist c pac os a z


-- | Devolve a orientação do ghost que maximiza a sua distância ao alvo tendo em conta as jogadas que lhe são permitidas

maiordist :: Coords
          -> Coords
          -> [Orientation]
          -> Int
          -> Orientation
          -> Orientation

maiordist c pac [] a z = z
maiordist c@(x,y) pac (o:os) a z = case o of U -> if dist (x-1,y) (pac) >= a || a == (-1) then maiordist c pac os (dist (x-1,y) (pac)) U
                                                                                          else maiordist c pac os a z
                                             
                                             D -> if dist (x+1,y) (pac) >= a || a == (-1) then maiordist c pac os (dist (x+1,y) (pac)) D
                                                                                          else maiordist c pac os a z
                                             
                                             R -> if dist (x,y+1) (pac) >= a || a == (-1) then maiordist c pac os (dist (x,y+1) (pac)) R
                                                                                          else maiordist c pac os a z
                                             
                                             L -> if dist (x,y-1) (pac) >= a || a == (-1) then maiordist c pac os (dist (x,y-1) (pac)) L
                                                                                          else maiordist c pac os a z


-- | Devolve as orientações válidas para o ghost
getOrientations :: Maze
                -> Coords
                -> Orientation
                -> [Orientation]

getOrientations m c U = (validOrientation (tp U) U) ++ (validOrientation (tp L) L) ++ (validOrientation (tp R) R)
 where tp x = typePlace m c x
getOrientations m c D = (validOrientation (tp D) D) ++ (validOrientation (tp L) L) ++ (validOrientation (tp R) R)
 where tp x = typePlace m c x
getOrientations m c R = (validOrientation (tp R) R) ++ (validOrientation (tp U) U) ++ (validOrientation (tp D) D)
 where tp x = typePlace m c x
getOrientations m c L = (validOrientation (tp L) L) ++ (validOrientation (tp U) U) ++ (validOrientation (tp D) D)
 where tp x = typePlace m c x


-- | auxiliar da getOrientations
validOrientation :: Piece
                 -> Orientation
                 -> [Orientation]

validOrientation x y = if x == Wall then [] else [y]


-- | Devolve as coordenadas atuais do pacman
pacoord :: [Player]
         -> Coords

pacoord ((Pacman (PacState (i,c,v,o,p,l) tm mou pm)):xs) = c
pacoord (x:xs) = pacoord xs



