module Generator where

import System.Random

import Types


sampleMaze :: Maze 
sampleMaze = [
                [Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall],
                [Empty, Food Little , Food Little, Food Big, Food Little, Food Little, Food Little, Empty],
                [Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall]
            ]


-- | Given a seed returns a list of n integer randomly generated
--
geraAleatorios :: Int -> Int -> [Int]
geraAleatorios n seed = let gen = mkStdGen seed -- creates a random generator
                        in take n $ randomRs (0,99) gen -- takes the first n elements from an infinite series of random numbers between 0-9


-- | Given a seed returns an integer randomly generated
--
nrAleatorio :: Int -> Int
nrAleatorio seed = head $ geraAleatorios 1 seed


-- | Converts a  list into a list of list of size n

subLista :: Int -> [a] -> [[a]]
subLista _ [] = []
subLista n l = take n l: subLista n (drop n l)


-- | Converts an integer number into a Peca
convertePeca :: Int -> Piece
convertePeca x
    | x == 3 = Food Big
    | x >= 0 && x < 70 = Food Little
    | otherwise = Wall


-- | Converts a Corredor to a string
--
printCorridor :: Corridor -> String
printCorridor []= "\n"
printCorridor (x:xs)= show x ++ printCorridor xs


-- | Converts a list of integers into a Corredor
--
converteCorredor :: [Int] -> Corridor
converteCorredor []=[]
converteCorredor (x:xs)=convertePeca x:converteCorredor xs


-- | Converts a list of lists of integers into a Labirinto
converteLabirinto :: [[Int]] -> Maze
converteLabirinto []=[]
converteLabirinto (x:xs)=converteCorredor x:converteLabirinto xs


-- | Generates a random maze 
geraLabirinto :: Int -> Int -> Int -> Maze
geraLabirinto x y s =
                 let random_nrs = geraAleatorios (x*y) s
                 in  converteLabirinto $ subLista x random_nrs


-- | prints a maze 
imprimeLabirinto :: Maze -> IO ()

imprimeLabirinto l = do putStrLn ( printMaze ( l ))


