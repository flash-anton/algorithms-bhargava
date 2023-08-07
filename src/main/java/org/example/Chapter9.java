package org.example;

import org.example.knapsackproblem.Item;
import org.example.knapsackproblem.KnapsackProblem;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

// 9. Динамическое программирование
public class Chapter9 {
    static final PrintStream OUT = new PrintStream(OutputStream.nullOutputStream());//System.out;

    public static void main(String[] args) {
        knapsackProblem();
    }

    static void knapsackProblem() {
        List<Item> items;
        KnapsackProblem knapsackProblem = new KnapsackProblem(4, OUT);

        Item guitar = new Item("Гитара", 1, 1500);
        Item tapeRecorder = new Item("Магнитофон", 4, 3000);
        Item notebook = new Item("Ноутбук", 3, 2000);
        System.out.println(">> " + (items = List.of(guitar, tapeRecorder, notebook)));
        System.out.println("   " + knapsackProblem.add(items).solving());

        Item iPhone = new Item("iPhone", 1, 2000);
        System.out.println(">> + " + iPhone );
        System.out.println("   " + knapsackProblem.add(iPhone).solving());

        Item mp3Player = new Item("MP3-плеер", 1, 1000);
        System.out.println(">> + " + mp3Player );
        System.out.println("   " + knapsackProblem.add(mp3Player).solving());

        System.out.println(">> " + (items = List.of(tapeRecorder, notebook, guitar)));
        System.out.println("   " + knapsackProblem.clear().add(items).solving());

        Item necklace = new Item("Ожерелье", 0.5f, 1000);
        System.out.println(">> " + (items = List.of(guitar, tapeRecorder, notebook, necklace)));
        System.out.println("   " + knapsackProblem.clear().add(items).solving());

        items = List.of(
                new Item("Вестминстер", 0.5f, 7),
                new Item("Глобус", 0.5f, 6),
                new Item("Галерея", 1.0f, 9),
                new Item("Музей", 2.0f, 9),
                new Item("Собор", 0.5f, 8));
        System.out.println(">> " + items);
        System.out.println("   " + new KnapsackProblem(2, OUT).add(items).solving());

        Item diamond = new Item("Бриллиант", 3.5f, 1_000_000);
        System.out.println(">> " + (items = List.of(guitar, tapeRecorder, notebook, diamond)));
        System.out.println("   " + knapsackProblem.clear().add(items).solving());

        items = List.of(
                new Item("Вода", 3, 10),
                new Item("Книга", 1, 3),
                new Item("Еда", 2, 9),
                new Item("Куртка", 2, 5),
                new Item("Камера", 1, 6));
        System.out.println(">> " + items);
        System.out.println("   " + new KnapsackProblem(6, OUT).add(items).solving());
    }
}
