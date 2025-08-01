package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.Monomial;

/**
 * Implements graded reverse lexicographic (GREVLEX) ordering for monomials.
 * <p>
 * GREVLEX ordering first compares monomials by their total degree.
 * When two monomials have the same total degree, they are compared using reverse lexicographic ordering
 *
 * @param <T> the numeric type of the monomial coefficients
 */
public class GrevlexOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private static final byte ORDER_ID = 3;

    /**
     * Default constructor for GrevlexOrdering.
     * Initializes a graded reverse lexicographic monomial ordering instance.
     */
    public GrevlexOrdering() {
    }

    @Override
    public int compare(Monomial<T> a, Monomial<T> b) {
        if (a.fieldSize() != b.fieldSize()) {
            throw new IllegalArgumentException("Both monomials should be defined in the same ring.");
        }

        var degreeDifference = a.degree() - b.degree();
        if (degreeDifference != 0) {
            return Integer.compare(degreeDifference, 0);
        }

        for (var i = 0; i < a.fieldSize(); i++) {
            var difference = b.getExponent(i) - a.getExponent(i);
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
