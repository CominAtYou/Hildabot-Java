package com.cominatyou.slashcommands;

import java.util.List;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.store.StoreItem;
import com.cominatyou.store.StoreItems;
import com.cominatyou.util.SelectMenuOptionData;
import com.cominatyou.util.Values;

public class Store implements SlashCommand {
    public void execute(SlashCommandInteraction interaction) {
        showStore((InteractionBase) interaction);
    }

    // This exists to let the showStore method be accessible to "store-return-to-menu" buttons
    public void execute(ButtonInteraction interaction) {
        showStore((InteractionBase) interaction);
    }

    private void showStore(InteractionBase interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());
        final List<StoreItem> items = StoreItems.getItems();
        final List<String> purchasedItems = user.getList("items:inventory");
        final EmbedBuilder embed = new EmbedBuilder().setAuthor(interaction.getUser()).setTitle("Store").setColor(Values.HILDA_BLUE).setFooter("Your token balance: " + user.getString("tokens"));
        final SelectMenuBuilder selectMenu = new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, "store_menu")
            .setPlaceholder("Purchase an item")
            .setMaximumValues(1);



        for (StoreItem i : items) {
            if (i.getPurchaseLimit() == StoreItem.PurchaseLimit.ONESHOT && purchasedItems.contains(i.getId())) {
                continue;
            }

            final long purchasableAgain = user.getLong("items:" + i.getId() + ":purchasableagain");
            if (purchasableAgain != 0L) {
                embed.addField(i.getName(), String.format("🪙 %d - %s You can buy this again <t:%d:R>.", i.getPrice(), i.getDescription(), purchasableAgain));
            }
            else {
                embed.addField(i.getName(), String.format("🪙 %d - %s%s", i.getPrice(), i.getDescription(), i.hasAdditionalInfo() ? " " + i.getAdditionalInfo() : ""));
            }

            selectMenu.addOption(SelectMenuOption.create(i.getName(), SelectMenuOptionData.from(user.getIdAsString(), i.getId()).encode(), String.format("🪙 %d - %s", i.getPrice(), i.getDescription())));
        }

        if (interaction instanceof SlashCommandInteraction) {
            interaction.createImmediateResponder().addEmbed(embed).addComponents(ActionRow.of(selectMenu.build())).respond();
        }
        else {
            ((ButtonInteraction) interaction).getMessage().createUpdater()
                .removeAllComponents()
                .removeAllEmbeds()
                .setContent("")
                .addEmbed(embed)
                .addComponents(ActionRow.of(selectMenu.build()))
                .applyChanges();

            ((ButtonInteraction) interaction).acknowledge();
        }
    }
}
