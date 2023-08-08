package org.example;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

// 10. Алгоритм k ближайших соседей.
public class Chapter10 {
    public static void main(String[] args) {
        Item priyanka = new Item("priyanka", List.of(3, 4, 4, 1, 4));
        Item justin = new Item("justin", List.of(4, 3, 5, 1, 5));
        Item morpheus = new Item("morpheus", List.of(2, 5, 1, 3, 1));
        priyanka.distance(justin, false);
        priyanka.distance(morpheus, false);

        System.out.println("----");
        Item yogi = new Item("yogi", List.of(2, 5, 5, 1, 5));
        priyanka.distance(yogi, false);
        priyanka.distance(yogi, true);

        System.out.println("----");
        Item tarantino = new Item("tarantino", 3, List.of(3, 4, 4, 2, 4));
        Item.regression(Map.of(yogi, 1, justin, 5));
        Item.regression(Map.of(yogi, 1, tarantino, 5));

        System.out.println("----");
        Item jc = new Item("jc", List.of(1, 1, 1, 1, 1));
        Item joseph = new Item("joseph", List.of(2, 2, 2, 2, 2));
        Item lans = new Item("lans", List.of(3, 3, 3, 3, 3));
        Item cris = new Item("cris", List.of(4, 4, 4, 4, 4));
        Item.regression(Map.of(justin, 5, jc, 4, joseph, 4, lans, 5, cris, 3));

        System.out.println("----");
        Item a = new Item("a", List.of(5, 1, 0));
        Item b = new Item("b", List.of(3, 1, 1));
        Item c = new Item("c", List.of(1, 1, 0));
        Item d = new Item("d", List.of(4, 0, 1));
        Item e = new Item("e", List.of(4, 0, 0));
        Item f = new Item("f", List.of(2, 0, 0));
        Map<Item, Integer> breads = Map.of(a, 300, b, 225, c, 75, d, 200, e, 150, f, 50);
        Item today = new Item("today", List.of(4, 1, 0));
        Set<Item> nearestDays = today.similarItems(4, false, Set.of(a, b, c, d, e, f));
        Map<Item, Integer> breadsOfNearest = nearestDays.stream().collect(Collectors.toMap(i -> i, breads::get));
        Item.regression(breadsOfNearest);

        System.out.println("----");
        Set<Item> viewers = Set.of(justin, morpheus, yogi, tarantino, jc, joseph, lans, cris);
        int k = (int) Math.round(Math.sqrt(viewers.size()));
        priyanka.similarItems(k, false, viewers);
        priyanka.similarItems(k, true, viewers);
    }
}

@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
class Item {
    private String name;
    private int authority;
    private List<Integer> features;
    private double averageFeaturesGrade;

    Item(String name, int authority, List<Integer> features) {
        this.name = name;
        this.authority = authority;
        this.features = List.copyOf(features);
        this.averageFeaturesGrade = features.stream().mapToInt(Integer::intValue).average().orElseThrow();
    }

    Item(String name, List<Integer> features) {
        this(name, 1, features);
    }

    double distance(Item item, boolean normalize) {
        double n = normalize ? item.averageFeaturesGrade / averageFeaturesGrade : 1;
        double sum = 0;
        for (int i = 0; i < features.size(); i++) {
            double difference = n * features.get(i) - item.features.get(i);
            sum += difference * difference;
        }
        double result = Math.sqrt(sum);
        System.out.printf("%s.distance(%s, %b) = %.2f%n", name, item.name, normalize, result);
        return result;
    }

    Set<Item> similarItems(int k, boolean normalize, Set<Item> items) {
        Map<Item, Double> f = items.stream().collect(Collectors.toMap(i -> i, i -> distance(i, normalize)));
        Set<Item> result = items.stream().sorted(Comparator.comparingDouble(f::get)).limit(k).collect(Collectors.toSet());
        System.out.printf("%s.similarItems(%d, %b, %s) = %s%n", name, k, normalize, items, result);
        return result;
    }

    static double regression(Map<Item, Integer> gradeByItem) {
        int n = 0;
        double sum = 0;
        for (Item item : gradeByItem.keySet()) {
            n += item.authority;
            sum += gradeByItem.get(item) * item.authority;
        }
        double result = sum / n;
        System.out.printf("regression(%s) = %.2f%n", gradeByItem, result);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
