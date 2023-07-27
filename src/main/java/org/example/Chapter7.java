package org.example;

import java.util.*;

// 7. Алгоритм Дейкстры, O(Vertices^2)
public class Chapter7 {
    public static void main(String[] args) {
        example0();
        example1a();
        example1b();
    }

    static void example0() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("start", Map.of("a", 6, "b", 2));
        graph.put("a", Map.of("fin", 1));
        graph.put("b", Map.of("a", 3, "fin", 5));
        graph.put("fin", Map.of());
        dijkstra(graph, "start", "fin");
    }

    static void example1a() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("start", Map.of("a", 5, "b", 2));
        graph.put("a", Map.of("c", 4, "d", 2));
        graph.put("b", Map.of("a", 8, "d", 7));
        graph.put("c", Map.of("d", 6, "fin", 3));
        graph.put("d", Map.of("fin", 1));
        graph.put("fin", Map.of());
        dijkstra(graph, "start", "fin");
    }

    static void example1b() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("start", Map.of("a", 10));
        graph.put("a", Map.of("b", 20));
        graph.put("b", Map.of("c", 1, "fin", 30));
        graph.put("c", Map.of("a", 1));
        graph.put("fin", Map.of());
        dijkstra(graph, "start", "fin");
    }

    static void dijkstra(Map<String, Map<String, Integer>> graph, String from, String to) {
        List<String> track = dijkstraTrack(graph, from, to);
        if (track == null) {
            System.out.println("Cheapest path not found");
        } else {
            System.out.println("Cheapest path found: " + trackToString(graph, track));
        }
    }

    static List<String> dijkstraTrack(Map<String, Map<String, Integer>> graph, String from, String to) {
        Map<String, Integer> costs = new HashMap<>(Map.of(from, 0));
        Set<String> processed = new HashSet<>();
        Map<String, String> parents = new HashMap<>(); // <child, parent>

        String node;
        while ((node = findLowestCostNode(costs, processed)) != null) {
            int cost = costs.get(node);
            Map<String, Integer> neighbors = graph.get(node);
            for (String n : neighbors.keySet()) {
                int newCost = cost + neighbors.get(n);
                if (costs.getOrDefault(n, Integer.MAX_VALUE) > newCost) {
                    costs.put(n, newCost);
                    parents.put(n, node);
                }
            }
            processed.add(node);
        }

        node = to;
        if (!parents.containsKey(node)) {
            return null;
        }

        LinkedList<String> track = new LinkedList<>();
        while (node != null) {
            track.addFirst(node);
            node = parents.get(node);
        }
        return track;
    }

    static String findLowestCostNode(Map<String, Integer> costs, Set<String> processed) {
        int loweatCost = Integer.MAX_VALUE;
        String lowestCostNode = null;
        for (String node : costs.keySet()) {
            int cost = costs.get(node);
            if (cost < loweatCost && !processed.contains(node)) {
                loweatCost = cost;
                lowestCostNode = node;
            }
        }
        return lowestCostNode;
    }

    static String trackToString(Map<String, Map<String, Integer>> graph, List<String> track) {
        StringBuilder sb = new StringBuilder();
        String previous = track.get(0);
        String current = null;
        for (int i = 1; i < track.size(); i++) {
            current = track.get(i);
            sb.append(previous);
            sb.append(" --");
            sb.append(graph.get(previous).get(current));
            sb.append("-> ");
            previous = current;
        }
        sb.append(current);
        return sb.toString();
    }
}
