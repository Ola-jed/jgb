package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import io.github.olajed.jgb.structures.SparseMonomial;
import org.junit.jupiter.api.Test;
import java.util.BitSet;
import static org.junit.jupiter.api.Assertions.*;

public class EliminationOrderingTest {
    private final MonomialOrdering<Real> lex = new LexOrdering<>();

    @Test
    void testConstructorBitSetValidation() {
        BitSet elimination = new BitSet(5);
        elimination.set(1);
        BitSet retained = new BitSet(5);
        retained.set(4);
        assertThrows(IllegalArgumentException.class,
                () -> new EliminationOrdering<>(elimination, retained, lex));
    }

    @Test
    void testDenseMonomialsEliminationFirst() {
        BitSet elimination = new BitSet();
        elimination.set(0);
        BitSet retained = new BitSet();
        retained.set(1);

        EliminationOrdering<Real> ordering = new EliminationOrdering<>(elimination, retained, lex);

        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 1}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 5}, new Real(1.0));

        assertTrue(ordering.compare(m1, m2) > 0, "Elimination variable 2 > 1");
        assertTrue(ordering.compare(m2, m1) < 0);
    }

    @Test
    void testSparseMonomialsEliminationFirst() {
        BitSet elimination = new BitSet();
        elimination.set(0);
        BitSet retained = new BitSet();
        retained.set(1);

        EliminationOrdering<Real> ordering = new EliminationOrdering<>(elimination, retained, lex);

        Monomial<Real> m1 = new SparseMonomial<>(new int[]{2, 0}, new Real(1.0));
        Monomial<Real> m2 = new SparseMonomial<>(new int[]{1, 5}, new Real(1.0));

        assertTrue(ordering.compare(m1, m2) > 0);
        assertTrue(ordering.compare(m2, m1) < 0);
    }

    @Test
    void testRetainedOrderingWhenEliminationEqual() {
        BitSet elimination = new BitSet();
        elimination.set(0);
        BitSet retained = new BitSet();
        retained.set(1);

        EliminationOrdering<Real> ordering = new EliminationOrdering<>(elimination, retained, lex);

        Monomial<Real> m1 = new DenseMonomial<>(new int[]{2, 3}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{2, 5}, new Real(1.0));

        assertTrue(ordering.compare(m1, m2) < 0, "Retained variable 3 < 5");
    }

    @Test
    void testDifferentFieldSizeThrows() {
        BitSet elimination = new BitSet();
        elimination.set(0);
        BitSet retained = new BitSet();
        retained.set(1);

        EliminationOrdering<Real> ordering = new EliminationOrdering<>(elimination, retained, lex);

        Monomial<Real> m1 = new DenseMonomial<>(new int[]{1, 2}, new Real(1.0));
        Monomial<Real> m2 = new DenseMonomial<>(new int[]{1, 2, 3}, new Real(1.0));

        assertThrows(IllegalArgumentException.class, () -> ordering.compare(m1, m2));
    }

    @Test
    void testOrderId() {
        BitSet elimination = new BitSet();
        elimination.set(0);
        BitSet retained = new BitSet();
        retained.set(1);

        EliminationOrdering<Real> ordering = new EliminationOrdering<>(elimination, retained, lex);
        assertEquals(5, ordering.orderId());
    }
}

