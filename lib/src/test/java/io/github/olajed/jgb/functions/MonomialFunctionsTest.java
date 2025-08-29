package io.github.olajed.jgb.functions;

import io.github.olajed.jgb.number.Rational;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import io.github.olajed.jgb.structures.SparseMonomial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MonomialFunctionsTest {
    private Rational testCoefficient;
    private static final Rational ONE = new Rational(1);

    @BeforeEach
    void setUp() {
        testCoefficient = new Rational(5, 2);
    }


    @Test
    void shouldCreateSparseMonomialWhenModelIsSparse() {
        int[] exponents = {2, 0, 3, 1};
        SparseMonomial<Rational> model = new SparseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.one(model);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        for (int i = 0; i < result.fieldSize(); i++) {
            assertEquals(0, result.getExponent(i), "Exponent at position " + i + " should be 0");
        }
    }

    @Test
    void shouldCreateDenseMonomialWhenModelIsDense() {
        int[] exponents = {1, 4, 2, 0};
        DenseMonomial<Rational> model = new DenseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.one(model);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        for (int i = 0; i < result.fieldSize(); i++) {
            assertEquals(0, result.getExponent(i), "Exponent at position " + i + " should be 0");
        }
    }

    @Test
    void shouldHandleEmptyFieldSize() {
        int[] exponents = {};
        SparseMonomial<Rational> model = new SparseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.one(model);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(0, result.fieldSize());
        assertEquals(ONE, result.coefficient());
    }

    @Test
    void shouldHandleSingleVariable() {
        int[] exponents = {5};
        DenseMonomial<Rational> model = new DenseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.one(model);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(1, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(0, result.getExponent(0));
    }

    @Test
    void shouldWorkWithDifferentCoefficients() {
        Rational negativeCoeff = new Rational(-7, 3);
        int[] exponents = {2, 1};
        SparseMonomial<Rational> model = new SparseMonomial<>(exponents, negativeCoeff);
        Monomial<Rational> result = MonomialFunctions.one(model);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(2, result.fieldSize());
        assertEquals(ONE, result.coefficient());
    }

    @Test
    void shouldHandleLargeFieldSizes() {
        int[] exponents = new int[1000];
        exponents[500] = 7;
        DenseMonomial<Rational> model = new DenseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.one(model);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(1000, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        for (int i = 0; i < result.fieldSize(); i++) {
            assertEquals(0, result.getExponent(i));
        }
    }

    @Test
    void shouldComputeLcmOfTwoSparseMonomials() {
        int[] xExponents = {3, 0, 2, 0};
        int[] yExponents = {0, 4, 0, 1};
        SparseMonomial<Rational> x = new SparseMonomial<>(xExponents, testCoefficient);
        SparseMonomial<Rational> y = new SparseMonomial<>(yExponents, new Rational(3, 4));
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(3, result.getExponent(0));
        assertEquals(4, result.getExponent(1));
        assertEquals(2, result.getExponent(2));
        assertEquals(1, result.getExponent(3));
    }

    @Test
    void shouldComputeLcmWithOverlappingPositions() {
        int[] xExponents = {3, 0, 5, 0};
        int[] yExponents = {1, 0, 7, 0};
        SparseMonomial<Rational> x = new SparseMonomial<>(xExponents, testCoefficient);
        SparseMonomial<Rational> y = new SparseMonomial<>(yExponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(3, result.getExponent(0));
        assertEquals(0, result.getExponent(1));
        assertEquals(7, result.getExponent(2));
        assertEquals(0, result.getExponent(3));
    }

    @Test
    void shouldComputeLcmSparseAndDense() {
        int[] sparseExponents = {2, 0, 3};
        int[] denseExponents = {1, 4, 2};
        SparseMonomial<Rational> sparse = new SparseMonomial<>(sparseExponents, testCoefficient);
        DenseMonomial<Rational> dense = new DenseMonomial<>(denseExponents, new Rational(7));
        Monomial<Rational> result = MonomialFunctions.lcm(sparse, dense);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(3, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(2, result.getExponent(0));
        assertEquals(4, result.getExponent(1));
        assertEquals(3, result.getExponent(2));
    }

    @Test
    void shouldComputeLcmDenseAndSparse() {
        int[] denseExponents = {5, 2, 0};
        int[] sparseExponents = {3, 7, 1};
        DenseMonomial<Rational> dense = new DenseMonomial<>(denseExponents, new Rational(-2, 3));
        SparseMonomial<Rational> sparse = new SparseMonomial<>(sparseExponents, new Rational(4, 5));
        Monomial<Rational> result = MonomialFunctions.lcm(dense, sparse);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(3, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(5, result.getExponent(0));
        assertEquals(7, result.getExponent(1));
        assertEquals(1, result.getExponent(2));
    }

    @Test
    void shouldComputeLcmOfTwoDenseMonomials() {
        int[] xExponents = {5, 2, 0, 3};
        int[] yExponents = {3, 7, 1, 2};
        DenseMonomial<Rational> x = new DenseMonomial<>(xExponents, testCoefficient);
        DenseMonomial<Rational> y = new DenseMonomial<>(yExponents, new Rational(1, 3));
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(5, result.getExponent(0));
        assertEquals(7, result.getExponent(1));
        assertEquals(1, result.getExponent(2));
        assertEquals(3, result.getExponent(3));
    }

    @Test
    void shouldHandleLcmWithAllZeroExponents() {
        int[] xExponents = {0, 0, 0};
        int[] yExponents = {0, 0, 0};
        SparseMonomial<Rational> x = new SparseMonomial<>(xExponents, testCoefficient);
        SparseMonomial<Rational> y = new SparseMonomial<>(yExponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(3, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        for (int i = 0; i < result.fieldSize(); i++) {
            assertEquals(0, result.getExponent(i));
        }
    }

    @Test
    void shouldHandleLcmWithIdenticalMonomials() {
        int[] exponents = {3, 0, 5, 2};
        DenseMonomial<Rational> monomial = new DenseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.lcm(monomial, monomial);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(3, result.getExponent(0));
        assertEquals(0, result.getExponent(1));
        assertEquals(5, result.getExponent(2));
        assertEquals(2, result.getExponent(3));
    }

    @Test
    void shouldHandleEmptyFieldSizeInLcm() {
        int[] exponents = {};
        SparseMonomial<Rational> x = new SparseMonomial<>(exponents, testCoefficient);
        DenseMonomial<Rational> y = new DenseMonomial<>(exponents, testCoefficient);
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(0, result.fieldSize());
        assertEquals(ONE, result.coefficient());
    }

    @Test
    void shouldHandleLargeExponents() {
        int[] xExponents = {Integer.MAX_VALUE - 1, 1000000};
        int[] yExponents = {100, Integer.MAX_VALUE};
        DenseMonomial<Rational> x = new DenseMonomial<>(xExponents, testCoefficient);
        DenseMonomial<Rational> y = new DenseMonomial<>(yExponents, new Rational(10));
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(2, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(Integer.MAX_VALUE - 1, result.getExponent(0));
        assertEquals(Integer.MAX_VALUE, result.getExponent(1));
    }

    @Test
    void shouldHandleVerySparseMonomial() {
        int[] xExponents = {5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] yExponents = {0, 0, 0, 0, 0, 0, 0, 0, 0, 3};
        SparseMonomial<Rational> x = new SparseMonomial<>(xExponents, testCoefficient);
        SparseMonomial<Rational> y = new SparseMonomial<>(yExponents, new Rational(8, 9));
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(10, result.fieldSize());
        assertEquals(5, result.getExponent(0));
        assertEquals(3, result.getExponent(9));
        for (int i = 1; i < 9; i++) {
            assertEquals(0, result.getExponent(i));
        }

    }


    @Test
    void shouldMaintainSparseTypeConsistency() {
        int[] exponents1 = {1, 0};
        int[] exponents2 = {0, 2};
        SparseMonomial<Rational> sparse1 = new SparseMonomial<>(exponents1, testCoefficient);
        SparseMonomial<Rational> sparse2 = new SparseMonomial<>(exponents2, new Rational(3, 7));
        Monomial<Rational> result = MonomialFunctions.lcm(sparse1, sparse2);
        assertInstanceOf(SparseMonomial.class, result,
                "LCM of two SparseMonomial instances should return SparseMonomial");
    }

    @Test
    void shouldUseDenseForMixedTypes() {
        int[] exponents = {1, 2, 3};
        SparseMonomial<Rational> sparse = new SparseMonomial<>(exponents, testCoefficient);
        DenseMonomial<Rational> dense = new DenseMonomial<>(exponents, new Rational(2, 5));
        Monomial<Rational> result1 = MonomialFunctions.lcm(sparse, dense);
        Monomial<Rational> result2 = MonomialFunctions.lcm(dense, sparse);
        assertInstanceOf(DenseMonomial.class, result1,
                "LCM of mixed types should return DenseMonomial");
        assertInstanceOf(DenseMonomial.class, result2,
                "LCM of mixed types should return DenseMonomial (order independent)");
    }

    @Test
    void shouldHandleSingleVariableMonomials() {
        int[] xExponents = {7};
        int[] yExponents = {3};
        SparseMonomial<Rational> x = new SparseMonomial<>(xExponents, testCoefficient);
        DenseMonomial<Rational> y = new DenseMonomial<>(yExponents, new Rational(1, 2));
        Monomial<Rational> result = MonomialFunctions.lcm(x, y);
        assertInstanceOf(DenseMonomial.class, result);
        assertEquals(1, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(7, result.getExponent(0));
    }

    @Test
    void shouldProduceCoefficientOfOne() {
        Rational differentCoeff = new Rational(42, 13);
        int[] exponents = {2, 3};
        DenseMonomial<Rational> monomial = new DenseMonomial<>(exponents, differentCoeff);
        Monomial<Rational> oneResult = MonomialFunctions.one(monomial);
        Monomial<Rational> lcmResult = MonomialFunctions.lcm(monomial, monomial);
        assertEquals(ONE, oneResult.coefficient());
        assertEquals(ONE, lcmResult.coefficient());
    }

    @Test
    void shouldHandleVariousRationalCoefficients() {
        Rational[] coefficients = {
                new Rational(1, 2),
                new Rational(-3, 4),
                new Rational(7),
                new Rational(-5, 3),
                new Rational(100, 7)
        };
        int[] exponents = {1, 2};
        for (Rational coeff : coefficients) {
            SparseMonomial<Rational> sparse = new SparseMonomial<>(exponents, coeff);
            DenseMonomial<Rational> dense = new DenseMonomial<>(exponents, coeff);
            Monomial<Rational> sparseOne = MonomialFunctions.one(sparse);
            Monomial<Rational> denseOne = MonomialFunctions.one(dense);
            Monomial<Rational> lcmResult = MonomialFunctions.lcm(sparse, dense);
            assertEquals(ONE, sparseOne.coefficient(),
                    "one() should return coefficient 1 for input: " + coeff);
            assertEquals(ONE, denseOne.coefficient(),
                    "one() should return coefficient 1 for input: " + coeff);
            assertEquals(ONE, lcmResult.coefficient(),
                    "lcm() should return coefficient 1 for input: " + coeff);
        }
    }

    @Test
    void shouldTestSparseOptimizationRealistic() {
        int[] poly1 = {5, 0, 2, 0};
        int[] poly2 = {0, 3, 0, 4};
        SparseMonomial<Rational> sparse1 = new SparseMonomial<>(poly1, testCoefficient);
        SparseMonomial<Rational> sparse2 = new SparseMonomial<>(poly2, new Rational(3));
        Monomial<Rational> result = MonomialFunctions.lcm(sparse1, sparse2);
        assertInstanceOf(SparseMonomial.class, result);
        assertEquals(4, result.fieldSize());
        assertEquals(ONE, result.coefficient());
        assertEquals(5, result.getExponent(0));
        assertEquals(3, result.getExponent(1));
        assertEquals(2, result.getExponent(2));
        assertEquals(4, result.getExponent(3));
    }

    @Test
    void shouldHandleBoundaryConditionsWithZeroCoefficients() {
        Rational zero = new Rational(0, 1);
        int[] exponents = {2, 1, 3};
        SparseMonomial<Rational> sparseWithZero = new SparseMonomial<>(exponents, zero);
        DenseMonomial<Rational> denseWithZero = new DenseMonomial<>(exponents, zero);
        Monomial<Rational> sparseOne = MonomialFunctions.one(sparseWithZero);
        Monomial<Rational> denseOne = MonomialFunctions.one(denseWithZero);
        Monomial<Rational> lcmResult = MonomialFunctions.lcm(sparseWithZero, denseWithZero);
        assertEquals(ONE, sparseOne.coefficient());
        assertEquals(ONE, denseOne.coefficient());
        assertEquals(ONE, lcmResult.coefficient());
        assertInstanceOf(SparseMonomial.class, sparseOne);
        assertInstanceOf(DenseMonomial.class, denseOne);
        assertInstanceOf(DenseMonomial.class, lcmResult);
    }
}
