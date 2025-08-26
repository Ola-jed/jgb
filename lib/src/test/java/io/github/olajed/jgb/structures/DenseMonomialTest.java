package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.number.Real;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenseMonomialTest {
    @Test
    void testDegree() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{2, 1}, new Real(3.0));
        assertEquals(3, m.degree());
    }

    @Test
    void testCoefficient() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{1, 0}, new Real(5.0));
        assertEquals(new Real(5.0), m.coefficient());
    }

    @Test
    void testFieldSize() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{3, 4, 5}, new Real(2.0));
        assertEquals(3, m.fieldSize());
    }

    @Test
    void testGetExponent() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{2, 0, 1}, new Real(7.0));
        assertEquals(2, m.getExponent(0));
        assertEquals(0, m.getExponent(1));
        assertEquals(1, m.getExponent(2));
    }

    @Test
    void testMultiplyMonomial() {
        Monomial<Real> a = new DenseMonomial<>(new int[]{1, 0}, new Real(2.0));
        Monomial<Real> b = new DenseMonomial<>(new int[]{0, 2}, new Real(3.0));
        Monomial<Real> result = a.multiply(b);

        assertEquals(new Real(6.0), result.coefficient());

        int[] expected = {1, 2};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.getExponent(i));
        }
    }

    @Test
    void testMultiplyScalar() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{1, 1}, new Real(2.0));
        Monomial<Real> result = m.multiply(new Real(5.0));

        assertEquals(new Real(10.0), result.coefficient());

        int[] expected = {1, 1};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.getExponent(i));
        }
    }

    @Test
    void testDivide() {
        Monomial<Real> a = new DenseMonomial<>(new int[]{3, 2}, new Real(6.0));
        Monomial<Real> b = new DenseMonomial<>(new int[]{1, 1}, new Real(2.0));
        Monomial<Real> result = a.divide(b);

        assertEquals(new Real(3.0), result.coefficient());

        int[] expected = {2, 1};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.getExponent(i));
        }
    }

    @Test
    void testExponentsEqual() {
        Monomial<Real> a = new DenseMonomial<>(new int[]{1, 1}, new Real(2.0));
        Monomial<Real> b = new DenseMonomial<>(new int[]{1, 1}, new Real(5.0));
        assertTrue(a.exponentsEqual(b));
    }

    @Test
    void testDisjointWith() {
        Monomial<Real> a = new DenseMonomial<>(new int[]{1, 0}, new Real(2.0));
        Monomial<Real> b = new DenseMonomial<>(new int[]{0, 2}, new Real(3.0));
        assertTrue(a.disjointWith(b));
    }

    @Test
    void testIsOneAndZero() {
        Monomial<Real> zero = new DenseMonomial<>(new int[]{0, 0}, Real.ZERO);
        Monomial<Real> one  = new DenseMonomial<>(new int[]{0, 0}, Real.ONE);
        assertTrue(zero.isZero());
        assertTrue(one.isOne());
    }

    @Test
    void testAccumulateWithIntegers() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{2, 3}, new Real(1.0));
        int result = m.accumulate(new int[]{10, 20},
                (exp, val) -> exp * val,
                0,
                Integer::sum
        );

        assertEquals(80, result);
    }

    @Test
    void testDivisors() {
        Monomial<Real> m = new DenseMonomial<>(new int[]{2, 1}, new Real(1.0));

        List<Monomial<Real>> divisors = new ArrayList<>();
        for (Monomial<Real> d : m.divisors()) {
            divisors.add(d);
        }

        assertTrue(divisors.stream().anyMatch(d -> d.exponentsEqual(
                new DenseMonomial<>(new int[]{1, 1}, Real.ONE)
        )));
    }

}
