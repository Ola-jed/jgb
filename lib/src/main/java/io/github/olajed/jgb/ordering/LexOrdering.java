package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.Monomial;

/**
 * Implements lexicographic (LEX) ordering for monomials.
 * <p>
 * Indeterminates are compared sequentially by their powers.
 * @param <T> the numeric type of the monomial coefficients
 */
public class LexOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private static final byte ORDER_ID = 1;

    /**
     * Default constructor for LexOrdering.
     * Initializes a lexicographic monomial ordering instance.
     */
    public LexOrdering() {
    }

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
