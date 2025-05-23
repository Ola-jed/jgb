package com.ola.utils;

/**
 * A generic immutable triple consisting of three elements: {@code first}, {@code second}, and {@code third}.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 * @param <V> the type of the third element
 */
public record Triple<T, U, V>(T first, U second, V third) {
}
