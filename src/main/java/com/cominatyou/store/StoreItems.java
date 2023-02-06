package com.cominatyou.store;

import java.util.List;

import com.cominatyou.store.StoreItem.PurchaseLimit;
import com.cominatyou.store.items.*;

public class StoreItems {
    private static final List<StoreItem> items = List.of(
        new SmallXPIncrease("50 XP", "Instantly get an extra 50 XP!", "", "smallxp", 150, PurchaseLimit.UNLIMITED),
        new SmallStreakBoost("3-Day Streak Boost", "Delay your streak expiry 3 days.", "This item can only be purchased once every 7 days.", "smallstreakboost", 280, PurchaseLimit.WEEKLY),
        new LargeSubmitBoost("3 Large Submission Boosts", "Increase the XP you receive for your next 3 submissions by 25%.", "", "largesubmissionboost", 400, PurchaseLimit.UNLIMITED),
        new StreakBoost("7-Day Streak Boost", "Delay your streak expiry by a week.", "This item can only be purchased once every 14 days.", "streakboost", 650, StoreItem.PurchaseLimit.BI_WEEKLY)
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

