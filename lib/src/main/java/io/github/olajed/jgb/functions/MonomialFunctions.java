package io.github.olajed.jgb.functions;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import io.github.olajed.jgb.structures.SparseMonomial;

/**
 * Utility class providing common functions on monomials.
 * <p>
 * This class offers static methods to create special monomials (e.g., the multiplicative identity)
 * and to compute operations like the least common multiple (LCM) of monomials.
 * </p>
 */
@SuppressWarnings("unchecked")
public final class MonomialFunctions {
    private MonomialFunctions() {
    }

    /**
     * Creates the multiplicative identity monomial ("one") matching the type and field size of the given monomial.
     * <p>
     * The returned monomial has all exponents zero and a coefficient of one.
     *
     * @param model the monomial whose type and field size to match
     * @param <T>   the numeric type of the monomial coefficients
     * @return a monomial representing the multiplicative identity
     */
    public static <T extends Numeric> Monomial<T> one(Monomial<T> model) {
        if (model instanceof SparseMonomial<T>) {
            return new SparseMonomial<>(new int[model.fieldSize()], (T) model.coefficient().one());
        } else {
            return new DenseMonomial<>(new int[model.fieldSize()], (T) model.coefficient().one());
        }
    }

    /**
     * Computes the least common multiple (LCM) of two monomials.
     * <p>
     * The LCM is a monomial whose exponent for each variable is the maximum exponent
     * found in either of the input monomials.
     *
     * @param x   the first monomial
     * @param y   the second monomial
     * @param <T> the numeric type of the monomial coefficients
     * @return the LCM of the two monomials
     */
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
