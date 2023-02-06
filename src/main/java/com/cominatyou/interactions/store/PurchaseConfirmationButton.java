package com.cominatyou.interactions.store;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.interactions.ButtonClickHandler;
import com.cominatyou.store.StoreItems;
import com.cominatyou.store.StoreTimeout;
import com.cominatyou.util.ButtonData;

public class PurchaseConfirmationButton implements ButtonClickHandler {
    public void execute(ButtonInteraction interaction) {
        final ButtonData data = ButtonData.decode(interaction.getCustomId());
        if (!interaction.getUser().getIdAsString().equals(data.getOwnerId())) {
            interaction.createImmediateResponder().setContent("You can't use this button. If you wish to use the store, please use the </store:1071965429012107486> command.").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        if (StoreTimeout.checkAndHandleExpiry(interaction)) return;

        StoreItems.getItemById(data.getPayload()).giveItem(interaction);
    }
}
