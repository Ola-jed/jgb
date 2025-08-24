package io.github.olajed.jgb.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GaloisFieldElementTest {
    @Test
    void constructor_validPrime() {
        var element = new GaloisFieldElement(3, 7);
        assertEquals(3, element.get());
        assertEquals(7, element.modulo());
    }

    @Test
    void constructor_nonPrimeModulus_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new GaloisFieldElement(1, 4));
        assertThrows(IllegalArgumentException.class, () -> new GaloisFieldElement(1, 6));
        assertThrows(IllegalArgumentException.class, () -> new GaloisFieldElement(1, 8));
        assertThrows(IllegalArgumentException.class, () -> new GaloisFieldElement(1, 9));
        assertThrows(IllegalArgumentException.class, () -> new GaloisFieldElement(1, 10));
    }

    @Test
    void constructor_elementReduction() {
        var element = new GaloisFieldElement(10, 7);
        assertEquals(3, element.get());
        assertEquals(7, element.modulo());
    }

    @Test
    void constructor_negativeElementReduction() {
        var element = new GaloisFieldElement(-3, 7);
        assertEquals(4, element.get());
        assertEquals(7, element.modulo());
    }

    @Test
    void constructor_elementEqualToModulus() {
        var element = new GaloisFieldElement(7, 7);
        assertEquals(0, element.get());
    }

    @Test
    void constructor_smallPrimes() {
        assertDoesNotThrow(() -> new GaloisFieldElement(1, 2));
        assertDoesNotThrow(() -> new GaloisFieldElement(2, 3));
        assertDoesNotThrow(() -> new GaloisFieldElement(4, 5));
        assertDoesNotThrow(() -> new GaloisFieldElement(6, 7));
        assertDoesNotThrow(() -> new GaloisFieldElement(10, 11));
    }

    @Test
    void get() {
        var element = new GaloisFieldElement(5, 7);
        assertEquals(5, element.get());
    }

    @Test
    void modulo() {
        var element = new GaloisFieldElement(3, 11);
        assertEquals(11, element.modulo());
    }

    @Test
    void add_sameField() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(4, 7);
        var result = (GaloisFieldElement) e1.add(e2);
        assertEquals(0, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void add_withModuloReduction() {
        var e1 = new GaloisFieldElement(5, 7);
        var e2 = new GaloisFieldElement(6, 7);
        var result = (GaloisFieldElement) e1.add(e2);
        assertEquals(4, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void add_withZero() {
        var e1 = new GaloisFieldElement(5, 7);
        var zero = new GaloisFieldElement(0, 7);
        var result = (GaloisFieldElement) e1.add(zero);
        assertEquals(5, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void add_differentFields_shouldThrow() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(3, 11);
        assertThrows(IllegalArgumentException.class, () -> e1.add(e2));
    }

    @Test
    void add_incompatibleType_shouldThrow() {
        var element = new GaloisFieldElement(3, 7);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> element.add(real));
    }

    @Test
    void subtract_sameField() {
        var e1 = new GaloisFieldElement(5, 7);
        var e2 = new GaloisFieldElement(3, 7);
        var result = (GaloisFieldElement) e1.subtract(e2);
        assertEquals(2, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void subtract_withModuloReduction() {
        var e1 = new GaloisFieldElement(2, 7);
        var e2 = new GaloisFieldElement(5, 7);
        var result = (GaloisFieldElement) e1.subtract(e2);
        assertEquals(4, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void subtract_fromZero() {
        var zero = new GaloisFieldElement(0, 7);
        var e2 = new GaloisFieldElement(3, 7);
        var result = (GaloisFieldElement) zero.subtract(e2);
        assertEquals(4, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void subtract_differentFields_shouldThrow() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(3, 11);
        assertThrows(IllegalArgumentException.class, () -> e1.subtract(e2));
    }

    @Test
    void subtract_incompatibleType_shouldThrow() {
        var element = new GaloisFieldElement(3, 7);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> element.subtract(real));
    }

    @Test
    void multiply_sameField() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(4, 7);
        var result = (GaloisFieldElement) e1.multiply(e2);
        assertEquals(5, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void multiply_withModuloReduction() {
        var e1 = new GaloisFieldElement(5, 7);
        var e2 = new GaloisFieldElement(6, 7);
        var result = (GaloisFieldElement) e1.multiply(e2);
        assertEquals(2, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void multiply_withZero() {
        var e1 = new GaloisFieldElement(5, 7);
        var zero = new GaloisFieldElement(0, 7);
        var result = (GaloisFieldElement) e1.multiply(zero);
        assertEquals(0, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void multiply_withOne() {
        var e1 = new GaloisFieldElement(5, 7);
        var one = new GaloisFieldElement(1, 7);
        var result = (GaloisFieldElement) e1.multiply(one);
        assertEquals(5, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void multiply_differentFields_shouldThrow() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(3, 11);
        assertThrows(IllegalArgumentException.class, () -> e1.multiply(e2));
    }

    @Test
    void multiply_incompatibleType_shouldThrow() {
        var element = new GaloisFieldElement(3, 7);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> element.multiply(real));
    }

    @Test
    void divide_sameField() {
        var e1 = new GaloisFieldElement(6, 7);
        var e2 = new GaloisFieldElement(2, 7);
        var result = (GaloisFieldElement) e1.divide(e2);
        assertEquals(3, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void divide_byOne() {
        var e1 = new GaloisFieldElement(5, 7);
        var one = new GaloisFieldElement(1, 7);
        var result = (GaloisFieldElement) e1.divide(one);
        assertEquals(5, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void divide_oneByElement() {
        var one = new GaloisFieldElement(1, 7);
        var e2 = new GaloisFieldElement(3, 7);
        var result = (GaloisFieldElement) one.divide(e2);
        assertEquals(5, result.get());
        assertEquals(7, result.modulo());
    }

    @Test
    void divide_differentFields_shouldThrow() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(3, 11);
        assertThrows(IllegalArgumentException.class, () -> e1.divide(e2));
    }

    @Test
    void divide_incompatibleType_shouldThrow() {
        var element = new GaloisFieldElement(3, 7);
        var real = new Real(3.0);
        assertThrows(IllegalArgumentException.class, () -> element.divide(real));
    }

    @Test
    void negate_nonZero() {
        var element = new GaloisFieldElement(3, 7);
        var negated = (GaloisFieldElement) element.negate();
        assertEquals(4, negated.get());
        assertEquals(7, negated.modulo());

        var sum = (GaloisFieldElement) element.add(negated);
        assertEquals(0, sum.get());
    }

    @Test
    void negate_zero() {
        var zero = new GaloisFieldElement(0, 7);
        var negated = (GaloisFieldElement) zero.negate();
        assertEquals(0, negated.get());
        assertEquals(7, negated.modulo());
    }

    @Test
    void inverse_nonZero() {
        var element = new GaloisFieldElement(3, 7);
        var inverse = (GaloisFieldElement) element.inverse();
        assertEquals(5, inverse.get());
        assertEquals(7, inverse.modulo());

        var product = (GaloisFieldElement) element.multiply(inverse);
        assertEquals(1, product.get());
    }

    @Test
    void inverse_one() {
        var one = new GaloisFieldElement(1, 7);
        var inverse = (GaloisFieldElement) one.inverse();
        assertEquals(1, inverse.get());
        assertEquals(7, inverse.modulo());
    }

    @Test
    void inverse_allNonZeroElements() {
        for (int i = 1; i < 7; i++) {
            var element = new GaloisFieldElement(i, 7);
            var inverse = (GaloisFieldElement) element.inverse();
            var product = (GaloisFieldElement) element.multiply(inverse);
            assertEquals(1, product.get(), "Inverse of " + i + " in GF(7) is incorrect");
        }
    }

    @Test
    void one() {
        var element = new GaloisFieldElement(5, 7);
        var one = (GaloisFieldElement) element.one();
        assertEquals(1, one.get());
        assertEquals(7, one.modulo());
    }

    @Test
    void zero() {
        var element = new GaloisFieldElement(5, 7);
        var zero = (GaloisFieldElement) element.zero();
        assertEquals(0, zero.get());
        assertEquals(7, zero.modulo());
    }

    @Test
    void testEquals_same_object() {
        var element = new GaloisFieldElement(3, 7);
        assertEquals(element, element);
    }

    @Test
    void testEquals_equal_values() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(3, 7);
        assertEquals(e1, e2);
    }

    @Test
    void testEquals_different_element() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(4, 7);
        assertNotEquals(e1, e2);
    }

    @Test
    void testEquals_different_modulus() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(3, 11);
        assertNotEquals(e1, e2);
    }

    @Test
    void testEquals_equivalent_representation() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(10, 7);
        assertEquals(e1, e2);
    }

    @Test
    void testEquals_null() {
        var element = new GaloisFieldElement(3, 7);
        assertNotEquals(null, element);
    }

    @Test
    void testEquals_different_type() {
        var element = new GaloisFieldElement(3, 7);
        var str = "3";
        assertNotEquals(element, str);
    }

    @Test
    void testHashCode_consistency() {
        var element = new GaloisFieldElement(3, 7);
        int hash1 = element.hashCode();
        int hash2 = element.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testHashCode_equal_objects() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(10, 7);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testToString() {
        var element = new GaloisFieldElement(3, 7);
        assertEquals("3", element.toString());

        var zero = new GaloisFieldElement(0, 7);
        assertEquals("0", zero.toString());

        var reduced = new GaloisFieldElement(10, 7);
        assertEquals("3", reduced.toString());
    }

    @Test
    void galoisField_additive_identity() {
        var element = new GaloisFieldElement(5, 7);
        var zero = (GaloisFieldElement) element.zero();

        var result1 = (GaloisFieldElement) element.add(zero);
        var result2 = (GaloisFieldElement) zero.add(element);

        assertEquals(element, result1);
        assertEquals(element, result2);
    }

    @Test
    void galoisField_multiplicative_identity() {
        var element = new GaloisFieldElement(5, 7);
        var one = (GaloisFieldElement) element.one();

        var result1 = (GaloisFieldElement) element.multiply(one);
        var result2 = (GaloisFieldElement) one.multiply(element);

        assertEquals(element, result1);
        assertEquals(element, result2);
    }

    @Test
    void galoisField_additive_inverse() {
        var element = new GaloisFieldElement(5, 7);
        var negated = (GaloisFieldElement) element.negate();
        var zero = (GaloisFieldElement) element.zero();

        var result = (GaloisFieldElement) element.add(negated);
        assertEquals(zero, result);
    }

    @Test
    void galoisField_multiplicative_inverse() {
        for (int i = 1; i < 7; i++) {
            var element = new GaloisFieldElement(i, 7);
            var inverse = (GaloisFieldElement) element.inverse();
            var one = (GaloisFieldElement) element.one();

            var result = (GaloisFieldElement) element.multiply(inverse);
            assertEquals(one, result);
        }
    }

    @Test
    void galoisField_commutativity() {
        var e1 = new GaloisFieldElement(3, 7);
        var e2 = new GaloisFieldElement(5, 7);

        var addResult1 = (GaloisFieldElement) e1.add(e2);
        var addResult2 = (GaloisFieldElement) e2.add(e1);
        assertEquals(addResult1, addResult2);

        var mulResult1 = (GaloisFieldElement) e1.multiply(e2);
        var mulResult2 = (GaloisFieldElement) e2.multiply(e1);
        assertEquals(mulResult1, mulResult2);
    }

    @Test
    void galoisField_associativity() {
        var e1 = new GaloisFieldElement(2, 7);
        var e2 = new GaloisFieldElement(3, 7);
        var e3 = new GaloisFieldElement(5, 7);

        var addResult1 = (GaloisFieldElement) e1.add(e2).add(e3);
        var addResult2 = (GaloisFieldElement) e1.add(e2.add(e3));
        assertEquals(addResult1, addResult2);

        var mulResult1 = (GaloisFieldElement) e1.multiply(e2).multiply(e3);
        var mulResult2 = (GaloisFieldElement) e1.multiply(e2.multiply(e3));
        assertEquals(mulResult1, mulResult2);
    }

    @Test
    void galoisField_distributivity() {
        var a = new GaloisFieldElement(2, 7);
        var b = new GaloisFieldElement(3, 7);
        var c = new GaloisFieldElement(5, 7);

        var left = (GaloisFieldElement) a.multiply(b.add(c));
        var right = (GaloisFieldElement) a.multiply(b).add(a.multiply(c));
        assertEquals(left, right);
    }

    @Test
    void galoisField_different_primes() {
        var gf3 = new GaloisFieldElement(2, 3);
        var gf5 = new GaloisFieldElement(3, 5);
        var gf11 = new GaloisFieldElement(7, 11);

        assertEquals(2, gf3.get());
        assertEquals(3, gf3.modulo());

        assertEquals(3, gf5.get());
        assertEquals(5, gf5.modulo());

        assertEquals(7, gf11.get());
        assertEquals(11, gf11.modulo());
    }

    @Test
    void galoisField_large_prime() {
        var element = new GaloisFieldElement(50, 97);
        assertEquals(50, element.get());
        assertEquals(97, element.modulo());

        var doubled = (GaloisFieldElement) element.add(element);
        assertEquals(3, doubled.get());
    }
}