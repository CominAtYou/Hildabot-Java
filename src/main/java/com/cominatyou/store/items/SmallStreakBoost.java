package com.cominatyou.store.items;

import java.time.ZonedDateTime;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.SuccessfulPurchaseEmbed;
import com.cominatyou.util.Values;

public class SmallStreakBoost extends StoreItem {

    public SmallStreakBoost(String name, String description, String additionalInfo, String id, int price, PurchaseLimit purchaseLimit) throws IllegalArgumentException {
        super(name, description, additionalInfo, id, price, purchaseLimit);
    }

    public void giveItem(ButtonInteraction interaction) {
        final RedisUserEntry userEntry = new RedisUserEntry(interaction.getUser());
        final long currentExpiry = userEntry.getLong("streakexpiry");

        if (currentExpiry == 0) {
            interaction.getMessage().createUpdater()
                .removeAllComponents()
                .removeAllEmbeds()
                .setContent("You don't have an active streak. You'll need to submit something before you can buy this.")
                .addComponents(ActionRow.of(new ButtonBuilder().setCustomId("store_return_to_menu-" + userEntry.getIdAsString()).setStyle(ButtonStyle.PRIMARY).setLabel("OK").build()))
                .applyChanges();

            interaction.acknowledge();
            return;
        }

        userEntry.decrementKey("tokens", getPrice());
        userEntry.incrementKey("streakexpiry", 259200);
        userEntry.expireKeyAt("streakexpiry", currentExpiry + 259200);

        final long oneWeek = ZonedDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO)
            .toLocalDate()
            .plusDays(1)
            .atStartOfDay(Values.TIMEZONE_AMERICA_CHICAGO)
            .plusDays(7)
            .toEpochSecond();

        userEntry.set("items:" + getId() + ":purchasableagain", String.valueOf(oneWeek));
        userEntry.expireKeyAt("items:" + getId() + ":purchasableagain", oneWeek);

        final EmbedBuilder embed = SuccessfulPurchaseEmbed.create(interaction.getUser(), String.format("Your streak has been extended by 3 days!\nYour streak will now expire on <t:%d>.", userEntry.getLong("streakexpiry")));

        interaction.getMessage().createUpdater()
            .removeAllComponents()
            .removeAllEmbeds()
            .setContent("")
            .addEmbed(embed)
            .applyChanges();

        interaction.acknowledge();
    }

}
