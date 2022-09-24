package com.cominatyou.slashcommands.birthdays;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;

public class SetBirthday {
    private static final List<String> thirtyDayMonths = Arrays.asList("09", "04", "06", "11");
    private static final List<String> thirtyOneDayMonths = Arrays.asList("01", "03", "05", "07", "08", "10", "12");

    public static void set(SlashCommandInteraction interaction) {
        final RedisUserEntry user = new RedisUserEntry(interaction.getUser());
        if (user.hasKey("birthday:string")) {
            interaction.createImmediateResponder().setContent("You already have a birthday set! If you want to change it, please use </birthday edit:1011153003853643816>.").respond();
            return;
        }

        final String monthValue = interaction.getOptions().get(0).getOptionStringValueByName("month").get();
        final int day = Integer.parseInt(interaction.getOptions().get(0).getOptionLongValueByName("day").get().toString()); // will not be larger than 31

        if (thirtyDayMonths.contains(monthValue) && day > 30) {
            interaction.createImmediateResponder().setContent("That month only has 30 days! Double-check the date you provided.");
            return;
        }
        else if (thirtyOneDayMonths.contains(monthValue) && day > 31) {
            interaction.createImmediateResponder().setContent("That month only has 31 days! Double-check the date you provided.").respond();
            return;
        }
        else if (monthValue.equals("02") && day > 29) {
            interaction.createImmediateResponder().setContent("February only has 29 days (at most)! Double-check the date you provided.").respond();
            return;
        }

        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        final int intMonth = Integer.valueOf(monthValue);
        final String dayValue = day < 10 ? "0" + day : String.valueOf(day);
        final String formattedDate =  monthValue + "-" + dayValue;

        user.set("birthday:month", monthValue);
        user.set("birthday:day", dayValue);
        user.set("birthday:string", formattedDate);
        RedisInstance.getInstance().rpush(String.format("birthdays:%s:%s", monthValue, dayValue), String.valueOf(user.getId()));

        if (intMonth == currentMonth && day == currentDay) {
            if (formattedDate.equals("02-29")) {
                interaction.createImmediateResponder().setContent("Happy Birthday! Your birthday has been set to February 29. Your birthday will be announced on March 1 on non-leap years.").respond();
                return;
            }
            else {
                final String monthString = DateFormatSymbols.getInstance().getMonths()[intMonth - 1];
                interaction.createImmediateResponder().setContent(String.format("Happy Birthday! Your birthday has been set to %s %d.", monthString, day)).respond();
                return;
            }
        }
        else if (formattedDate.equals("02-29")) {
            interaction.createImmediateResponder().setContent("Success! Your birthday has been set to February 29. Your birthday will be announced on March 1 on non-leap years.").respond();
        }
        else {
            final String monthString = DateFormatSymbols.getInstance().getMonths()[intMonth - 1];
            interaction.createImmediateResponder().setContent(String.format("Success! Your birthday has been set to %s %d.", monthString, day)).respond();
        }

    }
}
