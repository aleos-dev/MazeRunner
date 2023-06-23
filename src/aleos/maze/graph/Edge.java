package aleos.maze.graph;

import java.io.Serializable;

/**
 * Represents an edge between two nodes in the maze graph.
 */
public class Edge implements Comparable<Edge>, Serializable {

    /**
     * The destination node of the edge.
     */
    private Node targetNode;

    /**
     * The weight of the edge.
     */
    private int edgeWeight;

    /**
     * Creates a new Edge with the specified destination node and weight.
     *
     * @param targetNode     the destination node
     * @param edgeWeight the weight of the edge
     */
    public Edge(Node targetNode, int edgeWeight) {
        this.targetNode = targetNode;
        this.edgeWeight = edgeWeight;
    }

    /**
     * Returns the destination node of the edge.
     *
     * @return the destination node
     */
    public Node getTargetNode() {
        return targetNode;
    }

    /**
     * Returns the weight of the edge.
     *
     * @return the weight of the edge
     */
    public int getEdgeWeight() {
        return edgeWeight;
    }

    /**
     * Compares this edge with another edge based on their weights.
     *
     * @param other the other edge to compare to
     * @return a negative integer, zero, or a positive integer as this edge is less than, equal to, or greater than the other edge
     */
    @Override
    public int compareTo(Edge other) {
        return edgeWeight - other.edgeWeight;
    }

    /**
     * Checks if this edge is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (edgeWeight != edge.edgeWeight) return false;
        return targetNode.equals(edge.targetNode);
    }

    /**
     * Returns the hash code of this edge.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = 31 + targetNode.hashCode();
        result = 30 * result + edgeWeight;
        return result;
    }

    /**
     * Returns a string representation of this edge.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "{" + targetNode.getCoordinate().getX() + ", " + targetNode.getCoordinate().getY() + "}"
                + " -- " + edgeWeight;
    }

}
