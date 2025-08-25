package io.github.olajed.jgb.utils;

import io.github.olajed.jgb.number.Numeric;

import java.util.List;

/**
 * Solver for matrix equations.
 * Use augmented rref.
 * The operations done on the main matrix are also done on the values list
 *
 * @param <T> the numeric type of the  coefficients
 */
@SuppressWarnings("unchecked")
public final class MatrixSolver<T extends Numeric> {
    private final List<List<T>> matrix;
    private final List<T> values;
    private final T zero;
    private final T one;

    /**
     * Constructs a {@code MatrixSolver} with the given coefficient matrix and constant values.
     *
     * @param matrix a list of lists representing the coefficient matrix
     * @param values the list of constant terms (right-hand side values)
     */
    public MatrixSolver(List<List<T>> matrix, List<T> values) {
        this.matrix = matrix;
        this.values = values;
        this.zero = (T) values.getFirst().zero();
        this.one = (T) values.getFirst().one();
    }

    /**
     * Solves the system of linear equations represented by the matrix and values
     * using Gaussian elimination and checks for solvability.
     *
     * <p>The matrix is first reduced to row echelon form. Then, it verifies
     * whether the resulting matrix is an identity matrix, indicating a unique solution.</p>
     *
     * @return the list of solution values if the system is solvable; {@code null} otherwise
     */
    public List<T> solve() {
        rowEchelonReduction();
        var rowCount = matrix.size();
        var colCount = matrix.getFirst().size();

        // Ensure this is an Identity matrix
        var usedRows = new boolean[rowCount];
        for (var column = 0; column < colCount; column++) {
            var pivotRow = -1;
            for (var row = 0; row < rowCount; row++) {
                if (matrix.get(row).get(column).equals(one)) {
                    if (pivotRow != -1) {
                        // More than one '1' in this column → not identity
                        return null;
                    }
                    pivotRow = row;
                } else if (!matrix.get(row).get(column).equals(zero)) {
                    // Any non-zero besides the pivot → not identity
                    return null;
                }
            }

            if (pivotRow == -1 || usedRows[pivotRow]) {
                // No pivot in this column OR row already used
                return null;
            }
            usedRows[pivotRow] = true;
        }

        return values;
    }

    /**
     * Performs Gaussian elimination to convert the matrix to reduced row echelon form.
     * This implementation uses a more efficient approach with in-place operations.
     */
    public void rowEchelonReduction() {
        var rows = matrix.size();
        if (rows == 0) {
            return;
        }

        var cols = matrix.getFirst().size();
        if (cols == 0) {
            return;
        }

        var currentRow = 0;
        for (var col = 0; col < cols && currentRow < rows; col++) {
            // Find pivot - find the first non-zero entry in the current column
            var pivotRow = -1;
            for (var i = currentRow; i < rows; i++) {
                var val = matrix.get(i).get(col);
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
            var pivotValue = matrix.get(currentRow).get(col);
            if (!pivotValue.equals(pivotValue.one())) {
                scaleRow(currentRow, (T) pivotValue.one().divide(pivotValue));
            }

            // Eliminate other rows
            for (var i = 0; i < rows; i++) {
                if (i != currentRow && !matrix.get(i).get(col).equals(zero)) {
                    var factor = matrix.get(i).get(col);
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

        var temp = matrix.get(row1);
        matrix.set(row1, matrix.get(row2));
        matrix.set(row2, temp);
        var rowTempElement = values.get(row1);
        values.set(row1, values.get(row2));
        values.set(row2, rowTempElement);
    }

    /**
     * Scale a row by multiplying all elements by a scalar
     */
    private void scaleRow(int row, T scalar) {
        var targetRow = matrix.get(row);
        targetRow.replaceAll(t -> (T) t.multiply(scalar));
        values.set(row, (T) values.get(row).multiply(scalar));
    }

    /**
     * Subtract row2 * factor from row1
     */
    private void subtractRows(int row1, int row2, T factor) {
        var targetRow1 = matrix.get(row1);
        var targetRow2 = matrix.get(row2);
        for (var i = 0; i < targetRow1.size(); i++) {
            targetRow1.set(i, (T) targetRow1.get(i).subtract(targetRow2.get(i).multiply(factor)));
        }

        var row1Value = values.get(row1);
        var row2Value = values.get(row2);
        values.set(row1, (T) row1Value.subtract(row2Value.multiply(factor)));
    }
}
