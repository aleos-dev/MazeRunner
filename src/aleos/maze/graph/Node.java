package aleos.maze.graph;

import aleos.maze.general.Cell;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node implements Serializable, Comparable<Node> {
    private final Cell coordinate;
    private final Set<Edge> edges = new HashSet<>();

    public Node(Cell cell) {
        coordinate = cell;
    }

    public Cell getCoordinate() {
        return coordinate;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return Objects.equals(coordinate, node.coordinate);
    }

    @Override
    public int hashCode() {
        return coordinate != null ? coordinate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Node{" +
                "coordinate=" + coordinate +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return coordinate.compareTo(o.coordinate);
    }

    public Edge getEdgeTo(Node sourceNode) {
        return edges.stream()
                .filter(edge -> edge.getTargetNode().equals(sourceNode))
                .findAny()
                .orElse(null);
    }
}
