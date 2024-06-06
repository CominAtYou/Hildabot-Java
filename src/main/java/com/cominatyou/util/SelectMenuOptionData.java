package com.cominatyou.util;

/**
 * Ckass for encoding data into a select menu option's ID.
 */
public class SelectMenuOptionData {
    private final String ownerId;
    private final String payload;

    private SelectMenuOptionData(String userId, String payload) {
        this.ownerId = userId;
        this.payload = payload;
    }

    /**
     * Get the user ID of the user who owns the select menu.
     * @return The user ID of the user who owns the select menu.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Get the payload of the select menu option.
     * @return The payload of the select menu option.
     */
    public String getPayload() {
        return payload;
    }


    /**
     * Create a new {@code SelectMenuOptionData} object.
     * @param userId The user ID of the user who owns the select menu.
     * @param payload The payload of the select menu option.
     * @return The new {@code SelectMenuOptionData} object.
     */
    public static SelectMenuOptionData from(String userId, String payload) throws IllegalArgumentException {
        if (!userId.matches("[0-9]{17,}")) {
            throw new IllegalArgumentException("The value passed for userId is not a valid snowflake.");
        }

        return new SelectMenuOptionData(userId, payload);
    }

    /**
     * Encode the data into a string.
     * @return The encoded data.
     */
    public String encode() {
        return String.format("%s-%s", ownerId, payload);
    }

    /**
     * Decode the data from the select menu option's value.
     * @param data The select menu option's value.
     * @return The decoded data.
     */
    public static SelectMenuOptionData decode(String data) {
        final String[] parts = data.split("-");

        return new SelectMenuOptionData(parts[0], parts[1]);
    }
}
