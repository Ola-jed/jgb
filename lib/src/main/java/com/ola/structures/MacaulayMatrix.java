package com.ola.structures;

import com.ola.number.Numeric;
import com.ola.ordering.MonomialOrdering;

import java.util.*;

/**
 * Represents a Macaulay matrix for polynomial computations in algebraic geometry.
 * <p>
 * The matrix rows represent polynomials, columns represent monomials ordered according
 * to the specified monomial ordering, and entries are the coefficients from the field.
 */
@SuppressWarnings("unchecked")
public final class MacaulayMatrix<T extends Numeric> {
    private final List<Monomial<T>> monomials;
    private final List<List<T>> coefficients;
    private final T zero;
    private final MonomialOrdering<T> ordering;
    private final int fieldSize;

    /**
     * Building the matrix
     * The number of rows is the number of polynomials
     * The number of columns is the total number of monomials for all the polynomials
     * <p>
     * The monomials are sorted and their coefficients in each polynomial (if existing) are put in the matrix
     * We use a LinkedHashSet for fast lookups and insertion order preservation
     *
     * @param polynomials : The polynomials to build the matrix from
     */
    public MacaulayMatrix(List<Polynomial<T>> polynomials) {
        this.ordering = polynomials.getFirst().ordering();
        this.fieldSize = polynomials.getFirst().fieldSize();
        this.zero = (T) polynomials.getFirst().leadingCoefficient().zero();

        Set<Monomial<T>> allMonomials = new LinkedHashSet<>();
        for (var polynomial : polynomials) {
            for (Monomial<T> monomial : polynomial.monomials()) {
                allMonomials.add(monomial.withCoefficient((T) monomial.coefficient().one()));
            }
        }

        // Sorting
        var sortedMonomials = new ArrayList<>(allMonomials);
        sortedMonomials.sort((o1, o2) -> ordering.compare(o2, o1));
        this.monomials = sortedMonomials;

        // Initializing the coefficient matrix
        var rows = polynomials.size();
        var cols = monomials.size();
        this.coefficients = new ArrayList<>(rows);
        for (var i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(cols);
            for (var j = 0; j < cols; j++) {
                row.add(zero);
            }

            coefficients.add(row);
        }

        // Create a map for fast monomial lookups (avoiding binary search)
        Map<Monomial<T>, Integer> monomialIndices = new HashMap<>(monomials.size());
        for (var i = 0; i < monomials.size(); i++) {
            monomialIndices.put(monomials.get(i), i);
        }

        // Fill the coefficient matrix using the map
        for (var polynomialIndex = 0; polynomialIndex < rows; polynomialIndex++) {
            var polynomial = polynomials.get(polynomialIndex);
            for (var monomial : polynomial.monomials()) {
                var lookupMonomial = monomial.withCoefficient((T) monomial.coefficient().one());
                var monomialIndex = monomialIndices.get(lookupMonomial);
                coefficients.get(polynomialIndex).set(monomialIndex, monomial.coefficient());
            }
        }
    }

    /**
     * Performs Gaussian elimination to convert the matrix to reduced row echelon form.
     * This implementation uses a more efficient approach with in-place operations.
     */
    public void rowEchelonReduction() {
        var rows = coefficients.size();
        if (rows == 0) {
            return;
        }

        var cols = coefficients.getFirst().size();
        if (cols == 0) {
            return;
        }

        var currentRow = 0;
        for (var col = 0; col < cols && currentRow < rows; col++) {
            // Find pivot - find the first non-zero entry in the current column
            var pivotRow = -1;
            for (var i = currentRow; i < rows; i++) {
                var val = coefficients.get(i).get(col);
                if (!val.equals(zero)) {
                    pivotRow = i;
                    break;
                }
            }

            if (pivotRow == -1) {
                // No pivot in this column, we can move to the next column
                continue;
            }

            // Swap rows if needed
            if (pivotRow != currentRow) {
                swapRows(currentRow, pivotRow);
            }

            // Scale pivot row to make leading coefficient 1
            var pivotValue = coefficients.get(currentRow).get(col);
            if (!pivotValue.equals(pivotValue.one())) {
                scaleRow(currentRow, (T) pivotValue.one().divide(pivotValue));
            }

            // Eliminate other rows
            for (var i = 0; i < rows; i++) {
                if (i != currentRow && !coefficients.get(i).get(col).equals(zero)) {
                    var factor = coefficients.get(i).get(col);
                    subtractRows(i, currentRow, factor);
                }
            }

            currentRow++;
        }
    }

    /**
     * Swap two rows in the coefficient matrix
     */
    private void swapRows(int row1, int row2) {
        if (row1 == row2) {
            return;
        }

        var temp = coefficients.get(row1);
        coefficients.set(row1, coefficients.get(row2));
        coefficients.set(row2, temp);
    }

    /**
     * Scale a row by multiplying all elements by a scalar
     */
    private void scaleRow(int row, T scalar) {
        var targetRow = coefficients.get(row);
        targetRow.replaceAll(t -> (T) t.multiply(scalar));
    }

    /**
     * Subtract row2 * factor from row1
     */
    private void subtractRows(int row1, int row2, T factor) {
        var targetRow1 = coefficients.get(row1);
        var targetRow2 = coefficients.get(row2);
        for (var i = 0; i < targetRow1.size(); i++) {
            targetRow1.set(i, (T) targetRow1.get(i).subtract(targetRow2.get(i).multiply(factor)));
        }
    }

    /**
     * Converts the matrix back to a list of polynomials
     */
    public List<Polynomial<T>> polynomials() {
        var polynomials = new ArrayList<Polynomial<T>>(coefficients.size());
        for (var coefficientRow : coefficients) {
            List<Monomial<T>> nonZeroMonomials = new ArrayList<>();
            for (var i = 0; i < monomials.size(); i++) {
                var coefficient = coefficientRow.get(i);
                if (!coefficient.equals(zero)) {
                    nonZeroMonomials.add(monomials.get(i).withCoefficient(coefficient));
                }
            }

            if (!nonZeroMonomials.isEmpty()) {
                polynomials.add(new Polynomial<>(nonZeroMonomials, fieldSize, ordering));
            }
        }

        return polynomials;
    }
}