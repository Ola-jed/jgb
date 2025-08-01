package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.Monomial;

import java.util.Comparator;

/**
 * A functional interface that defines the ordering for comparing two monomials.
 * This interface extends {@link Comparator}, which allows it to be used wherever
 * a {@link Comparator} is required for sorting monomials. The ordering logic for
 * monomials can be customized by implementing this interface.
 *
 * @param <T> The type of the coefficient, which must extend {@link Numeric}.
 * @see Comparator
 * @see Monomial
 */
public interface MonomialOrdering<T extends Numeric> extends Comparator<Monomial<T>> {
    @Override
    int compare(Monomial<T> a, Monomial<T> b);

    /**
     * Returns a unique identifier for this monomial ordering.
     *
     * <p>This method is implemented by subclasses to efficiently determine
     * whether two ordering instances are the same. All instances of a specific
     * subclass must return the same {@code byte} value.</p>
     *
     * @return a unique identifier for the ordering
     */
    byte orderId();
}
