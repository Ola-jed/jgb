package io.github.olajed.jgb.utils;

/**
 * A generic immutable triple consisting of three elements: {@code first}, {@code second}, and {@code third}.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 * @param <V> the type of the third element
 * @param first the first element of the triple
 * @param second the second element of the triple
 * @param third the third element of the triple
 */
public record Triple<T, U, V>(T first, U second, V third) {
}
