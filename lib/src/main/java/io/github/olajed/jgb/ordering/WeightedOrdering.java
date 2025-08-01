package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.Monomial;

/**
 * Implements weighted monomial ordering.
 * <p>
 * A weight vector is assigned to the variables, and monomials are
 * first compared based on their weighted degrees (dot product with the weight vector).
 * If two monomials have the same weighted degree, a tie-breaking ordering is applied.
 *
 * @param <T> the numeric type of the monomial coefficients
 */
public class WeightedOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private final int[] weights;
    private final MonomialOrdering<T> tieBreaker;
    private static final byte ORDER_ID = 4;

    /**
     * Constructs a WeightedOrdering with the specified weights and a default tie-breaker using lexicographic ordering.
     *
     * @param weights an array of integer weights for the variables, used to compute weighted degree
     */
    public WeightedOrdering(int[] weights) {
        this.weights = weights;
        this.tieBreaker = new LexOrdering<>();
    }

    /**
     * Constructs a WeightedOrdering with the specified weights and a custom tie-breaker ordering.
     *
     * @param weights    an array of integer weights for the variables, used to compute weighted degree
     * @param tieBreaker a MonomialOrdering to use as a tie-breaker when weighted degrees are equal
     */
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
