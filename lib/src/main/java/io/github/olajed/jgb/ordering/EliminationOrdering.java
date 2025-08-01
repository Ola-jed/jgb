package io.github.olajed.jgb.ordering;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.DenseMonomial;
import io.github.olajed.jgb.structures.Monomial;
import io.github.olajed.jgb.structures.SparseMonomial;

import java.util.BitSet;

/**
 * Implements elimination ordering for monomials.
 * <p>
 * Elimination ordering is designed to eliminate specific variables from a polynomial system.
 * It divides variables into two blocks, with variables in earlier blocks
 * always considered greater than variables in later blocks regardless of degree.
 * @param <T> the numeric type of the monomial coefficients
 */
public class EliminationOrdering<T extends Numeric> implements MonomialOrdering<T> {
    private final BitSet elimination;
    private final BitSet retained;
    private final MonomialOrdering<T> ordering;
    private static final byte ORDER_ID = 5;

    /**
     * Constructs an elimination ordering for monomials.
     *
     * <p>This constructor partitions variables into two disjoint sets:
     * those to be eliminated and those to be retained. The ordering ensures that
     * variables marked for elimination are always considered greater than retained ones,
     * regardless of their degree. An additional internal ordering is used to order monomials
     * within each block.</p>
     *
     * @param elimination a {@link BitSet} indicating variables to eliminate; set bits represent variables to be eliminated
     * @param retained a {@link BitSet} indicating variables to retain; set bits represent retained variables
     * @param ordering the internal {@link MonomialOrdering} used to order monomials within each block
     *
     * @throws IllegalArgumentException if the XOR of {@code elimination} and {@code retained} does not produce a BitSet with all bits set to 1
     */
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
