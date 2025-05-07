package com.ola.ordering;

import com.ola.number.Numeric;
import com.ola.structures.Monomial;

/**
 * Implements graded lexicographic (GRLEX) ordering for monomials.
 * <p>
 * GRLEX ordering first compares monomials by their total degree.
 * When two monomials have the same total degree, they are compared using lexicographic ordering.
 */
public class GrlexOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private static final byte ORDER_ID = 2;

    @Override
    public int compare(Monomial<T> a, Monomial<T> b) {
        if (a.fieldSize() != b.fieldSize()) {
            throw new IllegalArgumentException("Both monomials should be defined in the same ring");
        }

        var degreeDifference = a.degree() - b.degree();
        if (degreeDifference != 0) {
            return Integer.compare(degreeDifference, 0);
        }

        for (var i = 0; i < a.fieldSize(); i++) {
            var difference = a.getExponent(i) - b.getExponent(i);
            if (difference != 0) {
                return Integer.compare(difference, 0);
            }
        }

        return 0;
    }

    @Override
    public byte orderId() {
        return ORDER_ID;
    }
}
