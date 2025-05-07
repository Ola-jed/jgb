package com.ola.enums;

/**
 * Strategies for selecting critical pairs during the Buchberger algorithm
 * for computing Gröbner bases.
 */
public enum PairSelectionStrategy {
    FIRST,
    DEGREE,
    NORMAL,
    SUGAR
}
