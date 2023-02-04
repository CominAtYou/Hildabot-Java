package com.cominatyou.store;

import java.util.List;

public class StoreItems {
    private static final List<StoreItem> items = List.of(
        new StoreItem("Streak Boost", "Increases your streak by a week.", "This item can only be purchased once every two weeks.", "streakboost", 650, StoreItem.PurchaseLimit.BI_WEEKLY)
    );

    public static List<StoreItem> getItems() {
        return items;
    }

    public static StoreItem getItemById(String id) {
        for (StoreItem i : items) {
            if (i.getId().equals(id)) {
                return i;
            }
        }

        return null;
    }
}

