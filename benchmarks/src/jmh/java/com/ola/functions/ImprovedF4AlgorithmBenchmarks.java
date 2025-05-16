package com.ola.functions;

import com.ola.functions.algorithms.ImprovedF4Algorithm;
import com.ola.number.GaloisFieldElement;
import com.ola.providers.Katsura3Generator;
import com.ola.providers.Katsura4Generator;
import com.ola.providers.Katsura5Generator;
import com.ola.providers.ReimerGenerator;
import com.ola.structures.Polynomial;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class ImprovedF4AlgorithmBenchmarks {
    private List<Polynomial<GaloisFieldElement>> polynomials;

    @Param({"dense", "sparse"})
    private String monomialType;

    @Param({"reimer3", "reimer4", "katsura3", "katsura4", "katsura5"})
    private String system;

    @Setup
    public void setup() {
        var dense = monomialType.equals("dense");
        polynomials = switch (system) {
            case "reimer3" -> ReimerGenerator.get(3, dense);
            case "reimer4" -> ReimerGenerator.get(4, dense);
            case "reimer5" -> ReimerGenerator.get(5, dense);
            case "katsura3" -> Katsura3Generator.get(dense);
            case "katsura4" -> Katsura4Generator.get(dense);
            case "katsura5" -> Katsura5Generator.get(dense);
            default -> throw new IllegalStateException("Unexpected value: " + system);
        };
    }

    @Benchmark
    public List<Polynomial<GaloisFieldElement>> compute() {
        return ImprovedF4Algorithm.compute(polynomials);
    }
}
