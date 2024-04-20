package com.cominatyou.util;

import java.awt.Color;
import java.time.ZoneId;

public class Values {
    /** Color for regular embeds. */
    public static final Color HILDA_BLUE = new Color(0x008080);
    /** Color for success embeds. */
    public static final Color SUCCESS_GREEN = new Color(0x77b255);
    /** Color for error embeds. */
    public static final Color ERROR_RED = new Color(0xed4245);
    /** Color for warning embeds. */
    public static final Color WARNING_YELLOW = new Color(0xfee75c);
    /** The timezone in which {@link com.cominatyou.routinetasks.RoutineTasks routine tasks} should be scheduled in. */
    public static final ZoneId TIMEZONE_AMERICA_CHICAGO = ZoneId.of("America/Chicago");
    /** A channel within {@code BASE_GUILD_ID} for running bot commands. */
    public static final long BOT_CHANNEL = 495034452422950915L;
    /** A channel within {@code BASE_GUILD_ID} that facilitates testing for the bot. */
    public static final long TESTING_CHANNEL = 565327145786802176L;
    /** The guild where the bot will be active. */
    public static final long BASE_GUILD_ID = 492572315138392064L;
    /** The channel for logging events. */
    public static final long LOG_CHANNEL = 1231339360348274709L;
}
