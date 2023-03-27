module Tarefa6 where

import Types
import Tarefa2

bot :: Int -> State -> Maybe Play

bot i s@(State m x n) = bot' s (x !! i)


bot' s@(State maz@(m:ms) z n) (Pacman (PacState (i,c@(x,y),v,o,p,l) tm mou pm)) = case pm of Normal -> Just (Move i newori0) 
                                                                                             Mega -> Just (Move i newori1)
                                                                                             Dying -> Nothing
 
 where newori0 = if (scatterMode z c) then menordist (x,y) cmpc (pacOrientations maz c) (-1) U
                                      else maiordist (x,y) fmpc (pacOrientations maz c) (-1) U
       newori1 = if (chaseMode z c) then menordist (x,y) fmpc (pacOrientations maz c) (-1) U
                                      else maiordist (x,y) fmpc (pacOrientations maz c) (-1) U
       fcoord = case foodcoord maz (0,0) 0 of [] -> foodcoord maz (0,0) 1
                                              a -> a
       fmpc = fmp z (x,y) (-1) (-1,-1)
       cmpc = cmp fcoord (x,y) (-1) (2,3)

-- | Decide a melhor jogada para o pacman quando este está em modo Normal
scatterMode :: [Player] 
            -> Coords
            -> Bool

scatterMode [] c = True
scatterMode ((Ghost (GhoState (i,c,v,o,p,l) gm)):os) c1 = if (dist c1 c) < 10 then False else scatterMode os c1
scatterMode ((Pacman (PacState (i,c,v,o,p,l) tm mou pm)):os) c1 = scatterMode os c1

-- | Decide a melhor jogada para o pacman quando este está em modo Mega
chaseMode :: [Player]
          -> Coords
          -> Bool

chaseMode [] c = True
chaseMode ((Ghost (GhoState (i,c,v,o,p,l) gm)):os) c1 = if (dist c1 c) < 10 && gm == Alive then False else chaseMode os c1
chaseMode ((Pacman (PacState (i,c,v,o,p,l) tm mou pm)):os) c1 = chaseMode os c1

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



-- | Verifica qual o fantasma mais próximo ao pacman
fmp :: [Player]
    -> Coords
    -> Int
    -> Coords
    -> Coords

fmp [] c d c1 = c1

fmp (z@(Ghost (GhoState (i2,c1,v,o,p,l) gm)):zs) c d i1 = if gm == Dead && ((dist c c1) < d || d == (-1)) then fmp zs c (dist c c1) c1
                                                                                                          else fmp zs c d i1
fmp ((Pacman y):zs) c d i1 = fmp zs c d i1



-- | Verifica as orientações válidas para o pacman
pacOrientations :: Maze
                -> Coords
                -> [Orientation]

pacOrientations m c = (pacvalidOrientation (tp U) U) ++ (pacvalidOrientation (tp L) L) ++ (pacvalidOrientation (tp R) R) ++ (pacvalidOrientation (tp D) D)
 where tp x = typePlace m c x


-- | auxiliar pacOrientations
pacvalidOrientation :: Piece
                    -> Orientation
                    -> [Orientation]

pacvalidOrientation x y = if x == Wall then [] else [y]

-- | Retorna as coordenadas da comida Grande ou Pequena
foodcoord :: Maze
          -> Coords
          -> Int
          -> [Coords]

foodcoord [] c a = []
foodcoord (m:ms) c@(x,y) 0 = aux m c ++ foodcoord ms (x+1,0) 0
 where aux [] c = []
       aux (p:ps) c@(x,y) = if p == Food Big then c : aux ps (x,y+1) 
                                             else aux ps (x,y+1)
foodcoord (m:ms) c@(x,y) 1 = aux m c ++ foodcoord ms (x+1,0) 1
 where aux [] c = []
       aux (p:ps) c@(x,y) = if p == Food Little then c : aux ps (x,y+1) 
                                                else aux ps (x,y+1)

-- | Verifica qual das comidas está mais proxima ao pacman
cmp :: [Coords]
    -> Coords
    -> Int
    -> Coords
    -> Coords

cmp [] c d c1 = c1

cmp (a:xs) c@(x,y) d c1 = if distancia < d || d == -1 then cmp xs c distancia a
                                                       else cmp xs c d c1
 where distancia = dist c a





































































