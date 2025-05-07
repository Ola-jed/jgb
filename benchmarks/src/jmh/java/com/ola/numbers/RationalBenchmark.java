package com.ola.numbers;

import com.ola.number.Numeric;
import com.ola.number.Rational;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class RationalBenchmark {
    private Rational a;
    private Rational b;

    @Setup
    public void setup() {
        a = new Rational(314, 100);
        b = new Rational(271, 100);
    }

    @Benchmark
    public Numeric initialization() {
        return new Rational(42, 13);
    }

    @Benchmark
    public Numeric addition() {
        return a.add(b);
    }

    @Benchmark
    public Numeric subtraction() {
        return a.subtract(b);
    }

    @Benchmark
    public Numeric multiplication() {
        return a.multiply(b);
    }

    @Benchmark
    public Numeric division() {
        return a.divide(b);
    }

    @Benchmark
    public Numeric negation() {
        return a.negate();
    }
}
