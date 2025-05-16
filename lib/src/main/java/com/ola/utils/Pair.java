package com.ola.utils;

import java.util.ArrayList;
import java.util.List;

public record Pair<T, U>(T first, U second) {
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

    public static <T, U> List<Pair<T, U>> cartesianProduct(List<T> items, U fixed) {
        var size = items.size();
        var result = new ArrayList<Pair<T, U>>(size);
        for (var element : items) {
            result.add(new Pair<>(element, fixed));
        }

        return result;
    }
}
