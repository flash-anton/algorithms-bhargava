package org.example;

import java.util.*;
import java.util.stream.Collectors;

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

    static boolean isSimple(Node node) {
        return SIMPLE_NUMBERS.contains(node.value);
    }

    public static void main(String[] args) {
        int nodesCount = 9;
        int minNodeValue = 0;
        int maxNodeValue = 1000;
        int maxLinksCount = 2;
        Graph graph = new Graph().generate(nodesCount, minNodeValue, maxNodeValue, maxLinksCount);
        System.out.println(graph);

        StringBuilder msg;

        Node node = breadthFirstSearchFromBook(graph.nodes.get(0));
        msg = new StringBuilder();
        if (node == null) {
            msg.append("not found");
        } else {
            msg.append(node.value).append(" is simple");
        }
        System.out.println(msg.append(System.lineSeparator()));

        List<Node> track = breadthFirstSearchWithTrack(graph.nodes.get(0));
        msg = new StringBuilder("track to simple: ");
        if (track.isEmpty()) {
            msg.append("not found");
        } else {
            msg.append(trackToString(track));
        }
        System.out.println(msg.append(System.lineSeparator()));
    }

    // из книги - просто находит ближайшее простое число
    static Node breadthFirstSearchFromBook(Node root) {
        List<Node> queue = new ArrayList<>(List.of(root));
        Set<Node> explored = new HashSet<>();

        while (!queue.isEmpty()) {
            Node node = queue.remove(0);
            if (!explored.contains(node)) {
                if (isSimple(node)) {
                    return node;
                } else {
                    System.out.println(node.value);
                    explored.add(node);
                    queue.addAll(node.out);
                }
            }
        }
        return null;
    }

    // находит кратчайший путь до ближайшего простого числа
    static List<Node> breadthFirstSearchWithTrack(Node root) {
        List<List<Node>> tracksQueue = new ArrayList<>();
        tracksQueue.add(new ArrayList<>(List.of(root)));

        Set<Node> explored = new HashSet<>();

        while (!tracksQueue.isEmpty()) {
            List<Node> track = tracksQueue.remove(0);

            Node node = track.get(track.size() - 1);
            if (explored.contains(node)) {
                continue;
            }

            if (isSimple(node)) {
                return track;
            }

            System.out.println("checking simple: " + trackToString(track));
            explored.add(node);

            for (Node outNode : node.out) {
                List<Node> outTrack = new ArrayList<>(track);
                outTrack.add(outNode);
                tracksQueue.add(outTrack);
            }
        }

        return new ArrayList<>();
    }

    static class Graph {
        List<Node> nodes = new ArrayList<>();

        Graph generate(int nodesCount, int minNodeValue, int maxNodeValue, int maxLinksCount) {
            nodes = randomUniqInts(nodesCount, minNodeValue, maxNodeValue)
                    .stream()
                    .map(Node::new)
                    .toList();

            nodes.forEach(node -> {
                List<Node> list = randomUniqInts(maxLinksCount, 0, nodes.size() - 1)
                        .stream()
                        .map(nodes::get)
                        .filter(n -> !n.equals(node))
                        .toList();
                node.out.addAll(list);
            });

            return this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            nodes.forEach(node -> sb.append(node).append(System.lineSeparator()));
            return sb.toString();
        }
    }

    static class Node {
        final int value;
        final List<Node> out = new ArrayList<>();

        Node(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + " -> " + out.stream().map(n -> String.valueOf(n.value)).collect(Collectors.joining(","));
        }
    }

    static Set<Integer> randomUniqInts(int count, int min, int max) {
        HashSet<Integer> set = new HashSet<>(count);
        int maxExclusive = max + 1;
        int rest;
        while ((rest = count - set.size()) > 0) {
            set.addAll(new Random().ints(rest, min, maxExclusive).distinct().boxed().collect(Collectors.toSet()));
        }
        return set;
    }

    static String trackToString(List<Node> track) {
        return track.stream().map(node -> String.valueOf(node.value)).collect(Collectors.joining(" -> "));
    }
}

