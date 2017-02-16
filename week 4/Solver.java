import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class Solver {
    private Node lastNode = null;

    private class Node implements Comparable<Node> {
        private final Board board;
        private final Node previous;
        private  final int moves;
        private  final int priority;

        public Node(Board board) {
            this.board = board;
            this.moves = 0;
            this.previous = null;
            this.priority = this.board.manhattan() + this.moves;
        }

        public Node(Board board, Node previous) {
            this.board = board;
            this.moves = previous.moves + 1;
            this.previous = previous;
            this.priority = this.board.manhattan() + this.moves;
        }

        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException();

        MinPQ<Node> nodes = new MinPQ<Node>();
        nodes.insert(new Node(initial));

        MinPQ<Node> twinNodes = new MinPQ<Node>();
        twinNodes.insert(new Node(initial.twin()));

        while (lastNode == null) {
            lastNode = nextStep(nodes);
            if (lastNode == null) {
                if (nextStep(twinNodes) != null)
                    break;
            }
        }
    }

    private Node nextStep(MinPQ<Node> nodes) {
        if (nodes.isEmpty()) return null;

        Node nextStep = nodes.delMin();
        if (nextStep.board.isGoal()) return nextStep;

        for (Board board: nextStep.board.neighbors())
            if (nextStep.previous == null || !board.equals(nextStep.previous.board))
                nodes.insert(new Node(board, nextStep));

        return null;
    }

    public boolean isSolvable() {
        return lastNode != null;
    }

    public int moves() {
        return lastNode == null ? -1 : lastNode.moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        LinkedList<Board> solutionNodes = new LinkedList<Board>();
        Node last = lastNode;
        while (last != null) {
            solutionNodes.addFirst(last.board);
            last = last.previous;
        }

        return solutionNodes;
    }

    public static void main(String[] args) {
        Solver solver = new Solver(new Board(new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}}));
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
