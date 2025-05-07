package com.ola.numbers;

import com.ola.number.Numeric;
import com.ola.number.Complex;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class ComplexBenchmark {
    private Complex a;
    private Complex b;

    @Setup
    public void setup() {
        a = new Complex(3.14, 1.5);
        b = new Complex(2.71, 0.5);
    }

    @Benchmark
    public Numeric initialization() {
        return new Complex(42.0, 13.37);
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