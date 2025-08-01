package io.github.olajed.jgb.graphs;

import io.github.olajed.jgb.functions.algorithms.M4GBAlgorithm;
import io.github.olajed.jgb.structures.Polynomial;
import io.github.olajed.jgb.structures.SparseMonomial;

import java.io.IOException;

/**
 * Represents a graph coloring problem defined by an input graph.
 *
 * <p>The graph is loaded from a file in DIMACS format.</p>
 */
public class GraphColoringProblem {
    private final Graph graph;

    /**
     * Constructs a {@code GraphColoringProblem} by loading a graph from a DIMACS-formatted file.
     *
     * @param fileName the path to the DIMACS graph file
     * @throws IOException if an error occurs while reading or parsing the file
     */
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
