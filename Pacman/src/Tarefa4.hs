{- | 

= Introdução 

A tarefa 4 foi a tarefa em que implementamos o efeito da passagem de um instante de tempo 
num estado de jogo , esta tarefa pode ser dividida em duas partes , sendo a primeira usar
as funções e tipos advindos da biblioteca NCurses e a a segunda parte através da informação 
obtida da primeira parte ara fazer com que o estado do jogo avance em iterações.


= Estratégia para resolução do problema 

A nossa estratégia para definir o passTime foi que para cada iteração par ou ímpar 
atualiza o estado do jogo segundo as restrições deste mesmo.   




= Conclusão

Na nossa opinião cumprimos o pretendido apesar de não termos seguido totalmente as indicações do 
guião.  


-}

module Tarefa4 where 

import Tarefa5
import Tarefa2
import Tarefa6
import Types


defaultDelayTime = 250 -- 250 ms

{-
passTime :: Int  
         -> State
         -> State

passTime step s@(State m x n) = geral (moveghost s leng step movesgh) 0
 where leng = ((length x)-1)
       movesgh = ghostPlay s
-}


passTime :: Int 
         -> State
         -> State

passTime step s@(State m x n) = if even step then geral (movepac s (paCid x) step) 0
                                             else geral (movepac s (paCid x) step) 1

-- | Dá prioridade a movimentações feitas pelo pacman

movepac :: State
        -> Int
        -> Int
        -> State

movepac s@(State m x n) i step = decideghost (play (unMaybe i s) s) i step


-- | Esta função é responsável por no instante em que o pacman come comida grande os fantasmas virarem na direção oposta 

decideghost :: State
            -> Int
            -> Int
            -> State

decideghost s@(State m x n) i step = if (pactm x) then moveghost s leng step (direcaoposta x)
                                                  else moveghost s leng step movesgh
 where leng = ((length x)-1)
       movesgh = ghostPlay s

-- | Dá a direção oposta de um player

direcaoposta :: [Player]
             -> [Play]

direcaoposta [] = []
direcaoposta ((Ghost (GhoState (i,c,v,o,p,l) gm)):xs) = case o of U -> (Move i D) : direcaoposta xs
                                                                  D -> (Move i U) : direcaoposta xs
                                                                  R -> (Move i L) : direcaoposta xs
                                                                  L -> (Move i R) : direcaoposta xs
direcaoposta (x:xs) = direcaoposta xs

-- | Verificar se tem de abrir ou fechar a boca

geral :: State
      -> Int
      -> State

geral (State m x n) 0 = State m (mouthOpen x []) n
geral (State m x n) 1 = State m (mouthClose x []) n


-- | Move os fantasmas conforme o seu estado

moveghost :: State
          -> Int
          -> Int
          -> [Play]
          -> State

moveghost s@(State m x n) (-1) step ghplays = s

moveghost s@(State m x n) i step ghplays
 | pacorgh (x !! i) == 0 = moveghost s (i-1) step ghplays 
 | pacorgh (x !! i) == 1 = moveghost (play playgh s) (i-1) step ghplays
 | pacorgh (x !! i) == 2 = if step == 0 
                       then moveghost (play playgh s) (i-1) step ghplays
                       else moveghost s (i-1) step ghplays
 where gt = getPlayerOrientation (x !! i)
       playgh = (jogadafantasma ghplays i)


-- | Auxiliar da função moveghost , verifica o estado dos ghosts 

pacorgh :: Player
        -> Int

pacorgh (Pacman x) = 0
pacorgh (Ghost (GhoState (i,c,v,o,p,l) gm)) = if gm == Alive then 1 else 2

-- | Abre a boca do pacman e atualiza o estado do pacman quando o termino do time mega

mouthOpen :: [Player]
          -> [Player]
          -> [Player]

mouthOpen [] z = z
mouthOpen ((Pacman (PacState s tm mou pm)):xs) z 
 | pm == Normal = (z ++ (Pacman (PacState s tm Open pm)) : xs)
 | pm == Mega = if tm > 0.25
                then (z ++ (Pacman (PacState s (tm-0.25) Open pm)) : xs)
                else updateGhosts (z ++ (Pacman (PacState s 0 Open Normal)) : xs)
 | otherwise = (z ++ (Pacman (PacState s tm Open pm)) : xs)
mouthOpen (x:xs) z = mouthOpen xs (z++[x])

-- | Fecha a boca do pacman e atualiza o estado do pacman quando o termino do time mega

mouthClose :: [Player]
           -> [Player]
           -> [Player]

mouthClose [] z = z
mouthClose ((Pacman (PacState s tm mou pm)):xs) z 
 | pm == Normal = (z ++ (Pacman (PacState s tm Closed pm)) : xs)
 | pm == Mega = if tm > 0.25
                then (z ++ (Pacman (PacState s (tm-0.25) Closed pm)) : xs)
                else updateGhosts (z ++ (Pacman (PacState s 0 Closed Normal)) : xs)
 | otherwise = (z ++ (Pacman (PacState s tm Closed pm)) : xs)
mouthClose (x:xs) z = mouthClose xs (z++[x])


-- | Atualiza o estado dos ghosts quando termina o time mega do pacman

updateGhosts :: [Player]
             -> [Player]

updateGhosts [] = []
updateGhosts (g@(Ghost (GhoState (i,c,v,o,p,l) gm)):xs) = if gm == Dead 
                                                          then (Ghost (GhoState (i,c,2*v,o,p,l) Alive)) : updateGhosts xs
                                                          else g : updateGhosts xs
updateGhosts (x:xs) = x : updateGhosts xs


-- | Escolhe de uma lista de plays a mais acertada para a situação em que o fantasma se encontra

jogadafantasma :: [Play]
               -> Int
               -> Play

jogadafantasma (m@(Move a b):xs) i = if a == i then m else jogadafantasma xs i

-- | Identifica o pacman retornando o seu ID

paCid :: [Player]
      -> Int

paCid ((Pacman (PacState (i,c,v,o,p,l) tm mou pm)):xs) = i
paCid (x:xs) = paCid xs


-- | Verifica se o pacman acabou de comer comida Grande

pactm :: [Player]
      -> Bool

pactm ((Pacman (PacState (i,c,v,o,p,l) tm mou pm)):xs) = if tm > 9.50 then True else False
pactm (x:xs) = pactm xs


-- | Função que implementa a jogada do bot do pacman
unMaybe :: Int   
        -> State 
        -> Play
unMaybe i (State m x n) = unMaybe (bot i (State m x n)) i
 where unMaybe y i = case y of
                   Nothing -> (Move i Null)
                   Just a -> a
