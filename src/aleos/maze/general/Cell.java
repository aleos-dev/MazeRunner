package aleos.maze.general;

import java.io.Serializable;

/**
 * Represents a cell in the maze grid.
 */
public class Cell implements Serializable, Comparable<Cell> {
    private final int x;
    private final int y;

    /**
     * Creates a new cell with the specified coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the cell.
     *
     * @return the x-coordinate of the cell
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the cell.
     *
     * @return the y-coordinate of the cell
     */
    public int getY() {
        return y;
    }

    /**
     * Shifts the cell in the specified direction by the given distance.
     *
     * @param direction the direction in which to shift the cell
     * @param distance  the distance by which to shift the cell
     * @return the shifted cell
     */
    public Cell shift(Direction direction, int distance) {
        int shiftX = direction.getX() * distance;
        int shiftY = direction.getY() * distance;
        return new Cell(x + shiftX, y + shiftY);
    }

    /**
     * Checks if this cell is equal to the specified object.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (x != cell.x) return false;
        return y == cell.y;
    }

    /**
     * Computes the hash code of this cell.
     *
     * @return the hash code of the cell
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }

    @Override
    public int compareTo(Cell o) {

        int xCompare = x - o.x;
        if (xCompare != 0) {
            return xCompare;
        }

        return y - o.y;
    }
}

