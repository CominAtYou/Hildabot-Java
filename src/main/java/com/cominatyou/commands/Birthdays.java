package com.cominatyou.commands;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.BirthdayEntry;
import com.cominatyou.util.Values;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class Birthdays {
    private static final List<String> thirtyDayMonths = Arrays.asList("09", "04", "06", "11");
    private static final List<String> thirtyOneDayMonths = Arrays.asList("01", "03", "05", "07", "08", "10", "12");

    public static void run(MessageCreateEvent message, List<String> messageArgs) {
        if (messageArgs.size() == 0 || messageArgs.size() == 1 && !messageArgs.get(0).equalsIgnoreCase("list")) {
            message.getMessage().reply("Looks like you're missing some arguments. Please make sure you provided a command (set|edit) and a date, or list and a month if you want to view upcoming birthdays!");
        }
        else if (messageArgs.get(0).equalsIgnoreCase("set")) {
            Birthdays.set(message, messageArgs);
        } else if (messageArgs.get(0).equalsIgnoreCase("edit")) {
            Birthdays.edit(message, messageArgs);
        }
        else if (messageArgs.get(0).equalsIgnoreCase("list")) {
            Birthdays.list(message, messageArgs);
        }
    }

    private static void set(MessageCreateEvent message, List<String> messageArgs) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final boolean birthdayStringExists = user.getString("birthday:string") != null;

        if (birthdayStringExists) {
            message.getMessage().reply("You already have a birthday set! If you want to change it, please use `h!birthday edit`.");
            return;
        }

        final String birthday = messageArgs.get(1);
        // Expected format is MM-DD
        if (!birthday.matches("(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])") || birthday.length() == 4) {
            message.getMessage().reply("Invalid birthday provided. Please provide your birthday in `mm-dd` format. (i.e., 06-21 is June 21)");
            return;
        }

        // "parse" the date
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
        else if (month.equals("02") && intDay > 29) { // Leap birthdays
            message.getMessage().reply("February only has 29 days (at most)! Double-check the date you provided.");
            return;
        }

        // Write birthday to DB
        user.set("birthday:month", month);
        user.set("birthday:day", day);
        user.set("birthday:string", birthday);
        RedisInstance.getInstance().rpush(String.format("birthdays:%s:%s", month, day), String.valueOf(user.getId()));

        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (intMonth == currentMonth && intDay == currentDay) {
            if (birthday.equals("02-29")) {
                message.getMessage().reply("Happy Birthday! Your birthday has been set to February 29. Your birthday will be announced on March 1 on non-leap years.");
            }
            else {
                final String monthString = DateFormatSymbols.getInstance().getMonths()[intMonth - 1];
                message.getMessage().reply(String.format("Happy Birthday! Your birthday has been set to %s %d.", monthString, intDay));
            }
            message.getServer().get().getRoleById(609258045759029250L).get().addUser(message.getMessageAuthor().asUser().get(), "Today is their birthday!");
        }
        else if (birthday.equals("02-29")) {
            message.getMessage().reply("Success! Your birthday has been set to February 29. Your birthday will be announced on March 1 on non-leap years.");
        }
        else {
            final String monthString = DateFormatSymbols.getInstance().getMonths()[intMonth - 1];
            message.getMessage().reply(String.format("Success! Your birthday has been set to %s %d.", monthString, intDay));
        }
    }

    private static void edit(MessageCreateEvent message, List<String> messageArgs) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final boolean birthdayStringExists = user.getString("birthday:string") != null;

        if (!birthdayStringExists) {
            message.getMessage().reply("You don't have a birthday set! If you're trying to set your birthday, please use `h!birthday set`.");
            return;
        }
        else if (!messageArgs.get(1).matches("(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])")) {
            message.getMessage().reply("Invalid birthday provided. Please provide your birthday in `mm-dd` format. (i.e., 06-21 is June 21)");
            return;
        }

        final Integer monthInt = user.getInt("birthday:month");
        final Integer dayInt = user.getInt("birthday:day");

        // DB needs leading zeroes if number is less than 10.
        final String month = monthInt < 10 ? "0" + monthInt : monthInt.toString();
        final String day = dayInt < 10 ? "0" + dayInt : dayInt.toString();

        RedisInstance.getInstance().lrem("birthdays" + ":" + month + ":" + day, 1, String.valueOf(user.getId()));
        RedisInstance.getInstance().del("users:" + user.getId() + ":birthday:month", "users:" + user.getId() + ":birthday:day", "users:" + user.getId() + ":birthday:string");
        set(message, messageArgs);
    }

    private static void list(MessageCreateEvent message, List<String> messageArgs) {
        final Integer month;
        // If no month is provided, get birthdays for this month
        if (messageArgs.size() == 1) month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        else {
            try {
                month = Integer.valueOf(messageArgs.get(1));
            }
            catch (Exception e) {
                message.getMessage().reply(messageArgs.get(1) + " doesn't seem to be a month! Please specify a month from 1 - 12.");
                return;
            }
        }

        // Single-digit numbers need a leading 0 for the DB.
        final String monthStr = month < 10 ? "0" + month : month.toString();
        final int numberOfDays = month == 2 ? 29 : (thirtyDayMonths.contains(monthStr) ? 30 : 31);
        ArrayList<BirthdayEntry> birthdays = new ArrayList<>();

        for (int i = 1; i <= numberOfDays; i++) {
            final String day = i < 10 ? "0" + i : String.valueOf(i);
            final int dayInt = i; // needed for forEach loop below
            final List<String> birthdaysForDay = RedisInstance.getInstance().lrange(String.format("birthdays:%s:%s", monthStr, day), 0, -1);

            birthdaysForDay.forEach(id -> {
                birthdays.add(new BirthdayEntry(id, dayInt));
            });
        }

        final EmbedBuilder embed = new EmbedBuilder()
            .setTitle(":birthday: Birthdays for " + DateFormatSymbols.getInstance().getMonths()[month - 1])
            .setColor(new java.awt.Color(Values.HILDA_BLUE))
            .setDescription(birthdays.size() == 0 ? "No birthdays for this month!" : "");

        for (int i = 0; i < birthdays.size(); i++) {
            final BirthdayEntry entry = birthdays.get(i);
            final Optional<User> user = message.getApi().getServerById(Values.HILDACORD_ID).get().getMemberById(entry.getUserId());
            if (user.isEmpty()) continue; // User is not in guild

            if (i == 8 && birthdays.size() > 9) {
                embed.addInlineField(birthdays.size() - 8 + " more", "up to " + month + "/" + numberOfDays);
                break;
            }
            else {
                embed.addInlineField(user.get().getDiscriminatedName(), month + "/" + entry.getDay());
                if (i == 9) break;
            }
        }

        message.getChannel().sendMessage(embed);
    }
}
