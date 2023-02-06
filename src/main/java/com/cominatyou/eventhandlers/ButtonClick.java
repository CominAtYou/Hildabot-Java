package com.cominatyou.eventhandlers;

import java.util.Map;

import org.javacord.api.event.interaction.ButtonClickEvent;

import com.cominatyou.interactions.ButtonClickHandler;
import com.cominatyou.interactions.store.PurchaseConfirmationButton;
import com.cominatyou.interactions.store.ReturnToMenuButton;
import com.cominatyou.util.ButtonData;

public class ButtonClick {
    private static final Map<String, ButtonClickHandler> commands = Map.of(
        "store_return_to_menu", new ReturnToMenuButton(),
        "store_confirm_purchase", new PurchaseConfirmationButton()
    );


    public static void route(ButtonClickEvent event) {
        final String command = ButtonData.decode(event.getButtonInteraction().getCustomId()).getCommand();

        if (!commands.containsKey(command)) return;
        commands.get(command).execute(event.getButtonInteraction());
    }
}
