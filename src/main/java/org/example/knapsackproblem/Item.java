package org.example.knapsackproblem;

import java.util.Objects;

/**
 * Вещь для рюкзака.
 */
public record Item(String name, float weight, int value) {
    public Item(String name, float weight, int value) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.weight = requirePositive(weight, "Weight must be positive");
        this.value = requirePositive(value, "Value must be positive");
    }

    @Override
    public String toString() {
        return "{" + name + " " + weight + " " + value + "}";
    }

    private static <T extends Number> T requirePositive(T obj, String message) {
        if (obj.doubleValue() <= 0)
            throw new IllegalArgumentException(message);
        return obj;
    }
}
