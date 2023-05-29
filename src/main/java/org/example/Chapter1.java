package org.example;

import lombok.NonNull;

import java.util.ArrayList;

// Глава 1. Знакомство с алгоритмами
public class Chapter1 {

    public record BinarySearchResult(Integer index, ArrayList<Integer> remains) {
    }

    public static @NonNull BinarySearchResult binarySearch(byte[] list, byte item) {
        int low = 0;
        int high = list.length - 1;
        ArrayList<Integer> remains = new ArrayList<>();

        while (low <= high) {
            remains.add(high - low + 1);
            int mid = (low + high) / 2;
            byte guess = list[mid];

            if (guess == item) {
                return new BinarySearchResult(mid, remains);
            } else if (guess > item) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return new BinarySearchResult(null, remains);
    }
}
