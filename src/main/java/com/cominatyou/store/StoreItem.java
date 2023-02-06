package com.cominatyou.store;

import org.javacord.api.interaction.ButtonInteraction;

/** Items that can be purchased in the store. */
public abstract class StoreItem {
    private final String name;
    private final String description;
    private final String additionalInfo;
    private final String id;
    private final int price;
    private final PurchaseLimit purchaseLimit;

    /**
     * Create a new item to sell in the store.
     * @param name The name of the item.
     * @param description A description of the item.
     * @param additionalInfo Additional information to display along with the description.
     * @param id A unique identifier for the item.
     * @param price The price of the item.
     * @param purchaseLimit The purchase limit of the item.
     * @throws IllegalArgumentException If the provided item ID already is bound to another item.
     */
    public StoreItem(String name, String description, String additionalInfo, String id, int price, PurchaseLimit purchaseLimit) throws IllegalArgumentException {
        this.name = name;
        this.description = description;
        this.additionalInfo = additionalInfo;

        if (StoreItems.getItems() != null && StoreItems.getItems().size() > 0) {
            for (StoreItem i : StoreItems.getItems()) {
                if (i.getId().equals(id)) {
                    throw new IllegalArgumentException("Item ID " + id + " already exists.");
                }
            }
        }

        this.id = id;
        this.price = price;
        this.purchaseLimit = purchaseLimit;
    }

    /**
     * Get the name of the item.
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the item.
     * @return The description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the additional information of the item.
     * @return The additional information of the item.
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Get the ID of the item.
     * @return The ID of the item.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the price of the item.
     * @return The price of the item.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Get the purchase limit of the item.
     * @return The purchase limit of the item.
     */
    public PurchaseLimit getPurchaseLimit() {
        return purchaseLimit;
    }

    /**
     * Check if the item has additional information.
     * @return True if the item has additional information, false otherwise.
     */
    public boolean hasAdditionalInfo() {
        return additionalInfo != null;
    }

    public abstract void giveItem(ButtonInteraction interaction);

    /** Limits on how often an item can be purchased. */
    public static enum PurchaseLimit {
        /** Purchasable once and only once. */
        ONESHOT(-1),
        /** Purchasable once per day. */
        DAILY(1),
        /** Purchasable once every two days. */
        WEEKLY(7),
        /** Purchasable once every two weeks. */
        BI_WEEKLY(14),
        /** Purchasable once every month. */
        MONTHLY(30),
        /** Purchasable as many times and as often as desired. */
        UNLIMITED(-1);

        private final int days;

        private PurchaseLimit(int days) {
            this.days = days;
        }

        /**
         * Get the number of days between purchases.
         * @return The number of days between purchases.
         */
        public int getDays() {
            return days;
        }
    }
}
