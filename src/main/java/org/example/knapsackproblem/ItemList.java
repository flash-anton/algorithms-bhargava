package org.example.knapsackproblem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * Список вещей для рюкзака.
 */
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
public class ItemList implements Cloneable {
    private List<Item> itemsList = new ArrayList<>();
    private float weight;
    private int value;

    public ItemList add(Item item) {
        Objects.requireNonNull(item, "Item must not be null");
        itemsList.add(item);
        weight += item.weight();
        value += item.value();
        return this;
    }

    public List<Item> itemsList() {
        return new ArrayList<>(itemsList);
    }

    @Override
    public ItemList clone() {
        try {
            ItemList il = (ItemList) super.clone();
            il.itemsList = new ArrayList<>(itemsList);
            il.weight = weight;
            il.value = value;
            return il;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        if (weight == 0) {
            return "{}";
        } else if (itemsList.size() == 1) {
            return itemsList.get(0).toString();
        } else {
            return "{" + itemsList + " w=" + weight + " v=" + value + "}";
        }
    }
}
