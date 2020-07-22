# The Goose Game Kata
Goose game is a game where two or more players move pieces around a track by rolling a die. The aim of the game is to reach square number sixty-three before any of the other players and avoid obstacles. ([wikipedia](https://en.wikipedia.org/wiki/Game_of_the_Goose))

#Installation Requirements
Java JDK/JRE >= 1.8
Maven 

#Installation Instructions
clone the repository or download as zip file
move in base path
type mvn clean install
move in target folder
open your shell/cmd and type "java -jar ./kata-1.0.0.jar"
enjoy the game! :)

#Game commands
1) "add player <name>"           | adds player                               | -> ex: add player Mario
2) "start game"                  | starts game                               | -> ex: start game
3) "move <playerName>"           | moves player with system's dice numbers   | -> ex: move Mario
4) "move <playerName> <X>, <Y>"  | moves player from current position to x+y | -> ex: move Mario 2, 4
5) "exit"                        | closes from game                          | -> ex: exit                   | closes from game                          | -> ex: exit