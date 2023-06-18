package org.example;

import org.example.graph.DirectedAcyclicGraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// 6. Поиск в ширину
public class Chapter6 {
    static final List<Integer> SIMPLE_NUMBERS = List.of(
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
            101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199,
            211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293,
            307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397,
            401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499,
            503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599,
            601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691,
            701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797,
            809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887,
            907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997);

    public static void main(String[] args) {
        checkBookExample();
        checkGenerated();
    }

    static void checkBookExample() {
        DirectedAcyclicGraph<String> graph = new DirectedAcyclicGraph<>();
        graph.putEdges("you", List.of("alice", "bob", "claire"));
        graph.putEdges("bob", List.of("anuj", "peggy"));
        graph.putEdges("alice", List.of("peggy"));
        graph.putEdges("claire", List.of("thom", "jonny"));
        System.out.println(graph);

        graph.setLog(System.out);
        List<String> track = graph.breadthFirstSearch("you", end -> end.endsWith("m"));
        if (track.isEmpty()) {
            System.out.println("track to mango seller not found");
        } else {
            System.out.println("track to mango seller is " + Util.joining(track, " -> "));
        }
    }

    static void checkGenerated() {
        int nodesCount = 9;
        int minNodeValue = 0;
        int maxNodeValue = 1000;
        int maxEdgesCount = 3;
        int maxWeight = 0;
        AtomicInteger node = new AtomicInteger();
        DirectedAcyclicGraph<Integer> graph = DirectedAcyclicGraph.generate(nodesCount, maxEdgesCount, maxWeight,
                () -> node.updateAndGet(v -> new Random().nextInt(minNodeValue, maxNodeValue)));
        System.out.println(graph);

        graph.setLog(System.out);
        List<Integer> track = graph.breadthFirstSearch(node.get(), SIMPLE_NUMBERS::contains);
        if (track.isEmpty()) {
            System.out.println("track to simple not found");
        } else {
            System.out.println("track to simple is " + Util.joining(track, " -> "));
        }
    }
}

