package io.github.olajed.jgb.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticUtilsTest {
    @Test
    void gcd_int_basicCases() {
        assertEquals(1, ArithmeticUtils.gcd(1, 1));
        assertEquals(2, ArithmeticUtils.gcd(2, 4));
        assertEquals(3, ArithmeticUtils.gcd(6, 9));
        assertEquals(5, ArithmeticUtils.gcd(10, 15));
        assertEquals(6, ArithmeticUtils.gcd(12, 18));
    }

    @Test
    void gcd_int_zeroInputs() {
        assertEquals(5, ArithmeticUtils.gcd(0, 5));
        assertEquals(7, ArithmeticUtils.gcd(7, 0));
        assertEquals(0, ArithmeticUtils.gcd(0, 0));
    }

    @Test
    void gcd_int_negativeInputs() {
        assertEquals(3, ArithmeticUtils.gcd(-6, 9));
        assertEquals(3, ArithmeticUtils.gcd(6, -9));
        assertEquals(3, ArithmeticUtils.gcd(-6, -9));
        assertEquals(5, ArithmeticUtils.gcd(-10, 15));
        assertEquals(5, ArithmeticUtils.gcd(10, -15));
        assertEquals(5, ArithmeticUtils.gcd(-10, -15));
    }

    @Test
    void gcd_int_coprimeNumbers() {
        assertEquals(1, ArithmeticUtils.gcd(7, 11));
        assertEquals(1, ArithmeticUtils.gcd(9, 16));
        assertEquals(1, ArithmeticUtils.gcd(13, 17));
        assertEquals(1, ArithmeticUtils.gcd(25, 49));
    }

    @Test
    void gcd_int_sameNumbers() {
        assertEquals(5, ArithmeticUtils.gcd(5, 5));
        assertEquals(12, ArithmeticUtils.gcd(12, 12));
        assertEquals(100, ArithmeticUtils.gcd(100, 100));
    }

    @Test
    void gcd_int_oneIsMultipleOfOther() {
        assertEquals(3, ArithmeticUtils.gcd(3, 15));
        assertEquals(7, ArithmeticUtils.gcd(7, 35));
        assertEquals(4, ArithmeticUtils.gcd(4, 20));
        assertEquals(6, ArithmeticUtils.gcd(18, 6));
    }

    @Test
    void gcd_int_largeNumbers() {
        assertEquals(21, ArithmeticUtils.gcd(1071, 462));
        assertEquals(21, ArithmeticUtils.gcd(252, 105));
        assertEquals(6, ArithmeticUtils.gcd(1998, 1716));
    }

    @Test
    void gcd_int_powersOfTwo() {
        assertEquals(8, ArithmeticUtils.gcd(16, 24));
        assertEquals(16, ArithmeticUtils.gcd(48, 64));
        assertEquals(4, ArithmeticUtils.gcd(12, 8));
        assertEquals(32, ArithmeticUtils.gcd(96, 160));
    }

    @Test
    void gcd_int_overflow() {
        assertThrows(ArithmeticException.class, () -> ArithmeticUtils.gcd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    void gcd_long_basicCases() {
        assertEquals(1L, ArithmeticUtils.gcd(1L, 1L));
        assertEquals(2L, ArithmeticUtils.gcd(2L, 4L));
        assertEquals(3L, ArithmeticUtils.gcd(6L, 9L));
        assertEquals(5L, ArithmeticUtils.gcd(10L, 15L));
        assertEquals(6L, ArithmeticUtils.gcd(12L, 18L));
    }

    @Test
    void gcd_long_zeroInputs() {
        assertEquals(5L, ArithmeticUtils.gcd(0L, 5L));
        assertEquals(7L, ArithmeticUtils.gcd(7L, 0L));
        assertEquals(0L, ArithmeticUtils.gcd(0L, 0L));
    }

    @Test
    void gcd_long_negativeInputs() {
        assertEquals(3L, ArithmeticUtils.gcd(-6L, 9L));
        assertEquals(3L, ArithmeticUtils.gcd(6L, -9L));
        assertEquals(3L, ArithmeticUtils.gcd(-6L, -9L));
        assertEquals(5L, ArithmeticUtils.gcd(-10L, 15L));
        assertEquals(5L, ArithmeticUtils.gcd(10L, -15L));
        assertEquals(5L, ArithmeticUtils.gcd(-10L, -15L));
    }

    @Test
    void gcd_long_coprimeNumbers() {
        assertEquals(1L, ArithmeticUtils.gcd(7L, 11L));
        assertEquals(1L, ArithmeticUtils.gcd(9L, 16L));
        assertEquals(1L, ArithmeticUtils.gcd(13L, 17L));
        assertEquals(1L, ArithmeticUtils.gcd(25L, 49L));
    }

    @Test
    void gcd_long_largeNumbers() {
        assertEquals(21L, ArithmeticUtils.gcd(1071L, 462L));
        assertEquals(21L, ArithmeticUtils.gcd(252L, 105L));
        assertEquals(6L, ArithmeticUtils.gcd(1998L, 1716L));
    }

    @Test
    void gcd_long_veryLargeNumbers() {
        assertEquals(6000L, ArithmeticUtils.gcd(123456000L, 987654000L));
        assertEquals(1L, ArithmeticUtils.gcd(1000000007L, 1000000009L));
    }

    @Test
    void gcd_long_powersOfTwo() {
        assertEquals(8L, ArithmeticUtils.gcd(16L, 24L));
        assertEquals(16L, ArithmeticUtils.gcd(48L, 64L));
        assertEquals(4L, ArithmeticUtils.gcd(12L, 8L));
        assertEquals(32L, ArithmeticUtils.gcd(96L, 160L));
    }

    @Test
    void gcd_long_overflow() {
        assertThrows(ArithmeticException.class, () ->
                ArithmeticUtils.gcd(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    @Test
    void isPrime_smallNumbers() {
        assertFalse(ArithmeticUtils.isPrime(-5));
        assertFalse(ArithmeticUtils.isPrime(-1));
        assertFalse(ArithmeticUtils.isPrime(0));
        assertFalse(ArithmeticUtils.isPrime(1));
        assertTrue(ArithmeticUtils.isPrime(2));
        assertTrue(ArithmeticUtils.isPrime(3));
        assertFalse(ArithmeticUtils.isPrime(4));
        assertTrue(ArithmeticUtils.isPrime(5));
        assertFalse(ArithmeticUtils.isPrime(6));
        assertTrue(ArithmeticUtils.isPrime(7));
        assertFalse(ArithmeticUtils.isPrime(8));
        assertFalse(ArithmeticUtils.isPrime(9));
        assertFalse(ArithmeticUtils.isPrime(10));
        assertTrue(ArithmeticUtils.isPrime(11));
    }

    @Test
    void isPrime_firstTwentyPrimes() {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71};
        for (int prime : primes) {
            assertTrue(ArithmeticUtils.isPrime(prime), prime + " should be prime");
        }
    }

    @Test
    void isPrime_compositeNumbers() {
        int[] composites = {4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28};
        for (int composite : composites) {
            assertFalse(ArithmeticUtils.isPrime(composite), composite + " should not be prime");
        }
    }

    @Test
    void isPrime_evenNumbers() {
        for (int i = 4; i <= 100; i += 2) {
            assertFalse(ArithmeticUtils.isPrime(i), i + " should not be prime");
        }
    }

    @Test
    void isPrime_multiplesOfThree() {
        for (int i = 9; i <= 99; i += 3) {
            assertFalse(ArithmeticUtils.isPrime(i), i + " should not be prime");
        }
    }

    @Test
    void isPrime_perfectSquares() {
        int[] squares = {4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225};
        for (int square : squares) {
            assertFalse(ArithmeticUtils.isPrime(square), square + " should not be prime");
        }
    }

    @Test
    void isPrime_largePrimes() {
        assertTrue(ArithmeticUtils.isPrime(97));
        assertTrue(ArithmeticUtils.isPrime(101));
        assertTrue(ArithmeticUtils.isPrime(103));
        assertTrue(ArithmeticUtils.isPrime(107));
        assertTrue(ArithmeticUtils.isPrime(109));
        assertTrue(ArithmeticUtils.isPrime(113));
        assertTrue(ArithmeticUtils.isPrime(127));
        assertTrue(ArithmeticUtils.isPrime(131));
        assertTrue(ArithmeticUtils.isPrime(137));
        assertTrue(ArithmeticUtils.isPrime(139));
    }

    @Test
    void isPrime_largeComposites() {
        assertFalse(ArithmeticUtils.isPrime(91));
        assertFalse(ArithmeticUtils.isPrime(93));
        assertFalse(ArithmeticUtils.isPrime(95));
        assertFalse(ArithmeticUtils.isPrime(99));
        assertFalse(ArithmeticUtils.isPrime(111));
        assertFalse(ArithmeticUtils.isPrime(119));
        assertFalse(ArithmeticUtils.isPrime(121));
        assertFalse(ArithmeticUtils.isPrime(133));
    }

    @Test
    void isPrime_edgeCases() {
        assertFalse(ArithmeticUtils.isPrime(50));
        assertFalse(ArithmeticUtils.isPrime(65));
        assertFalse(ArithmeticUtils.isPrime(85));

        assertFalse(ArithmeticUtils.isPrime(77));
        assertFalse(ArithmeticUtils.isPrime(143));
        assertFalse(ArithmeticUtils.isPrime(187));
    }

    @Test
    void isPrime_sixKPlusMinusOnePattern() {
        assertTrue(ArithmeticUtils.isPrime(5));
        assertTrue(ArithmeticUtils.isPrime(11));
        assertTrue(ArithmeticUtils.isPrime(17));
        assertTrue(ArithmeticUtils.isPrime(23));
        assertTrue(ArithmeticUtils.isPrime(29));

        assertTrue(ArithmeticUtils.isPrime(7));
        assertTrue(ArithmeticUtils.isPrime(13));
        assertTrue(ArithmeticUtils.isPrime(19));
        assertTrue(ArithmeticUtils.isPrime(31));  // 6*5+1
        assertTrue(ArithmeticUtils.isPrime(37));  // 6*6+1

        assertFalse(ArithmeticUtils.isPrime(35)); // 6*6-1 = 5*7
        assertFalse(ArithmeticUtils.isPrime(65)); // 6*11-1 = 5*13

        assertFalse(ArithmeticUtils.isPrime(25)); // 6*4+1 = 5*5
        assertFalse(ArithmeticUtils.isPrime(49)); // 6*8+1 = 7*7
    }

    @Test
    void isPrime_boundaryConditions() {
        assertTrue(ArithmeticUtils.isPrime(121 + 6)); // 127, where sqrt(127) ≈ 11.3
        assertFalse(ArithmeticUtils.isPrime(121)); // 11^2

        assertTrue(ArithmeticUtils.isPrime(169 + 4)); // 173, where sqrt(173) ≈ 13.2
        assertFalse(ArithmeticUtils.isPrime(169)); // 13^2
    }

    @Test
    void isPrime_mediumSizePrimes() {
        assertTrue(ArithmeticUtils.isPrime(149));
        assertTrue(ArithmeticUtils.isPrime(151));
        assertTrue(ArithmeticUtils.isPrime(157));
        assertTrue(ArithmeticUtils.isPrime(163));
        assertTrue(ArithmeticUtils.isPrime(167));
        assertTrue(ArithmeticUtils.isPrime(173));
        assertTrue(ArithmeticUtils.isPrime(179));
        assertTrue(ArithmeticUtils.isPrime(181));
        assertTrue(ArithmeticUtils.isPrime(191));
        assertTrue(ArithmeticUtils.isPrime(193));
        assertTrue(ArithmeticUtils.isPrime(197));
        assertTrue(ArithmeticUtils.isPrime(199));
    }

    @Test
    void isPrime_performance() {
        assertTrue(ArithmeticUtils.isPrime(1009));
        assertTrue(ArithmeticUtils.isPrime(1013));
        assertTrue(ArithmeticUtils.isPrime(1019));
        assertTrue(ArithmeticUtils.isPrime(1021));

        assertFalse(ArithmeticUtils.isPrime(1001)); // 7 * 11 * 13
        assertFalse(ArithmeticUtils.isPrime(1003)); // 17 * 59
        assertFalse(ArithmeticUtils.isPrime(1007)); // 19 * 53
        assertFalse(ArithmeticUtils.isPrime(1011)); // 3 * 337
    }

    @Test
    void gcd_int_symmetry() {
        assertEquals(ArithmeticUtils.gcd(12, 18), ArithmeticUtils.gcd(18, 12));
        assertEquals(ArithmeticUtils.gcd(15, 25), ArithmeticUtils.gcd(25, 15));
        assertEquals(ArithmeticUtils.gcd(7, 11), ArithmeticUtils.gcd(11, 7));
    }

    @Test
    void gcd_long_symmetry() {
        assertEquals(ArithmeticUtils.gcd(12L, 18L), ArithmeticUtils.gcd(18L, 12L));
        assertEquals(ArithmeticUtils.gcd(15L, 25L), ArithmeticUtils.gcd(25L, 15L));
        assertEquals(ArithmeticUtils.gcd(7L, 11L), ArithmeticUtils.gcd(11L, 7L));
    }

    @Test
    void gcd_int_associativity() {
        int a = 12, b = 18, c = 24;
        assertEquals(ArithmeticUtils.gcd(ArithmeticUtils.gcd(a, b), c),
                ArithmeticUtils.gcd(a, ArithmeticUtils.gcd(b, c)));
    }

    @Test
    void gcd_long_associativity() {
        long a = 12L, b = 18L, c = 24L;
        assertEquals(ArithmeticUtils.gcd(ArithmeticUtils.gcd(a, b), c),
                ArithmeticUtils.gcd(a, ArithmeticUtils.gcd(b, c)));
    }

    @Test
    void gcd_int_extremeValues() {
        assertEquals(1, ArithmeticUtils.gcd(Integer.MAX_VALUE, Integer.MAX_VALUE - 1));
        assertEquals(Integer.MAX_VALUE, ArithmeticUtils.gcd(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void gcd_long_extremeValues() {
        assertEquals(1L, ArithmeticUtils.gcd(Long.MAX_VALUE, Long.MAX_VALUE - 1L));
        assertEquals(Long.MAX_VALUE, ArithmeticUtils.gcd(Long.MAX_VALUE, Long.MAX_VALUE));
    }
}