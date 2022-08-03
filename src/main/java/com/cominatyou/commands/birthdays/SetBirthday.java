package com.cominatyou.commands.birthdays;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;

public class SetBirthday {
    private static final List<String> thirtyDayMonths = Arrays.asList("09", "04", "06", "11");
    private static final List<String> thirtyOneDayMonths = Arrays.asList("01", "03", "05", "07", "08", "10", "12");

    public static void set(MessageCreateEvent message, List<String> messageArgs) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final boolean birthdayStringExists = user.getString("birthday:string") != null;

        if (birthdayStringExists) {
            message.getMessage().reply("You already have a birthday set! If you want to change it, please use `h!birthday edit month-day` or use `/birthday edit`.");
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
}
