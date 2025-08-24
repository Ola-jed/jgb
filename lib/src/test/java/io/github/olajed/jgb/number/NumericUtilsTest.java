package io.github.olajed.jgb.number;

import io.github.olajed.jgb.enums.NumericType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericUtilsTest {
    @Test
    void tryAssign_galoisFieldElementToComplex() {
        var galois = new GaloisFieldElement(3, 7);
        var result = (Complex) NumericUtils.tryAssign(galois, NumericType.Complex, 0);
        assertEquals(new Complex(3.0, 0.0), result);
    }

    @Test
    void tryAssign_rationalToComplex() {
        var rational = new Rational(3, 4);
        var result = (Complex) NumericUtils.tryAssign(rational, NumericType.Complex, 0);
        assertEquals(new Complex(0.75, 0.0), result);
    }

    @Test
    void tryAssign_realToComplex() {
        var real = new Real(2.5);
        var result = (Complex) NumericUtils.tryAssign(real, NumericType.Complex, 0);
        assertEquals(new Complex(2.5, 0.0), result);
    }

    @Test
    void tryAssign_complexToComplex() {
        var complex = new Complex(2.0, 3.0);
        var result = (Complex) NumericUtils.tryAssign(complex, NumericType.Complex, 0);
        assertSame(complex, result);
    }

    @Test
    void tryAssign_galoisFieldElementToReal() {
        var galois = new GaloisFieldElement(5, 7);
        var result = (Real) NumericUtils.tryAssign(galois, NumericType.Real, 0);
        assertEquals(new Real(5.0), result);
    }

    @Test
    void tryAssign_rationalToReal() {
        var rational = new Rational(3, 4);
        var result = (Real) NumericUtils.tryAssign(rational, NumericType.Real, 0);
        assertEquals(new Real(0.75), result);
    }

    @Test
    void tryAssign_realToReal() {
        var real = new Real(2.5);
        var result = (Real) NumericUtils.tryAssign(real, NumericType.Real, 0);
        assertSame(real, result);
    }

    @Test
    void tryAssign_complexWithZeroImaginaryToReal() {
        var complex = new Complex(3.0, 0.0);
        var result = (Real) NumericUtils.tryAssign(complex, NumericType.Real, 0);
        assertEquals(new Real(3.0), result);
    }

    @Test
    void tryAssign_complexWithNonZeroImaginaryToReal_shouldThrow() {
        var complex = new Complex(3.0, 2.0);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.Real, 0));
    }

    @Test
    void tryAssign_galoisFieldElementToRational() {
        var galois = new GaloisFieldElement(4, 7);
        var result = (Rational) NumericUtils.tryAssign(galois, NumericType.Rational, 0);
        assertEquals(new Rational(4), result);
    }

    @Test
    void tryAssign_rationalToRational() {
        var rational = new Rational(3, 4);
        var result = (Rational) NumericUtils.tryAssign(rational, NumericType.Rational, 0);
        assertSame(rational, result);
    }

    @Test
    void tryAssign_realIntegerToRational() {
        var real = new Real(5.0);
        var result = (Rational) NumericUtils.tryAssign(real, NumericType.Rational, 0);
        assertEquals(new Rational(5), result);
    }

    @Test
    void tryAssign_realDecimalToRational_shouldThrow() {
        var real = new Real(5.5);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(real, NumericType.Rational, 0));
    }

    @Test
    void tryAssign_complexIntegerWithZeroImaginaryToRational() {
        var complex = new Complex(7.0, 0.0);
        var result = (Rational) NumericUtils.tryAssign(complex, NumericType.Rational, 0);
        assertEquals(new Rational(7), result);
    }

    @Test
    void tryAssign_complexDecimalWithZeroImaginaryToRational_shouldThrow() {
        var complex = new Complex(7.5, 0.0);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.Rational, 0));
    }

    @Test
    void tryAssign_complexWithNonZeroImaginaryToRational_shouldThrow() {
        var complex = new Complex(7.0, 1.0);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.Rational, 0));
    }

    @Test
    void tryAssign_galoisFieldElementToGaloisFieldElement() {
        var galois = new GaloisFieldElement(3, 7);
        var result = (GaloisFieldElement) NumericUtils.tryAssign(galois, NumericType.GaloisField, 11);
        assertEquals(new GaloisFieldElement(3, 7), result);
    }

    @Test
    void tryAssign_rationalIntegerToGaloisFieldElement() {
        var rational = new Rational(5);
        var result = (GaloisFieldElement) NumericUtils.tryAssign(rational, NumericType.GaloisField, 7);
        assertEquals(new GaloisFieldElement(5, 7), result);
    }

    @Test
    void tryAssign_rationalFractionToGaloisFieldElement_shouldThrow() {
        var rational = new Rational(3, 4);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(rational, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_realIntegerToGaloisFieldElement() {
        var real = new Real(4.0);
        var result = (GaloisFieldElement) NumericUtils.tryAssign(real, NumericType.GaloisField, 7);
        assertEquals(new GaloisFieldElement(4, 7), result);
    }

    @Test
    void tryAssign_realDecimalToGaloisFieldElement_shouldThrow() {
        var real = new Real(4.5);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(real, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_complexIntegerWithZeroImaginaryToGaloisFieldElement() {
        var complex = new Complex(6.0, 0.0);
        var result = (GaloisFieldElement) NumericUtils.tryAssign(complex, NumericType.GaloisField, 7);
        assertEquals(new GaloisFieldElement(6, 7), result);
    }

    @Test
    void tryAssign_complexDecimalWithZeroImaginaryToGaloisFieldElement_shouldThrow() {
        var complex = new Complex(6.5, 0.0);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_complexWithNonZeroImaginaryToGaloisFieldElement_shouldThrow() {
        var complex = new Complex(6.0, 1.0);
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_zeroValues() {
        var galoisZero = new GaloisFieldElement(0, 7);
        var rationalZero = new Rational(0);
        var realZero = new Real(0.0);
        var complexZero = new Complex(0.0, 0.0);

        assertEquals(new Complex(0.0, 0.0), NumericUtils.tryAssign(galoisZero, NumericType.Complex, 0));
        assertEquals(new Real(0.0), NumericUtils.tryAssign(galoisZero, NumericType.Real, 0));
        assertEquals(new Rational(0), NumericUtils.tryAssign(galoisZero, NumericType.Rational, 0));
        assertEquals(new GaloisFieldElement(0, 11), NumericUtils.tryAssign(galoisZero, NumericType.GaloisField, 11));

        assertEquals(new Complex(0.0, 0.0), NumericUtils.tryAssign(rationalZero, NumericType.Complex, 0));
        assertEquals(new Real(0.0), NumericUtils.tryAssign(rationalZero, NumericType.Real, 0));
        assertEquals(new GaloisFieldElement(0, 7), NumericUtils.tryAssign(rationalZero, NumericType.GaloisField, 7));

        assertEquals(new Complex(0.0, 0.0), NumericUtils.tryAssign(realZero, NumericType.Complex, 0));
        assertEquals(new Rational(0), NumericUtils.tryAssign(realZero, NumericType.Rational, 0));
        assertEquals(new GaloisFieldElement(0, 7), NumericUtils.tryAssign(realZero, NumericType.GaloisField, 7));

        assertEquals(new Real(0.0), NumericUtils.tryAssign(complexZero, NumericType.Real, 0));
        assertEquals(new Rational(0), NumericUtils.tryAssign(complexZero, NumericType.Rational, 0));
        assertEquals(new GaloisFieldElement(0, 7), NumericUtils.tryAssign(complexZero, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_negativeValues() {
        var realNegative = new Real(-3.0);
        var complexNegative = new Complex(-5.0, 0.0);
        var rationalNegative = new Rational(-2);

        assertEquals(new Complex(-3.0, 0.0), NumericUtils.tryAssign(realNegative, NumericType.Complex, 0));
        assertEquals(new Rational(-3), NumericUtils.tryAssign(realNegative, NumericType.Rational, 0));
        assertEquals(new GaloisFieldElement(-3, 7), NumericUtils.tryAssign(realNegative, NumericType.GaloisField, 7));

        assertEquals(new Real(-5.0), NumericUtils.tryAssign(complexNegative, NumericType.Real, 0));
        assertEquals(new Rational(-5), NumericUtils.tryAssign(complexNegative, NumericType.Rational, 0));
        assertEquals(new GaloisFieldElement(-5, 7), NumericUtils.tryAssign(complexNegative, NumericType.GaloisField, 7));

        assertEquals(new Complex(-2.0, 0.0), NumericUtils.tryAssign(rationalNegative, NumericType.Complex, 0));
        assertEquals(new Real(-2.0), NumericUtils.tryAssign(rationalNegative, NumericType.Real, 0));
        assertEquals(new GaloisFieldElement(-2, 7), NumericUtils.tryAssign(rationalNegative, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_largeIntegerValues() {
        var largInteger = 1000;
        var real = new Real(largInteger);
        var complex = new Complex(largInteger, 0.0);
        var rational = new Rational(largInteger);

        assertEquals(new Complex(largInteger, 0.0), NumericUtils.tryAssign(real, NumericType.Complex, 0));
        assertEquals(new Rational(largInteger), NumericUtils.tryAssign(real, NumericType.Rational, 0));

        assertEquals(new Real(largInteger), NumericUtils.tryAssign(complex, NumericType.Real, 0));
        assertEquals(new Rational(largInteger), NumericUtils.tryAssign(complex, NumericType.Rational, 0));

        assertEquals(new Complex(largInteger, 0.0), NumericUtils.tryAssign(rational, NumericType.Complex, 0));
        assertEquals(new Real(largInteger), NumericUtils.tryAssign(rational, NumericType.Real, 0));
    }

    @Test
    void tryAssign_fractionalValues() {
        var rationalFraction = new Rational(3, 4);
        var realFraction = new Real(0.75);

        assertEquals(new Complex(0.75, 0.0), NumericUtils.tryAssign(rationalFraction, NumericType.Complex, 0));
        assertEquals(new Real(0.75), NumericUtils.tryAssign(rationalFraction, NumericType.Real, 0));

        assertEquals(new Complex(0.75, 0.0), NumericUtils.tryAssign(realFraction, NumericType.Complex, 0));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(realFraction, NumericType.Rational, 0));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(realFraction, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_verySmallDecimals() {
        var realSmallDecimal = new Real(1e-10);
        var complexSmallDecimal = new Complex(1e-10, 0.0);

        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(realSmallDecimal, NumericType.Rational, 0));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(realSmallDecimal, NumericType.GaloisField, 7));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complexSmallDecimal, NumericType.Rational, 0));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complexSmallDecimal, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_moduloParameterUsage() {
        var rational = new Rational(10);

        var result7 = (GaloisFieldElement) NumericUtils.tryAssign(rational, NumericType.GaloisField, 7);
        var result11 = (GaloisFieldElement) NumericUtils.tryAssign(rational, NumericType.GaloisField, 11);

        assertEquals(7, result7.modulo());
        assertEquals(3, result7.get());

        assertEquals(11, result11.modulo());
        assertEquals(10, result11.get());
    }

    @Test
    void tryAssign_moduloParameterIgnored() {
        var rational = new Rational(5);

        var complex = (Complex) NumericUtils.tryAssign(rational, NumericType.Complex, 999);
        var real = (Real) NumericUtils.tryAssign(rational, NumericType.Real, 999);

        assertEquals(new Complex(5.0, 0.0), complex);
        assertEquals(new Real(5.0), real);
    }

    @Test
    void tryAssign_complexWithTinyImaginaryPart() {
        var complex = new Complex(5.0, 1e-15);

        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.Real, 0));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.Rational, 0));
        assertThrows(NumericException.class, () ->
                NumericUtils.tryAssign(complex, NumericType.GaloisField, 7));
    }

    @Test
    void tryAssign_preservesOriginalWhenSameType() {
        var real = new Real(5.0);
        var rational = new Rational(3, 4);
        var complex = new Complex(2.0, 3.0);

        assertSame(real, NumericUtils.tryAssign(real, NumericType.Real, 0));
        assertSame(rational, NumericUtils.tryAssign(rational, NumericType.Rational, 0));
        assertSame(complex, NumericUtils.tryAssign(complex, NumericType.Complex, 0));
    }
}