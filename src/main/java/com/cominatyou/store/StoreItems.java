package com.cominatyou.store;

import java.util.List;

import com.cominatyou.store.items.*;

public class StoreItems {
    private static final List<StoreItem> items = List.of(
        new SmallXPIncrease(),
        new MediumSubmitBoost(),
        new MediumXPIncrease(),
        new LargeSubmitBoost()
    );

    public static List<StoreItem> getItems() {
        return items;
    }

    public static StoreItem getItemById(String id) {
        return items.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }
}
