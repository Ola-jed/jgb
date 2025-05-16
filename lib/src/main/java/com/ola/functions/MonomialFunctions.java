package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.SparseMonomial;

@SuppressWarnings("unchecked")
public final class MonomialFunctions {
    private MonomialFunctions() {
    }

    public static <T extends Numeric> Monomial<T> one(Monomial<T> model) {
        if (model instanceof SparseMonomial<T>) {
            return new SparseMonomial<>(new int[model.fieldSize()], (T) model.coefficient().one());
        } else {
            return new DenseMonomial<>(new int[model.fieldSize()], (T) model.coefficient().one());
        }
    }

    public static <T extends Numeric> Monomial<T> lcm(Monomial<T> x, Monomial<T> y) {
        // Both sparse, we can optimize
        if (x instanceof SparseMonomial<T> xAsSparse && y instanceof SparseMonomial<T> yAsSparse) {
            var ptr1 = 0;
            var ptr2 = 0;
            var exponents = new int[x.fieldSize()];
            for (var i = 0; i < x.fieldSize(); i++) {
                var currentExponent = 0;
                if (xAsSparse.bitset().get(i)) {
                    currentExponent = xAsSparse.getExponentAtPosition(ptr1);
                    ptr1++;
                }

                if (yAsSparse.bitset().get(i)) {
                    currentExponent = Math.max(currentExponent, yAsSparse.getExponentAtPosition(ptr2));
                    ptr2++;
                }

                exponents[i] = currentExponent;
            }

            return new SparseMonomial<>(exponents, (T) x.coefficient().one());
        }

        // Otherwise, just create a DenseMonomial
        var exponents = new int[x.fieldSize()];
        for (var i = 0; i < x.fieldSize(); i++) {
            exponents[i] = Math.max(x.getExponent(i), y.getExponent(i));
        }

        return new DenseMonomial<>(exponents, (T) x.coefficient().one());
    }
}
