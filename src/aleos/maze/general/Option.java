package aleos.maze.general;


import java.util.Optional;

enum Option {
    EXIT("Exit."),
    GENERATE("Generate a new maze."),
    LOAD("Load a maze."),
    SAVE("Save the maze."),
    DISPLAY("Display the maze."),
    FIND_PATH_TO_ESCAPE("Find the escape.");

    private final String prompt;

    Option(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    /**
     * Gets the option based on its ordinal.
     *
     * @param ordinal the ordinal of the option
     * @return an Optional containing the option if found, otherwise an empty Optional
     */
    public static Optional<Option> getOption(int ordinal) {

        Option option = null;
        try {

            option = values()[ordinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            // NOPE
        }

        return Optional.ofNullable(option);
    }
}
