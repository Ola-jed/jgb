package com.ola.structures;

import com.ola.number.Numeric;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public final class MonomialDivisionIterator<T extends Numeric> implements Iterator<Monomial<T>> {
    private final T one;
    private final Monomial<T> internal;
    private boolean hasNext = true;
    private final int[] current;

    public MonomialDivisionIterator(Monomial<T> internal) {
        this.internal = internal;
        this.one = (T) internal.coefficient().one();
        this.current = new int[internal.fieldSize()];
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

        if (internal instanceof DenseMonomial<T>) {
            var result = new DenseMonomial<>(current.clone(), one);
            advance();
            return result;
        }

        var result = new SparseMonomial<>(current.clone(), one);
        advance();
        return result;
    }

    private void advance() {
        for (var i = internal.fieldSize() - 1; i >= 0; i--) {
            if (current[i] < internal.getExponent(i)) {
                current[i]++;

                for (var j = i + 1; j < internal.fieldSize(); j++) {
                    current[j] = 0;
                }

                return;
            }
        }

        hasNext = false;
    }
}
