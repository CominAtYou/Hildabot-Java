package com.cominatyou.store;

import java.util.List;

import com.cominatyou.store.StoreItem.PurchaseLimit;
import com.cominatyou.store.items.*;

public class StoreItems {
    private static final List<StoreItem> items = List.of(
        new SmallXPIncrease("50 XP", "Instantly get an extra 50 XP!", null, "smallxp", 150, PurchaseLimit.UNLIMITED),
        new MediumXPIncrease("250 XP", "Instantly get an extra 250 XP!", null, "mediumxp", 300, PurchaseLimit.UNLIMITED),
        new MediumSubmitBoost("3 Medium Submission Boosts", "Increase the XP you receive for your next 3 submissions by 10%.", "This item can only be purchased once every seven days.", "mediumsubmissionboost", 200, PurchaseLimit.WEEKLY),
        new LargeSubmitBoost("3 Large Submission Boosts", "Increase the XP you receive for your next 3 submissions by 25%.", "This item can only be purchased once every seven days.", "largesubmissionboost", 400, PurchaseLimit.WEEKLY)
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
