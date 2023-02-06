package com.cominatyou.interactions;

import org.javacord.api.interaction.ButtonInteraction;

public interface ButtonClickHandler {
    public void execute(ButtonInteraction interaction);
}
