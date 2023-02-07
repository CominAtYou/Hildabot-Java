package com.cominatyou.store.items;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.ButtonInteraction;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.SuccessfulPurchaseEmbed;
import com.cominatyou.util.Values;

public class StreakBoost extends StoreItem {
    public StreakBoost(String name, String description, String additionalInfo, String id, int price, PurchaseLimit purchaseLimit) throws IllegalArgumentException {
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

        final ZonedDateTime newExpiry = Instant.ofEpochSecond(currentExpiry).atZone(Values.TIMEZONE_AMERICA_CHICAGO).plusWeeks(1);

        userEntry.decrementKey("tokens", getPrice());
        userEntry.set("streakexpiry", String.valueOf(newExpiry.toEpochSecond()));
        userEntry.expireKeyAt("streakexpiry", newExpiry.toEpochSecond());

        final long twoWeeks = ZonedDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO)
            .toLocalDate()
            .plusDays(1)
            .atStartOfDay(Values.TIMEZONE_AMERICA_CHICAGO)
            .plusDays(14)
            .toEpochSecond();

        userEntry.set("items:" + getId() + ":purchasableagain", String.valueOf(twoWeeks));
        userEntry.expireKeyAt("items:" + getId() + ":purchasableagain", twoWeeks);

        // Remove old streak expiry warning
        final ZonedDateTime oldExpiry = Instant.ofEpochSecond(currentExpiry).atZone(Values.TIMEZONE_AMERICA_CHICAGO);
        final int month = oldExpiry.getMonthValue();
        final int day = oldExpiry.getDayOfMonth();

        final String key = String.format("streakexpiries:%d:%d", month, day);
        RedisInstance.getInstance().lrem(key, 0, interaction.getUser().getIdAsString());

        // Create new streak expiry warning
        final String newExpiryKey = String.format("streakexpiries:%d:%d", newExpiry.getMonthValue(), newExpiry.getDayOfMonth());
        RedisInstance.getInstance().rpush(newExpiryKey, userEntry.getIdAsString());
        if (RedisInstance.getInstance().ttl(newExpiryKey) < 0) {
            RedisInstance.getInstance().expireat(newExpiryKey, newExpiry.toEpochSecond());
        }

        final EmbedBuilder embed = SuccessfulPurchaseEmbed.create(interaction.getUser(), String.format("Your streak has been extended by 7 days!\nYour streak will now expire on <t:%d>.", userEntry.getLong("streakexpiry")));

        interaction.getMessage().createUpdater()
            .removeAllComponents()
            .removeAllEmbeds()
            .setContent("")
            .addEmbed(embed)
            .applyChanges();

        interaction.acknowledge();
    }
}
