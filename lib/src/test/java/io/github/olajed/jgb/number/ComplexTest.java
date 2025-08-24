package io.github.olajed.jgb.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexTest {
    @Test
    void constructor() {
        var c = new Complex(3.0, 4.0);
        assertEquals(3.0, c.real());
        assertEquals(4.0, c.imaginary());
    }

    @Test
    void real() {
        var c = new Complex(5.5, -2.3);
        assertEquals(5.5, c.real());
    }

    @Test
    void imaginary() {
        var c = new Complex(5.5, -2.3);
        assertEquals(-2.3, c.imaginary());
    }

    @Test
    void negate() {
        var c = new Complex(3.0, -4.0);
        var negated = c.negate();
        assertEquals(-3.0, negated.real());
        assertEquals(4.0, negated.imaginary());
    }

    @Test
    void negate_zero() {
        var c = Complex.zero;
        var negated = c.negate();
        assertEquals(0.0, negated.real());
        assertEquals(0.0, negated.imaginary());
    }

    @Test
    void add_complex() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(1.0, -1.0);
        var result = (Complex) c1.add(c2);
        assertEquals(new Complex(3.0, 2.0), result);
    }

    @Test
    void add_complex_zero() {
        var c = new Complex(2.0, 3.0);
        var result = (Complex) c.add(Complex.zero);
        assertEquals(c, result);
    }

    @Test
    void add_double() {
        var c = new Complex(2.0, 3.0);
        var result = c.add(1.5);
        assertEquals(new Complex(3.5, 3.0), result);
    }

    @Test
    void add_double_zero() {
        var c = new Complex(2.0, 3.0);
        var result = c.add(0.0);
        assertEquals(c, result);
    }

    @Test
    void add_incompatibleType_shouldThrow() {
        var c = new Complex(1.0, 2.0);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> c.add(real));
    }

    @Test
    void subtract_complex() {
        var c1 = new Complex(5.0, 7.0);
        var c2 = new Complex(2.0, 3.0);
        var result = (Complex) c1.subtract(c2);
        assertEquals(new Complex(3.0, 4.0), result);
    }

    @Test
    void subtract_complex_zero() {
        var c = new Complex(2.0, 3.0);
        var result = (Complex) c.subtract(Complex.zero);
        assertEquals(c, result);
    }

    @Test
    void subtract_double() {
        var c = new Complex(5.0, 3.0);
        var result = c.subtract(1.5);
        assertEquals(new Complex(3.5, 3.0), result);
    }

    @Test
    void subtract_double_zero() {
        var c = new Complex(2.0, 3.0);
        var result = c.subtract(0.0);
        assertEquals(c, result);
    }

    @Test
    void subtract_incompatibleType_shouldThrow() {
        var c = new Complex(1.0, 2.0);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> c.subtract(real));
    }

    @Test
    void multiply_complex_simple() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(1.0, -1.0);
        var result = (Complex) c1.multiply(c2);
        // (2 + 3i)(1 - i) = 2 - 2i + 3i - 3i² = 2 + i + 3 = 5 + i
        assertEquals(new Complex(5.0, 1.0), result);
    }

    @Test
    void multiply_complex_zero() {
        var c = new Complex(2.0, 3.0);
        var result = (Complex) c.multiply(Complex.zero);
        assertEquals(Complex.zero, result);
    }

    @Test
    void multiply_complex_one() {
        var c = new Complex(2.0, 3.0);
        var result = (Complex) c.multiply(Complex.one);
        assertEquals(c, result);
    }

    @Test
    void multiply_complex_i() {
        var c = new Complex(2.0, 3.0);
        var result = (Complex) c.multiply(Complex.I);
        // (2 + 3i)(i) = 2i + 3i² = 2i - 3 = -3 + 2i
        assertEquals(new Complex(-3.0, 2.0), result);
    }

    @Test
    void multiply_double() {
        var c = new Complex(2.0, 3.0);
        var result = c.multiply(2.5);
        assertEquals(new Complex(5.0, 7.5), result);
    }

    @Test
    void multiply_double_zero() {
        var c = new Complex(2.0, 3.0);
        var result = c.multiply(0.0);
        assertEquals(Complex.zero, result);
    }

    @Test
    void multiply_incompatibleType_shouldThrow() {
        var c = new Complex(1.0, 2.0);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> c.multiply(real));
    }

    @Test
    void multiply_infinity_handling() {
        var c1 = new Complex(Double.POSITIVE_INFINITY, 1.0);
        var c2 = new Complex(2.0, 3.0);
        var result = (Complex) c1.multiply(c2);
        assertTrue(Double.isInfinite(result.real()) || Double.isInfinite(result.imaginary()));
    }

    @Test
    void multiply_nan_handling() {
        var c1 = new Complex(Double.NaN, Double.NaN);
        var c2 = new Complex(0.0, 0.0);
        var result = (Complex) c1.multiply(c2);
        // Should handle NaN cases gracefully
        assertNotNull(result);
    }

    @Test
    void divide_complex_simple() {
        var c1 = new Complex(5.0, 5.0);
        var c2 = new Complex(1.0, 1.0);
        var result = (Complex) c1.divide(c2);
        // (5 + 5i)/(1 + 1i) = (5 + 5i)(1 - 1i)/(1 + 1) = (5 + 5 + 5i - 5i)/2 = 10/2 = 5 + 0i
        assertEquals(new Complex(5.0, 0.0), result);
    }

    @Test
    void divide_complex_one() {
        var c = new Complex(2.0, 3.0);
        var result = (Complex) c.divide(Complex.one);
        assertEquals(c, result);
    }

    @Test
    void divide_double() {
        var c = new Complex(6.0, 9.0);
        var result = c.divide(3.0);
        assertEquals(new Complex(2.0, 3.0), result);
    }

    @Test
    void divide_incompatibleType_shouldThrow() {
        var c = new Complex(1.0, 2.0);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> c.divide(real));
    }

    @Test
    void divide_infinity_handling() {
        var c1 = new Complex(Double.POSITIVE_INFINITY, 1.0);
        var c2 = new Complex(2.0, 3.0);
        var result = (Complex) c1.divide(c2);
        assertTrue(Double.isInfinite(result.real()) || Double.isInfinite(result.imaginary()));
    }

    @Test
    void divide_by_infinity_handling() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(Double.POSITIVE_INFINITY, 1.0);
        var result = (Complex) c1.divide(c2);
        // Should produce finite result close to zero
        assertTrue(Double.isFinite(result.real()) && Double.isFinite(result.imaginary()));
    }

    @Test
    void divide_zero_by_nonzero() {
        var result = (Complex) Complex.zero.divide(new Complex(1.0, 1.0));
        assertEquals(Complex.zero, result);
    }

    @Test
    void divide_by_zero_special_case() {
        var c1 = new Complex(1.0, 1.0);
        var c2 = Complex.zero;
        var result = (Complex) c1.divide(c2);
        assertTrue(Double.isInfinite(result.real()) || Double.isInfinite(result.imaginary()));
    }

    @Test
    void inverse_simple() {
        var c = new Complex(3.0, 4.0);
        var inv = (Complex) c.inverse();
        // 1/(3 + 4i) = (3 - 4i)/(9 + 16) = (3 - 4i)/25 = 0.12 - 0.16i
        assertEquals(new Complex(0.12, -0.16), inv);
    }

    @Test
    void inverse_one() {
        var inv = (Complex) Complex.one.inverse();
        assertEquals(Complex.one, inv);
    }

    @Test
    void inverse_i() {
        var inv = (Complex) Complex.I.inverse();
        // 1/i = -i
        assertEquals(new Complex(0.0, -1.0), inv);
    }

    @Test
    void one() {
        var one = (Complex) new Complex(5.0, 7.0).one();
        assertEquals(Complex.one, one);
        assertEquals(new Complex(1.0, 0.0), one);
    }

    @Test
    void zero() {
        var zero = (Complex) new Complex(5.0, 7.0).zero();
        assertEquals(Complex.zero, zero);
        assertEquals(new Complex(0.0, 0.0), zero);
    }

    @Test
    void constants() {
        assertEquals(new Complex(0.0, 1.0), Complex.I);
        assertEquals(new Complex(1.0, 0.0), Complex.one);
        assertEquals(new Complex(0.0, 0.0), Complex.zero);
    }

    @Test
    void testEquals_same_object() {
        var c = new Complex(2.0, 3.0);
        assertEquals(c, c);
    }

    @Test
    void testEquals_equal_values() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(2.0, 3.0);
        assertEquals(c1, c2);
    }

    @Test
    void testEquals_different_real() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(2.1, 3.0);
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_different_imaginary() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(2.0, 3.1);
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_null() {
        var c = new Complex(2.0, 3.0);
        assertNotEquals(null, c);
    }

    @Test
    void testEquals_different_type() {
        var c = new Complex(2.0, 3.0);
        var str = "2.0 + 3.0i";
        assertNotEquals(str, c);
    }

    @Test
    void testEquals_special_values() {
        var c1 = new Complex(Double.NaN, 2.0);
        var c2 = new Complex(Double.NaN, 2.0);
        assertEquals(c1, c2); // NaN should equal NaN in this implementation

        var c3 = new Complex(Double.POSITIVE_INFINITY, 2.0);
        var c4 = new Complex(Double.POSITIVE_INFINITY, 2.0);
        assertEquals(c3, c4);

        var c5 = new Complex(0.0, -0.0);
        var c6 = new Complex(-0.0, 0.0);
        assertNotEquals(c5, c6); // Different sign zeros should not be equal
    }

    @Test
    void testHashCode_consistency() {
        var c = new Complex(2.0, 3.0);
        int hash1 = c.hashCode();
        int hash2 = c.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCode_equal_objects() {
        var c1 = new Complex(2.0, 3.0);
        var c2 = new Complex(2.0, 3.0);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString_zero() {
        assertEquals("0", Complex.zero.toString());
    }

    @Test
    void testToString_real_only() {
        var c = new Complex(5.0, 0.0);
        assertEquals("5.0", c.toString());
    }

    @Test
    void testToString_imaginary_only_positive() {
        var c = new Complex(0.0, 3.0);
        assertEquals("3.0i", c.toString());
    }

    @Test
    void testToString_imaginary_only_negative() {
        var c = new Complex(0.0, -3.0);
        assertEquals("-3.0i", c.toString());
    }

    @Test
    void testToString_both_positive() {
        var c = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", c.toString());
    }

    @Test
    void testToString_positive_real_negative_imaginary() {
        var c = new Complex(2.0, -3.0);
        assertEquals("2.0 - 3.0i", c.toString());
    }

    @Test
    void testToString_negative_real_positive_imaginary() {
        var c = new Complex(-2.0, 3.0);
        assertEquals("-2.0 + 3.0i", c.toString());
    }

    @Test
    void testToString_both_negative() {
        var c = new Complex(-2.0, -3.0);
        assertEquals("-2.0 - 3.0i", c.toString());
    }

    @Test
    void testToString_unit_imaginary() {
        var c = new Complex(0.0, 1.0);
        assertEquals("1.0i", c.toString());
    }

    @Test
    void testToString_negative_unit_imaginary() {
        var c = new Complex(0.0, -1.0);
        assertEquals("-1.0i", c.toString());
    }

    // Additional edge case tests for robustness
    @Test
    void operations_with_very_large_numbers() {
        var c1 = new Complex(1e100, 1e100);
        var c2 = new Complex(1e100, -1e100);

        var sum = (Complex) c1.add(c2);
        assertEquals(2e100, sum.real(), 1e95);
        assertEquals(0.0, sum.imaginary(), 1e95);

        var product = (Complex) c1.multiply(c2);
        assertNotNull(product);
    }

    @Test
    void operations_with_very_small_numbers() {
        var c1 = new Complex(1e-100, 1e-100);
        var c2 = new Complex(1e-100, -1e-100);

        var sum = (Complex) c1.add(c2);
        assertEquals(2e-100, sum.real(), 1e-105);
        assertEquals(0.0, sum.imaginary(), 1e-105);
    }
}