package com.cominatyou.store;

import java.time.Instant;

import org.javacord.api.interaction.MessageComponentInteractionBase;

public class StoreTimeout {
    /**
     * Checks if the interaction is older than 10 minutes, and if so, prevents the interaction from being completed.
     * @param interaction The interaction to check.
     * @return {@code true} if the interaction is older than 10 minutes, {@code false} otherwise.
     */
    public static boolean checkAndHandleExpiry(MessageComponentInteractionBase interaction) {
        if (interaction.getMessage().getCreationTimestamp().isBefore(Instant.now().minusSeconds(600))) {
            interaction.getMessage()
                .createUpdater()
                .removeAllComponents()
                .removeAllEmbeds()
                .setContent("This store session has expired. If you wish to access the store, please use the </store:1071965429012107486> command.")
                .applyChanges();

            interaction.acknowledge();

            return true;
        }

        return false;
    }
}
