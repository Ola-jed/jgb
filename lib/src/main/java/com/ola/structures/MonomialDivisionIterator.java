package com.ola.structures;

import com.ola.number.Numeric;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over the divisions of a monomial by all possible monomials
 * with exponents less than or equal to those of the given monomial.
 * <p>
 * This iterator produces monomials that divide the original monomial.
 * </p>
 *
 * @param <T> the field of the coefficients
 */
public final class MonomialDivisionIterator<T extends Numeric> implements Iterator<Monomial<T>> {
    private final Monomial<T> internal;
    private final int[] current;
    private boolean hasNext = true;

    public MonomialDivisionIterator(Monomial<T> internal) {
        this.internal = internal;
        this.current = new int[internal.fieldSize()];
        for (var i = 0; i < internal.fieldSize(); i++) {
            current[i] = internal.getExponent(i);
        }

        findNextDivisor();
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Monomial<T> next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }

        var result = internal instanceof DenseMonomial<T>
                ? new DenseMonomial<>(current.clone(), internal.coefficient())
                : new SparseMonomial<>(current.clone(), internal.coefficient());

        findNextDivisor();
        return result;
    }

    private void findNextDivisor() {
        while (true) {
            var decremented = false;
            for (var i = internal.fieldSize() - 1; i >= 0; i--) {
                if (current[i] > 0) {
                    current[i]--;
                    for (var j = i + 1; j < internal.fieldSize(); j++) {
                        current[j] = internal.getExponent(j);
                    }

                    decremented = true;
                    break;
                }
            }

            if (!decremented) {
                hasNext = false;
                return;
            }

            if (!isSameAsInternal() && !isAllZeros()) {
                return;
            }
        }
    }

    private boolean isAllZeros() {
        for (var exponent : current) {
            if (exponent != 0) {
                return false;
            }
        }

        return true;
    }

    private boolean isSameAsInternal() {
        for (var exponentIndex = 0; exponentIndex < current.length; exponentIndex++) {
            if (current[exponentIndex] != internal.getExponent(exponentIndex)) {
                return false;
            }
        }

        return true;
    }
}
