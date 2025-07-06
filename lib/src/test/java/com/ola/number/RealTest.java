package com.ola.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RealTest {
    @Test
    void get() {
        var r = new Real(5.5);
        assertEquals(5.5, r.get());
    }

    @Test
    void add() {
        var r1 = new Real(2);
        var r2 = new Real(3.5);
        var result = (Real) r1.add(r2);
        assertEquals(new Real(5.5), result);
    }

    @Test
    void subtract() {
        var r1 = new Real(5);
        var r2 = new Real(2);
        var result = (Real) r1.subtract(r2);
        assertEquals(new Real(3), result);
    }

    @Test
    void multiply() {
        var r1 = new Real(2);
        var r2 = new Real(3);
        var result = (Real) r1.multiply(r2);
        assertEquals(new Real(6), result);
    }

    @Test
    void divide() {
        var r1 = new Real(6);
        var r2 = new Real(2);
        var result = r1.divide(r2);
        assertEquals(new Real(3), result);
    }

    @Test
    void divide_byZero_shouldThrow() {
        var r1 = new Real(1);
        var zero = new Real(0);
        assertThrows(ArithmeticException.class, () -> r1.divide(zero));
    }

    @Test
    void negate() {
        var r = new Real(5);
        var negated = r.negate();
        assertEquals(new Real(-5), negated);
    }

    @Test
    void inverse() {
        Real r = new Real(2);
        Real inv = (Real) r.inverse();
        assertEquals(new Real(0.5), inv);
    }

    @Test
    void inverse_zero_shouldThrow() {
        var zero = new Real(0);
        assertThrows(ArithmeticException.class, zero::inverse);
    }

    @Test
    void one() {
        var one = (Real) new Real(0).one();
        assertEquals(new Real(1), one);
    }

    @Test
    void zero() {
        var zero = new Real(0).zero();
        assertEquals(new Real(0), zero);
    }

    @Test
    void testToString() {
        var r = new Real(3.14);
        assertEquals("3.14", r.toString());
    }

    @Test
    void testEquals() {
        var r1 = new Real(2);
        var r2 = new Real(2);
        var r3 = new Real(3);
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(null, r1);
    }
}
