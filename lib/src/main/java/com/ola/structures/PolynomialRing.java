package com.ola.structures;

import com.ola.dsl.generator.PolynomialGenerator;
import com.ola.dsl.lexer.Lexer;
import com.ola.dsl.parser.Parser;
import com.ola.enums.MonomialType;
import com.ola.number.*;
import com.ola.ordering.MonomialOrdering;

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
public record PolynomialRing(Class<?> numberType, String[] indeterminates) {
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

    public <T extends Numeric> Polynomial<T> createPolynomial(List<Monomial<T>> monomials, MonomialOrdering<T> ordering) {
        return new Polynomial<>(monomials, indeterminates.length, ordering);
    }

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
            sb.append("-");
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
                    sb.append(" ");
                } else {
                    sb.append(" + ");
                }
                sb.append(formattedMonomial);
            }
        }

        return sb.toString();
    }

    public <T extends Numeric> Polynomial<T> parse(String polynomial) {
        return parse(polynomial, 2);
    }

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
