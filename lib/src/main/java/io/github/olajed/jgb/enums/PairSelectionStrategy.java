package io.github.olajed.jgb.enums;

/**
 * Enumeration of strategies for selecting critical pairs during
 * the Buchberger algorithm for computing Gröbner bases.
 *
 * <p>These strategies influence the order and efficiency of the
 * basis computation.</p>
 */
public enum PairSelectionStrategy {
    /** Selects the first pair encountered without additional criteria (fifo). */
    FIRST,

    /** Selects pairs based on the degree of the lcm. */
    DEGREE,

    /** Selects pairs using the normal selection strategy (comparing lcm). */
    NORMAL,

    /**
     * Selects pairs based on the sugar degree.
     * <p>
     * This strategy is described in:
     * <br>“One sugar cube, please” or selection strategies in the Buchberger algorithm.
     * <br>See: <a href="https://dl.acm.org/doi/10.1145/120694.120701">...</a>
     */
    SUGAR
}
