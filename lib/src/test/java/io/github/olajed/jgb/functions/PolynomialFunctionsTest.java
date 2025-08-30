package io.github.olajed.jgb.functions;

import io.github.olajed.jgb.enums.MonomialType;
import io.github.olajed.jgb.number.Rational;
import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.ordering.GrevlexOrdering;
import io.github.olajed.jgb.ordering.LexOrdering;
import io.github.olajed.jgb.structures.PolynomialRing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

public class PolynomialFunctionsTest {
    @Test
    public void testSPolynomialWithDifferentFields() {
        var field1 = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var field2 = field1.extend(new String[]{"z"});
        var f = field1.createPolynomial(
                Arrays.asList(
                        field1.createMonomial(new Real(2), Map.of("x", 2), MonomialType.DENSE),
                        field1.createMonomial(new Real(2), Map.of("y", 2), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );
        var g = field2.createPolynomial(
                Arrays.asList(
                        field2.createMonomial(new Real(2), Map.of("x", 2, "y", 1), MonomialType.DENSE),
                        field2.createMonomial(new Real(2), Map.of("z", 3), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> PolynomialFunctions.sPolynomial(f, g));
        var actualMessage = exception.getMessage();
        Assertions.assertEquals("Both polynomials should be defined in the same ring.", actualMessage);
    }

    @Test
    public void testSPolynomialWithDifferentOrderings() {
        var field1 = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = field1.createPolynomial(
                Arrays.asList(
                        field1.createMonomial(new Real(2), Map.of("x", 2), MonomialType.DENSE),
                        field1.createMonomial(new Real(2), Map.of("y", 2), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );
        var g = field1.createPolynomial(
                Arrays.asList(
                        field1.createMonomial(new Real(2), Map.of("x", 2, "y", 1), MonomialType.DENSE),
                        field1.createMonomial(new Real(2), Map.of("y", 3), MonomialType.DENSE)
                ),
                new GrevlexOrdering<>()
        );

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> PolynomialFunctions.sPolynomial(f, g));
        var actualMessage = exception.getMessage();
        Assertions.assertEquals("Both polynomials should be defined using the same ordering.", actualMessage);
    }

    @Test
    public void computation1() {
        var field = new PolynomialRing(Rational.class, new String[]{"x", "y"});
        var p1 = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Rational(2), Map.of("x", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(-4), Map.of("x", 1), MonomialType.DENSE),
                        field.createMonomial(new Rational(1), Map.of("y", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(-4), Map.of("y", 1), MonomialType.DENSE),
                        field.createMonomial(new Rational(3), Map.of(), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        var p2 = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Rational(1), Map.of("x", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(-2), Map.of("x", 1), MonomialType.DENSE),
                        field.createMonomial(new Rational(3), Map.of("y", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(-12), Map.of("y", 1), MonomialType.DENSE),
                        field.createMonomial(new Rational(9), Map.of(), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        var p3 = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Rational(-5, 2), Map.of("y", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(10), Map.of("y", 1), MonomialType.DENSE),
                        field.createMonomial(new Rational(-15, 2), Map.of(), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        Assertions.assertEquals(PolynomialFunctions.sPolynomial(p1, p2), p3);
    }

    @Test
    public void computation2() {
        var field = new PolynomialRing(Rational.class, new String[]{"x", "y"});

        var p1 = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Rational(1), Map.of("x", 3, "y", 1), MonomialType.DENSE),
                        field.createMonomial(new Rational(-2), Map.of("x", 2, "y", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(1), Map.of("x", 1), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        var p2 = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Rational(3), Map.of("x", 4), MonomialType.DENSE),
                        field.createMonomial(new Rational(-1), Map.of("y", 1), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        var p3 = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Rational(-2), Map.of("x", 3, "y", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(1), Map.of("x", 2), MonomialType.DENSE),
                        field.createMonomial(new Rational(1, 3), Map.of("y", 2), MonomialType.DENSE)
                ),
                new LexOrdering<>()
        );

        Assertions.assertEquals(PolynomialFunctions.sPolynomial(p1, p2), p3);
    }
}
