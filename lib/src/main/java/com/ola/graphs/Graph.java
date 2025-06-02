package com.ola.graphs;

import com.ola.number.Complex;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.structures.SparseMonomial;

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

    public Graph(int vertices, List<Set<Integer>> adjacencyList) {
        this.vertexCount = vertices;
        this.adjacencyList = adjacencyList;
    }

    public int getVertexCount() {
        return vertexCount;
    }

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
                var monomials = new ArrayList<Monomial<Complex>>();
                for (var i = 0; i < k; i++) {
                    var exponents = vertexMonomial(k - 1 - i, u);
                    exponents[v - 1] = i; // -1 because containers are 0-indexes
                    monomials.add(new SparseMonomial<>(exponents, Complex.one));
                }

                polynomials.add(new Polynomial<>(monomials, vertexCount, GraphProblemsConstants.ORDERING));
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
