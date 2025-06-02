package com.ola.graphs;

import com.ola.number.Complex;
import com.ola.ordering.GrevlexOrdering;
import com.ola.ordering.MonomialOrdering;

public final class GraphProblemsConstants {
    private GraphProblemsConstants() {
    }

    public static final MonomialOrdering<Complex> ORDERING = new GrevlexOrdering<>();
    public static final Complex ONE = Complex.one;
    public static final Complex ZERO = Complex.zero;
    public static final Complex MINUS_ONE = new Complex(-1, 0);
}
