import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Solution to Arrows and Arrows. The implementation parses the input and
 * generates a directed graph with a single direction arrow from each node.
 * This graph is then traversed to find the longest cycle.
 */
public class ArrowsAndArrows {

    /**
     * Class to represent a node in a graph; storing the x and y positions of
     * the corresponding arrow, the direction arrow itself and the node that it
     * points to.
     */
    public static class Node {
        private int x;
        private int y;
        private char direction;
        private Node next;

        public Node(int x, int y, char direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public char getDirection() {
            return direction;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node[x=" + x + " y=" + y + " direction=" + direction + " next=[" + next.x + ", " + next.y + "]]";
        }
    }

    /**
     * Find the longest cycle in the grid of arrows and print the result. Uses
     * an algorithm which generates a directed graph then traverses it by
     * iterating through nodes and their children until a cycle is found. The
     * longest cycle is kept.
     * @param input  The string input with the width and height of the grid and
     * 				 all arrows
     */
    public static void findLongestCycle(String input) {
        String[] splitInput = input.split("\n");
        int width = Integer.parseInt(splitInput[0].split(" ")[0]);
        int height = Integer.parseInt(splitInput[0].split(" ")[1]);

        String[] grid = Arrays.copyOfRange(splitInput, 1, splitInput.length);

        Map<String, Node> nodeMap = new HashMap<>();

        // Generate a map of coordinates (in String form "x,y") to the
        // corresponding node in that position. This map is then used to find
        // the next nodes of each node.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                nodeMap.put(Integer.toString(x) + "," +  Integer.toString(y), new Node(x, y, grid[y].charAt(x)));
            }
        }

        // Find the next nodes of each node using each direction to find the
        // position of the next node. For each node, get the coordinates of the
        // next node and use this as a key to reference the {@code nodeMap}.
        for (Node node : nodeMap.values()) {
            char direction = node.getDirection();
            String key = getNextNodeKey(node, direction, width, height);
            node.setNext(nodeMap.get(key));
        }

        // Track all unseenNodes; we must visit every node in the graph to
        // find all possible cycles.
        Set<Node> unseenNodes = new HashSet<>(nodeMap.values());

        // Keep references to the length of the current longest cycle (to
        // compare to all cycles found) and the starting node of the current
        // longest cycle (for use in finding the arrows involved in the actual
        // cycle once we are done).
        Node longestCycleNode = null;
        int longestCycle = 0;

        // Until we have visited every node in the graph, we pick an unvisited
        // node to begin with and create a map of Nodes to the Integers.
        while (unseenNodes.size() > 0) {
            Node newNode = unseenNodes.iterator().next();
            Set<Node> seenNodes = new HashSet<>();

            // cyclesMap is used to keep track of all nodes we have seen in the
            // current traversal and count the length of the cycle. The Integer
            // represents the length of the cycle that involves that node as the
            // repeated node. As we visit nodes, we increment all values in the
            // cyclesMap by one.
            Map<Node, Integer> cyclesMap = new HashMap<>();

            // We then iterate through next node links until we find a node that
            // we have seen on this traversal. This node becomes the start and
            // end of the cycle. All other nodes that we visited before this
            // node cannot possibly be involved in this cycle (or any cycle).
            // Therefore the Integer we have stored corresponding to the
            // repeated node is the length of this cycle.
            // Alternatively, stop early if we encounter a node that we have
            // already seen on a previous traversal; we have already found the
            // cycle that occurs when we iterate through that node.
            while (!seenNodes.contains(newNode) && unseenNodes.contains(newNode)) {
                for (Node node : cyclesMap.keySet()) {
                    cyclesMap.put(node, cyclesMap.get(node) + 1);
                }
                cyclesMap.put(newNode, 0);

                seenNodes.add(newNode);
                newNode = newNode.getNext();
            }

            // Remove all nodes that we have seen from this traversal from our
            // set of unseen nodes since we don't need to visit the node ever
            // again.
            unseenNodes.removeAll(seenNodes);

            // Update the longest cycle node if we have encountered a longer
            // cycle than we have previously seen.
            if (cyclesMap.containsKey(newNode) && cyclesMap.get(newNode) > longestCycle) {
                longestCycle = cyclesMap.get(newNode);
                longestCycleNode = newNode;
            }
        }

        // Create the grid of the arrows we have used in our longest cycle by
        // starting at our longestCycleNode and traversing for the length of
        // the cycle. Finally, output the grid in text form.
        char[][] cycleGrid = new char[height][width];

        Node node = longestCycleNode;
        for (int i = 0; i < longestCycle + 1; i++) {
            cycleGrid[node.getY()][node.getX()] = node.getDirection();
            node = node.getNext();
        }

        System.out.println("Longest Cycle: " + (longestCycle + 1));

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cycleGrid[i][j] != '\u0000') {
                    sb.append(cycleGrid[i][j]);
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
        System.out.println();
    }

    /**
     * Return the the coordinates of the next node, given the current node, the
     * direction of the node and the width and height of the grid.
     * @param node  The current node to find the next of
     * @param direction  The direction arrow of the current node
     * @param width  The width of the grid
     * @param height  The height of the grid
     * @return  A {@code String} representing the coordinates of the next node.
     * To be used to lookup the next node in {@code nodeMap}
     */
    public static String getNextNodeKey(Node node, char direction, int width, int height) {
        String key = "";
        switch(direction) {
            case '^':
                if (node.getY() == 0) {
                    key = Integer.toString(node.getX()) + "," +  Integer.toString(height - 1);
                } else {
                    key = Integer.toString(node.getX()) + "," +  Integer.toString(node.getY() - 1);
                }
                break;
            case 'v':
                if (node.getY() == (height - 1)) {
                    key = Integer.toString(node.getX()) + "," +  Integer.toString(0);
                } else {
                    key = Integer.toString(node.getX()) + "," +  Integer.toString(node.getY() + 1);
                }
                break;
            case '<':
                if (node.getX() == 0) {
                    key = Integer.toString(width - 1) + "," +  Integer.toString(node.getY());
                } else {
                    key = Integer.toString(node.getX() - 1) + "," +  Integer.toString(node.getY());
                }
                break;
            case '>':
                if (node.getX() == (width - 1)) {
                    key = Integer.toString(0) + "," + Integer.toString(node.getY());
                } else {
                    key = Integer.toString(node.getX() + 1) + "," + Integer.toString(node.getY());
                }
                break;
        }
        return key;
    }

    public static void main(String[] args) {
        String input1 = "5 5\n"
                + ">>>>v\n"
                + "^v<<v\n"
                + "^vv^v\n"
                + "^>>v<\n"
                + "^<<<^"
                ;

        String input2 = "45 20\n"
                + "^^v>>v^>>v<<<v>v<>>>>>>>>^vvv^^vvvv<v^^><^^v>\n"
                + ">><<>vv<><<<^><^<^v^^<vv>>^v<v^vv^^v<><^>><v<\n"
                + "vv<^v<v<v<vvv>v<v<vv<^<v<<<<<<<<^<><>^><^v>>>\n"
                + "<v<v^^<v<>v<>v<v<^v^>^<^<<v>^v><^v^>>^^^<><^v\n"
                + "^>>>^v^v^<>>vvv>v^^<^<<<><>v>>^v<^^<>v>>v<v>^\n"
                + "^^^<<^<^>>^v>>>>><>>^v<^^^<^^v^v<^<v^><<^<<<>\n"
                + "v<>v^vv^v<><^>v^vv>^^v^<>v^^^>^>vv<^<<v^<<>^v\n"
                + "<<<<<^<vv<^><>^^>>>^^^^<^<^v^><^v^v>^vvv>^v^^\n"
                + "<<v^<v<<^^v<>v>v^<<<<<>^^v<v^>>>v^><v^v<v^^^<\n"
                + "^^>>^<vv<vv<>v^<^<^^><><^vvvv<<v<^<<^>^>vv^<v\n"
                + "^^v^>>^>^<vv^^<>>^^v>v>>v>>v^vv<vv^>><>>v<<>>\n"
                + "^v<^v<v>^^<>>^>^>^^v>v<<<<<>><><^v<^^v><v>^<<\n"
                + "v>v<><^v<<^^<^>v>^><^><v^><v^^^>><^^<^vv^^^>^\n"
                + "v><>^><vv^v^^>><>^<^v<^><v>^v^<^<>>^<^vv<v>^v\n"
                + "><^<v>>v>^<<^>^<^^>v^^v<>>v><<>v<<^><<>^>^v<v\n"
                + ">vv>^>^v><^^<v^>^>v<^v><>vv>v<^><<<<v^<^vv<>v\n"
                + "<><<^^>>^<>vv><^^<vv<<^v^v^<^^^^vv<<>^<vvv^vv\n"
                + ">v<<v^><v<^^><^v^<<<>^<<vvvv^^^v<<v>vv>^>>^<>\n"
                + "^^^^<^<>^^vvv>v^<<>><^<<v>^<<v>>><>>><<^^>vv>\n"
                + "<^<^<>vvv^v><<<vvv<>>>>^<<<^vvv>^<<<^vv>v^><^"
                ;

        findLongestCycle(input1);
        findLongestCycle(input2);
    }
}
