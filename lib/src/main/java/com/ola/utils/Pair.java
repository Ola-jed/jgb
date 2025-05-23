package com.ola.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic immutable pair consisting of two elements: {@code first} and {@code second}.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 */
public record Pair<T, U>(T first, U second) {
    /**
     * Generates all unique pairs (combinations of two distinct elements) from the given list.
     * <p>
     * For a list of size {@code n}, this method returns {@code n * (n - 1) / 2} pairs,
     * where each pair contains two different elements from the list, without repetition
     * and order does not matter (i.e., (a, b) and (b, a) are considered the same pair).
     * </p>
     *
     * @param values the list of values to generate pairs from
     * @param <T>    the type of elements in the input list and the resulting pairs
     * @return a list of unique pairs generated from the input list
     */
    public static <T> List<Pair<T, T>> generatePairs(List<T> values) {
        var size = values.size();
        var pairsCount = (size * (size - 1)) >> 1;
        var result = new ArrayList<Pair<T, T>>(pairsCount);
        for (var i = 0; i < size; i++) {
            for (var j = i + 1; j < size; j++) {
                result.add(new Pair<>(values.get(i), values.get(j)));
            }
        }

        return result;
    }

    /**
     * Creates a list of pairs combining each element from the input list with the given fixed value.
     * <p>
     * This represents the Cartesian product of the list {@code items} and the single element {@code fixed}.
     * </p>
     *
     * @param items the list of elements to pair with the fixed element
     * @param fixed the fixed element to pair with each element from the list
     * @param <T>   the type of elements in the input list
     * @param <U>   the type of the fixed element
     * @return a list of pairs where each pair consists of an element from {@code items} and the fixed element {@code fixed}
     */
    public static <T, U> List<Pair<T, U>> cartesianProduct(List<T> items, U fixed) {
        var size = items.size();
        var result = new ArrayList<Pair<T, U>>(size);
        for (var element : items) {
            result.add(new Pair<>(element, fixed));
        }

        return result;
    }
}
