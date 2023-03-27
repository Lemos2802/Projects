{-# OPTIONS_GHC -XNPlusKPatterns #-}
{-# LANGUAGE GeneralizedNewtypeDeriving, DeriveDataTypeable, FlexibleInstances #-}


module Prob3 where

import Cp
import Nat
import Rose
import List
import Svg
import Control.Concurrent
import NEList

type Square = (Point, Side)
type Point  = (Double, Double)
type Side   = Double

squares :: (Square, Int) -> Rose Square
squares = anaRose gsq


gsq = split initSquare singular 
    where singular (((x,y),l),n) =  [(((a,b), l/3), n-1) | b <- [y, y + l/3, y + 2*(l/3)] , a <- [x + 2*(l/3), x + l/3, x], (a,b) /= (x + l/3, y + l/3), n>0 ]
          initSquare (((x,y),l), _ ) = ((x + l/3,y + l / 3),l/3)


rose2List :: Rose a -> [a]
rose2List = cataRose gr2l

gr2l = cons . (id >< concat)

sierpinski :: (Square, Int) -> [Square]
sierpinski = hyloRose gr2l gsq

drawSq x = picd'' [ Svg.scale 0.44 (0,0) (x >>= sq2svg) ]
sq2svg (p,l) = (color "#67AB9F" . polyg) [ p, p .+ (0,l), p .+ (l,l), p .+ (l,0) ]
await = threadDelay 1000000

constructSierp :: Int -> IO [()]
constructSierp = present . carpets

carpets :: Int -> [[Square]]
carpets = ana $ ((f.const(-1)) -|- split f id) . outNat
    where f n = sierpinski(((0,0),32), n+1)

present :: [[Square]] -> IO [()]
present = cataList $ either (return.nil) (fmap cons.dstr.(f><id))
    where f x = do {r <- drawSq x; await}