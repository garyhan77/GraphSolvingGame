import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Maze {
    //instance variables for class
    private Graph mazeGraph;
    private GraphNode startingNode;
    private GraphNode exitNode;
    private int coinsAvailable;

    //constructor that reads the input file and builds the graph representing
    //the maze. If the input file does not exist, or the format of the input file is incorrect this method should
    //throw a MazeException. Read below to learn about the format of the input file.
    public Maze(String inputFile) throws MazeException {
        try (BufferedReader input = new BufferedReader(new FileReader(inputFile))) {
            initializeMaze(input);
        } catch (IOException e) {
            throw new MazeException("There was a problem when reading file " + e.getMessage());
        }
    }
    //creating new maze based on maze layout
    private void initializeMaze(BufferedReader in) throws IOException, MazeException {
        try {
            int sFactor = Integer.parseInt(in.readLine());
            int wid = Integer.parseInt(in.readLine());
            int hei = Integer.parseInt(in.readLine());
            coinsAvailable = Integer.parseInt(in.readLine());

            mazeGraph = new Graph(wid * hei);

            //2*height -1 gives numbers of columns of entire maze construction input
            String[][] mazeLayout = new String[2 * hei - 1][2 * wid - 1];
            for (int i = 0; i < 2 * hei - 1; i++) {
                String line = in.readLine();
                for (int j = 0; j < line.length(); j++) {
                    mazeLayout[i][j] = String.valueOf(line.charAt(j));
                }
            }

            processMazeLayout(mazeLayout, wid, hei);
        } catch (NumberFormatException | GraphException e) {
            throw new MazeException("Wrong file format");
        }
    }
    //identifying the maze layout after reading the maze layout file
    private void processMazeLayout(String[][] mazeLayout, int width, int height) throws MazeException, GraphException {
        int curIndex = 0;
        for (int i = 0; i < 2 * height - 1; i++) {
            for (int j = 0; j < 2 * width - 1; j++) {
                String currentChar = mazeLayout[i][j];

                if (i % 2 == 0 && (currentChar.equals("s") || currentChar.equals("o") || currentChar.equals("x"))) {
                    if (currentChar.equals("s")) {
                        startingNode = mazeGraph.getNode(curIndex);
                    } else if (currentChar.equals("x")) {
                        exitNode = mazeGraph.getNode(curIndex);
                    }
                    connectNodes(mazeLayout, i, j, curIndex, width, height);
                    curIndex++;
                }
            }
        }
    }

    //
    private void connectNodes(String[][] mazeLayout, int i, int j, int curIndex, int wid, int hei) throws MazeException, GraphException {
        String rightNeighbour, downNeighbour;

        if (2 * hei - 1 > i + 1) {
            downNeighbour = mazeLayout[i + 1][j];
            if (!downNeighbour.equals("w")) {
                int coinsRequired = downNeighbour.equals("c") ? 0 : Integer.parseInt(downNeighbour);
                mazeGraph.insertEdge(mazeGraph.getNode(curIndex), mazeGraph.getNode(curIndex + wid), coinsRequired, downNeighbour.equals("c") ? "corridor" : "door");
            }
        }

        if (2 * wid - 1 > j + 1) {
            rightNeighbour = mazeLayout[i][j + 1];
            if (!rightNeighbour.equals("w")) { // Check if it's not a wall
                int coinsRequired = rightNeighbour.equals("c") ? 0 : Integer.parseInt(rightNeighbour); // Determine the cost (0 for corridor, a number for door)
                mazeGraph.insertEdge(mazeGraph.getNode(curIndex), mazeGraph.getNode(curIndex + 1), coinsRequired, rightNeighbour.equals("c") ? "corridor" : "door");
            }
        }
    }
    //returns a reference to the Graph object representing the maze. Throws a
    //MazeException if the graph is null.
    public Graph getGraph() throws MazeException {
        if (mazeGraph == null) {
            throw new MazeException("Graph is not initialized");
        }
        return mazeGraph;
    }
    //returns a java Iterator containing the nodes of the path from the entrance to
    //the exit of the maze, if such a path exists. If the path does not exist, this method returns the value null.
    //For example for the maze described below the Iterator returned by this method should contain the
    //nodes 0, 1, 5, 6, and 10.
    public Iterator<GraphNode> solve() throws GraphException {
        Stack<GraphNode> stack = new Stack<>();
        Stack<GraphNode> path = new Stack<>();
        if(explore(stack, path, startingNode, coinsAvailable)) {
            return path.iterator();
        }
        return null;
    }
    //algorithm for the rules when exploring the maze
    private boolean explore(Stack<GraphNode> stack, Stack<GraphNode> path, GraphNode currentNode, int coinAmount) throws GraphException {
        if (currentNode.equals(exitNode)) {
            path.push(currentNode);
            while (!stack.isEmpty()) {
                path.push(stack.pop());
            }
            return true;
        }

        if (!currentNode.isMarked()) {
            currentNode.mark(true);
            stack.push(currentNode);

            Iterator<GraphEdge> edges = mazeGraph.incidentEdges(currentNode);
            while (edges.hasNext()) {
                GraphEdge edge = edges.next();
                GraphNode neighbor = edge.firstEndpoint().equals(currentNode) ? edge.secondEndpoint() : edge.firstEndpoint();

                if (!neighbor.isMarked()) {
                    int coinsRequired = edge.getLabel().equals("door") ? edge.getType() : 0;
                    int newCoins = coinAmount - coinsRequired;

                    if (newCoins >= 0 && explore(stack, path, neighbor, newCoins)) {
                        return true;
                    }
                }
            }

            stack.pop();
            currentNode.mark(false);
        }

        return false;
    }
}
