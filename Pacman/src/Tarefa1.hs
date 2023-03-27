{- |

A  tarefa 1 foi a tarefa em que pusemos os labirintos a ser gerados.
Para fazer isto dividimos o problema em várias tarefas , como por exemplo,
gerar primeiro corredores e paredes , depois construir o tunel e a casa dos 
fantasmas. Usando esta estratégia o problema tornou-se mais simples de resolver.    









-}

module Tarefa1 where

import Generator

import Types



-- | Gera o Labirinto através da largura e da altura pretendida e ainda de uma seed
generateMaze :: Int
             -> Int 
             -> Int 
             -> Maze

generateMaze x y z = labPar x y ((geraCorredor x) : geraParedes (geraLabirinto (x-2) (y-2) z) y y ++ [geraCorredor x])


-- | Dado uma largura do labirinto dá a respetiva casa do fantasma
constroiCasa :: Int 
             -> Maze
constroiCasa x
  | even x = [[Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty],
             [Empty, Wall, Wall, Wall, Empty, Empty, Wall, Wall, Wall,Empty],
             [Empty, Wall, Empty, Empty, Empty, Empty, Empty, Empty, Wall,Empty],
             [Empty, Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall,Empty],
             [Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty]] 
  | otherwise = [[Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty],
               [Empty, Wall, Wall, Wall, Empty, Empty, Empty, Wall, Wall, Wall,Empty],
               [Empty, Wall, Empty, Empty,Empty, Empty, Empty, Empty, Empty, Wall,Empty],
               [Empty, Wall, Wall, Wall, Wall, Wall, Wall, Wall,Wall,Wall,Empty], 
               [Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty,Empty]] 


-- | Gera um corredor preenchido por walls da largura pretendida
geraCorredor :: Int 
             -> Corridor

geraCorredor x = replicate x Wall


-- | A um labirinto coloca Paredes com o respetivo túnel
geraParedes :: Maze 
            -> Int 
            -> Int 
            -> Maze

geraParedes [] y z = []
geraParedes (x:xs) y z = ((colocaTunel z y) : x ++ [colocaTunel z y]) : geraParedes xs (y-1) z


-- | Verifica se está a meio da altura do labirinto para verificar se é necessário colocar Wall ou Empty
colocaTunel :: Int 
            -> Int 
            -> Piece

colocaTunel x y
 | x `mod` 2 == 0 = if ((div x 2)+1) == y || ((div x 2)+2) == y then Empty else Wall
 | otherwise = if div (x+3) 2 == y then Empty else Wall


-- | Verifica se a Largura do labirinto e par para realizar a substituição da respetiva casa dos fantasmas
labPar :: Int 
       -> Int 
       -> Maze 
       -> Maze

labPar x y z
 | mod y 2 == 0 = colocaCasa x y z 0 0
 | otherwise = colocaCasa' x y z 0 0


-- | Obtém o meio do labirinto onde é necessário alterar para a casa dos fantasmas quando o labirinto é par
colocaCasa :: Int -- ^ Largura do labirinto
           -> Int  -- ^ Altura do labirinto
           -> Maze -- ^ Labirinto a ser substituido
           -> Int -- ^ Variável para determinar quando é necessário substituir o corredor
           -> Int -- ^ Variável para determinar que lista e necessária ir usar da função 'ConstroiCasa'
           -> Maze

colocaCasa x y [] a b= []
colocaCasa x y (z:zs) a b
 | a >= ((div y 2)-3) && a <= ((div y 2)+1) = subCasa x b z : colocaCasa x y zs (a+1) (b+1)
 | otherwise = z : colocaCasa x y zs (a+1) b


-- | Obtém o meio do labirinto onde é necessário alterar para a casa dos fantasmas quando o labirinto é ímpar
colocaCasa' :: Int -- ^ Largura do labirinto
            -> Int -- ^ Altura do labirinto
            -> Maze -- ^ Labirinto a ser substituido
            -> Int -- ^ Variável para determinar quando é necessário substituir o corredor
            -> Int -- ^ Variável para determinar que lista e necessária ir usar da função 'ConstroiCasa'
            -> Maze

colocaCasa' x y [] a b= []
colocaCasa' x y (z:zs) a b
 | a >= ((div y 2)-2) && a <= ((div y 2)+2) = subCasa x b z : colocaCasa' x y zs (a+1) (b+1)
 | otherwise = z : colocaCasa' x y zs (a+1) b


-- | Obtém a lista da casa dos fantasmas necessária para substituir no maze
subCasa :: Int -- ^ Largura do labirinto
        -> Int  -- ^ Nº da lista que se vai buscar à função 'constroiCasa'
        -> Corridor -- ^ Corredor que se pretende substituir
        -> Corridor

subCasa x a z 
 |mod x 2 == 0 = (take ((div x 2)-5) z) ++ (!!) (constroiCasa x) a ++ (drop ((div x 2)+5) z)
 |otherwise = (take ((div x 2)-5) z) ++ (!!) (constroiCasa x) a ++ (drop ((div x 2)+6) z)


---------------------Testes---------------------



testar :: Int -> Int -> Int -> IO ()
testar x y z = do putStrLn ( testes x y z 1 )

testes x y z a
 | a == 1 = (teste1 (generateMaze x y z) x y) ++ "\n" ++ testes x y z 2
 | a == 2 = (teste2 (generateMaze x y z) x y) ++ "\n" ++ testes x y z 3
 | a == 3 = (teste3 (generateMaze x y z) x y)

teste1 (m:ms) x y = if ((length (m:ms)) == y) && ((length m) == x) then "O Labirinto tem as dimensoes corretas" else "(Failed) O Labirinto nao tem as dimensoes corretas"

teste2 (m:ms) x y = if (m == (replicate x Wall)) && ((last ms) == (replicate x Wall)) then "O Labirinto esta bem limitado superiormente e inferiormente" else "(Failed) O Labirinto nao esta bem limitado superiormente e inferiormente"

teste3 m x y = if even y then if ((head (m !! div1)) == Empty && (last (m !! div1)) == Empty && (head (m !! div0)) == Empty && (last (m !! div0)) == Empty) then "O Labirinto tem o tunel colocado corretamente" else "(Failed) O Labirinto nao tem o tunel colocado corretamente"
                         else if ((head (m !! div0)) == Empty && (last (m !! div0)) == Empty) then "O Labirinto tem o tunel colocado corretamente" else "(Failed) O Labirinto nao tem o tunel colocado corretamente"
 where div1 = ((div y 2)-1)
       div0 = (div y 2)


