{-

A tarefa 3 foi uma tarefa relativamente mais facil uando comparada com a tarefa anterior
e nesta o objetivo era compactar os labirintos através do type Instructions , de forma a  
qu a leitura do mesmo fosse ais rápida. 

-}

module Tarefa3 where

import Generator    

import Types



-- | Transforma o maze em Instructions usando Repeats tornando-o mais compacto
compactMaze :: Maze -- ^ 
            -> Instructions

compactMaze [] = []
compactMaze m = instructmaze m []


-- | Transforma os corredores em Instruction
compactcorridor :: Corridor 
                -> Instruction 

compactcorridor [] = Instruct []
compactcorridor (x:xs) = Instruct (compac 1 x xs)


-- | Função auxiliar da função compactCorridor que junta as peças que são iguais num determinado corredor 
compac :: Int 
       -> Piece 
       -> Corridor 
       -> [(Int,Piece)]

compac n a [] = [(n,a)]
compac n a (x:xs) 
            | a == x = compac (n+1) x xs 
            | otherwise = (n,a) : compac 1 x xs 


-- | Transforma o maze em Instructions
instructmaze :: Maze
             -> Instructions
             -> Instructions

instructmaze [] z = z
instructmaze (x:xs) z = instructmaze xs (z++[vertical (compactcorridor x) z 0]) 



-- | Encontra corredores iguais e substiuis por Repeats
vertical :: Instruction
         -> Instructions
         -> Int -- ^ Nº usado pelo Repeat
         -> Instruction

vertical x [] a = x
vertical x (z:zs) a = if z == x then Repeat a else vertical x zs (a+1)

---------------------------------------------------

--teste :: Maze -> Instructions -> Bool
--teste x [] = False 
--teste x y  
  --           | y == compactMaze x = True 
    --         | otherwise = False 

teste x y = if y == compactMaze x then " os labirintos ficaram iguais , a funcao esta correta" else "a função está incorreta" 







