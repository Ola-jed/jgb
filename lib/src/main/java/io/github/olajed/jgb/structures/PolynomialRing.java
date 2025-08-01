package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.dsl.generator.PolynomialGenerator;
import io.github.olajed.jgb.dsl.lexer.Lexer;
import io.github.olajed.jgb.dsl.parser.Parser;
import io.github.olajed.jgb.enums.MonomialType;
import io.github.olajed.jgb.number.*;
import io.github.olajed.jgb.ordering.MonomialOrdering;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Represents a polynomial ring with a specific number type and a set of indeterminates.
 * A polynomial ring consists of polynomials where each polynomial is constructed from a
 * coefficient of a specified numeric type and one or more indeterminates, raised to integer exponents.
 *
 * @param numberType     The class type of the number used as the coefficient in the polynomials
 *                       ({@link Real}, {@link Complex}, {@link Rational}).
 *                       This ensures that all coefficients in the polynomials follow the same type.
 * @param indeterminates An array of strings representing the names of the indeterminates
 *                       (e.g., "x", "y", "z") used in the polynomials within the ring.
 */
@SuppressWarnings("unchecked")
public record PolynomialRing(Class<? extends Numeric> numberType, String[] indeterminates) {
    /**
     * Compact constructor for {@code PolynomialRing}.
     *
     * @param numberType the numeric type class used for the ring elements; must implement {@link Numeric}
     * @throws IllegalArgumentException if {@code numberType} does not implement {@link Numeric}
     */
    public PolynomialRing {
        if (!Numeric.class.isAssignableFrom(numberType)) {
            throw new IllegalArgumentException("numberType must be a subclass of Numeric");
        }
    }

    @Override
    public String toString() {
        String numberTypeString;
        if (numberType.equals(Real.class)) {
            numberTypeString = "R";
        } else if (numberType.equals(Complex.class)) {
            numberTypeString = "C";
        } else if (numberType.equals(Rational.class)) {
            numberTypeString = "Q";
        } else {
            numberTypeString = "K";
        }

        return "%s[%s]".formatted(numberTypeString, String.join(", ", indeterminates));
    }

    /**
     * Creates a new {@code PolynomialRing} by contracting (keeping) only the first {@code toKeep} indeterminates.
     *
     * @param toKeep the number of indeterminates to retain; must be between 0 and the current number of indeterminates inclusive
     * @return a new {@code PolynomialRing} with the specified subset of indeterminates
     * @throws IllegalArgumentException if {@code toKeep} is negative or greater than the current number of indeterminates
     */
    public PolynomialRing contract(int toKeep) {
        if (toKeep < 0 || toKeep > indeterminates.length) {
            throw new IllegalArgumentException("Invalid number of indeterminates to keep : " + toKeep);
        }

        var newIndeterminates = new String[toKeep];
        System.arraycopy(indeterminates, 0, newIndeterminates, 0, toKeep);
        return new PolynomialRing(numberType, newIndeterminates);
    }

    /**
     * Creates a new {@code PolynomialRing} by extending the current indeterminates with additional ones.
     *
     * @param extraIndeterminates an array of indeterminate names to add to the ring
     * @return a new {@code PolynomialRing} containing the original and the extra indeterminates
     */
    public PolynomialRing extend(String[] extraIndeterminates) {
        var newIndeterminates = new String[indeterminates.length + extraIndeterminates.length];
        System.arraycopy(indeterminates, 0, newIndeterminates, 0, indeterminates.length);
        System.arraycopy(extraIndeterminates, 0, newIndeterminates, indeterminates.length, extraIndeterminates.length);
        return new PolynomialRing(numberType, newIndeterminates);
    }

    /**
     * Creates a new {@code PolynomialRing} with the same indeterminates but a different numeric type.
     *
     * @param <T> the new numeric type extending {@link Numeric}
     * @param newNumberType the class of the new numeric type; must implement {@link Numeric}
     * @return a new {@code PolynomialRing} instance with the specified numeric type and existing indeterminates
     * @throws IllegalArgumentException if {@code newNumberType} does not implement {@link Numeric}
     */
    public <T extends Numeric> PolynomialRing typeChange(Class<T> newNumberType) {
        if (!Numeric.class.isAssignableFrom(newNumberType)) {
            throw new IllegalArgumentException("newNumberType must be a subclass of Numeric");
        }

        return new PolynomialRing(newNumberType, indeterminates);
    }

    /**
     * Creates a new monomial with the specified coefficient, variable exponents, and monomial representation type.
     *
     * @param <T> the numeric type of the coefficient
     * @param coefficient the coefficient of the monomial
     * @param elements a map from variable names to their exponents in the monomial
     * @param type the monomial representation type (e.g., dense or sparse)
     * @return a new {@link Monomial} instance with the given coefficient and exponents
     */
    public <T extends Numeric> Monomial<T> createMonomial(T coefficient, Map<String, Integer> elements, MonomialType type) {
        if (!numberType.isAssignableFrom(coefficient.getClass())) {
            throw new IllegalArgumentException(
                    "Coefficient must be of type %s, but got %s"
                            .formatted(numberType.getSimpleName(), coefficient.getClass().getSimpleName())
            );
        }

        var indeterminateSet = new HashSet<>(Arrays.asList(indeterminates));
        for (var key : elements.keySet()) {
            if (!indeterminateSet.contains(key)) {
                throw new IllegalArgumentException("Unknown indeterminate: " + key);
            }
        }

        var exponents = new int[indeterminates.length];
        for (var i = 0; i < indeterminates.length; i++) {
            exponents[i] = elements.getOrDefault(indeterminates[i], 0);
        }

        return type == MonomialType.SPARSE ? new SparseMonomial<>(exponents, coefficient) : new DenseMonomial<>(exponents, coefficient);
    }

    /**
     * Creates a zero polynomial with the current ring's number of indeterminates and the specified monomial ordering.
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param ordering the monomial ordering to use for the polynomial
     * @return a zero polynomial with no terms and the specified ordering
     */
    public <T extends Numeric> Polynomial<T> zero(MonomialOrdering<T> ordering) {
        return new Polynomial<>(indeterminates.length, ordering);
    }

    /**
     * Creates a polynomial representing the constant one with the specified monomial ordering and monomial type.
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param ordering the monomial ordering to use for the polynomial
     * @param monomialType the monomial representation type (e.g., dense or sparse)
     * @return a polynomial representing the constant one
     */
    public <T extends Numeric> Polynomial<T> one(MonomialOrdering<T> ordering, MonomialType monomialType) {
        return one(ordering, monomialType, 2);
    }

    /**
     * Creates a polynomial representing the constant one with the specified monomial ordering, monomial type, and modulo (for finite fields).
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param ordering the monomial ordering to use for the polynomial
     * @param monomialType the monomial representation type (e.g., dense or sparse)
     * @param modulo the modulo value used when the numeric type is a finite field ({@link GaloisFieldElement})
     * @return a polynomial representing the constant one
     * @throws IllegalArgumentException if the numeric type is unsupported
     */
    public <T extends Numeric> Polynomial<T> one(MonomialOrdering<T> ordering, MonomialType monomialType, int modulo) {
        var exponents = new int[indeterminates.length];
        T coefficient;
        if (numberType == Complex.class) {
            coefficient = (T) new Complex(1, 0);
        } else if (numberType == Rational.class) {
            coefficient = (T) new Rational(1);
        } else if (numberType == Real.class) {
            coefficient = (T) new Real(1.0);
        } else if (numberType == GaloisFieldElement.class) {
            coefficient = (T) new GaloisFieldElement(1, modulo);
        } else {
            throw new IllegalArgumentException("Unsupported number type : " + numberType.getName());
        }

        if (monomialType == MonomialType.DENSE) {
            return new Polynomial<>(new DenseMonomial<>(exponents, coefficient), ordering);
        } else {
            return new Polynomial<>(new SparseMonomial<>(exponents, coefficient), ordering);
        }
    }

    /**
     * Creates a polynomial from the given list of monomials with the specified monomial ordering.
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param monomials the list of monomials composing the polynomial
     * @param ordering the monomial ordering to use for the polynomial
     * @return a new polynomial composed of the provided monomials
     */
    public <T extends Numeric> Polynomial<T> createPolynomial(List<Monomial<T>> monomials, MonomialOrdering<T> ordering) {
        return new Polynomial<>(monomials, indeterminates.length, ordering);
    }

    /**
     * Formats a given monomial into its string representation based on the polynomial ring’s variables.
     *
     * @param <T> the numeric type of the monomial coefficient
     * @param monomial the monomial to format
     * @return a string representation of the monomial
     */
    public <T extends Numeric> String format(Monomial<T> monomial) {
        var sb = new StringBuilder();
        var coefficient = monomial.coefficient().toString();

        if (coefficient.endsWith(".0")) {
            coefficient = coefficient.substring(0, coefficient.length() - 2);
        }

        var hasIndeterminates = false;
        for (int i = 0; i < indeterminates.length; i++) {
            if (monomial.getExponent(i) != 0) {
                hasIndeterminates = true;
                break;
            }
        }

        boolean showCoefficient = !coefficient.equals("1") && !coefficient.equals("-1") || !hasIndeterminates;
        if (showCoefficient) {
            sb.append(coefficient);
        } else if (coefficient.equals("-1")) {
            sb.append("- ");
        }

        if (hasIndeterminates && showCoefficient) {
            sb.append("*");
        }

        boolean firstVar = true;
        for (int i = 0; i < indeterminates.length; i++) {
            int exponent = monomial.getExponent(i);
            if (exponent != 0) {
                if (!firstVar) {
                    sb.append("*");
                }
                sb.append(indeterminates[i]);
                if (exponent != 1) {
                    sb.append("^").append(exponent);
                }
                firstVar = false;
            }
        }

        return sb.toString();
    }

    /**
     * Formats a given polynomial into its string representation based on the polynomial ring’s variables.
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param polynomial the polynomial to format
     * @return a string representation of the polynomial
     */
    public <T extends Numeric> String format(Polynomial<T> polynomial) {
        if (polynomial.monomials().isEmpty()) {
            return "0";
        }

        var sb = new StringBuilder();
        var first = true;
        for (var monomial : polynomial.monomials()) {
            var formattedMonomial = format(monomial);

            if (first) {
                sb.append(formattedMonomial);
                first = false;
            } else {
                if (formattedMonomial.startsWith("-")) {
                    sb.append(" - ");
                    sb.append(formattedMonomial.substring(1)); // skip the minus
                } else {
                    sb.append(" + ");
                    sb.append(formattedMonomial);
                }
            }
        }

        return sb.toString();
    }

    /**
     * Parses a polynomial from its string representation using a default modulo value of 2.
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param polynomial the string representation of the polynomial to parse
     * @return the parsed polynomial
     */
    public <T extends Numeric> Polynomial<T> parse(String polynomial) {
        return parse(polynomial, 2);
    }

    /**
     * Parses a polynomial from its string representation within the current polynomial ring context,
     * using a specified modulo for finite fields if applicable.
     *
     * <p>This method constructs a parsing context by declaring variables and specifying the field
     * based on the number type of this polynomial ring. Supported fields include Galois fields
     * (with the given modulo), rationals, reals, and complex numbers.</p>
     *
     * <p>The polynomial string is then parsed using a lexer and parser, and converted into a
     * {@link Polynomial} object using a {@link PolynomialGenerator}.</p>
     *
     * @param <T> the numeric type of the polynomial coefficients
     * @param polynomial the string representation of the polynomial to parse
     * @param modulo the modulo value to use for Galois fields; ignored for other number types
     * @return the parsed polynomial of type {@code T}
     * @throws IllegalArgumentException if the number type is unsupported or if no polynomial
     *                                  could be parsed from the input
     */
    public <T extends Numeric> Polynomial<T> parse(String polynomial, int modulo) {
        var context = new StringBuilder()
                .append("@variables(")
                .append(String.join(",", indeterminates))
                .append(")\n");
        if (GaloisFieldElement.class.isAssignableFrom(numberType)) {
            context.append("@field(GF[").append(modulo).append("])\n");
        } else if (Rational.class.isAssignableFrom(numberType)) {
            context.append("@field(Q)\n");
        } else if (Real.class.isAssignableFrom(numberType)) {
            context.append("@field(R)\n");
        } else if (Complex.class.isAssignableFrom(numberType)) {
            context.append("@field(C)\n");
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + numberType.getName());
        }

        context.append(polynomial);

        var parsed = new Parser(new Lexer().scan(context.toString())).parse();
        var polynomials = new PolynomialGenerator().generate(parsed);
        if (polynomials.isEmpty()) {
            throw new IllegalArgumentException("No polynomials could be parsed from input.");
        }

        @SuppressWarnings("unchecked")
        var result = (Polynomial<T>) polynomials.getFirst();
        return result;
    }
}
