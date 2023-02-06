package com.cominatyou.util;

/**
 * Ckass for encoding data into a button's ID.
 */
public class ButtonData {
    private final String command;
    private final String ownerId;
    private final String payload;

    private ButtonData(String command, String ownerId, String payload) {
        this.command = command;
        this.ownerId = ownerId;
        this.payload = payload;
    }

    /**
     * Get the command of the button.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Get the user ID of the user who owns the button.
     * @return The user ID of the user who owns the button.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Get the payload of the button.
     * @return The payload of the button.
     */
    public String getPayload() {
        return payload;
    }


    /**
     * Create a new {@code SelectMenuOptionData} object.
     * @param command The command of the button.
     * @param userId The user ID of the user who owns the button.
     * @param payload The payload of the button.
     * @return The new {@code SelectMenuOptionData} object.
     */
    public static ButtonData from(String command, String userId, String payload) {
        return new ButtonData(command, userId, payload);
    }

    /**
     * Encode the data into a string.
     * @return The encoded data.
     */
    public String encode() {
        return String.format("%s-%s-%s", command, ownerId, payload);
    }

    /**
     * Decode the data from a string.
     * @param data The encoded data.
     * @return The decoded data.
     */
    public static ButtonData decode(String data) {
        final String[] parts = data.split("-");

        return new ButtonData(parts[0], parts[1], parts[2]);
    }
}
