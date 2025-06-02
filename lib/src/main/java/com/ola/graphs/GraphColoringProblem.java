package com.ola.graphs;

import com.ola.functions.algorithms.M4GBAlgorithm;
import com.ola.structures.Polynomial;
import com.ola.structures.SparseMonomial;

import java.io.IOException;

public class GraphColoringProblem {
    private final Graph graph;

    public GraphColoringProblem(String fileName) throws IOException {
        var parser = new DimacsParser(fileName);
        graph = parser.parse();
    }

    /**
     * Determines whether the graph can be properly colored using at most k colors.
     *
     * <p>This method uses and Gröbner bases to solve the graph k-coloring problem.
     * It constructs the k-coloring ideal generators
     * for the graph, computes their Gröbner basis using the M4GB algorithm, and then
     * checks if the constant polynomial 1 reduces to 0 modulo the ideal.</p>
     *
     * @param k the maximum number of colors to use (must be positive)
     * @return {@code true} if the graph can be k-colored, {@code false} otherwise
     * @throws IllegalArgumentException if k ≤ 0
     */
    public boolean isKColorable(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Number of colors must be positive, got: " + k);
        }

        if (k >= graph.getVertexCount()) {
            return true;
        }

        var kColoringPolynomials = graph.kColoringIdealGenerators(k);
        var basis = M4GBAlgorithm.compute(kColoringPolynomials);
        var oneMonomial = new SparseMonomial<>(new int[graph.getVertexCount()], GraphProblemsConstants.ONE);
        var one = new Polynomial<>(oneMonomial, GraphProblemsConstants.ORDERING);
        var reductionResult = one.reduce(basis);

        return !reductionResult.monomials().isEmpty()
                && (reductionResult.monomials().size() != 1
                || !reductionResult.monomials().getFirst().coefficient().equals(GraphProblemsConstants.ZERO));
    }
}
