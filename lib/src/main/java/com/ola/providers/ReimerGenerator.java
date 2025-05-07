package com.ola.providers;


import com.ola.number.GaloisFieldElement;
import com.ola.ordering.GrlexOrdering;
import com.ola.ordering.MonomialOrdering;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.structures.SparseMonomial;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the Reimer benchmark polynomial system, commonly used in
 * computer algebra and polynomial system solving for performance evaluation.
 * <p>
 * Each polynomial is of the form:
 * <pre>
 *   -1 + ∑_{i=1}^{n} 2 * -1^{i+1} * x_i^k,  where k \in 2, ..., n + 1
 * </pre>
 * </p>
 *
 * <p>Usage :</p>
 * <pre>{@code
 * List<Polynomial<GaloisFieldElement>> system = ReimerGenerator.get(5);
 * }</pre>
 *
 * <p>All methods throw {@link IllegalArgumentException} if {@code n < 3}.</p>
 */
public final class ReimerGenerator {
    private ReimerGenerator() {
    }

    public static List<Polynomial<GaloisFieldElement>> get(int n) {
        return get(n, new GrlexOrdering<>(), true);
    }

    public static List<Polynomial<GaloisFieldElement>> get(int n, MonomialOrdering<GaloisFieldElement> ordering) {
        return get(n, ordering, true);
    }

    public static List<Polynomial<GaloisFieldElement>> get(int n, boolean dense) {
        return get(n, new GrlexOrdering<>(), dense);
    }

    public static List<Polynomial<GaloisFieldElement>> get(int n, MonomialOrdering<GaloisFieldElement> ordering, boolean dense) {
        if (n < 3) {
            throw new IllegalArgumentException("n must be at least 3");
        }

        var polynomials = new ArrayList<Polynomial<GaloisFieldElement>>(n);
        for (var k = 2; k <= n + 1; k++) {
            var monomials = new ArrayList<Monomial<GaloisFieldElement>>(n + 1);
            for (var i = 0; i < n; i++) {
                var coefficient = new GaloisFieldElement(i % 2 == 0 ? 2 : -2, 5);
                var exponents = new int[n];
                exponents[i] = k;

                if (dense) {
                    monomials.add(new DenseMonomial<>(exponents, coefficient));
                } else {
                    monomials.add(new SparseMonomial<>(exponents, coefficient));
                }
            }

            if (dense) {
                monomials.add(new DenseMonomial<>(new int[n], new GaloisFieldElement(-1, 5)));
            } else {
                monomials.add(new SparseMonomial<>(new int[n], new GaloisFieldElement(-1, 5)));
            }

            polynomials.add(new Polynomial<>(monomials, n, ordering));
        }

        return polynomials;
    }
}
