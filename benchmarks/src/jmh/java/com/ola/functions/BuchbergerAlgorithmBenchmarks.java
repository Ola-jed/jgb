package com.ola.functions;

import com.ola.enums.PairSelectionStrategy;
import com.ola.functions.algorithms.BuchbergerAlgorithm;
import com.ola.number.GaloisFieldElement;
import com.ola.providers.KatsuraGenerator;
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
public class BuchbergerAlgorithmBenchmarks {
    private List<Polynomial<GaloisFieldElement>> polynomials;

    @Param({"dense", "sparse"})
    private String monomialType;

    @Param({"reimer3", "reimer4", "reimer5", "reimer6", "katsura3", "katsura4", "katsura5", "katsura6"})
    private String system;

    @Setup
    public void setup() {
        var dense = monomialType.equals("dense");
        polynomials = switch (system) {
            case "reimer3" -> ReimerGenerator.get(3, dense);
            case "reimer4" -> ReimerGenerator.get(4, dense);
            case "reimer5" -> ReimerGenerator.get(5, dense);
            case "reimer6" -> ReimerGenerator.get(6, dense);
            case "katsura3" -> KatsuraGenerator.get(3, dense);
            case "katsura4" -> KatsuraGenerator.get(4, dense);
            case "katsura5" -> KatsuraGenerator.get(5, dense);
            case "katsura6" -> KatsuraGenerator.get(6, dense);
            default -> throw new IllegalStateException("Unexpected value: " + system);
        };
    }

    @Benchmark
    public List<Polynomial<GaloisFieldElement>> benchmarkFirstSelectionStrategy() {
        return BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.FIRST);
    }

    @Benchmark
    public List<Polynomial<GaloisFieldElement>> benchmarkDegreeSelectionStrategy() {
        return BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.DEGREE);
    }

    @Benchmark
    public List<Polynomial<GaloisFieldElement>> benchmarkNormalSelectionStrategy() {
        return BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.NORMAL);
    }

    @Benchmark
    public List<Polynomial<GaloisFieldElement>> benchmarkSugarSelectionStrategy() {
        return BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.SUGAR);
    }
}