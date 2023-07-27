package org.example;

import java.util.*;

// 6. Поиск в ширину, Breadth First Search, BFS, O(Vertices + Edges)
public class Chapter6 {
    static final HashMap<String, List<String>> graph = new HashMap<>();

    static {
        graph.put("you", List.of("alice", "bob", "claire"));
        graph.put("bob", List.of("anuj", "peggy"));
        graph.put("alice", List.of("peggy"));
        graph.put("claire", List.of("thom", "jonny"));
        graph.put("anuj", List.of());
        graph.put("peggy", List.of());
        graph.put("thom", List.of());
        graph.put("jonny", List.of());
    }

    public static void main(String[] args) {
        breadthFirstSearch();

        List<String> track = breadthFirstSearchTrack();
        if (track == null) {
            System.out.println("Mango seller not found");
        } else {
            System.out.println("Mango seller found: " + String.join(" -> ", track));
        }
    }

    static boolean isMangoSeller(String person) {
        return person.endsWith("m");
    }

    // алгоритм из книги
    // требует предварительной проверки "you"
    static void breadthFirstSearch() {
        Queue<String> searchQueue = new LinkedList<>(graph.get("you"));
        HashSet<String> searched = new HashSet<>();
        while (!searchQueue.isEmpty()) {
            String person = searchQueue.poll();
            if (!searched.contains(person)) {
                if (isMangoSeller(person)) {
                    System.out.println(person + " is a mango seller!");
                    return;
                } else {
                    searchQueue.addAll(graph.get(person));
                    searched.add(person);
                }
            }
        }
        System.out.println("Mango seller not found");
    }

    static List<String> breadthFirstSearchTrack() {
        LinkedList<String> searchQueue = new LinkedList<>(List.of("you"));
        HashSet<String> searched = new HashSet<>();
        HashMap<String, String> backLog = new HashMap<>(Map.of("you", "")); // <to, from>
        while (!searchQueue.isEmpty()) {
            String person = searchQueue.poll();
            if (!searched.contains(person)) {
                if (isMangoSeller(person)) {
                    LinkedList<String> track = new LinkedList<>();
                    while (!person.isEmpty()) {
                        track.addFirst(person);
                        person = backLog.get(person);
                    }
                    return track;
                } else {
                    List<String> nextList = graph.get(person);
                    searchQueue.addAll(nextList);
                    searched.add(person);
                    for (String next : nextList) {
                        backLog.put(next, person);
                    }
                }
            }
        }
        return null;
    }
}

