package org.example.knapsackproblem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * Наборы списков вещей для рюкзака одного веса и ценности.
 */
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
public class ItemListSet implements Cloneable {
    private LinkedHashSet<ItemList> itemListSet = new LinkedHashSet<>();
    private float weight;
    private int value;

    public ItemListSet add(ItemListSet ils) {
        Objects.requireNonNull(ils, "ItemListSet must not be null");
        if (itemListSet.isEmpty()) {
            weight = ils.weight();
            value = ils.value();
        } else {
            requireTrue(ils.weight() == weight, "ItemListSet weight must be " + weight);
            requireTrue(ils.value() == value, "ItemListSet value must be " + value);
        }
        for (ItemList il : ils.itemListSet) {
            itemListSet.add(il.clone());
        }
        return this;
    }

    public ItemListSet add(ItemList il) {
        Objects.requireNonNull(il, "ItemList must not be null");
        if (itemListSet.isEmpty()) {
            weight = il.weight();
            value = il.value();
        } else {
            requireTrue(il.weight() == weight, "ItemList weight must be " + weight);
            requireTrue(il.value() == value, "ItemList value must be " + value);
        }
        itemListSet.add(il.clone());
        return this;
    }

    public ItemListSet add(Item item) {
        if (itemListSet.isEmpty()) {
            itemListSet.add(new ItemList());
        }
        itemListSet.forEach(il -> il.add(item));
        weight += item.weight();
        value += item.value();
        return this;
    }

    public Set<ItemList> itemListSet() {
        return new LinkedHashSet<>(itemListSet);
    }

    @Override
    public ItemListSet clone() {
        try {
            ItemListSet ils = (ItemListSet) super.clone();
            ils.itemListSet = new LinkedHashSet<>();
            for (ItemList il : itemListSet) {
                ils.itemListSet.add(il.clone());
            }
            ils.weight = weight;
            ils.value = value;
            return ils;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        if (weight == 0) {
            return "{}";
        } else if (itemListSet.size() == 1) {
            return itemListSet.iterator().next().toString();
        } else {
            StringBuilder sb = new StringBuilder("{[");
            for (ItemList il : itemListSet) {
                List<Item> itemsList = il.itemsList();
                if (itemsList.size() == 1) {
                    sb.append(itemsList.get(0).toString());
                } else {
                    sb.append(itemsList);
                }
                sb.append(", ");
            }
            sb.delete(sb.length()-2, sb.length());
            sb.append("] w=").append(weight).append(" v=").append(value).append("}");
            return sb.toString();
        }
    }

    private static void requireTrue(boolean expression, String message) {
        if (!expression)
            throw new IllegalArgumentException(message);
    }
}
