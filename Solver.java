import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private boolean solvable;
    private SearchNode solveNode;
    private int moves;
    private final Stack<Board> solutionBoards;


    private class SearchNode {
        private final Board myBoard;
        private final SearchNode previousNode;
        private final int moves;
        private final int priority;

        public SearchNode(SearchNode previous, Board board) {
            myBoard = board;
            previousNode = previous;

            if (previous == null) {
                moves = 0;
            }
            else moves = previous.moves + 1;
            priority = moves + myBoard.manhattan();
        }

        public boolean goalReached() {
            return myBoard.isGoal();
        }

        public Comparator<SearchNode> priorityComparator() {
            return new ComparePriority();

        }

        private class ComparePriority implements Comparator<SearchNode> {
            public int compare(SearchNode a, SearchNode b) {
                return Integer.compare(a.priority, b.priority);
            }
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        moves = 0;
        solvable = false;
        solveNode = null;
        if (initial == null) throw new IllegalArgumentException();

        solutionBoards = new Stack<Board>();


        // initialize the PQ and the initial node
        SearchNode initialNode = new SearchNode(null, initial);
        SearchNode initialTwinNode = new SearchNode(null, initial.twin());

        MinPQ<SearchNode> solverPQ = new MinPQ<SearchNode>(initialNode.priorityComparator());
        MinPQ<SearchNode> solverTwinPQ = new MinPQ<SearchNode>(
                initialTwinNode.priorityComparator());

        solverPQ.insert(initialNode);
        solverTwinPQ.insert(initialTwinNode);

        SearchNode minNode = solverPQ.delMin();
        SearchNode minTwinNode = solverTwinPQ.delMin();

        while (true) {

            if (minNode.goalReached()) {
                solvable = true;
                solveNode = minNode;
                break;
            }
            if (minTwinNode.goalReached()) {
                break;
            }

            Iterable<Board> neighbors = minNode.myBoard.neighbors();
            Iterable<Board> twinNeighbors = minTwinNode.myBoard.neighbors();

            for (Board board : neighbors) {
                if (minNode.previousNode == null || !board.equals(minNode.previousNode.myBoard)) {
                    solverPQ.insert(new SearchNode(minNode, board));
                }
            }

            for (Board twinBoard : twinNeighbors) {
                if (minNode.previousNode == null || !twinBoard
                        .equals(minTwinNode.previousNode.myBoard)) {
                    solverTwinPQ.insert(new SearchNode(minTwinNode, twinBoard));
                }
            }

            minNode = solverPQ.delMin();
            minTwinNode = solverTwinPQ.delMin();
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        solution();
        return moves - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!solvable) return null;
        if (!solutionBoards.isEmpty()) return solutionBoards;

        moves = 0;
        SearchNode currentNode = solveNode;
        while (currentNode != null) {
            solutionBoards.push(currentNode.myBoard);
            currentNode = currentNode.previousNode;
            moves++;
        }
        return solutionBoards;
    }


    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
