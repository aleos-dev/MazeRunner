package aleos.maze.general;

/**
 * Represents the directions in the maze.
 */
public enum Direction {

    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

    final int x, y;

    /**
     * Creates a new direction with the specified x and y components.
     *
     * @param x the x component of the direction
     * @param y the y component of the direction
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x component of the direction.
     *
     * @return the x component of the direction
     */
    int getX() {
        return x;
    }

    /**
     * Gets the y component of the direction.
     *
     * @return the y component of the direction
     */
    int getY() {
        return y;
    }
}
