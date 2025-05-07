package com.ola.structures;

import com.ola.number.Real;
import com.ola.ordering.*;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class PolynomialsBenchmark {
    @Param({"1", "5", "10", "20", "50"})
    public int ringSize;

    @Param({"1", "5", "10", "20", "50", "100", "200", "500"})
    public int polynomialSize;

    @Param({"10", "25", "50", "100"})
    public int density;

    @Param({"dense", "sparse"})
    private String monomialType;

    @Param({"lex", "grlex", "grevlex", "elimination", "weighted"})
    private String orderingType;

    private Polynomial<Real> polynomial;
    private Polynomial<Real> otherPolynomial;
    private Monomial<Real> monomial;

    @Setup
    public void setup() {
        MonomialOrdering<Real> ordering;
        switch (orderingType) {
            case "lex":
                ordering = new LexOrdering<>();
                break;
            case "grlex":
                ordering = new GrlexOrdering<>();
                break;
            case "grevlex":
                ordering = new GrevlexOrdering<>();
                break;
            case "elimination":
                var block1 = new BitSet(ringSize);
                var block2 = new BitSet(ringSize);
                for (int i = 0; i < ringSize; i++) {
                    if (i % 2 == 0) {
                        block1.set(i);
                    } else {
                        block2.set(i);
                    }
                }

                ordering = new EliminationOrdering<>(block1, block2, new GrlexOrdering<>());
                break;
            default:
                var weights = new int[ringSize];
                var weightRandom = new Random(42);
                for (int i = 0; i < ringSize; i++) {
                    weights[i] = weightRandom.nextInt(10) + 1;
                }
                ordering = new WeightedOrdering<>(weights);
                break;
        }

        var exponents = new int[ringSize];
        var random = new Random(42);
        var nonZeroCount = Math.max(1, ringSize * density / 100);
        nonZeroCount = Math.min(nonZeroCount, ringSize);
        var selectedIndices = new HashSet<Integer>();
        while (selectedIndices.size() < nonZeroCount) {
            selectedIndices.add(random.nextInt(ringSize));
        }

        for (var idx : selectedIndices) {
            exponents[idx] = random.nextInt(5) + 1;
        }

        if (monomialType.equals("dense")) {
            monomial = new DenseMonomial<>(exponents, new Real(random.nextDouble() * 20 - 10));
        } else {
            monomial = new SparseMonomial<>(exponents, new Real(random.nextDouble() * 20 - 10));
        }

        polynomial = generateRandomPolynomial(polynomialSize, density, monomialType, ordering);
        otherPolynomial = generateRandomPolynomial(polynomialSize, density, monomialType, ordering);
    }

    @Benchmark
    public Polynomial<Real> initialization() {
        MonomialOrdering<Real> ordering;
        switch (orderingType) {
            case "lex":
                ordering = new LexOrdering<>();
                break;
            case "grlex":
                ordering = new GrlexOrdering<>();
                break;
            case "grevlex":
                ordering = new GrevlexOrdering<>();
                break;
            case "elimination":
                var block1 = new BitSet(ringSize);
                var block2 = new BitSet(ringSize);
                for (int i = 0; i < ringSize; i++) {
                    if (i % 2 == 0) {
                        block1.set(i);
                    } else {
                        block2.set(i);
                    }
                }

                ordering = new EliminationOrdering<>(block1, block2, new GrlexOrdering<>());
                break;
            default:
                var weights = new int[ringSize];
                var weightRandom = new Random(42);
                for (int i = 0; i < ringSize; i++) {
                    weights[i] = weightRandom.nextInt(10) + 1;
                }

                ordering = new WeightedOrdering<>(weights);
                break;
        }

        return generateRandomPolynomial(polynomialSize, density, monomialType, ordering);
    }

    @Benchmark
    public Polynomial<Real> addition() {
        return polynomial.add(otherPolynomial);
    }

    @Benchmark
    public Polynomial<Real> subtraction() {
        return polynomial.subtract(otherPolynomial);
    }

    @Benchmark
    public Polynomial<Real> multiplicationByMonomial() {
        return polynomial.multiply(monomial);
    }

    private Polynomial<Real> generateRandomPolynomial(
            int size,
            int density,
            String monomialType,
            MonomialOrdering<Real> ordering
    ) {
        var monomials = new ArrayList<Monomial<Real>>();
        var random = new Random(42);
        for (var i = 0; i < size; i++) {
            var exponents = new int[ringSize];
            var nonZeroCount = Math.max(1, ringSize * density / 100);
            nonZeroCount = Math.min(nonZeroCount, ringSize);
            var selectedIndices = new HashSet<Integer>();
            while (selectedIndices.size() < nonZeroCount) {
                selectedIndices.add(random.nextInt(ringSize));
            }

            for (var idx : selectedIndices) {
                exponents[idx] = random.nextInt(5) + 1;
            }

            var coefficient = new Real(random.nextDouble() * 20 - 10);
            Monomial<Real> monomial;
            if (monomialType.equals("dense")) {
                monomial = new DenseMonomial<>(exponents, coefficient);
            } else {
                monomial = new SparseMonomial<>(exponents, coefficient);
            }

            monomials.add(monomial);
        }

        return new Polynomial<>(monomials, ringSize, ordering);
    }
}
