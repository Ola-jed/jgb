package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.structures.Polynomial;

/**
 * Utility class providing common functions for polynomials.
 * <p>
 * This class contains static methods that operate on polynomials,
 * such as computing S-polynomials.
 * </p>
 */
public final class PolynomialFunctions {
    private PolynomialFunctions() {
    }

    /**
     * Computes the S-polynomial of two polynomials.
     * <p>
     * Defined as:
     * S(f,g) = (lcm(LT(f),LT(g))/LT(f)) * f - (lcm(LT(f),LT(g))/LT(g)) * g
     * <p>
     * where LT is the leading term and lcm is the least common multiple.
     *
     * @param f The first polynomial
     * @param g The second polynomial
     * @return The S-polynomial of f and g
     * @throws IllegalArgumentException if the polynomials use different monomial orderings
     */
    public static <T extends Numeric> Polynomial<T> sPolynomial(Polynomial<T> f, Polynomial<T> g) {
        if (f.fieldSize() != g.fieldSize()) {
            throw new IllegalArgumentException("Both polynomials should be defined in the same ring.");
        }

        if (f.ordering().orderId() != g.ordering().orderId()) {
            throw new IllegalArgumentException("Both polynomials should be defined using the same ordering.");
        }

        var lcm = MonomialFunctions.lcm(f.leadingMonomial(), g.leadingMonomial());
        var firstTerm = f.multiply(lcm.divide(f.leadingTerm()));
        var secondTerm = g.multiply(lcm.divide(g.leadingTerm()));
        return firstTerm.subtract(secondTerm);
    }
}
