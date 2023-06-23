package aleos.maze.general;


import java.util.Optional;
import java.util.Scanner;


/**
 * Represents a menu for interacting with the maze.
 */
public class MazeMenu {

    /**
     * The scanner for reading user input.
     */
    private final Scanner scanner;

    public MazeMenu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the maze menu and handles user input.
     *
     * @param maze the maze to interact with
     */
    public void start(Maze maze) {

        MazeMenuHandler handler = new MazeMenuHandler(maze, scanner);

        Optional<Option> menuOption;
        do {

            printMenu(maze.isExist());

            int menuIndex = Integer.parseInt(scanner.nextLine());
            menuOption = Option.getOption(menuIndex);


            if (menuOption.isEmpty()) {
                System.out.println("Incorrect option. Please try again;");
                continue;
            }

            handler.handle(menuOption.get());


        } while (menuOption.isEmpty() || !menuOption.get().name().equals("EXIT"));
    }

    /**
     * Prints the menu options based on the current state of the maze.
     *
     * @param isMazeExists true if the maze exists, false otherwise
     */
    private void printMenu(boolean isMazeExists) {

        System.out.println("=== Menu ===");

        Option[] arr = Option.values();
        int size = arr.length;

        if (isMazeExists) {

            for (int i = 1; i <= arr.length; i++) {
                System.out.println(i % size + ". " + arr[i % size].getPrompt());
            }
        } else {

            for (int i = 1; i <= arr.length; i++) {
                if (arr[i % size] == Option.GENERATE || arr[i % size] == Option.LOAD || arr[i % size] == Option.EXIT) {
                    System.out.println(i % size + ". " + arr[i % size].getPrompt());
                }

            }
        }
    }
}
