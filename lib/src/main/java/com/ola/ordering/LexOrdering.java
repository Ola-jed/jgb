package com.ola.ordering;

import com.ola.number.Numeric;
import com.ola.structures.Monomial;

/**
 * Implements lexicographic (LEX) ordering for monomials.
 * <p>
 * Indeterminates are compared sequentially by their powers.
 */
public class LexOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private static final byte ORDER_ID = 1;

    @Override
    public int compare(Monomial<T> a, Monomial<T> b) {
        if (a.fieldSize() != b.fieldSize()) {
            throw new IllegalArgumentException("Both monomials should be defined in the same ring.");
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
