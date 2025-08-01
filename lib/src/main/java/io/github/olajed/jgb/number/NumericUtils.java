package io.github.olajed.jgb.number;

import io.github.olajed.jgb.enums.NumericType;

/**
 * Utility class for performing type-safe operations between various {@link Numeric} types.
 *
 * <p>This class provides helper methods for coercing numeric values between types such as
 * {@link Rational}, {@link Real}, {@link Complex}, and finite field elements (e.g., {@link GaloisFieldElement}).
 */
public final class NumericUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private NumericUtils() {
    }

    /**
     * Attempts to convert a {@link Numeric} value to a specified {@link NumericType},
     * optionally using a modulo if required (e.g., for finite fields).
     *
     * <p>This method may perform exact or lossy conversions depending on the types involved.
     * If the conversion is not possible, it may throw a {@link NumericException}.</p>
     *
     * @param from   The source numeric value to convert.
     * @param to     The target numeric type to convert to.
     * @param modulo The modulus to use for modular arithmetic (used only for Galois fields; ignored otherwise).
     * @return A new {@link Numeric} instance converted to the target type.
     * @throws NumericException if the conversion is invalid or lossy in a disallowed way.
     */
    public static Numeric tryAssign(Numeric from, NumericType to, int modulo) {
        if (to == NumericType.Complex) {
            return switch (from) {
                case GaloisFieldElement x -> new Complex(x.get(), 0);
                case Rational x -> new Complex(x.get(), 0);
                case Real x -> new Complex(x.get(), 0);
                default -> from;
            };
        } else if (to == NumericType.Real) {
            return switch (from) {
                case GaloisFieldElement x -> new Real(x.get());
                case Rational x -> new Real(x.get());
                case Real ignored -> from;
                case Complex x -> {
                    if (x.imaginary() == 0) {
                        yield new Real(x.real());
                    } else {
                        throw new NumericException("Cannot convert complex number with non-zero imaginary part to real");
                    }
                }
            };
        } else if (to == NumericType.Rational) {
            return switch (from) {
                case GaloisFieldElement x -> new Rational(x.get());
                case Rational ignored -> from;
                case Real x -> {
                    if (x.get() % 1 == 0) {
                        yield new Rational((int) x.get());
                    } else {
                        throw new NumericException("Cannot convert Real number with decimal part to rational");
                    }
                }
                case Complex x -> {
                    if (x.imaginary() == 0 && x.real() % 1 == 0) {
                        yield new Rational((int) x.real());
                    } else {
                        throw new NumericException("Cannot convert this Complex number to rational");
                    }
                }
            };
        } else {
            return switch (from) {
                case GaloisFieldElement x -> new GaloisFieldElement(x.get(), x.modulo());
                case Rational x -> {
                    if (x.get() % 1 == 0) {
                        yield new GaloisFieldElement((int) x.get(), modulo);
                    } else {
                        throw new NumericException("Cannot convert Rational number with non - 1 denominator to integer");
                    }
                }
                case Real x -> {
                    if (x.get() % 1 == 0) {
                        yield new GaloisFieldElement((int) x.get(), modulo);
                    } else {
                        throw new NumericException("Cannot convert Real number with decimal part to integer");
                    }
                }
                case Complex x -> {
                    if (x.imaginary() == 0 && x.real() % 1 == 0) {
                        yield new GaloisFieldElement((int) x.real(), modulo);
                    } else {
                        throw new NumericException("Cannot convert this Complex number to integer");
                    }
                }
            };
        }
    }
}
