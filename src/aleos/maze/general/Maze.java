package aleos.maze.general;


import aleos.maze.graph.Node;
import aleos.maze.graph.Edge;
import aleos.maze.graph.Graph;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a maze and provides methods for maze generation, finding paths, and displaying the maze.
 */
public class Maze implements Serializable {
    private int width;
    private int height;

    private Graph graph;
    private int[][] grid;
    private Node entrance;
    private Node exit;

    /**
     * The thickness of the maze walls.
     */
    private static final int WALL_THICKNESS = 1;

    /**
     * The identifier for passage cells in the maze grid.
     */
    private static final int PASSAGE_CELL_IDENTIFIER = 0;

    /**
     * The identifier for block cells (walls) in the maze grid.
     */
    private static final int BLOCK_CELL_IDENTIFIER = 1;

    /**
     * The identifier for path cells in the maze grid.
     */
    private static final int PATH_CELL_IDENTIFIER = 2;

    /**
     * The list of frontier nodes during maze generation.
     */
    private transient List<Node> frontierNodes;

    /**
     * The random number generator.
     */
    private transient final Random randomGenerator = new Random();


    /**
     * Generates a new maze of the specified size.
     *
     * @param size the size of the maze
     */
    public void generate(int size) {
        frontierNodes = new ArrayList<>();
        graph = new Graph();

        if (size < 5) {
            throw new IllegalArgumentException("Invalid maze dimensions: Width and height must be at least 5.");
        }

        size = size % 2 == 0 ? size + 1 : size;

        this.width = size;
        this.height = size;
        grid = new int[width][height];

        for (int[] row : grid) {
            Arrays.fill(row, BLOCK_CELL_IDENTIFIER);
        }

        generateGraph();
        displayGraphOnGrid();
        createEntrances();

    }

    /**
     * Finds the shortest paths from the entrance to all other nodes in the maze.
     *
     * @return a map of nodes and their respective previous nodes on the shortest paths
     */
    public Map<Node, Node> findPaths() {
        // Initialize a map to store the previous nodes on the shortest paths
        Map<Node, Node> paths = new HashMap<>();

        // Initialize a map to store the distances of each node from the entrance
        Map<Node, Integer> distances = new HashMap<>();
        graph.getNodes().forEach(node -> distances.put(node, Integer.MAX_VALUE));
        distances.put(entrance, 0);

        // Create a map of adjacency lists for efficient access to neighboring nodes
        Map<Node, PriorityQueue<Edge>> adjacencyList = graph.getNodes().stream()
                .collect(Collectors.toMap(
                        node -> node,
                        graph::getAdjacencyNodes
                ));

        // Create a priority queue to track the nodes being processed in order of their distances
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        pq.offer(entrance);

        Node sourceNode;
        while (!pq.isEmpty()) {
            sourceNode = pq.poll();
            PriorityQueue<Edge> edges = adjacencyList.get(sourceNode);
            if (edges == null) continue;
            for (Edge edge : edges) {
                Node destNode = edge.getTargetNode();

                int newDistance = distances.get(sourceNode) + edge.getEdgeWeight();
                int minCurrentDistance = distances.get(destNode);
                if (newDistance < minCurrentDistance) {
                    distances.put(destNode, newDistance);
                    pq.offer(destNode);
                    paths.put(destNode, sourceNode);
                }

                adjacencyList.get(destNode).remove(destNode.getEdgeTo(sourceNode));
                if (adjacencyList.get(destNode).isEmpty()) {
                    adjacencyList.remove(destNode);
                }

            }
            adjacencyList.remove(sourceNode);
        }

        return paths;
    }

    /**
     * Generates the graph representation of the maze based on the seed and frontier nodes.
     */
    private void generateGraph() {
        Node seed = generateInitialSeed();
        graph.addNode(seed);

        do {
            searchForFrontierNodes(seed);
            seed = getNextSeed();
        } while (!frontierNodes.isEmpty());

    }

    /**
     * Searches for frontier nodes adjacent to the given seed node and adds them to the graph.
     *
     * @param seed the seed node to search for frontier nodes
     */
    private void searchForFrontierNodes(Node seed) {
        int distance = 2;
        for (Direction direction : Direction.values()) {

            Cell shiftedCell = seed.getCoordinate().shift(direction, distance);
            Node newFrontierNode = new Node(shiftedCell);

            if (!graph.contains(newFrontierNode) && isValidNode(newFrontierNode)) {

                graph.addNode(newFrontierNode);

                Edge edgeToSeed = new Edge(seed, distance);
                newFrontierNode.addEdge(edgeToSeed);
                graph.getAdjacencyNodes(newFrontierNode).offer(edgeToSeed);

                Edge edgeToNewFrontierNode = new Edge(newFrontierNode, distance);
                seed.addEdge(edgeToNewFrontierNode);
                graph.getAdjacencyNodes(seed).offer(edgeToNewFrontierNode);


                frontierNodes.add(newFrontierNode);
            }
        }
    }

    /**
     * Retrieves the next seed node from the list of frontier nodes.
     *
     * @return the next seed node
     */
    private Node getNextSeed() {
        int index = 0;
        if (frontierNodes.size() > 1) {
            index = randomGenerator.nextInt(frontierNodes.size());
            index = index % 2 == 0 ? index : index - 1;
        }

        return frontierNodes.remove(index);
    }

    /**
     * Displays the graph representation on the maze grid.
     */
    private void displayGraphOnGrid() {
        for (var node : graph.getNodes()) {
            for (var edge : graph.getAdjacencyNodes(node)) {

                Cell fromCoordinate = node.getCoordinate();
                Cell toCoordinate = edge.getTargetNode().getCoordinate();
                pavePathBetweenCellsOnGridWithIdentifier(fromCoordinate, toCoordinate, PASSAGE_CELL_IDENTIFIER);
            }
        }
    }

    /**
     * Displays the path from the exit to the entrance on the maze grid.
     *
     * @param path the map of nodes and their respective previous nodes on the path
     * @param end  the end node of the path (exit)
     */
    public void displayEscapePath(Map<Node, Node> path, Node end) {

        if (!path.containsKey(end)) {
            return;
        }

        pavePathBetweenCellsOnGridWithIdentifier(end.getCoordinate(), path.get(end).getCoordinate(), PATH_CELL_IDENTIFIER);

        displayEscapePath(path, path.get(end));

    }

    /**
     * Finds and creates entrances at the already existed maze.
     */
    private void createEntrances() {
        Optional<Node> nodeAdjacentToWestBorder = graph.getNodes().stream()
                .filter(n -> n.getCoordinate().getX() == WALL_THICKNESS)
                .skip(randomGenerator.nextInt(width / 2))
                .findAny();
        Optional<Node> nodeAdjacentToEastBorder = graph.getNodes().stream()
                .filter(n -> n.getCoordinate().getX() == width - WALL_THICKNESS - 1)
                .skip(randomGenerator.nextInt(width / 4, width / 2))
                .findAny();

        if (nodeAdjacentToWestBorder.isEmpty() || nodeAdjacentToEastBorder.isEmpty()) {
            throw new IllegalStateException("Node can't be null");
        }

        Cell entranceCoordinate = nodeAdjacentToWestBorder.get().getCoordinate().shift(Direction.WEST, 1);
        entrance = new Node(entranceCoordinate);
        bindNewNodeToGraph(entrance, nodeAdjacentToWestBorder.get());

        Cell exitCoordinate = nodeAdjacentToEastBorder.get().getCoordinate().shift(Direction.EAST, 1);
        exit = new Node(exitCoordinate);
        bindNewNodeToGraph(exit, nodeAdjacentToEastBorder.get());
    }

    /**
     * Binds a new node to the maze graph and creates a path between the new node and an existing node.
     *
     * @param newNode     the new node to add
     * @param graphNode   the existing node to connect the new node to
     */
    private void bindNewNodeToGraph(Node newNode, Node graphNode) {
        pavePathBetweenCellsOnGridWithIdentifier(graphNode.getCoordinate(), newNode.getCoordinate(), PASSAGE_CELL_IDENTIFIER);

        Edge edgeToNodeAdjacentToExit = new Edge(graphNode, 1);
        Edge edgeToExit = new Edge(newNode, 1);

        newNode.addEdge(edgeToNodeAdjacentToExit);
        graphNode.addEdge(edgeToExit);

        graph.addNode(newNode);
        graph.getAdjacencyNodes(newNode).offer(edgeToNodeAdjacentToExit);
        graph.getAdjacencyNodes(graphNode).offer(edgeToExit);
    }


    /**
     * Generates the initial seed (starting node) for maze generation.
     *
     * @return the initial seed node
     */
    private Node generateInitialSeed() {
        int shift = 2;

        while (true) {
            int x = randomGenerator.nextInt(width - shift) + WALL_THICKNESS;
            int y = randomGenerator.nextInt(height - shift) + WALL_THICKNESS;

            if (width % 2 == 1 && x % 2 != 1) {
                continue;
            }

            if (height % 2 == 1 && y % 2 != 1) {
                continue;
            }

            return new Node(new Cell(x, y));
        }
    }

    /**
     * Paves a path between two cells on the maze grid with the specified identifier.
     *
     * @param from  the starting cell
     * @param to    the ending cell
     * @param value the identifier to use for the path cells
     */
    private void pavePathBetweenCellsOnGridWithIdentifier(Cell from, Cell to, int value) {
        int x1 = from.getX();
        int y1 = from.getY();

        int x2 = to.getX();
        int y2 = to.getY();

        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                grid[x1][i] = value;
            }
        } else if (y1 == y2) {
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                grid[i][y1] = value;
            }
        }
    }


    /**
     * Checks if a node is a valid candidate for maze generation.
     *
     * @param node the node to check
     * @return true if the node is valid, false otherwise
     */
    private boolean isValidNode(Node node) {
        Cell cell = node.getCoordinate();
        if (frontierNodes.contains(cell)) {
            return false;
        }
        return cell.getX() >= WALL_THICKNESS && cell.getX() < width - WALL_THICKNESS && cell.getY() >= WALL_THICKNESS && cell.getY() < height - WALL_THICKNESS;
    }

    /**
     * Checks if the maze exists.
     *
     * @return true if the maze exists, false otherwise
     */
    public boolean isExist() {
        return grid != null;
    }

    /**
     * Copies the properties of another Maze object to this maze.
     *
     * @param maze the maze to copy from
     */
    public void copyOf(Maze maze) {
        this.width = maze.width;
        this.height = maze.height;

        this.entrance = maze.entrance;
        this.exit = maze.exit;

        this.grid = maze.grid;
        this.graph = maze.graph;
    }

    /**
     * Returns the exit node of the maze.
     *
     * @return the exit node
     */
    public Node getExit() {
        return exit;
    }

    /**
     * Returns a string representation of the maze.
     *
     * @return the string representation of the maze
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String wall = "██";
        String passage = "  ";
        String path = "//";

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String sign = switch (grid[x][y]) {
                    case BLOCK_CELL_IDENTIFIER -> wall;
                    case PASSAGE_CELL_IDENTIFIER -> passage;
                    case PATH_CELL_IDENTIFIER -> path;
                    default -> "  ";
                };

                result.append(sign);
            }

            result.append('\n');
        }

        return result.toString();
    }
}
