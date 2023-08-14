/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public final class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return sap(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return sap(v, w)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[1];
    }

    // helper methods
    private void checkVertex(Integer v) {
        if (v == null) throw new IllegalArgumentException("Vertex is null");
        if (v < 0 || v >= digraph.V())
            throw new IllegalArgumentException("Vertex is out of bounds");
    }

    private void checkVertices(Iterable<Integer> v) {
        for (Integer w : v) {
            checkVertex(w);
        }
    }

    private int[] sap(int v, int w) {
        checkVertex(v);
        checkVertex(w);

        BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(this.digraph, w);

        int[] da = new int[2];
        da[0] = Integer.MAX_VALUE;
        da[1] = -1;
        for (int vertex = 0; vertex < this.digraph.V(); vertex++) {
            if (vbfs.hasPathTo(vertex) && wbfs.hasPathTo(vertex) && vbfs.distTo(vertex) < da[0]
                    && wbfs.distTo(vertex) < da[0]) {
                int sum = vbfs.distTo(vertex) + wbfs.distTo(vertex);
                if (sum < da[0]) {
                    da[0] = sum;
                    da[1] = vertex;
                }
            }
        }
        if (da[0] == Integer.MAX_VALUE) {
            da[0] = -1;
        }
        return da;
    }

    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null) throw new IllegalArgumentException("Iterable is null");
        Iterator<Integer> iteratorv = v.iterator();
        int countv = 0;
        while (iteratorv.hasNext()) {
            iteratorv.next();
            countv++;
        }
        if (countv == 0) {
            int[] da = {-1, -1};
            return da;
        }
        if (w == null) throw new IllegalArgumentException("Iterable is null");
        Iterator<Integer> iteratorw = w.iterator();
        int countw = 0;
        while (iteratorw.hasNext()) {
            iteratorw.next();
            countw++;
        }
        if (countw == 0) {
            int[] da = {-1, -1};
            return da;
        }
        checkVertices(v);
        checkVertices(w);

        BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(this.digraph, w);

        int[] da = new int[2];
        da[0] = Integer.MAX_VALUE;
        da[1] = -1;
        for (int vertex = 0; vertex < this.digraph.V(); vertex++) {
            if (vbfs.hasPathTo(vertex) && wbfs.hasPathTo(vertex) && vbfs.distTo(vertex) < da[0]
                    && wbfs.distTo(vertex) < da[0]) {
                int sum = vbfs.distTo(vertex) + wbfs.distTo(vertex);
                if (sum < da[0]) {
                    da[0] = sum;
                    da[1] = vertex;
                }
            }
        }
        if (da[0] == Integer.MAX_VALUE) {
            da[0] = -1;
        }
        return da;
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
