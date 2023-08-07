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

        System.out.println("\nlongestCommonSubstringLength");
        System.out.println(longestCommonSubstringLength("hish", "fish") + " for hish and fish");
        System.out.println(longestCommonSubstringLength("hish", "vista") + " for hish and vista");

        System.out.println("\nlongestCommonSubsequenceLength");
        System.out.println(longestCommonSubsequenceLength("fosh", "fish") + " for fosh and fish");
        System.out.println(longestCommonSubsequenceLength("fosh", "fort") + " for fosh and fort");

        System.out.println("\nlongestCommonSubstringLength");
        System.out.println(longestCommonSubstringLength("blue", "clues") + " for blue and clues");
    }

    static int longestCommonSubstringLength(String a, String b) {
        int[][] cell = new int[a.length() + 1][b.length() + 1];

        int max = 0;
        for (int i = 1; i < a.length() + 1; i++) {
            for (int j = 1; j < b.length() + 1; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    cell[i][j] = cell[i - 1][j - 1] + 1;
                    if (cell[i][j] > max) {
                        max = cell[i][j];
                    }
                }
            }
        }
        return max;
    }

    static int longestCommonSubsequenceLength(String a, String b) {
        int[][] cell = new int[a.length() + 1][b.length() + 1];

        int max = 0;
        for (int i = 1; i < a.length() + 1; i++) {
            for (int j = 1; j < b.length() + 1; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    cell[i][j] = cell[i - 1][j - 1] + 1;
                    if (cell[i][j] > max) {
                        max = cell[i][j];
                    }
                } else {
                    cell[i][j] = Math.max(cell[i - 1][j], cell[i][j - 1]);
                }
            }
        }
        return max;
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
        System.out.println(">> + " + iPhone);
        System.out.println("   " + knapsackProblem.add(iPhone).solving());

        Item mp3Player = new Item("MP3-плеер", 1, 1000);
        System.out.println(">> + " + mp3Player);
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
