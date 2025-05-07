package com.ola.ordering;

import com.ola.number.Numeric;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.SparseMonomial;

import java.util.BitSet;

/**
 * Implements elimination ordering for monomials.
 * <p>
 * Elimination ordering is designed to eliminate specific variables from a polynomial system.
 * It divides variables into two blocks, with variables in earlier blocks
 * always considered greater than variables in later blocks regardless of degree.
 */
public class EliminationOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private final BitSet elimination;
    private final BitSet retained;
    private final MonomialOrdering<T> ordering;
    private static final byte ORDER_ID = 5;

    public EliminationOrdering(BitSet elimination, BitSet retained, MonomialOrdering<T> ordering) {
        var bitsetForChecking = (BitSet) elimination.clone();
        bitsetForChecking.xor(retained);
        if (bitsetForChecking.cardinality() != bitsetForChecking.length()) {
            throw new IllegalArgumentException("The XOR of both BitSets should result in a BitSet with all bits set to 1.");
        }

        this.elimination = elimination;
        this.retained = retained;
        this.ordering = ordering;
    }

    @Override
    public int compare(Monomial<T> a, Monomial<T> b) {
        if (a.fieldSize() != b.fieldSize()) {
            throw new IllegalArgumentException("Both monomials should be defined in the same ring.");
        }

        var aEliminationExponents = new int[a.fieldSize()];
        var bEliminationExponents = new int[a.fieldSize()];
        for (var i = 0; i < a.fieldSize(); i++) {
            if (elimination.get(i)) {
                aEliminationExponents[i] = a.getExponent(i);
                bEliminationExponents[i] = b.getExponent(i);
            }
        }

        if (a instanceof DenseMonomial<T> && b instanceof DenseMonomial<T>) {
            var aElim = new DenseMonomial<>(aEliminationExponents, a.coefficient());
            var bElim = new DenseMonomial<>(bEliminationExponents, b.coefficient());
            var eliminationResult = ordering.compare(aElim, bElim);
            if (eliminationResult != 0) {
                return eliminationResult;
            }
        }

        if (a instanceof SparseMonomial<T> && b instanceof SparseMonomial<T>) {
            var aElim = new SparseMonomial<>(aEliminationExponents, a.coefficient());
            var bElim = new SparseMonomial<>(bEliminationExponents, b.coefficient());
            var eliminationResult = ordering.compare(aElim, bElim);
            if (eliminationResult != 0) {
                return eliminationResult;
            }
        }


        var aRetainedExponents = new int[a.fieldSize()];
        var bRetainedExponents = new int[a.fieldSize()];
        for (var i = 0; i < a.fieldSize(); i++) {
            if (retained.get(i)) {
                aRetainedExponents[i] = a.getExponent(i);
                bRetainedExponents[i] = b.getExponent(i);
            }
        }

        if (a instanceof DenseMonomial<T> && b instanceof DenseMonomial<T>) {
            var aRetained = new DenseMonomial<>(aRetainedExponents, a.coefficient());
            var bRetained = new DenseMonomial<>(bRetainedExponents, b.coefficient());
            return ordering.compare(aRetained, bRetained);
        }

        var aRetained = new SparseMonomial<>(aRetainedExponents, a.coefficient());
        var bRetained = new SparseMonomial<>(bRetainedExponents, b.coefficient());
        return ordering.compare(aRetained, bRetained);
    }

    @Override
    public byte orderId() {
        return ORDER_ID;
    }
}
