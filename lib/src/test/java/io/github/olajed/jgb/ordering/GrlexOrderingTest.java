package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrlexOrderingTest {
    @Test
    void testEqualMonomialsCompareToZero() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 3}, new Real(4.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{2, 3}, new Real(7.0));

        MonomialOrdering<Real> ordering = new GrlexOrdering<>();
        assertEquals(0, ordering.compare(m1, m2), "Same exponents must compare equal regardless of coefficient");
    }

    @Test
    void testDegreeDecidesOrdering() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{3, 1}, new Real(1.0)); // degree = 4
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{2, 1}, new Real(1.0)); // degree = 3

        MonomialOrdering<Real> ordering = new GrlexOrdering<>();
        assertTrue(ordering.compare(m1, m2) > 0, "m1 should be greater because degree 4 > degree 3");
        assertTrue(ordering.compare(m2, m1) < 0, "m2 should be less than m1");
    }

    @Test
    void testLexTieBreakerWhenDegreesEqual() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 3}, new Real(1.0)); // degree = 5
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 4}, new Real(1.0)); // degree = 5

        MonomialOrdering<Real> ordering = new GrlexOrdering<>();
        assertTrue(ordering.compare(m1, m2) > 0,
                "Degrees equal (5), fallback to lex: compare first exponent 2 > 1 so m1 > m2");
    }

    @Test
    void testDifferentFieldSizesThrows() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{1, 2}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 2, 3}, new Real(1.0));

        MonomialOrdering<Real> ordering = new GrlexOrdering<>();
        assertThrows(IllegalArgumentException.class, () -> ordering.compare(m1, m2));
    }

    @Test
    void testOrderIdIsConstant() {
        MonomialOrdering<Real> ordering = new GrlexOrdering<>();
        assertEquals(2, ordering.orderId(), "GrlexOrdering orderId should always be 2");
    }
}
