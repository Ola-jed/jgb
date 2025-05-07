package com.ola.numbers;

import com.ola.number.GaloisFieldElement;
import com.ola.number.Numeric;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class GaloisFieldElementBenchmark {
    private GaloisFieldElement a;
    private GaloisFieldElement b;

    @Setup
    public void setup() {
        a = new GaloisFieldElement(3, 5);
        b = new GaloisFieldElement(4, 5);
    }

    @Benchmark
    public Numeric initialization() {
        return new GaloisFieldElement(42, 5);
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
