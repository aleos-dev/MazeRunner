package aleos.maze;

import aleos.maze.general.Maze;
import aleos.maze.general.MazeMenu;

public class Main {

    public static void main(String[] args) {
        Maze maze = new Maze();
        MazeMenu menu = new MazeMenu();

        menu.start(maze);
    }
}