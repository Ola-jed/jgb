package com.ola.graphs;

import com.ola.number.Complex;
import com.ola.ordering.GrevlexOrdering;
import com.ola.ordering.MonomialOrdering;

/**
 * Constants used in graph-related polynomial computations.
 *
 * <p>Includes a default monomial ordering and commonly used complex constants.</p>
 */
public final class GraphProblemsConstants {
    private GraphProblemsConstants() {
        // Prevent instantiation
    }

    /**
     * Default monomial ordering (graded reverse lexicographic).
     */
    public static final MonomialOrdering<Complex> ORDERING = new GrevlexOrdering<>();

    /**
     * Complex constant representing 1 + 0i.
     */
    public static final Complex ONE = Complex.one;

    /**
     * Complex constant representing 0 + 0i.
     */
    public static final Complex ZERO = Complex.zero;

    /**
     * Complex constant representing -1 + 0i.
     */
    public static final Complex MINUS_ONE = new Complex(-1, 0);
}
