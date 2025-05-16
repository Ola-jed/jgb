package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.structures.Polynomial;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class GrobnerBasisAlgorithms {
    private GrobnerBasisAlgorithms() {
    }

    public static <T extends Numeric> List<Polynomial<T>> reduceGrobnerBasis(List<Polynomial<T>> polynomials) {
        var minimized = minimizeGrobnerBasis(polynomials);
        var size = minimized.size();
        var result = new ArrayList<Polynomial<T>>(size);

        if (size <= 1) {
            return new ArrayList<>(minimized);
        }

        for (var i = 0; i < size; i++) {
            var current = minimized.get(i);
            var reducers = new ArrayList<>(minimized);
            reducers.remove(i);
            result.add(current.reduce(reducers));
        }

        return result;
    }

    public static <T extends Numeric> List<Polynomial<T>> minimizeGrobnerBasis(List<Polynomial<T>> polynomials) {
        var size = polynomials.size();
        var monicBasis = new ArrayList<Polynomial<T>>(size);
        for (Polynomial<T> polynomial : polynomials) {
            monicBasis.add(polynomial.divide(polynomial.leadingCoefficient()));
        }

        var toKeep = new BitSet(size);
        toKeep.set(0, size);
        for (var i = 0; i < size; i++) {
            if (!toKeep.get(i)) {
                continue;
            }

            for (var j = 0; j < size; j++) {
                if (i != j && toKeep.get(j)) {
                    var divisionResult = monicBasis.get(i)
                            .leadingMonomial()
                            .divide(monicBasis.get(j).leadingMonomial())
                            .coefficient();

                    if (!divisionResult.equals(divisionResult.zero())) {
                        toKeep.clear(i);
                        break;
                    }
                }
            }
        }

        var result = new ArrayList<Polynomial<T>>();
        for (int i = 0; i < size; i++) {
            if (toKeep.get(i)) {
                result.add(monicBasis.get(i));
            }
        }

        return result;
    }
}
