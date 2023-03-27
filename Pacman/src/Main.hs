module Main where

import Data.Time.Clock.POSIX
import Control.Monad.IO.Class
import UI.NCurses
import Types
import Tarefa4
import Tarefa5
import Tarefa6


loadManager :: Manager
loadManager = ( Manager statest3 1 0 0 0 defaultDelayTime )


updateControlledPlayer :: Key -> Manager -> Manager
updateControlledPlayer KeyUpArrow (Manager s@(State m x n) p step bf delt del) = Manager (updatePlayer s U (x !! p)) p step bf delt del
updateControlledPlayer KeyDownArrow (Manager s@(State m x n) p step bf delt del) = Manager (updatePlayer s D (x !! p)) p step bf delt del
updateControlledPlayer KeyLeftArrow (Manager s@(State m x n) p step bf delt del) = Manager (updatePlayer s L (x !! p)) p step bf delt del
updateControlledPlayer KeyRightArrow (Manager s@(State m x n) p step bf delt del) = Manager (updatePlayer s R (x !! p)) p step bf delt del


updateScreen :: Window  -> ColorID -> Manager -> Curses ()
updateScreen w a man =
                  do
                    updateWindow w $ do
                      clear
                      setColor a
                      moveCursor 0 0 
                      drawString $ show (state man)
                    render
     
currentTime :: IO Integer
currentTime = fmap ( round . (* 1000) ) getPOSIXTime

updateTime :: Integer -> Manager -> Manager
updateTime now (Manager s p step bf delt del) = Manager s p step bf (now-bf) del

resetTimer :: Integer -> Manager -> Manager
resetTimer now man = man

nextFrame :: Integer -> Manager -> Manager
nextFrame now (Manager s p step bf delt del) = if step == 0 then Manager (passTime step s) p 1 now 0 del --Manager (passTime step s) p (step+1) now 0 del
                                                            else Manager (passTime step s) p 0 now 0 del

loop :: Window -> Manager -> Curses ()
loop w man@(Manager s pid step bf delt del ) = do 
  color_schema <- newColorID ColorBlue ColorWhite  10
  now <- liftIO  currentTime
  updateScreen w  color_schema man
  if ( delt > del )
    then loop w $ nextFrame now man
    else do
          ev <- getEvent w $ Just 0
          case ev of
                Nothing -> loop w (updateTime now man)
                Just (EventSpecialKey arrow ) -> loop w $ updateControlledPlayer arrow (updateTime now man)
                Just ev' ->
                  if (ev' == EventCharacter 'q')
                    then return ()
                    else loop w (updateTime now man)


main :: IO ()
main =
  runCurses $ do
    setEcho False
    setCursorMode CursorInvisible
    w <- defaultWindow
    loop w loadManager


updatePlayer :: State
             -> Orientation
             -> Player
             -> State

updatePlayer (State m x n) o1 (Pacman (PacState (i,c,v,o,p,l) tm mou pm)) = State m ((take i x) ++ [Pacman (PacState (i,c,v,o1,p,l) tm mou pm)] ++ drop (i+1) x) n


