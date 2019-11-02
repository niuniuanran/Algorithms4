import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph myDigraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("null digraph for SAP constructor");
        myDigraph = new Digraph(G);
        // order = new Topological(G).order();
        // topological order is achieved through depth-first order.
        // This means the path recognized is feasible, but not neccessarily the shortest.

        // In order to find the shortest path from one vertex to another,
        // I really want to use Breadth First Search.

        // Please read BreadthFirstDirectedPaths.java
        // After running a BreadthFirstDirectedPaths constructor on vertex v,
        // You will be able to know the shortest distance and path from v
        // to each of the vertices in the Digraph.
        // If a vertex w is not reachable from v, the distance will be INFINITY and marked[w] = false.
    }

    private int sca(Iterable<Integer> v, Iterable<Integer> w, boolean wantLength) {
        if (!validateInput(v, w))
            throw new IllegalArgumentException("Illegal argument for SAP calculation");
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(myDigraph, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(myDigraph, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < myDigraph.V(); i++) {
            if (vPaths.hasPathTo(i) && wPaths.hasPathTo(i)) {
                if (vPaths.distTo(i) + wPaths.distTo(i) < minLength) {
                    ancestor = i;
                    minLength = vPaths.distTo(i) + wPaths.distTo(i);
                }
            }
        }
        if (ancestor != -1) return wantLength ? minLength : ancestor;
        return -1;
    }

    private boolean validateInput(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) return false;
        for (Integer i : v) {
            if (i == null) return false;
            if (i < 0) return false;
            if (i >= myDigraph.V()) return false;
        }
        for (Integer i : w) {
            if (i == null) return false;
            if (i < 0) return false;
            if (i > myDigraph.V()) return false;
        }
        return true;
    }

    private Iterable<Integer> pack(int v) {
        Bag<Integer> vBag = new Bag<Integer>();
        vBag.add(v);
        return vBag;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return sca(pack(v), pack(w), true);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return sca(pack(v), pack(w), false);
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return sca(v, w, true);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return sca(v, w, false);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
