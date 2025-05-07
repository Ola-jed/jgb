package com.ola.numbers;

import com.ola.number.Numeric;
import com.ola.number.Real;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class RealBenchmark {
    private Real a;
    private Real b;

    @Setup
    public void setup() {
        a = new Real(3.14);
        b = new Real(2.71);
    }

    @Benchmark
    public Numeric initialization() {
        return new Real(42.0);
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
