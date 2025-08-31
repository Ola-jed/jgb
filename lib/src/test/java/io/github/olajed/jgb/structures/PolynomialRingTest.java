package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.enums.MonomialType;
import io.github.olajed.jgb.number.Complex;
import io.github.olajed.jgb.number.Rational;
import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.ordering.GrevlexOrdering;
import io.github.olajed.jgb.ordering.LexOrdering;
import io.github.olajed.jgb.ordering.MonomialOrdering;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialRingTest {
    private PolynomialRing realRing;
    private PolynomialRing rationalRing;

    @BeforeEach
    void setup() {
        realRing = new PolynomialRing(Real.class, new String[]{"x", "y"});
        rationalRing = new PolynomialRing(Rational.class, new String[]{"a"});
    }

    @Test
    void testToStringForReal() {
        assertEquals("R[x, y]", realRing.toString());
    }

    @Test
    void testToStringForRational() {
        assertEquals("Q[a]", rationalRing.toString());
    }

    @Test
    void testContractValid() {
        PolynomialRing contracted = realRing.contract(1);
        assertEquals("R[x]", contracted.toString());
    }

    @Test
    void testContractInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                realRing.contract(3));
    }

    @Test
    void testExtend() {
        PolynomialRing extended = realRing.extend(new String[]{"z"});
        assertEquals("R[x, y, z]", extended.toString());
    }

    @Test
    void testTypeChange() {
        PolynomialRing changed = realRing.typeChange(Rational.class);
        assertEquals("Q[x, y]", changed.toString());
    }

    @Test
    void testTypeChangeInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                realRing.typeChange((Class) String.class));
    }

    @Test
    void testCreateMonomialWithValidCoefficient() {
        Real coeff = new Real(3.0);
        Monomial<Real> monomial = realRing.createMonomial(
                coeff,
                Map.of("x", 2, "y", 1),
                MonomialType.DENSE
        );

        assertNotNull(monomial);
        assertEquals(coeff, monomial.coefficient());
        assertEquals(2, monomial.getExponent(0));
        assertEquals(1, monomial.getExponent(1));
    }

    @Test
    void testCreateMonomialWithWrongCoefficientTypeThrows() {
        Complex wrongCoeff = new Complex(1, 2);
        assertThrows(IllegalArgumentException.class, () -> realRing.createMonomial(wrongCoeff, Map.of("x", 1), MonomialType.DENSE));
    }

    @Test
    void testCreateMonomialWithUnknownIndeterminateThrows() {
        Real coeff = new Real(2.0);
        assertThrows(IllegalArgumentException.class, () -> realRing.createMonomial(coeff, Map.of("z", 1), MonomialType.SPARSE));
    }

    @Test
    void testZeroPolynomial() {
        MonomialOrdering<Real> ordering = new LexOrdering<>();
        Polynomial<Real> zeroPoly = realRing.zero(ordering);
        assertTrue(zeroPoly.monomials().isEmpty());
        assertTrue(zeroPoly.isZero());
    }

    @Test
    void testOnePolynomial() {
        MonomialOrdering<Real> ordering = new LexOrdering<>();
        Polynomial<Real> onePoly = realRing.one(ordering, MonomialType.DENSE);
        assertEquals(1, onePoly.monomials().size());
        assertTrue(onePoly.isOne());
    }

    @Test
    void testFormatMonomial() {
        Real coeff = new Real(5.0);
        Monomial<Real> monomial = new DenseMonomial<>(new int[]{2, 1}, coeff);
        String formatted = realRing.format(monomial);
        assertEquals("5*x^2*y", formatted);
    }

    @Test
    void testFormatPolynomial() {
        Real coeff = new Real(1.0);
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{1, 0}, coeff);
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{0, 1}, coeff);
        Polynomial<Real> poly = new Polynomial<>(List.of(m1, m2), 2, new LexOrdering<>());
        String formatted = realRing.format(poly);
        assertEquals("y + x", formatted);
    }

    @Test
    void testParsePolynomial() {
        Polynomial<Real> parsed = realRing.parse("x^2 + 3*y");
        assertNotNull(parsed);
        assertFalse(parsed.monomials().isEmpty());
        var field1 = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var f = field1.createPolynomial(
                Arrays.asList(
                        field1.createMonomial(new Real(1), Map.of("x", 2), MonomialType.DENSE),
                        field1.createMonomial(new Real(3), Map.of("y", 1), MonomialType.DENSE)
                ),
                new GrevlexOrdering<>()
        );
        assertEquals(parsed, f);
    }
}
