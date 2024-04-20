package com.cominatyou.interactions.store;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.interactions.ButtonClickHandler;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.StoreItems;
import com.cominatyou.store.StoreTimeout;
import com.cominatyou.util.ButtonData;
import com.cominatyou.util.logging.Log;

public class PurchaseConfirmationButton implements ButtonClickHandler {
    public void execute(ButtonInteraction interaction) {
        final ButtonData data = ButtonData.decode(interaction.getCustomId());
        if (!interaction.getUser().getIdAsString().equals(data.getOwnerId())) {
            interaction.createImmediateResponder().setContent("You can't use this button. If you wish to use the store, please use the </store:1071965429012107486> command.").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        if (StoreTimeout.checkAndHandleExpiry(interaction)) return;

        final StoreItem item = StoreItems.getItemById(data.getPayload());
        item.giveItem(interaction);

        Log.eventf("Store", "%s (%d) purchased %s for %d tokens.", interaction.getUser().getName(), interaction.getUser().getId(), item.getName(), item.getPrice());
    }
}
