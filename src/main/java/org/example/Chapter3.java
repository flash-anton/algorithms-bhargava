package org.example;

public class Chapter3 {
    public static void main(String[] args) {
        int x = 10;
        countdown(x);

        System.out.printf("fact(%d) = ", x);
        long f = fact(x);
        System.out.printf(" = %d", f);
    }

    public static void countdown(int i) {
        System.out.println(i);
        if (i > 0) {
            countdown(--i);
        }
    }

    public static long fact(int x) {
        if (x == 1) {
            System.out.print(1);
            return 1;
        } else {
            System.out.printf("%d*", x);
            return x * fact(--x);
        }
    }
}
