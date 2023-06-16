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
import java.util.stream.Stream;

public class DirectedAcyclicGraph<T> {
    @NonNull
    @Getter
    @Setter
    private PrintStream log = new PrintStream(OutputStream.nullOutputStream());

    private final HashMap<T, HashMap<T, Integer>> edges = new HashMap<>();

    public static <T> DirectedAcyclicGraph<T> generate(int nodesCount, int maxEdgesCount, int maxWeight,
                                                       @NonNull Supplier<T> nodesSupplier) {
        Assertions.assertTrue(nodesCount >= 3, "Graph must contain at least 3 nodes");
        Assertions.assertTrue(maxEdgesCount >= 2, "Max graph edges must be at least 2");
        Assertions.assertTrue(maxWeight >= 0, "Max graph edges weight must be at least 0");

        Random random = new Random();
        int minEdgesCountInclusive = 1;
        int maxEdgesCountExclusive = maxEdgesCount + 1;
        int maxWeightExclusive = maxWeight + 1;

        DirectedAcyclicGraph<T> dag = new DirectedAcyclicGraph<>();
        dag.edges.put(nodesSupplier.get(), new HashMap<>());
        while (dag.edges.size() < nodesCount) {

            T from = nodesSupplier.get();
            if (dag.edges.containsKey(from)) {
                continue;
            }

            ArrayList<T> toList = new ArrayList<>(dag.edges.keySet());

            int fromEdgesCount = random.nextInt(minEdgesCountInclusive, maxEdgesCountExclusive);
            fromEdgesCount = Math.min(fromEdgesCount, toList.size());
            HashMap<T, Integer> fromEdges = new HashMap<>(fromEdgesCount);
            Util.randomUniqInts(fromEdgesCount, 0, toList.size() - 1)
                    .stream()
                    .map(toList::get)
                    .forEach(to -> fromEdges.put(to, random.nextInt(maxWeightExclusive)));

            dag.edges.put(from, fromEdges);
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
                Assertions.assertFalse(isCyclic(from, to), "Graph must be acyclic");
            }
        }

        HashMap<T, Integer> fromEdges = edges.getOrDefault(from, new HashMap<>());
        fromEdges.put(to, weight);
        edges.putIfAbsent(from, fromEdges);
        edges.putIfAbsent(to, new HashMap<>());
    }

    public void putEdge(@NonNull T from, @NonNull T to) {
        putEdge(from, to, 0);
    }

    public void removeEdge(@NonNull T from, @NonNull T to) {
        Optional.ofNullable(edges.get(from)).ifPresent(fromEdges -> fromEdges.remove(to));

        if (edges.containsKey(to) && edges.get(to).isEmpty()) {
            Stream<T> t = edges.entrySet().parallelStream().flatMap(e -> e.getValue().keySet().stream()).distinct();
            if (t.noneMatch(to::equals)) {
                edges.remove(to);
            }
        }
    }

    @NonNull
    public HashMap<T, HashMap<T, Integer>> getEdges() {
        return (HashMap<T, HashMap<T, Integer>>) edges.entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashMap<>(e.getValue())));
    }

    /**
     * Возвращает не просто искомый узел (6. Поиск в ширину), а кратчайший путь до него.
     */
    @NonNull
    public List<T> breadthFirstSearch(@NonNull T begin, @NonNull Predicate<T> endValidator) {
        Assertions.assertTrue(edges.containsKey(begin), "Graph must contains begin node");

        LinkedList<LinkedList<T>> tracks = new LinkedList<>();
        tracks.add(new LinkedList<>(List.of(begin)));

        HashSet<T> explored = new HashSet<>();

        while (!tracks.isEmpty()) {
            LinkedList<T> track = tracks.poll();
            log.printf("check last node of track %s ... ", Util.collectionJoining(track, " -> "));

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
        edges.forEach((from, nodeEdges) -> {
            sb.append(from);
            if (!nodeEdges.isEmpty()) {
                sb.append(" -> ");
                nodeEdges.forEach((to, weight) -> sb.append(String.format("%s(%d), ", to, weight)));
                sb.delete(sb.length()-2, sb.length());
            }
            sb.append(System.lineSeparator());
        });
        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

    private boolean isCyclic(@NonNull T from, @NonNull T to) {
        for (T next : edges.get(to).keySet()) {
            if (from.equals(next) || isCyclic(from, next)) {
                return true;
            }
        }
        return false;
    }
}
