package io.github.olajed.jgb.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RationalTest {
    @Test
    void constructor_twoArgs() {
        var r = new Rational(3, 4);
        assertEquals(3.0 / 4.0, r.get());
    }

    @Test
    void constructor_oneArg() {
        var r = new Rational(5);
        assertEquals(5.0, r.get());
    }

    @Test
    void constructor_zeroDenominator_shouldThrow() {
        assertThrows(ArithmeticException.class, () -> new Rational(1, 0));
    }

    @Test
    void constructor_reduction() {
        var r = new Rational(6, 9);
        assertEquals(new Rational(2, 3), r);
    }

    @Test
    void constructor_sameNumeratorDenominator() {
        var r = new Rational(5, 5);
        assertEquals(Rational.ONE, r);
    }

    @Test
    void constructor_evenNumbers() {
        var r = new Rational(4, 6);
        assertEquals(new Rational(2, 3), r);
    }

    @Test
    void constructor_negativeValues() {
        var r1 = new Rational(-3, 4);
        assertEquals(-3.0 / 4.0, r1.get());

        var r2 = new Rational(3, -4);
        assertEquals(-3.0 / 4.0, r2.get());

        var r3 = new Rational(-3, -4);
        assertEquals(3.0 / 4.0, r3.get());
    }

    @Test
    void of_zero() {
        var r = Rational.of(0);
        assertSame(Rational.ZERO, r);
    }

    @Test
    void of_nonZero() {
        var r = Rational.of(5);
        assertEquals(new Rational(5), r);
    }

    @Test
    void of_twoArgs_zero() {
        var r = Rational.of(0, 5);
        assertSame(Rational.ZERO, r);
    }

    @Test
    void of_twoArgs_nonZero() {
        var r = Rational.of(3, 4);
        assertEquals(new Rational(3, 4), r);
    }

    @Test
    void zero() {
        var zero = new Rational(5, 7).zero();
        assertSame(Rational.ZERO, zero);
    }

    @Test
    void isZero() {
        assertTrue(Rational.ZERO.isZero());
        assertTrue(new Rational(0, 5).isZero());
        assertFalse(new Rational(1, 5).isZero());
    }

    @Test
    void one() {
        var one = new Rational(3, 7).one();
        assertSame(Rational.ONE, one);
    }

    @Test
    void isOne() {
        assertTrue(Rational.ONE.isOne());
        assertTrue(new Rational(5, 5).isOne());
        assertTrue(new Rational(-3, -3).isOne());
        assertFalse(new Rational(2, 3).isOne());
        assertFalse(Rational.ZERO.isOne());
    }

    @Test
    void signum() {
        assertEquals(1, new Rational(3, 4).signum());
        assertEquals(-1, new Rational(-3, 4).signum());
        assertEquals(-1, new Rational(3, -4).signum());
        assertEquals(1, new Rational(-3, -4).signum());
        assertEquals(0, Rational.ZERO.signum());
    }

    @Test
    void negate() {
        var r1 = new Rational(3, 4);
        var negated1 = r1.negate();
        assertEquals(new Rational(-3, 4), negated1);

        var r2 = new Rational(-3, 4);
        var negated2 = r2.negate();
        assertEquals(new Rational(3, 4), negated2);
    }

    @Test
    void negate_integerMinValue() {
        var r = new Rational(Integer.MIN_VALUE, 2);
        var negated = r.negate();
        assertNotNull(negated);
        assertEquals(-r.get(), negated.get(), 1e-10);
    }

    @Test
    void negate_zero() {
        var negated = Rational.ZERO.negate();
        assertEquals(Rational.ZERO, negated);
    }

    @Test
    void add_int_zero() {
        var r = new Rational(3, 4);
        var result = r.add(0);
        assertSame(r, result);
    }

    @Test
    void add_int_toZero() {
        var result = Rational.ZERO.add(5);
        assertEquals(new Rational(5), result);
    }

    @Test
    void add_int_normal() {
        var r = new Rational(1, 2);
        var result = r.add(1);
        assertEquals(new Rational(3, 2), result);
    }

    @Test
    void add_numeric() {
        var r1 = new Rational(1, 2);
        var r2 = new Rational(1, 3);
        var result = (Rational) r1.add(r2);
        assertEquals(new Rational(5, 6), result);
    }

    @Test
    void add_numeric_zero() {
        var r = new Rational(3, 4);
        var result = (Rational) r.add(Rational.ZERO);
        assertEquals(r, result);
    }

    @Test
    void add_numeric_toZero() {
        var r = new Rational(3, 4);
        var result = (Rational) Rational.ZERO.add(r);
        assertEquals(r, result);
    }

    @Test
    void add_incompatibleType_shouldThrow() {
        var r = new Rational(1, 2);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> r.add(real));
    }

    @Test
    void subtract_int_zero() {
        var r = new Rational(3, 4);
        var result = r.subtract(0);
        assertSame(r, result);
    }

    @Test
    void subtract_int_fromZero() {
        var result = Rational.ZERO.subtract(5);
        assertEquals(new Rational(-5), result);
    }

    @Test
    void subtract_int_fromZero_integerMinValue() {
        var result = Rational.ZERO.subtract(Integer.MIN_VALUE);
        assertEquals(new Rational(Integer.MIN_VALUE, -1), result);
    }

    @Test
    void subtract_int_normal() {
        var r = new Rational(3, 2);
        var result = r.subtract(1);
        assertEquals(new Rational(1, 2), result);
    }

    @Test
    void subtract_numeric() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(1, 6);
        var result = (Rational) r1.subtract(r2);
        assertEquals(new Rational(1, 2), result);
    }

    @Test
    void subtract_numeric_zero() {
        var r = new Rational(3, 4);
        var result = (Rational) r.subtract(Rational.ZERO);
        assertEquals(r, result);
    }

    @Test
    void subtract_numeric_fromZero() {
        var r = new Rational(3, 4);
        var result = (Rational) Rational.ZERO.subtract(r);
        assertEquals(r.negate(), result);
    }

    @Test
    void subtract_incompatibleType_shouldThrow() {
        var r = new Rational(1, 2);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> r.subtract(real));
    }

    @Test
    void multiply_int_zero() {
        var r = new Rational(3, 4);
        var result = r.multiply(0);
        assertSame(Rational.ZERO, result);
    }

    @Test
    void multiply_int_fromZero() {
        var result = Rational.ZERO.multiply(5);
        assertSame(Rational.ZERO, result);
    }

    @Test
    void multiply_int_normal() {
        var r = new Rational(2, 3);
        var result = r.multiply(3);
        assertEquals(new Rational(2), result);
    }

    @Test
    void multiply_int_withReduction() {
        var r = new Rational(4, 6);
        var result = r.multiply(9);
        assertEquals(new Rational(6), result);
    }

    @Test
    void multiply_numeric() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(3, 4);
        var result = (Rational) r1.multiply(r2);
        assertEquals(new Rational(1, 2), result);
    }

    @Test
    void multiply_numeric_zero() {
        var r = new Rational(3, 4);
        var result = (Rational) r.multiply(Rational.ZERO);
        assertSame(Rational.ZERO, result);
    }

    @Test
    void multiply_numeric_fromZero() {
        var r = new Rational(3, 4);
        var result = (Rational) Rational.ZERO.multiply(r);
        assertSame(Rational.ZERO, result);
    }

    @Test
    void multiply_incompatibleType_shouldThrow() {
        var r = new Rational(1, 2);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> r.multiply(real));
    }

    @Test
    void divide_int_zero_shouldThrow() {
        var r = new Rational(1, 2);
        assertThrows(ArithmeticException.class, () -> r.divide(0));
    }

    @Test
    void divide_int_fromZero() {
        var result = Rational.ZERO.divide(5);
        assertSame(Rational.ZERO, result);
    }

    @Test
    void divide_int_normal() {
        var r = new Rational(6, 4);
        var result = r.divide(2);
        assertEquals(new Rational(3, 4), result);
    }

    @Test
    void divide_numeric_zero_shouldThrow() {
        var r = new Rational(1, 2);
        assertThrows(ArithmeticException.class, () -> r.divide(Rational.ZERO));
    }

    @Test
    void divide_numeric_fromZero() {
        var r = new Rational(3, 4);
        var result = (Rational) Rational.ZERO.divide(r);
        assertSame(Rational.ZERO, result);
    }

    @Test
    void divide_numeric_normal() {
        var r1 = new Rational(1, 2);
        var r2 = new Rational(1, 4);
        var result = (Rational) r1.divide(r2);
        assertEquals(new Rational(2), result);
    }

    @Test
    void divide_incompatibleType_shouldThrow() {
        var r = new Rational(1, 2);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> r.divide(real));
    }

    @Test
    void inverse() {
        var r = new Rational(3, 4);
        var inv = (Rational) r.inverse();
        assertEquals(new Rational(4, 3), inv);
    }

    @Test
    void inverse_one() {
        var inv = (Rational) Rational.ONE.inverse();
        assertEquals(Rational.ONE, inv);
    }

    @Test
    void inverse_negative() {
        var r = new Rational(-2, 3);
        var inv = (Rational) r.inverse();
        assertEquals(new Rational(3, -2), inv);
    }

    @Test
    void get() {
        var r = new Rational(3, 4);
        assertEquals(0.75, r.get());

        var r2 = new Rational(1, 3);
        assertEquals(1.0 / 3.0, r2.get(), 1e-15);
    }

    @Test
    void constants() {
        assertEquals(0.0, Rational.ZERO.get());
        assertEquals(1.0, Rational.ONE.get());
    }

    @Test
    void testToString_zero() {
        assertEquals("0", Rational.ZERO.toString());
    }

    @Test
    void testToString_wholeNumber() {
        var r = new Rational(5, 1);
        assertEquals("5", r.toString());
    }

    @Test
    void testToString_fraction() {
        var r = new Rational(3, 4);
        assertEquals("3 / 4", r.toString());
    }

    @Test
    void testToString_negativeNumerator() {
        var r = new Rational(-3, 4);
        assertEquals("-3 / 4", r.toString());
    }

    @Test
    void testEquals_same_object() {
        var r = new Rational(2, 3);
        assertEquals(r, r);
    }

    @Test
    void testEquals_equal_values() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(2, 3);
        assertEquals(r1, r2);
    }

    @Test
    void testEquals_equivalent_fractions() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(4, 6);
        assertEquals(r1, r2);
    }

    @Test
    void testEquals_different_values() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(3, 4);
        assertNotEquals(r1, r2);
    }

    @Test
    void testEquals_null() {
        var r = new Rational(2, 3);
        assertNotEquals(null, r);
    }

    @Test
    void testEquals_different_type() {
        var r = new Rational(2, 3);
        var str = "2/3";
        assertNotEquals(r, str);
    }

    @Test
    void testEquals_negative_values() {
        var r1 = new Rational(-2, 3);
        var r2 = new Rational(2, -3);
        assertEquals(r1, r2);

        var r3 = new Rational(-2, -3);
        var r4 = new Rational(2, 3);
        assertEquals(r3, r4);
    }

    @Test
    void testEquals_different_signs() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(-2, 3);
        assertNotEquals(r1, r2);
    }

    @Test
    void testHashCode_consistency() {
        var r = new Rational(2, 3);
        int hash1 = r.hashCode();
        int hash2 = r.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCode_equal_objects() {
        var r1 = new Rational(2, 3);
        var r2 = new Rational(4, 6);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testHashCode_equivalent_representations() {
        var r1 = new Rational(-2, 3);
        var r2 = new Rational(2, -3);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void large_numbers() {
        var r = new Rational(1000000, 2000000);
        assertEquals(new Rational(1, 2), r);
    }

    @Test
    void arithmetic_overflow_protection() {
        var r1 = new Rational(Integer.MAX_VALUE / 2, 2);
        var r2 = new Rational(1, 2);

        assertDoesNotThrow(() -> r1.add(r2));
        assertDoesNotThrow(() -> r1.multiply(r2));
    }

    @Test
    void gcd_optimization() {
        var r = new Rational(12, 18);
        assertEquals(new Rational(2, 3), r);

        var r2 = new Rational(15, 25);
        assertEquals(new Rational(3, 5), r2);
    }

    @Test
    void complex_arithmetic() {
        var r1 = new Rational(1, 2);
        var r2 = new Rational(1, 3);
        var r3 = new Rational(1, 6);

        var result = (Rational) r1.add(r2).subtract(r3);
        assertEquals(new Rational(2, 3), result);
    }
}