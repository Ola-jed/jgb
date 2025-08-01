package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.number.Real;
import org.openjdk.jmh.annotations.*;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class MonomialsBenchmark {
    @Param({"1", "5", "10", "20", "50"})
    public int ringSize;

    @Param({"10", "25", "50", "100"})
    public int density;

    @Param({"dense", "sparse"})
    private String monomialType;

    private Monomial<Real> monomial;

    private Monomial<Real> otherMonomial;

    @Setup
    public void setup() {
        var exponents = new int[ringSize];
        var otherExponents = new int[ringSize];
        var random = new Random(42);
        var nonZeroCount = ringSize * density / 100;
        var selectedIndices = new HashSet<Integer>();
        while (selectedIndices.size() < nonZeroCount) {
            selectedIndices.add(random.nextInt(ringSize));
        }

        for (int i : selectedIndices) {
            exponents[i] = random.nextInt(5) + 1;
            otherExponents[i] = random.nextInt(5) + 1;
        }

        if (monomialType.equals("dense")) {
            monomial = new DenseMonomial<>(exponents, Real.ONE);
            otherMonomial = new DenseMonomial<>(otherExponents, Real.ONE);
        } else {
            monomial = new SparseMonomial<>(exponents, Real.ONE);
            otherMonomial = new SparseMonomial<>(otherExponents, Real.ONE);
        }
    }

    @Benchmark
    public Monomial<Real> multiplicationByMonomial() {
        return monomial.multiply(otherMonomial);
    }

    @Benchmark
    public Monomial<Real> multiplicationByFactor() {
        return monomial.multiply(new Real(3));
    }

    @Benchmark
    public Monomial<Real> division() {
        return monomial.divide(otherMonomial);
    }

    @Benchmark
    public boolean equality() {
        return monomial.equals(otherMonomial);
    }

    @Benchmark
    public int exponentRecuperation() {
        return monomial.getExponent(ringSize - 1);
    }

    @Benchmark
    public Monomial<Real> copy() {
        return monomial.withCoefficient(new Real(4));
    }

    @Benchmark
    public int accumulation() {
        return monomial.accumulate(
                IntStream.range(0, 26).boxed().toArray(Integer[]::new),
                (x, y) -> x * y,
                0,
                Integer::sum
        );
    }
}
