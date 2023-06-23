package aleos.maze.general;

import aleos.maze.general.Maze;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Handles the maze menu options and interacts with the maze based on user input.
 */
public class MazeMenuHandler {

    private final Maze maze;
    private final Scanner scanner;

    MazeMenuHandler(Maze maze, Scanner scanner) {
        this.maze = maze;
        this.scanner = scanner;
    }

    /**
     * Handles the specified menu option.
     *
     * @param command the menu option to handle
     */
    public void handle(Option command) {
        switch (command) {
            case GENERATE -> generateMazeHandler();
            case LOAD -> loadMazeHandler();
            case SAVE -> saveMazeHandler();
            case DISPLAY -> displayMazeHandler();
            case FIND_PATH_TO_ESCAPE -> findPathMazeHandler();
            case EXIT -> exitMazeHandler();
        }
    }

    private void findPathMazeHandler() {
        var paths = maze.findPaths();
        maze.displayEscapePath(paths, maze.getExit());

        System.out.println(maze);
    }


    private void generateMazeHandler() {
        System.out.println("Enter the size of a new maze");

        int size = Integer.parseInt(scanner.nextLine());
        maze.generate(size);

        System.out.println(maze);
    }


    private void saveMazeHandler() {
        Path path = Path.of(scanner.nextLine());
        try (FileOutputStream fileOutput = new FileOutputStream(path.toFile());
             ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {

            objectOutput.writeObject(maze);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadMazeHandler() {
        Path path = Path.of(scanner.nextLine());

        try (FileInputStream fileInput = new FileInputStream(path.toFile());
             ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {

            maze.copyOf((Maze) objectInput.readObject());

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void displayMazeHandler() {
        System.out.println(maze);
    }

    private void exitMazeHandler() {
        System.out.println("Bye!");
    }


}
