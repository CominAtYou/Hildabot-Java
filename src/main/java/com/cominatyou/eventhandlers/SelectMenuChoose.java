package com.cominatyou.eventhandlers;

import java.util.Map;

import org.javacord.api.event.interaction.SelectMenuChooseEvent;

import com.cominatyou.interactions.SelectMenuChooseHandler;
import com.cominatyou.interactions.store.StoreEvent;

public class SelectMenuChoose {
    private final static Map<String, SelectMenuChooseHandler> handlers = Map.of(
        "store_menu", new StoreEvent()
    );

    public static void route(SelectMenuChooseEvent event) {
        final String command = event.getSelectMenuInteraction().getCustomId();

        if (!handlers.containsKey(command)) return;
        handlers.get(command).execute(event.getSelectMenuInteraction());
    }
}
