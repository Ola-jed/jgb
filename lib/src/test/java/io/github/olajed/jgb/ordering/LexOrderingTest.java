package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LexOrderingTest {
    @Test
    void testEqualMonomialsCompareToZero() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 3}, new Real(5.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{2, 3}, new Real(7.0));

        MonomialOrdering<Real> ordering = new LexOrdering<>();
        assertEquals(0, ordering.compare(m1, m2), "Same exponents must compare equal regardless of coefficient");
    }

    @Test
    void testFirstVariableDecides() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{3, 1}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{2, 5}, new Real(1.0));

        MonomialOrdering<Real> ordering = new LexOrdering<>();
        assertTrue(ordering.compare(m1, m2) > 0, "m1 should be greater than m2 since 3 > 2 in first variable");
        assertTrue(ordering.compare(m2, m1) < 0, "m2 should be less than m1");
    }

    @Test
    void testSecondVariableDecidesIfFirstEqual() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 5}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{2, 3}, new Real(1.0));

        MonomialOrdering<Real> ordering = new LexOrdering<>();
        assertTrue(ordering.compare(m1, m2) > 0, "m1 should be greater than m2 since 5 > 3 in second variable");
    }

    @Test
    void testDifferentFieldSizesThrows() {
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{1, 2}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 2, 3}, new Real(1.0));

        MonomialOrdering<Real> ordering = new LexOrdering<>();
        assertThrows(IllegalArgumentException.class, () -> ordering.compare(m1, m2));
    }

    @Test
    void testOrderIdIsConstant() {
        MonomialOrdering<Real> ordering = new LexOrdering<>();
        assertEquals(1, ordering.orderId(), "LexOrdering orderId should always be 1");
    }
}

