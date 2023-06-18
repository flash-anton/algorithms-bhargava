package org.example.graph;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.example.Util;
import org.junit.jupiter.api.Assertions;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DirectedAcyclicGraph<T> {
    @NonNull
    @Getter
    @Setter
    private PrintStream log = new PrintStream(OutputStream.nullOutputStream());

    private final Map<T, Map<T, Integer>> edges = new HashMap<>();

    public static <T> DirectedAcyclicGraph<T> generate(int nodesCount, int maxEdgesCount, int maxWeight,
                                                       @NonNull Supplier<T> nodeSupplier) {
        Assertions.assertTrue(nodesCount >= 3, "Graph must contain at least 3 nodes");
        Assertions.assertTrue(maxEdgesCount >= 2, "Max graph edges must be at least 2");
        Assertions.assertTrue(maxWeight >= 0, "Max graph edges weight must be at least 0");

        Random random = new Random();
        int minEdgesCountInclusive = 1;
        int maxEdgesCountExclusive = maxEdgesCount + 1;
        int maxWeightExclusive = maxWeight + 1;

        DirectedAcyclicGraph<T> dag = new DirectedAcyclicGraph<>();
        dag.edges.put(nodeSupplier.get(), new HashMap<>());
        while (dag.edges.size() < nodesCount) {

            T from = nodeSupplier.get();
            if (dag.edges.containsKey(from)) {
                continue;
            }

            ArrayList<T> nodes = new ArrayList<>(dag.edges.keySet());

            int minNodeIndex = 0;
            int maxNodeIndex = nodes.size() - 1;
            int toListSize = Math.min(nodes.size(), random.nextInt(minEdgesCountInclusive, maxEdgesCountExclusive));

            Map<T, Integer> toList = Util.ints(toListSize, minNodeIndex, maxNodeIndex).parallelStream()
                    .collect(Collectors.toMap(nodes::get, i -> random.nextInt(maxWeightExclusive)));

            dag.edges.put(from, toList);
        }
        return dag;
    }

    public void putEdge(@NonNull T from, @NonNull T to, int weight) {
        Assertions.assertTrue(weight >= 0, "Graph edge weight must be at least 0");
        Assertions.assertNotEquals(from, to, "Graph edge must consist of two different nodes");

        if (!edges.isEmpty()) {
            boolean containsFrom = edges.containsKey(from);
            boolean containsTo = edges.containsKey(to);
            Assertions.assertTrue(containsFrom || containsTo,
                    "Graph must be empty or contain at least one of the specified nodes");
            if (containsFrom && containsTo) {
                Assertions.assertTrue(breadthFirstSearch(to, from::equals).isEmpty(), "Graph must be acyclic");
            }
        }

        Map<T, Integer> toList = edges.getOrDefault(from, new HashMap<>());
        toList.put(to, weight);
        edges.putIfAbsent(from, toList);
        edges.putIfAbsent(to, new HashMap<>());
    }

    public void putEdge(@NonNull T from, @NonNull T to) {
        putEdge(from, to, 0);
    }

    public void putEdges(@NonNull T from, @NonNull List<T> toList) {
        toList.forEach(to -> putEdge(from, to));
    }

    public void removeEdge(@NonNull T from, @NonNull T to) {
        boolean containsFrom = edges.containsKey(from);
        boolean containsTo = edges.containsKey(to);
        if (!containsFrom || !containsTo) {
            return;
        }

        edges.get(from).remove(to);

        if (edges.get(to).size() > 0 || edges.entrySet().parallelStream().anyMatch(e -> e.getValue().containsKey(to))) {
            return;
        }

        edges.remove(to);
    }

    @NonNull
    public Map<T, Map<T, Integer>> getEdges() {
        return edges.entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashMap<>(e.getValue())));
    }

    /**
     * Возвращает не просто искомый узел (6. Поиск в ширину), а кратчайший путь до него.
     */
    @NonNull
    public List<T> breadthFirstSearch(@NonNull T begin, @NonNull Predicate<T> endValidator) {
        Assertions.assertTrue(edges.containsKey(begin), "Graph must contain the specified begin-node");

        LinkedList<LinkedList<T>> tracks = new LinkedList<>();
        tracks.add(new LinkedList<>(List.of(begin)));

        HashSet<T> explored = new HashSet<>();

        while (!tracks.isEmpty()) {
            LinkedList<T> track = tracks.poll();
            log.printf("BFS: check last node of track %s ... ", Util.joining(track, " -> "));

            T node = track.peekLast();

            if (explored.contains(node)) {
                log.println("already explored");
                continue;
            }

            if (endValidator.test(node)) {
                log.println("is end");
                return track;
            }

            log.println("isn't end");
            explored.add(node);

            for (T outNode : edges.get(node).keySet()) {
                LinkedList<T> outTrack = new LinkedList<>(track);
                outTrack.add(outNode);
                tracks.add(outTrack);
            }
        }

        return new LinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean weighted = edges.values().parallelStream().flatMap(v -> v.values().stream()).anyMatch(i -> i != 0);
        edges.forEach((from, toList) -> {
            sb.append(from);
            if (toList.size() > 0) {
                sb.append(" -> ");
                toList.forEach((to, weight) -> {
                    if (weighted) {
                        sb.append(String.format("%s(%d), ", to, weight));
                    } else {
                        sb.append(String.format("%s, ", to));
                    }
                });
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append(System.lineSeparator());
        });
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
