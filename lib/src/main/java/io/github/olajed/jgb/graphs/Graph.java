package io.github.olajed.jgb.graphs;

import io.github.olajed.jgb.number.Complex;
import io.github.olajed.jgb.structures.Monomial;
import io.github.olajed.jgb.structures.Polynomial;
import io.github.olajed.jgb.structures.SparseMonomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents an undirected graph using an adjacency list.
 * Each vertex is represented by an integer in the range [0, vertices - 1].
 */
public class Graph {
    private final int vertexCount;
    private final List<Set<Integer>> adjacencyList;

    /**
     * Constructs a Graph with the specified number of vertices and adjacency list.
     *
     * @param vertices      the number of vertices in the graph
     * @param adjacencyList the adjacency list representing edges between vertices
     */
    public Graph(int vertices, List<Set<Integer>> adjacencyList) {
        this.vertexCount = vertices;
        this.adjacencyList = adjacencyList;
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Get the edges of the graph
     * @return the adjacency list
     */
    public List<Set<Integer>> getEdges() {
        return adjacencyList;
    }

    /**
     * Generates the polynomial ideal generators corresponding to a k-coloring problem on this graph.
     *
     * <p>These polynomials encode the constraints for a proper k-coloring, where each vertex
     * is assigned one of k colors and adjacent vertices have different colors.</p>
     *
     * @param k the number of colors
     * @return a list of polynomials representing the k-coloring ideal generators
     */
    public List<Polynomial<Complex>> kColoringIdealGenerators(int k) {
        var polynomials = new ArrayList<Polynomial<Complex>>(vertexCount + adjacencyList.size());

        // Vertex polynomials
        for (var i = 0; i < vertexCount; i++) {
            var monomials = new ArrayList<Monomial<Complex>>();
            monomials.add(new SparseMonomial<>(new int[vertexCount], GraphProblemsConstants.MINUS_ONE));
            monomials.add(new SparseMonomial<>(vertexMonomial(k, i), GraphProblemsConstants.ONE));
            polynomials.add(new Polynomial<>(monomials, vertexCount, GraphProblemsConstants.ORDERING));
        }

        // Edge polynomials
        for (var u = 0; u < adjacencyList.size(); u++) {
            var neighbors = adjacencyList.get(u);
            if (neighbors == null || neighbors.isEmpty()) {
                continue;
            }

            for (Integer v : neighbors) {
                // Only handle edge once: u < v
                if (u < v) {
                    var monomials = new ArrayList<Monomial<Complex>>();
                    for (var i = 0; i < k; i++) {
                        var exponents = vertexMonomial(k - 1 - i, u);
                        exponents[v - 1] = i; // adjust index since containers are 0-based
                        monomials.add(new SparseMonomial<>(exponents, Complex.one));
                    }
                    polynomials.add(new Polynomial<>(monomials, vertexCount, GraphProblemsConstants.ORDERING));
                }
            }
        }

        return polynomials;
    }

    private int[] vertexMonomial(int exponent, int vertex) {
        var exponents = new int[vertexCount];
        exponents[vertex] = exponent;
        return exponents;
    }
}
