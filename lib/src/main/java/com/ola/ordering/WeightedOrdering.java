package com.ola.ordering;

import com.ola.number.Numeric;
import com.ola.structures.Monomial;

/**
 * Implements weighted monomial ordering.
 * <p>
 * A weight vector is assigned to the variables, and monomials are
 * first compared based on their weighted degrees (dot product with the weight vector).
 * If two monomials have the same weighted degree, a tie-breaking ordering is applied.
 */
public class WeightedOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private final int[] weights;
    private final MonomialOrdering<T> tieBreaker;
    private static final byte ORDER_ID = 4;

    public WeightedOrdering(int[] weights) {
        this.weights = weights;
        this.tieBreaker = new LexOrdering<>();
    }

    public WeightedOrdering(int[] weights, MonomialOrdering<T> tieBreaker) {
        this.weights = weights;
        this.tieBreaker = tieBreaker;
    }

    @Override
    public int compare(Monomial<T> a, Monomial<T> b) {
        if (a.fieldSize() != b.fieldSize()) {
            throw new IllegalArgumentException("Both monomials should be defined in the same ring.");
        }

        if (a.fieldSize() != weights.length) {
            throw new IllegalArgumentException("Ordering weights are not suited for this ring.");
        }

        var aAggregate = a.accumulate(weights, (x, y) -> x * y, 0, Integer::sum);
        var bAggregate = b.accumulate(weights, (x, y) -> x * y, 0, Integer::sum);
        var difference = aAggregate - bAggregate;
        if (difference != 0) {
            return Integer.compare(difference, 0);
        }

        return tieBreaker.compare(a, b);
    }

    @Override
    public byte orderId() {
        return ORDER_ID;
    }
}
