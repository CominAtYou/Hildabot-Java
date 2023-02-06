package com.cominatyou.interactions.store;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SelectMenuInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.interactions.SelectMenuChooseHandler;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.StoreItems;
import com.cominatyou.store.StoreTimeout;
import com.cominatyou.util.ButtonData;
import com.cominatyou.util.SelectMenuOptionData;

public class StoreEvent implements SelectMenuChooseHandler {
    public void execute(SelectMenuInteraction interaction) {
        final User user = interaction.getUser();
        final RedisUserEntry userEntry = new RedisUserEntry(user);
        final SelectMenuOptionData data = SelectMenuOptionData.decode(interaction.getChosenOptions().get(0).getValue());
        final boolean selectorIsAuthor = data.getOwnerId().equals(user.getIdAsString());

        if (!selectorIsAuthor) {
            interaction.createImmediateResponder()
                .setContent("You can't use this menu. If you wish to access the store, please use the </store:1071965429012107486> command.")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond();
            return;
        }

        if (StoreTimeout.checkAndHandleExpiry(interaction)) return;

        final String selectedOption = data.getPayload();
        final StoreItem item = StoreItems.getItemById(selectedOption);

        if (userEntry.getInt("tokens") < item.getPrice()) {
            interaction.getMessage().createUpdater()
                .removeAllComponents()
                .removeAllEmbeds()
                .setContent("You don't have enough tokens to purchase this item.")
                .addComponents(ActionRow.of(new ButtonBuilder().setCustomId(ButtonData.from("store_return_to_menu", user.getIdAsString(), null).encode()).setStyle(ButtonStyle.PRIMARY).setLabel("OK").build()))
                .applyChanges();

            interaction.acknowledge();
            return;
        }

        final long purchasableAgain = userEntry.getLong("items:" + item.getId() + ":purchasableagain");

        if (purchasableAgain != 0) {
            interaction.getMessage().createUpdater()
                .removeAllComponents()
                .removeAllEmbeds()
                .setContent(String.format("You can't purchase this item yet. You can purchase it again <t:%d:R>.", purchasableAgain))
                .addComponents(ActionRow.of(new ButtonBuilder().setCustomId(ButtonData.from("store_return_to_menu", user.getIdAsString(), null).encode()).setStyle(ButtonStyle.PRIMARY).setLabel("OK").build()))
                .applyChanges();

            interaction.acknowledge();
            return;
        }

        interaction.getMessage().createUpdater()
            .removeAllComponents()
            .removeAllEmbeds()
            .setContent(String.format("Are you sure you want to purchase %s for %d tokens?", item.getName(), item.getPrice()))
            .addComponents(ActionRow.of(
                new ButtonBuilder().setCustomId(ButtonData.from("store_confirm_purchase", user.getIdAsString(), item.getId()).encode()).setStyle(ButtonStyle.PRIMARY).setLabel("Yes").build(),
                new ButtonBuilder().setCustomId(ButtonData.from("store_return_to_menu", user.getIdAsString(), null).encode()).setStyle(ButtonStyle.SECONDARY).setLabel("No").build()
            ))
            .applyChanges();

        interaction.acknowledge();
    }
}
