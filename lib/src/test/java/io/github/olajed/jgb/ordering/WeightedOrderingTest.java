package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeightedOrderingTest {
    @Test
    void testCompareDifferentWeightedDegree() {
        WeightedOrdering<Real> ordering = new WeightedOrdering<>(new int[]{2, 1});
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 0}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{0, 3}, new Real(1.0));
        assertTrue(ordering.compare(m1, m2) > 0, "m1 should be greater than m3");
        assertTrue(ordering.compare(m2, m1) < 0, "m3 should be less than m1");
    }

    @Test
    void testTieBreakerLexOrdering() {
        WeightedOrdering<Real> ordering = new WeightedOrdering<>(new int[]{1, 1});
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 0}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 1}, new Real(1.0));
        int cmp = ordering.compare(m1, m2);
        assertTrue(cmp > 0, "Lex ordering: m1 > m2 since 2 > 1 in first coordinate");
    }

    @Test
    void testCustomTieBreaker() {
        MonomialOrdering<Real> reverseLex = new MonomialOrdering<>() {
            private static final byte ORDER_ID = 5;

            @Override
            public int compare(Monomial<Real> a, Monomial<Real> b) {
                if (a.fieldSize() != b.fieldSize()) {
                    throw new IllegalArgumentException("Both monomials should be defined in the same ring.");
                }

                for (int i = a.fieldSize() - 1; i >= 0; i--) {
                    int diff = a.getExponent(i) - b.getExponent(i);
                    if (diff != 0) return diff;
                }

                return 0;
            }

            @Override
            public byte orderId() {
                return ORDER_ID;
            }
        };

        WeightedOrdering<Real> ordering = new WeightedOrdering<>(new int[]{1, 1}, reverseLex);
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 0}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 1}, new Real(1.0));

        int cmp = ordering.compare(m1, m2);
        assertTrue(cmp < 0, "Reverse lex ordering: m1 < m2 since compare on last index (0 < 1)");
    }

    @Test
    void testDifferentFieldSizeThrows() {
        WeightedOrdering<Real> ordering = new WeightedOrdering<>(new int[]{1, 1});
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{1, 0}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 0, 0}, new Real(1.0));
        assertThrows(IllegalArgumentException.class, () -> ordering.compare(m1, m2));
    }

    @Test
    void testWeightsMismatchThrows() {
        WeightedOrdering<Real> ordering = new WeightedOrdering<>(new int[]{1, 1, 1});
        Monomial<Real> m1 = new DenseMonomial<>(new int[]{1, 0}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{0, 1}, new Real(1.0));
        assertThrows(IllegalArgumentException.class, () -> ordering.compare(m1, m2));
    }

    @Test
    void testOrderId() {
        WeightedOrdering<Real> ordering = new WeightedOrdering<>(new int[]{1, 1});
        assertEquals(4, ordering.orderId());
    }
}