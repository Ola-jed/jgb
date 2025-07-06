package com.ola.enums;

/**
 * Enumeration of strategies for selecting critical pairs during
 * the Buchberger algorithm for computing Gröbner bases.
 *
 * <p>These strategies influence the order and efficiency of the
 * basis computation.</p>
 */
public enum PairSelectionStrategy {
    FIRST,
    DEGREE,
    NORMAL,
    SUGAR
}
