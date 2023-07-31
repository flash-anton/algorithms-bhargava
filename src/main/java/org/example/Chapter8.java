package org.example;

import java.util.*;
import java.util.function.Supplier;

// 8. Жадные алгоритмы
public class Chapter8 {
    public static void main(String[] args) {
        Set<String> states = Set.of("mt", "wa", "or", "id", "nv", "ut", "ca", "az");

        Map<String, Set<String>> stations = Map.of(
                "kone", Set.of("id", "nv", "ut"),
                "ktwo", Set.of("wa", "id", "mt"),
                "kthree", Set.of("or", "nv", "ca"),
                "kfour", Set.of("nv", "ut"),
                "kfive", Set.of("ca", "az"));


        measure(1_000_000, () -> greedyAlgorithm(states, stations));
        measure(1_000_000, () -> greedyAlgorithm2(states, stations));
    }

    static <T> void measure(int iterations, Supplier<T> func) {
        T result = null;

        long ms = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            result = func.get();
        }
        ms = System.currentTimeMillis() - ms;

        System.out.println(ms + " ms on " + iterations + " iterations: " + result);
    }

    // Алгоритм из книги. Из поиска очередной bestStation не исключаются предыдущие.
    static Set<String> greedyAlgorithm(Set<String> states, Map<String, Set<String>> stations) {
        Set<String> finalStations = new HashSet<>();
        Set<String> statesNeeded = new HashSet<>(states);
        while (!statesNeeded.isEmpty()) {
            String bestStation = null;
            Set<String> statesCovered = new HashSet<>();
            for (String station : stations.keySet()) {
                Set<String> statesForStation = stations.get(station);
                Set<String> covered = intersection(statesNeeded, statesForStation);
                if (covered.size() > statesCovered.size()) {
                    bestStation = station;
                    statesCovered = covered;
                }
            }
            statesNeeded.removeAll(statesCovered);
            finalStations.add(bestStation);
        }
        return finalStations;
    }

    static Set<String> greedyAlgorithm2(Set<String> states, Map<String, Set<String>> stations) {
        Set<String> finalStations = new HashSet<>();
        Map<String, Set<String>> remainedStations = new LinkedHashMap<>(stations);
        Set<String> statesNeeded = new HashSet<>(states);
        while (!statesNeeded.isEmpty()) {
            String bestStation = null;
            Set<String> statesCovered = new HashSet<>();
            for (String station : remainedStations.keySet()) {
                Set<String> statesForStation = remainedStations.get(station);
                Set<String> covered = intersection(statesNeeded, statesForStation);
                if (covered.size() > statesCovered.size()) {
                    bestStation = station;
                    statesCovered = covered;
                }
            }
            finalStations.add(bestStation);
            remainedStations.remove(bestStation);
            statesNeeded.removeAll(statesCovered);
        }
        return finalStations;
    }

    static Set<String> intersection(Set<String> a, Set<String> b) {
        // iterates by small, search in big
        Set<String> intersection = new HashSet<>(a.size() < b.size() ? a : b);
        intersection.retainAll(a.size() < b.size() ? b : a);
        return intersection;
    }
}
