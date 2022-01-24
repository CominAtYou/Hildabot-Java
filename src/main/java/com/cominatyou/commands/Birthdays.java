package com.cominatyou.commands;

import java.util.List;

import com.cominatyou.RedisUserEntry;
import com.cominatyou.util.RedisInstance;

import java.text.DateFormatSymbols;
import java.util.Arrays;

import org.javacord.api.event.message.MessageCreateEvent;

public class Birthdays {
    private static List<String> thirtyDayMonths = Arrays.asList("09", "04", "06", "11");
    private static List<String> thirtyOneDayMonths = Arrays.asList("01", "03", "05", "07", "08", "10", "12");

    public static void set(MessageCreateEvent message, List<String> messageArgs) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        if (messageArgs.size() == 1) {
            message.getMessage().reply("You've gotta specify a date for this command!");
            return;
        }
        else if (user.getBirthday() != null) {
            message.getMessage().reply("You already have a birthday set! If you want to change it, please use `h!birthday edit`.");
            return;
        }

        final String birthday = messageArgs.get(1);
        // Expected format is MM-DD
        if (!birthday.matches("(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])")) {
            message.getMessage().reply("Invalid birthday provided. Please provide your birthday in `mm-dd` format. (i.e., 06-21 is June 21)");
        }

        final String[] splitDate = birthday.split("-");
        final String month = splitDate[0];
        final String day = splitDate[1];

        final int intMonth = Integer.valueOf(month);
        final int intDay = Integer.valueOf(day);

        if (thirtyDayMonths.contains(month) && intDay > 30) {
            message.getMessage().reply("That month only has 30 days! Double-check the date you provided.");
            return;
        }
        else if (thirtyOneDayMonths.contains(month) && intDay > 31) {
            message.getMessage().reply("That month only has 31 days! Double-check the date you provided.");
            return;
        }
        else if (month.equals("02") && intDay > 29) {
            message.getMessage().reply("Feburary only has 29 days (at most)! Double-check the date you provided.");
            return;
        }

        RedisInstance.getInstance().set("users:" + message.getMessageAuthor().getIdAsString() + ":birthday:month", month);
        RedisInstance.getInstance().set("users:" + message.getMessageAuthor().getIdAsString() + ":birthday:day", day);
        RedisInstance.getInstance().set("users:" + message.getMessageAuthor().getIdAsString() + ":birthday:string", birthday);
        RedisInstance.getInstance().rpush(String.format("birthdays:%s:%s", month, day), message.getMessageAuthor().getIdAsString());

        if (birthday.equals("02-29")) {
            message.getMessage().reply("Success! Your birthday has been set to Feburary 29. Your birthday will be announced on March 1 on non-leap years.");
        }
        else {
            final String monthString = DateFormatSymbols.getInstance().getMonths()[intMonth - 1];
            message.getMessage().reply(String.format("Success! Your birthday has been set to %s %d.", monthString, intDay));
        }
    }

    public static void edit(MessageCreateEvent message, List<String> messageArgs) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor().getId());
        if (user.getBirthday() == null) {
            message.getMessage().reply("You don't have a birthday set! If you're trying to set your birthday, please use `h!birthday set`.");
            return;
        }
        else if (!messageArgs.get(1).matches("(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])")) {
            message.getMessage().reply("Invalid birthday provided. Please provide your birthday in `mm-dd` format. (i.e., 06-21 is June 21)");
            return;
        }
        else if (messageArgs.size() == 1) {
            message.getMessage().reply("You've gotta specify a date for this command!");
            return;
        }
        RedisInstance.getInstance().lrem("birthdays" + ":" + user.getRedisFormattedBirthday(), 1, message.getMessageAuthor().getIdAsString());
        RedisInstance.getInstance().del(user.getRedisKey() + ":birthday:month", user.getRedisKey() + ":birthday:day", user.getRedisKey() + ":birthday:string");
        set(message, messageArgs);
    }
}
