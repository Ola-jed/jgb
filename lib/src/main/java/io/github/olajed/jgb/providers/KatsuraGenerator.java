package io.github.olajed.jgb.providers;

import io.github.olajed.jgb.number.GaloisFieldElement;
import io.github.olajed.jgb.ordering.GrlexOrdering;
import io.github.olajed.jgb.ordering.MonomialOrdering;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import io.github.olajed.jgb.structures.Polynomial;
import io.github.olajed.jgb.structures.SparseMonomial;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the Katsura benchmark polynomial system, commonly used in
 * computer algebra and polynomial system solving for performance evaluation.
 * <p>
 * The Katsura system models quantum spin chain correlations with n+1 variables
 * {u_0, u_1, ..., u_n} and equations of the form:
 * <pre>
 *   u_0 - 1 = 0  (normalization)
 *   u_k - ∑_{i+j=k} c_{ij} * u_i * u_j = 0  (k = 1 to n)
 * </pre>
 * where c_{ij} = 1 if i = j, otherwise c_{ij} = 2.
 *
 * <p>Usage:</p>
 * <pre>{@code
 * List<Polynomial<GaloisFieldElement>> system = KatsuraGenerator.get(5);
 * }</pre>
 *
 * <p>All methods throw {@link IllegalArgumentException} if {@code n < 1}.</p>
 */
public final class KatsuraGenerator {
    private KatsuraGenerator() {
    }

    /**
     * Generates the Katsura system with {@code n} variables using the default
     * graded lexicographic ({@link GrlexOrdering}) monomial ordering and dense representation.
     *
     * @param n the number of variables; must be at least 1
     * @return a list of {@link Polynomial} instances over a Galois field
     * @throws IllegalArgumentException if {@code n} is less than 1
     */
    public static List<Polynomial<GaloisFieldElement>> get(int n) {
        return get(n, new GrlexOrdering<>(), true);
    }

    /**
     * Generates the Katsura system with {@code n} variables using the given monomial ordering
     * and dense representation.
     *
     * @param n the number of variables; must be at least 1
     * @param ordering the monomial ordering to use
     * @return a list of {@link Polynomial} instances over a Galois field
     * @throws IllegalArgumentException if {@code n} is less than 1
     */
    public static List<Polynomial<GaloisFieldElement>> get(int n, MonomialOrdering<GaloisFieldElement> ordering) {
        return get(n, ordering, true);
    }

    /**
     * Generates the Katsura system with {@code n} variables using the default
     * graded lexicographic ordering and specified monomial representation.
     *
     * @param n the number of variables; must be at least 1
     * @param dense {@code true} for dense monomial representation, {@code false} for sparse
     * @return a list of {@link Polynomial} instances over a Galois field
     * @throws IllegalArgumentException if {@code n} is less than 1
     */
    public static List<Polynomial<GaloisFieldElement>> get(int n, boolean dense) {
        return get(n, new GrlexOrdering<>(), dense);
    }

    /**
     * Generates the Katsura system with {@code n} variables using the specified
     * monomial ordering and monomial representation.
     *
     * @param n the number of variables; must be at least 1
     * @param ordering the monomial ordering to use
     * @param dense {@code true} for dense monomial representation, {@code false} for sparse
     * @return a list of {@link Polynomial} instances over a Galois field
     * @throws IllegalArgumentException if {@code n} is less than 1
     */
    public static List<Polynomial<GaloisFieldElement>> get(int n, MonomialOrdering<GaloisFieldElement> ordering, boolean dense) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be at least 1");
        }

        var polynomials = new ArrayList<Polynomial<GaloisFieldElement>>(n + 1);
        var firstPolynomialMonomials = new ArrayList<Monomial<GaloisFieldElement>>(n + 2);
        if (dense) {
            firstPolynomialMonomials.add(new DenseMonomial<>(new int[n + 1], new GaloisFieldElement(4, 5)));
        } else {
            firstPolynomialMonomials.add(new SparseMonomial<>(new int[n + 1], new GaloisFieldElement(4, 5)));
        }

        for (var i = 0; i <= n; i++) {
            var exponents = variableMonomial(n + 1, i);
            var coefficient = new GaloisFieldElement(i == 0 ? 1 : 2, 5);

            if (dense) {
                firstPolynomialMonomials.add(new DenseMonomial<>(exponents, coefficient));
            } else {
                firstPolynomialMonomials.add(new SparseMonomial<>(exponents, coefficient));
            }
        }

        polynomials.add(new Polynomial<>(firstPolynomialMonomials, n + 1, ordering));
        for (var m = 0; m < n; m++) {
            var polynomialMonomials = new ArrayList<Monomial<GaloisFieldElement>>(n + 1);
            var xM = variableMonomial(n + 1, m);
            if (dense) {
                polynomialMonomials.add(new DenseMonomial<>(xM, new GaloisFieldElement(4, 5)));
            } else {
                polynomialMonomials.add(new SparseMonomial<>(xM, new GaloisFieldElement(4, 5)));
            }

            for (var l = -n; l <= n; l++) {
                if (m - l >= n + 1) {
                    continue;
                }

                var temp = variableMonomial(n + 1, Math.abs(l));
                temp[Math.abs(m - l)]++;

                if (dense) {
                    polynomialMonomials.add(new DenseMonomial<>(temp, new GaloisFieldElement(1, 5)));
                } else {
                    polynomialMonomials.add(new SparseMonomial<>(temp, new GaloisFieldElement(1, 5)));
                }
            }

            polynomials.add(new Polynomial<>(polynomialMonomials, n + 1, ordering));
        }


        return polynomials;

    }

    private static int[] variableMonomial(int variableCount, int variable) {
        var exponents = new int[variableCount];
        exponents[variable] = 1;
        return exponents;
    }
}
