package org.example;

import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class Util {
    @NonNull
    public static <T> String collectionJoining(@NonNull Collection<T> collection, @NonNull String delimiter) {
        return collection.stream().map(String::valueOf).collect(Collectors.joining(delimiter));
    }

    @NonNull
    public static Set<Integer> randomUniqInts(int count, int min, int max) {
        HashSet<Integer> set = new HashSet<>(count);
        int maxExclusive = max + 1;
        for (int rest = count; rest > 0; rest = count - set.size()) {
            set.addAll(new Random().ints(rest, min, maxExclusive).distinct().boxed().toList());
        }
        return set;
    }
}
