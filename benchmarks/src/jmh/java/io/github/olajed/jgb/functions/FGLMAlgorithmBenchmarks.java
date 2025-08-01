package io.github.olajed.jgb.functions;

import io.github.olajed.jgb.functions.algorithms.FGLMAlgorithm;
import io.github.olajed.jgb.number.Rational;
import io.github.olajed.jgb.structures.Polynomial;
import io.github.olajed.jgb.structures.PolynomialRing;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class FGLMAlgorithmBenchmarks {
    private List<Polynomial<Rational>> polynomials;

    @Setup
    public void setup() {
        var ring = new PolynomialRing(Rational.class, new String[]{"x", "y", "z"});
        polynomials = Arrays.asList(
                ring.parse("980 * z^2 - 18 * y - 201 * z + 13"),
                ring.parse("35 * y * z - 4 * y + 2 * z - 1"),
                ring.parse("10 * y^2 - y - 12 * z + 1"),
                ring.parse("5 * x^2 - 4 * y + 2 * z - 1")
        );
    }

    @Benchmark
    public List<Polynomial<Rational>> compute() {
        return FGLMAlgorithm.compute(polynomials);
    }
}
