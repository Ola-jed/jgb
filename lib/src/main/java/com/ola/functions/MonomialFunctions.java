package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.ordering.MonomialOrdering;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.SparseMonomial;

import java.util.List;

@SuppressWarnings("unchecked")
public final class MonomialFunctions {
    private MonomialFunctions() {
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

    public static <T extends Numeric> int binarySearch(
            List<Monomial<T>> haystack,
            Monomial<T> needle,
            MonomialOrdering<T> ordering
    ) {
        var low = 0;
        var high = haystack.size() - 1;
        while (low <= high) {
            var mid = low + (high - low) / 2;
            var comparison = ordering.compare(needle, haystack.get(mid));

            if (comparison == 0) {
                return mid;
            } else if (comparison > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    public static <T extends Numeric> boolean containsMonomial(
            List<Monomial<T>> haystack,
            Monomial<T> needle,
            MonomialOrdering<T> ordering
    ) {
        int low = 0;
        int high = haystack.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = ordering.compare(needle, haystack.get(mid));

            if (comparison == 0) {
                return true;
            } else if (comparison > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return false;
    }

}
