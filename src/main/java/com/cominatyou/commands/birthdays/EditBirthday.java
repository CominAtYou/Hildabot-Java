package com.cominatyou.commands.birthdays;

import java.util.List;

import org.javacord.api.event.message.MessageCreateEvent;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;

public class EditBirthday {
    public static void edit(MessageCreateEvent message, List<String> messageArgs) {
        final RedisUserEntry user = new RedisUserEntry(message.getMessageAuthor());
        final boolean birthdayStringExists = user.getString("birthday:string") != null;

        if (!birthdayStringExists) {
            message.getMessage().reply("You don't have a birthday set! If you're trying to set your birthday, please use `h!birthday set` or </birthday set:1011153003853643816>.");
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

        RedisInstance.getInstance().lrem("birthdays" + ":" + month + ":" + day, 0, String.valueOf(user.getId()));
        RedisInstance.getInstance().del("users:" + user.getId() + ":birthday:month", "users:" + user.getId() + ":birthday:day", "users:" + user.getId() + ":birthday:string");
        SetBirthday.set(message, messageArgs);
    }
}
