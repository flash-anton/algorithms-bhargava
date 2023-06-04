package org.example;

import java.util.function.Consumer;

public class Ticker {
    private long current = System.nanoTime();

    public void restart() {
        current = System.nanoTime();
    }

    public long getMs() {
        return (System.nanoTime() - current) / 1_000_000;
    }

    public static long measureMs(Runnable r) {
        Ticker t = new Ticker();
        r.run();
        return t.getMs();
    }

    public static <T> long measureMs(T t, Consumer<T> r) {
        Ticker ticker = new Ticker();
        r.accept(t);
        return ticker.getMs();
    }
}
