package org.example;

import java.io.PrintStream;
import java.util.*;

// 9. Динамическое программирование
public class Chapter9 {
    public static void main(String[] args) {
        backpackProblem();
    }

    static void backpackProblem() {
        LinkedHashSet<Item> items;
        Backpack backpack = new Backpack(4, null);

        System.out.println("---- Гитара, Магнитофон, Ноутбук");
        items = new LinkedHashSet<>(List.of(
                new Item("Г", 1, 1500),
                new Item("М", 4, 3000),
                new Item("Н", 3, 2000)));
        System.out.println(backpack.putMostExpensive(items));

        System.out.println("---- Гитара, Магнитофон, Ноутбук, iPhone");
        items = new LinkedHashSet<>(List.of(
                new Item("Г", 1, 1500),
                new Item("М", 4, 3000),
                new Item("Н", 3, 2000),
                new Item("I", 1, 2000)));
        System.out.println(backpack.putMostExpensive(items));

        System.out.println("---- Гитара, Магнитофон, Ноутбук, iPhone, MP3-плеер");
        items = new LinkedHashSet<>(List.of(
                new Item("Г", 1, 1500),
                new Item("М", 4, 3000),
                new Item("Н", 3, 2000),
                new Item("I", 1, 2000),
                new Item("П", 1, 1000)));
        System.out.println(backpack.putMostExpensive(items));

        System.out.println("---- Магнитофон, Ноутбук, Гитара");
        items = new LinkedHashSet<>(List.of(
                new Item("М", 4, 3000),
                new Item("Н", 3, 2000),
                new Item("Г", 1, 1500)));
        System.out.println(backpack.putMostExpensive(items));

        System.out.println("---- Гитара, Магнитофон, Ноутбук, Ожерелье");
        items = new LinkedHashSet<>(List.of(
                new Item("Г", 1, 1500),
                new Item("М", 4, 3000),
                new Item("Н", 3, 2000),
                new Item("О", 0.5f, 1000)));
        System.out.println(backpack.putMostExpensive(items));

        System.out.println("---- Вестминстер, Глобус, Галерея, Музей, Собор");
        items = new LinkedHashSet<>(List.of(
                new Item("W", 0.5f, 7),
                new Item("G", 0.5f, 6),
                new Item("N", 1.0f, 9),
                new Item("M", 2.0f, 9),
                new Item("S", 0.5f, 8)));
        Backpack backpack2 = new Backpack(2, null);
        System.out.println(backpack2.putMostExpensive(items));

        System.out.println("---- Гитара, Магнитофон, Ноутбук, Бриллиант");
        items = new LinkedHashSet<>(List.of(
                new Item("Г", 1, 1500),
                new Item("М", 4, 3000),
                new Item("Н", 3, 2000),
                new Item("Б", 3.5f, 1_000_000)));
        System.out.println(backpack.putMostExpensive(items));

        System.out.println("---- Вода, Книга, Еда, Куртка, Камера");
        items = new LinkedHashSet<>(List.of(
                new Item("W", 3, 10),
                new Item("B", 1, 3),
                new Item("F", 2, 9),
                new Item("J", 2, 5),
                new Item("C", 1, 6)));
        Backpack backpack3 = new Backpack(6, null);
        System.out.println(backpack3.putMostExpensive(items));
    }
}

record Item(String name, float weight, int cost) {
    @Override
    public String toString() {
        String w = String.format(weight % 1 == 0 ? "%.0f" : "%.1f", weight);
        return String.format("%s-%s-%d", name, w, cost);
    }
}

class Backpack {
    final float CARRYING;
    final PrintStream OUT;
    TreeMap<Float, LinkedHashSet<Item>> mostExpensiveItems = new TreeMap<>();
    Map<Float, Integer> mostExpensiveItemsCost = new HashMap<>();
    TreeMap<Float, LinkedHashSet<Item>> previousMostExpensiveItems = new TreeMap<>();

    Backpack(float carrying, PrintStream printStream) {
        CARRYING = carrying;
        OUT = printStream;
    }

    LinkedHashSet<Item> putMostExpensive(LinkedHashSet<Item> items) {
        if (items.parallelStream().anyMatch(item -> item.weight() <= 0 || item.weight() > CARRYING)) {
            throw new IllegalArgumentException("Item weight must be in range (0; backpackCarrying]");
        }

        if (items.isEmpty()) {
            return new LinkedHashSet<>();
        }

        mostExpensiveItems = new TreeMap<>(Map.of(0f, new LinkedHashSet<>()));
        mostExpensiveItemsCost = new HashMap<>(Map.of(0f, 0));

        for (Item item : items) {
            putItem(item);
            if (OUT != null) {
                OUT.println(mostExpensiveItems);
            }
        }

        return mostExpensiveItems.lastEntry().getValue();
    }

    private void putItem(Item item) {
        previousMostExpensiveItems = new TreeMap<>(mostExpensiveItems);
        for (float carrying : previousMostExpensiveItems.keySet()) {
            putItem(item, carrying);
            putItem(item, Float.sum(carrying, item.weight()));
        }
    }

    private void putItem(Item item, float newCarrying) {
        if (newCarrying < item.weight() || newCarrying > CARRYING) {
            return;
        }

        float remainderCarrying = previousMostExpensiveItems.floorKey(newCarrying - item.weight());
        int remainderCost = mostExpensiveItemsCost.get(remainderCarrying);

        float oldCarrying = previousMostExpensiveItems.floorKey(newCarrying);
        int oldCost = mostExpensiveItemsCost.get(oldCarrying);

        if (Integer.sum(item.cost(), remainderCost) > oldCost) {
            LinkedHashSet<Item> newItems = new LinkedHashSet<>(Set.of(item));
            newItems.addAll(previousMostExpensiveItems.get(remainderCarrying));
            mostExpensiveItems.put(newCarrying, newItems);
            mostExpensiveItemsCost.put(newCarrying, newItems.parallelStream().mapToInt(Item::cost).sum());
        }
    }
}
