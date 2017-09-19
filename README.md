# TimeBomb
Simple Minesweeper-esque game built using Java Swing.

## Gameplay
Similarly to the classic game Minesweeper, the game is set on a grid of tiles, each of which may or may not be hiding a bomb.
The objective is to reveal every non-bomb tile without revealing a bomb.
Each non-bomb tile contains a number, which is the Manhattan distance between that tile and the nearest bomb (i.e. the "safe" distance around that tile). Clicking again an already-revealed number will thus reveal all such "safe" tiles.

Left-click to reveal tiles, right-click to mark tiles.

## To run
First, clone this repository. In a terminal, navigate to the repository folder and compile all source files:
    javac \*.java
Then, start the game by running
    java Game.java
