package io.github.olajed.jgb.utils;

import io.github.olajed.jgb.number.Real;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixSolverTest {
    private void assertSolution(List<List<Real>> matrix, List<Real> values, List<Real> solution) {
        assertNotNull(solution, "Solution must not be null");
        double tol = 1e-9;
        for (int i = 0; i < matrix.size(); i++) {
            double lhs = 0.0;
            for (int j = 0; j < matrix.get(i).size(); j++) {
                lhs += matrix.get(i).get(j).get() * solution.get(j).get();
            }
            double rhs = values.get(i).get();
            assertEquals(rhs, lhs, tol, "Equation " + i + " not satisfied");
        }
    }

    @Test
    void testSolveSimpleSystem() {
        // System:
        // 2x +  y = 5
        //  x + 3y = 6
        //
        // True solution: x=1.8, y=1.4 (9/5, 7/5)

        List<List<Real>> matrix = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(new Real(2), new Real(1))),
                new ArrayList<>(Arrays.asList(new Real(1), new Real(3)))
        ));

        List<Real> values = new ArrayList<>(Arrays.asList(new Real(5), new Real(6)));

        MatrixSolver<Real> solver = new MatrixSolver<>(matrix, values);
        List<Real> solution = solver.solve();

        assertSolution(matrix, values, solution);
    }

    @Test
    void testUnsolvableSystem() {
        // x + y = 2
        // 2x + 2y = 5   (inconsistent)

        List<List<Real>> matrix = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(new Real(1), new Real(1))),
                new ArrayList<>(Arrays.asList(new Real(2), new Real(2)))
        ));

        List<Real> values = new ArrayList<>(Arrays.asList(new Real(2), new Real(5)));

        MatrixSolver<Real> solver = new MatrixSolver<>(matrix, values);
        assertNull(solver.solve());
    }

    @Test
    void testIdentitySystem() {
        // x = 4, y = -3

        List<List<Real>> matrix = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(new Real(1), new Real(0))),
                new ArrayList<>(Arrays.asList(new Real(0), new Real(1)))
        ));

        List<Real> values = new ArrayList<>(Arrays.asList(new Real(4), new Real(-3)));

        MatrixSolver<Real> solver = new MatrixSolver<>(matrix, values);
        List<Real> solution = solver.solve();

        assertSolution(matrix, values, solution);
    }

    @Test
    void testZeroMatrix() {
        List<List<Real>> matrix = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(new Real(0), new Real(0))),
                new ArrayList<>(Arrays.asList(new Real(0), new Real(0)))
        ));

        List<Real> values = new ArrayList<>(Arrays.asList(new Real(0), new Real(0)));

        MatrixSolver<Real> solver = new MatrixSolver<>(matrix, values);
        assertNull(solver.solve());
    }
}
