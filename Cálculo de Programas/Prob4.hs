{-# LANGUAGE NPlusKPatterns #-}
{-# LANGUAGE FlexibleInstances #-}

module Prob4 where

import Cp
import Nat
import Rose
import List
import Control.Concurrent
import NEList
import Data.List
import Probability
import RelCalc
import LTree


type Team = String
type Group = [Team]
type Match = (Team, Team) 
groupP = last groups

rankings = [
         ("Argentina",4.8),
         ("Australia",4.0),
         ("Belgium",5.0),
         ("Brazil",5.0),
         ("Cameroon",4.0),
         ("Canada",4.0),
         ("Costa Rica",4.1),
         ("Croatia",4.4),
         ("Denmark",4.5),
         ("Ecuador",4.0),
         ("England",4.7),
         ("France",4.8),
         ("Germany",4.5),
         ("Ghana",3.8),
         ("Iran",4.2),
         ("Japan",4.2),
         ("Korea Republic",4.2),
         ("Mexico",4.5),
         ("Morocco",4.2),
         ("Netherlands",4.6),
         ("Poland",4.2),
         ("Portugal",4.6),
         ("Qatar",3.9),
         ("Saudi Arabia",3.9),
         ("Senegal",4.3),
         ("Serbia",4.2),
         ("Spain",4.7),
         ("Switzerland",4.4),
         ("Tunisia",4.1),
         ("USA",4.4),
         ("Uruguay",4.5),
         ("Wales",4.3)]

groups :: [Group]
groups = [["Qatar", "Ecuador", "Senegal", "Netherlands"],
          ["England", "Iran", "USA", "Wales"],
          ["Argentina", "Saudi Arabia", "Mexico", "Poland"],
          ["France", "Denmark", "Tunisia", "Australia"],
          ["Spain", "Germany", "Japan", "Costa Rica"],
          ["Belgium", "Canada", "Morocco", "Croatia"],
          ["Brazil", "Serbia", "Switzerland", "Cameroon"],
          ["Portugal", "Ghana", "Uruguay", "Korea Republic"]]

winners = map (take 2) groups


--groupWinners criteria = best 2 . consolidate . (>>= matchResult criteria)
winner :: Team
winner = wcup groups

wcup = knockoutStage . groupStage 

generateMatches = pairup

knockoutStage = cataLTree (either id koCriteria)

groupStage :: [Group] -> LTree Team
groupStage = initKnockoutStage . simulateGroupStage . genGroupStageMatches

genGroupStageMatches :: [Group] -> [[Match]]
genGroupStageMatches = map generateMatches

simulateGroupStage :: [[Match]] -> [[Team]]
simulateGroupStage = map (groupWinners gsCriteria)

arrangement = (>>= swapTeams) . chunksOf 4 where
  swapTeams [[a1,a2],[b1,b2],[c1,c2],[d1,d2]] = [a1,b2,c1,d2,b1,a2,d1,c2]

rank x = 4 ** (pap rankings x - 3.8)

gsCriteria = s . split (id >< id) (rank >< rank) where
  s ((s1, s2), (r1, r2)) = let d = r1 - r2 in
                           if d > 0.5 then Just s1
                                      else if d < -0.5 then Just s2
                                                       else Nothing

koCriteria = s . split (id >< id) (rank >< rank) where
  s ((s1, s2), (r1, r2)) = let d = r1 - r2 in
                           if d == 0 then s1
                                     else if d > 0 then s1 else s2

pgsCriteria = s . split (id >< id) (rank >< rank) where
  s ((s1, s2), (r1, r2)) =
     if abs(r1-r2) > 0.5 then fmap Just (pkoCriteria(s1,s2)) else f (s1,s2)
  f = D . ((Nothing,0.5):) . map (Just><(/2)) . unD . pkoCriteria

pkoCriteria (e1, e2) = D [(e1, 1 - r2 / (r1 + r2)), (e2, 1 - r1 / (r1 + r2))] where
    r1 = rank e1
    r2 = rank e2

psimulateGroupStage = trim .  map (pgroupWinners pgsCriteria)
trim = top 5 . sequence . map (filterP.norm)  where
   filterP (D x) = D [(a,p) | (a,p) <- x, p > 0.0001 ]
   top n = vec2Dist . take n . reverse . presort snd . unD 
   vec2Dist x = D [ (a,n/t) | (a,n) <- x ] where t = sum(map snd x) 

pwinner :: Dist Team
pwinner = mbin f x >>= pknockoutStage where
   f(x,y) = initKnockoutStage(x++y)
   x = split (g . take 4) (g . drop 4) groups
   g = psimulateGroupStage . genGroupStageMatches

best n = map fst . take n . reverse . presort p2



initKnockoutStage = anaLTree glt . arrangement

mmbin :: Monad m => ((a, b) -> m c) -> (m a, m b) -> m c
mmbin f (a,b) = do { x <- a ; y <- b ; f(x,y) }

mbin :: Monad m => ((a, b) -> c) -> (m a, m b) -> m c
mbin = mmbin . (return.)


groupWinners criteria = best 2 . consolidate' . (>>= matchResult criteria)


matchResult :: (Match -> Maybe Team) -> Match -> [(Team, Int)]
matchResult =  curry $ g . split ap p2
    where g ((Just x),_) = singl (x,3)
          g (Nothing, (e1,e2)) = [(e1,1),(e2,1)] 


pairup :: Eq b => [b] -> [(b,b)] 
pairup = g
    where g (x:xs) = [(x,b) | b <- xs] ++ [(head xs,b) | b <- tail xs] ++ [(xs !! 1, last xs)]


glt = f
    where f [x] = i1 x
          f l = i2 (left, right)
            where (left, right) = splitAt (length l `div` 2) l





cgene :: (Num b, Eq a) => Either n ((a, b), [(a, b)])-> [(a, b)]
cgene = either nil foo

foo :: (Num b, Eq a) => ((a, b), [(a,b)]) -> [(a,b)]
foo = adiciona . (split id f)
    where f = (uncurry lookupList) . (p1 >< id)

adiciona :: (Num b, Eq a) => (((a, b), [(a,b)]), Maybe b) -> [(a,b)]
adiciona ((p,l), Nothing) = cons (p,l)
adiciona ((p,l), Just n) = (uncurry (++)) ([(a,b) | (a,b) <- l, a /= (p1 p)], (sec (+n) [p]))
    where sec f [(x,y)] = [(x, f y)]

consolidate' :: (Eq a, Num b) => [(a, b)] -> [(a, b)]
consolidate' = cataList cgene






-----------------------
--pwinner :: Dist Team
--pwinner = pwcup groups

pwcup = pknockoutStage .! pgroupStage 

pknockoutStage = mcataLTree' (either return pkoCriteria)

pgroupStage = pinitKnockoutStage .! psimulateGroupStage . genGroupStageMatches

mcataLTree' g = k where
        k (Leaf a) = g1 a
        k (Fork(x,y)) = mmbin g2 (k x, k y)
        g1 = g . i1
        g2 = g . i2


pinitKnockoutStage = undefined --do {x <- unD; map (\(a,b) -> (initKnockoutStage a, b)); return D }  


pgroupWinners :: (Match -> Dist (Maybe Team)) -> [Match] -> Dist [Team]
pgroupWinners pgsCriteria = D . g . unD . j . f 
    where f = map (pmatchResult pgsCriteria)
          g = map (\(a,b) -> ( best 2 $ consolidate' a, b))
          j l = joinWith (++) (j1 l) (j2 l)
          j1 l = joinWith (++) (joinWith (++) (l !! 0) (l !! 1)) (l!!2)
          j2 l = joinWith (++) (joinWith (++) (l !! 3) (l !! 4)) (l !! 5) 



pmatchResult = curry $ D . adiciona2 . unD . ap
          
adiciona2 :: [(Maybe Team, ProbRep)] -> [([(Team, Int)], ProbRep)] 
adiciona2 l =  map g l
    where [Just e1, Just e2] = map fst $ take 2 ( reverse l)
          g ((Just x),p) = (singl (x,3) , p)
          g (Nothing, p) = ([(e1,1),(e2,1)], p)