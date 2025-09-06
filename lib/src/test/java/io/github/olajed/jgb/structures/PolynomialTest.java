package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.enums.MonomialType;
import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.ordering.GrevlexOrdering;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PolynomialTest {
    @Test
    void testPolynomialConstruction() {
        var ring = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var polynomial = ring.createPolynomial(
                Arrays.asList(
                        ring.createMonomial(new Real(1), Map.of("x", 2), MonomialType.DENSE),
                        ring.createMonomial(new Real(3), Map.of("y", 1), MonomialType.DENSE)
                ),
                new GrevlexOrdering<>()
        );

        assertEquals(2, polynomial.degree());
        assertEquals(2, polynomial.length());
        assertFalse(polynomial.isZero());
        assertFalse(polynomial.isOne());
    }

    @Test
    void testAddition() {
        var ring = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = ring.createPolynomial(
                List.of(
                        ring.createMonomial(new Real(1), Map.of("x", 1), MonomialType.DENSE)
                ),
                new GrevlexOrdering<>()
        );

        var g = ring.createPolynomial(
                List.of(ring.createMonomial(new Real(2), Map.of("x", 1), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        var sum = f.add(g);
        assertEquals(1, sum.degree());
        assertEquals(new Real(3), sum.leadingCoefficient());
    }

    @Test
    void testSubtractionToZero() {
        var ring = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = ring.createPolynomial(
                List.of(ring.createMonomial(new Real(5), Map.of("y", 2), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        var g = ring.createPolynomial(
                List.of(ring.createMonomial(new Real(5), Map.of("y", 2), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        var result = f.subtract(g);
        assertTrue(result.isZero());
    }

    @Test
    void testMultiplyByScalar() {
        var ring = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = ring.createPolynomial(
                List.of(ring.createMonomial(new Real(2), Map.of("x", 1), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        var product = f.multiply(new Real(3));
        assertEquals(new Real(6), product.leadingCoefficient());
    }

    @Test
    void testDivideByScalar() {
        var ring = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = ring.createPolynomial(
                List.of(ring.createMonomial(new Real(6), Map.of("x", 1), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        var quotient = f.divide(new Real(2));
        assertEquals(new Real(3), quotient.leadingCoefficient());
    }

    @Test
    void testLeadingTermAndMonomial() {
        var ring = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = ring.createPolynomial(
                Arrays.asList(
                        ring.createMonomial(new Real(4), Map.of("x", 3), MonomialType.DENSE),
                        ring.createMonomial(new Real(2), Map.of("y", 1), MonomialType.DENSE)
                ),
                new GrevlexOrdering<>()
        );

        assertEquals(3, f.degree());
        assertEquals(new Real(4), f.leadingCoefficient());
        assertEquals(new Real(4), f.leadingTerm().coefficient());
    }
}

