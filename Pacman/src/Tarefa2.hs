{-  | 

A tarefa 2 foi uma tarefa mais trabalhosa , em que tivemos de determinar os efeitos
de uma jogada no estado do jogo.


-}

module Tarefa2 where

import Types


-- | Efetua uma jogada movendo o Pacman espicificado segundo uma orientação
play :: Play 
     -> State 
     -> State

play a@(Move i o) s@(State m x n) = movePlayer s (x !! i) o

-- | Verifica se o Pacman tem a mesma orientação da jogada e se pode transitar na direção desejada
movePlayer :: State
           -> Player -- ^ Player que vai ser atualizado
           -> Orientation -- ^ Orientação do movimento
           -> State -- ^ Devolve o State atualizado
movePlayer s@(State (m:ms) x n) gh@(Ghost (GhoState (i,c,v,o,p,l) gm)) o1

 | o == o1 = if tp == Wall then (State (m:ms) x n)
                           else (State  (m:ms) (player ghostup) n)
 | otherwise = play (Move i o1) (State (m:ms) ghostno n)

 where np = (nextPlace ((length m)-1) c o1)
       tp = (typePlace (m:ms) c o1)
       lm = ((length (m:ms)) `div` 2, (length m) `div` 2) 
       ghostup = ((take i x) ++ [Ghost (GhoState (i,np,v,o,p,l) gm)] ++ drop (i+1) x)
       ghostno = ((take i x) ++ [Ghost (GhoState (i,c,v,o1,p,l) gm)] ++ drop (i+1) x)
       player x = gHost x (pAc x) lm [] 1


movePlayer s@(State (m:ms) x n) (Pacman (PacState (i,c,v,o,p,l) tm mou pm)) o1
 
 | pm == Dying = s
 | o == o1 = if tp == Wall then (State (m:ms) x n)
                           else (State cm (newPlayerState x (Pacman (PacState (i,np,v,o,p,l) tm mou pm)) tp lm) n)
 | otherwise = (State (m:ms) ((take i x) ++ [Pacman (PacState (i,c,v,o1,p,l) tm mou pm)] ++ drop (i+1) x) n)

 where tp = (typePlace (m:ms) c o)
       cm = (correctMaze (m:ms) c)
       np = (nextPlace ((length m)-1) c o1)
       lm = ((length (m:ms)) `div` 2, (length m) `div` 2)


-- | Realiza os efeitos no estado do jogador segundo o que contém a proxima peça
newPlayerState :: [Player] -- ^ Lista inicial de Players
               -> Player -- ^ É necessário o Player que queremos atualizar para atulizar o seu próprio estado na lista original
               -> Piece -- ^ Usamos a peça para onde o Pacman se vai mover para realizar a ação pretendida
               -> Coords -- ^ Coordenadas do meio do labirinto para caso um fantasma seja comido reapareça na casa dos fantasmas
               -> [Player]
newPlayerState x ps@(Pacman (PacState (i,c,v,o,p,l) tm mou pm)) z lm

 | z == Empty = (gHost ((take i x) ++ [pacm] ++ drop (i+1) x) ps lm [] 1)
 | z == Food Little = (gHost ((take i x) ++ [pacm1] ++ drop (i+1) x) pacm1 lm [] 1)
 | z == Food Big = (updateGhost ((take i x) ++ [pacm5] ++ drop (i+1) x) pacm5 [] lm 1)

 where pacm = Pacman (PacState (i,c,v,o,p,l) tm mou pm)
       pacm1 = Pacman (PacState (i,c,v,o,p+1,l) tm mou pm)
       pacm5 = Pacman (PacState (i,c,v,o,p+5,l) 10 mou Mega)

-- | Corrige o labirinto quando o Pacman transita de peça, colocando Empty de onde ele sai
correctMaze :: Maze
            -> Coords -- ^ Coordenadas de onde o Pacman parte inicialmente
            -> Maze

correctMaze m (x,y) = ((take x m) ++ [(take y (m !! x)) ++ [Empty] ++ (drop (y+1) (m !! x))] ++ (drop (x+1) m))


-- | Obtem a peça para onde o Pacman se vai mover
typePlace :: Maze 
          -> Coords -- ^ Coordenadas originais do Pacman
          -> Orientation -- ^ Orientação da jogada
          -> Piece

typePlace m (x,y) U = (m !! (x-1)) !! y
typePlace m (x,y) D = (m !! (x+1)) !! y
typePlace (m:ms) (x,y) L = if y == 0 then ((m:ms) !! x) !! ((length m)-1) else ((m:ms) !! x) !! (y-1)
typePlace (m:ms) (x,y) R = if y == ((length m)-1) then ((m:ms) !! x) !! 0 else ((m:ms) !! x) !! (y+1)


-- | Obtem as coordenadas para onde o Pacman se vai mover
nextPlace :: Int -- ^ largura do maze
          -> Coords -- ^ Coordenadas originais do Pacman
          -> Orientation -- ^ Orientação da jogada
          -> Coords

nextPlace lm (x,y) U = (x-1,y)
nextPlace lm (x,y) D = (x+1,y)
nextPlace lm (x,y) L = if y == 0 then (x,lm) else (x,y-1)
nextPlace lm (x,y) R = if y == lm then (x,0) else (x,y+1)


-- | Atualiza todos os estados dos Ghosts para Dead, quando o Pacman come comida grande e verifica também se existe algum Ghost na mesma coordenada do Pacman
updateGhost ::  [Player] -- ^ Lista com o estado do Pacman atualizado segundo a peça
             -> Player -- ^ É necessário para informações e comparações
             -> [Player] -- ^ Usamos uma lista vazia para manter a ordenação da lista original
             -> Coords -- ^ Coordenadas do meio do labirinto caso um fantasma seja comido
             -> Int -- ^ Variável para dizer quantos fantasmas comeu ou se o Pacman foi comido
             -> [Player]

updateGhost [] ps@(Pacman (PacState (i1,c1,v1,o1,p1,l1) tm mou pm)) z lm a

 | a == 0 = aux z ps
 | a == 1 = z
 | otherwise = (take i1 z) ++ [pacm10] ++ (drop (i1+1) z)
 where pacm10 = (Pacman (PacState (i1,c1,v1,o1,p1+(10*(a-1)),l1) tm mou pm))

updateGhost (g@(Ghost (GhoState (i,c,v,o,p,l) gm)):xs) ps@(Pacman (PacState (i1,c1,v1,o1,p1,l1) tm mou pm)) z lm a

 |c == c1 = if gm == Dead then updateGhost xs ps (z++[ghalive]) lm (a+1) else updateGhost xs ps (z++[ghdead]) lm 0
 |otherwise = updateGhost xs ps (z++[ghdead]) lm a

 where ghdead = if v == 1 then Ghost (GhoState (i,c,(0.5*v),o,p,l) Dead) else Ghost (GhoState (i,c,v,o,p,l) Dead)
       ghalive = Ghost (GhoState (i,lm,(2*v),o,p,l) Alive)
updateGhost (x:xs) ps z lm a = updateGhost xs ps (z++[x]) lm a


-- | É uma função auxiliar para as funções updateGhost e gHost que corrige o estado do Pacman quando embate com um fantasma Alive
aux ::  [Player]
     -> Player
     -> [Player]

aux z (Pacman (PacState (i,c,v,o,p,l) tm mou pm))
 | l <= 1 = (take i z) ++ [(Pacman (PacState (i,c,v,o,p,0) tm mou Dying))] ++ (drop (i+1) z)
 | otherwise = (take i z) ++ [(Pacman (PacState (i,(1,1),v,o,p,l-1) tm mou pm))] ++ (drop (i+1) z)


-- | Verifica e determina o efeito da existência e do estado de um fantasma na mesma coordenada do Pacman
gHost ::  [Player] -- ^ Lista com o estado do Pacman atualizado segundo a peça
       -> Player -- ^ É necessário para informações e comparações
       -> Coords -- ^ Coordenadas do meio do labirinto caso o fantasma seja comido
       -> [Player] -- ^ Usamos uma lista vazia para manter a ordenação da lista original
       -> Int -- ^ Variável para dizer quantos fantasmas comeu ou se o Pacman foi comido
       -> [Player]
gHost [] pac@(Pacman (PacState (i,c,v,o,p,l) tm mou pm)) c0 z a

 | a == 0 = aux z pac
 | a == 1 = z
 | otherwise = ((take i z) ++ [pacm10] ++ (drop (i+1) z))
 where pacm10 = (Pacman (PacState (i,c,v,o,p+(10*(a-1)),l) tm mou pm))

gHost (g@(Ghost (GhoState (i1,c1,v1,o1,p1,l1) gm)):xs) pac@(Pacman (PacState (i,c,v,o,p,l) tm mou pm)) c0 z a
 | c == c1 = if gm == Dead && pm == Mega then gHost xs pac c0 (z++[ghalive]) (a+1) else gHost [] pac c0 (z++(g:xs)) 0
 | otherwise = gHost xs pac c0 (z++[g]) a
 where ghalive = (Ghost (GhoState (i1,c0,(2*v1),U,p1,l1) Alive))
gHost (x:xs) pac c0 z a = gHost xs pac c0 (z++[x]) a

pAc :: [Player]
    -> Player

pAc (p@(Pacman x):xs) = p
pAc (x:xs) = pAc xs

---------------------Testes---------------------


testar a = testes statest a

testes x a
 | a == 1 = play (Move 3 R) x
 | a == 2 = play (Move 3 L) x
 | a == 3 = play (Move 4 L) x 
 | a == 4 = play (Move 1 L) x
 | a == 5 = play (Move 2 R) x
 | a == 6 = play (Move 0 L) x