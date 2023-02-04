package com.cominatyou.slashcommands;

import java.util.List;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.StoreItems;
import com.cominatyou.util.Values;

public class Store implements SlashCommand {
    public void execute(SlashCommandInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());
        final List<StoreItem> items = StoreItems.getItems();
        final List<String> purchasedItems = user.getList("items:inventory");
        final EmbedBuilder embed = new EmbedBuilder().setTitle("Store").setColor(Values.HILDA_BLUE).setFooter("Your token balance: " + user.getString("tokens"));
        final SelectMenuBuilder selectMenu = new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, "store_menu")
            .setPlaceholder("Purchase an item")
            .setMaximumValues(1);



        for (StoreItem i : items) {
            if (i.getPurchaseLimit() == StoreItem.PurchaseLimit.ONESHOT) {
                for (String s : purchasedItems) {
                    if (s.equals(i.getId())) {
                        continue;
                    }
                }
            }

            final long purchasableAgain = user.getLong("items:" + i.getId() + ":purchasableagain");
            if (purchasableAgain != 0L) {
                embed.addField(i.getName(), String.format("🪙 %d - %s\nYou can buy this again <t:%d:R>", i.getPrice(), i.getDescription(), purchasableAgain));
            }
            else {
                embed.addField(i.getName(), String.format("🪙 %d - %s%s", i.getPrice(), i.getDescription(), i.hasAdditionalInfo() ? " " + i.getAdditionalInfo() : ""));
                selectMenu.addOption(SelectMenuOption.create(i.getName(), i.getId(), String.format("🪙 %d - %s", i.getPrice(), i.getDescription())));
            }
        }

        interaction.createImmediateResponder().addEmbed(embed).addComponents(ActionRow.of(selectMenu.build())).respond();
    }
}
