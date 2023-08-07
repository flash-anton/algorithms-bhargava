package org.example.knapsackproblem;

import java.io.PrintStream;
import java.util.*;

/**
 * <pre>
 * Задача о рюкзаке - выбрать вещи максимальной суммарной ценности, помещающиеся в рюкзак.
 * Решение задачи о рюкзаке методом динамического программирования.
 *
 * В книге рассматриваются:
 * - грузоподъемность рюкзака (W) - натуральное число.
 * - вес вещи (wi) - натуральное число или положительное вещественное кратное 0.5.
 * - ценность вещи (vi) - натуральное число.
 * - вещи в единственном экземпляре.
 * - кеш максимальной ценности в виде таблицы m[n,W], притом wi - натуральное число или положительное вещественное кратное 0.5.
 * - m[i,wi] содержит первый вычисленный набор вещей среди всех наборов с одинаковым весом и максимальной ценностью.
 *
 * Отличие реализации:
 * - грузоподъемность рюкзака (W) - положительное вещественное число.
 * - вещи не в единственном экземпляре.
 * - кеш максимальной ценности в виде набора списков вещей.
 * - сохраняются все наборы вещей с одинаковым весом и ценностью.
 * </pre>
 */
public class KnapsackProblem {
    private final float CARRYING;
    private final PrintStream OUT;
    private static final ItemListSet EMPTY_ILS = new ItemListSet().add(new ItemList());
    private TreeMap<Float, ItemListSet> ilsByWeight;
    private ItemListSet ilsMostExpensive;

    public KnapsackProblem(float carrying, PrintStream printStream) {
        CARRYING = carrying;
        OUT = printStream;
        clear();
    }

    public KnapsackProblem add(Collection<Item> items) {
        for (Item i : items) {
            add(i);
        }
        return this;
    }

    public KnapsackProblem add(Item item) {
        TreeMap<Float, ItemListSet> tmp = new TreeMap<>(ilsByWeight);
        for (ItemListSet ils : ilsByWeight.values()) {
            ItemListSet ilsNew = ils.clone().add(item);
            if (ilsNew.weight() <= CARRYING) {
                ItemListSet ilsOld = tmp.getOrDefault(ilsNew.weight(), new ItemListSet());
                if (ilsNew.value() == ilsOld.value()) {
                    ilsOld.add(ilsNew);
                } else if (ilsNew.value() > ilsOld.value()) {
                    tmp.put(ilsNew.weight(), ilsNew);
                }
            }
        }
        ilsByWeight = tmp;

        int previousValue = 0;
        Iterator<Map.Entry<Float, ItemListSet>> it = ilsByWeight.entrySet().iterator();
        while (it.hasNext()) {
            int value = it.next().getValue().value();
            if (value < previousValue) {
                it.remove();
            } else {
                previousValue = value;
            }
        }

        if (OUT != null) {
            OUT.println(ilsByWeight);
        }

        ilsMostExpensive = ilsByWeight.lastEntry().getValue();

        return this;
    }

    public ItemListSet solving() {
        return ilsMostExpensive.clone();
    }

    public KnapsackProblem clear() {
        ilsByWeight = new TreeMap<>(Map.of(0f, EMPTY_ILS));
        ilsMostExpensive = EMPTY_ILS;
        return this;
    }
}
