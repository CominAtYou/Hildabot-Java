package com.cominatyou.interactions.store;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.interactions.ButtonClickHandler;
import com.cominatyou.slashcommands.Store;
import com.cominatyou.store.StoreTimeout;
import com.cominatyou.util.ButtonData;

public class ReturnToMenuButton implements ButtonClickHandler {
    public void execute(ButtonInteraction interaction) {
        if (!interaction.getUser().getIdAsString().equals(ButtonData.decode(interaction.getCustomId()).getOwnerId())) {
            interaction.createImmediateResponder().setContent("You can't use this button. If you wish to use the store, please use the </store:1071965429012107486> command.").setFlags(MessageFlag.EPHEMERAL).respond();
        }
        else {
            if (StoreTimeout.checkAndHandleExpiry(interaction)) return;

            new Store().execute(interaction);
        }
    }
}
