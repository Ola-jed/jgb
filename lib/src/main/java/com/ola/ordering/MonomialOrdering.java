package com.ola.ordering;

import com.ola.number.Numeric;
import com.ola.structures.Monomial;

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

    // Method implemented in subclasses to provide an efficient way to check if two orderings are the same
    // All instances of a subtype should return the same value
    byte orderId();
}
