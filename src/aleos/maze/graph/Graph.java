package aleos.maze.graph;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a graph that stores nodes and their adjacency lists.
 */
public class Graph implements Serializable {

    /**
     * The adjacency list of nodes in the graph.
     */
    private final Map<Node, PriorityQueue<Edge>> adjacencyList = new HashMap<>();

    /**
     * Returns a set of all nodes in the graph.
     *
     * @return a set of nodes
     */
    public Set<Node> getNodes() {
        return new HashSet<>(adjacencyList.keySet());
    }

    /**
     * Adds a new node to the graph.
     *
     * @param node the node to add
     */
    public void addNode(Node node) {
        adjacencyList.computeIfAbsent(node, k -> new PriorityQueue<>());
    }

    /**
     * Returns the adjacency nodes of a given node.
     *
     * @param node the node to get the adjacency nodes for
     * @return the adjacency nodes of the given node
     */
    public PriorityQueue<Edge> getAdjacencyNodes(Node node) {
        return adjacencyList.get(node);
    }


    /**
     * Checks if the graph contains a specific node.
     *
     * @param node the node to check
     * @return true if the graph contains the node, false otherwise
     */
    public boolean contains(Node node) {
        return adjacencyList.containsKey(node);
    }


}
