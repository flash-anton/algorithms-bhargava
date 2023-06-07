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
        quickSort(arr, 0, arr.length-1);
    }

    // быстрая сортировка по книге: массив меньших опорного, опорный, массив не меньших опорного.
    private static void quickSort(int[] arr, int begin, int end) {
        if (begin >= end) {
            return;
        }

        int pivot = begin;
        int pivotValue = arr[end];

        for (int i = begin; i < end; i++) {
            if (arr[i] < pivotValue) {
                int temp = arr[i];
                arr[i] = arr[pivot];
                arr[pivot] = temp;
                pivot++;
            }
        }
        arr[end] = arr[pivot];
        arr[pivot] = pivotValue;

        quickSort(arr, begin, pivot - 1);
        quickSort(arr, pivot + 1, end);
    }

    public static void measureSorts(int arraySize) {
        int[] arr = new Random().ints(arraySize, 1, Integer.MAX_VALUE).toArray();
        System.out.printf("Time measuring for int[%d] array (%d bytes)\n", arr.length, 4 * arr.length);

        System.out.printf("Arrays.sort()               : % 16d\n",
                Ticker.measureMs(Arrays.copyOf(arr, arr.length), Arrays::sort));

        System.out.printf("IntStream.sorted().toArray(): % 16d\n",
                Ticker.measureMs(Arrays.stream(arr), s -> s.sorted().toArray()));

        System.out.printf("quickSort()                 : % 16d\n",
                Ticker.measureMs(Arrays.copyOf(arr, arr.length), Chapter4::quickSort));
    }
}
