package org.example;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

// 4. Быстрая сортировка
public class Chapter4 {
    public static void main(String[] args) {

        int[] arr = new Random().ints(20, 1, 128).toArray();
        assertEquals(Arrays.stream(arr).sum(), sum(arr, arr.length));
        assertEquals(Arrays.stream(arr).count(), count(arr, arr.length));
        assertEquals(Arrays.stream(arr).max().orElse(0), max(arr, arr.length));

        int[] copy1 = Arrays.copyOf(arr, arr.length);
        Arrays.sort(copy1);
        int[] copy2 = Arrays.copyOf(arr, arr.length);
        quickSort(copy2);
        assertArrayEquals(copy1, copy2);

        measureSorts(125_000_000);
    }


    public static int sum(int[] arr, int size) {
        if (size == 0) {
            return 0;
        } else {
            size--;
            return arr[size] + sum(arr, size);
        }
    }

    public static int count(int[] arr, int size) {
        if (size == 0) {
            return 0;
        } else {
            size--;
            return 1 + count(arr, size);
        }
    }

    public static int max(int[] arr, int size) {
        if (size == 0) {
            return Integer.MIN_VALUE;
        } else {
            size--;
            return Math.max(arr[size], max(arr, size));
        }
    }

    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length);
    }

    // быстрая сортировка по книге: массив меньших опорного, опорный, массив не меньших опорного.
    // время и память хуже библиотечных примерно в 2 раза.
    private static void quickSort(int[] arr, int offset, int size) {
        if (size < 2) {
            return;
        }

        int smallerSize = 0;
        int notSmallerOffset = size;
        {
            int[] buf = new int[size];
            int mid = size / 2;
            int pivot = arr[offset + mid];

            for (int i = 0; i < size; i++) {
                if (i == mid) {
                    continue;
                }
                int arrItem = arr[offset + i];
                if (arrItem < pivot) {
                    buf[smallerSize++] = arrItem;
                } else {
                    buf[--notSmallerOffset] = arrItem;
                }
            }

            buf[smallerSize] = pivot;

            System.arraycopy(buf, 0, arr, offset, size);
        }

        quickSort(arr, offset, smallerSize);
        quickSort(arr, offset + notSmallerOffset, size - notSmallerOffset);
    }

    public static void measureSorts(int arraySize) {
        int[] arr = new Random().ints(arraySize, 1, Integer.MAX_VALUE).toArray();
        System.out.printf("Time measuring for int[%d] array (%d bytes)\n", arr.length, 4 * arr.length);

        System.out.printf("Arrays.sort()               : % 16d\n", // 0.5 GB -> 11 sec 1.25 GB
                Ticker.measureMs(Arrays.copyOf(arr, arr.length), Arrays::sort));

        System.out.printf("IntStream.sorted().toArray(): % 16d\n", // 0.5 GB -> 12 sec 1.25 GB
                Ticker.measureMs(Arrays.stream(arr), s -> s.sorted().toArray()));

        System.out.printf("quickSort2()                : % 16d\n", // 0.5 GB -> 24 sec 2.5 GB
                Ticker.measureMs(Arrays.copyOf(arr, arr.length), Chapter4::quickSort));
    }
}
