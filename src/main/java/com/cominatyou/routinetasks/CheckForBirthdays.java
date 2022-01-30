package com.cominatyou.routinetasks;

import java.util.Calendar;
import java.util.List;

import com.cominatyou.App;
import com.cominatyou.db.RedisInstance;
import com.cominatyou.util.Values;

import org.javacord.api.entity.channel.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CheckForBirthdays implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final int month = Calendar.getInstance().get(Calendar.MONTH) + 1; // January is 0
        final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final String monthString = month < 10 ? "0" + month : String.valueOf(month);
        final String dayString = day < 10 ? "0" + day : String.valueOf(day);
        final List<String> birthdays = RedisInstance.getInstance().lrange(String.format("birthdays:%s:%s", monthString, dayString), 0, -1);
        // TODO: CLEAR THIS FOR TESTING
        final TextChannel birthdayChannel =  App.getClient().getServerById(Values.HILDACORD_ID).get().getTextChannelById(609253148564914187L).get();

        if (birthdays.size() == 0) {
            return;
        }
        else if (birthdays.size() == 1) {
            birthdayChannel.sendMessage(String.format("Hey <@%s>! I just wanted to wish you the happiest of birthdays! Can I have a slice of cake too? :birthday::heart:", birthdays.get(0)));
        }
        else if (birthdays.size() == 2) {
            birthdayChannel.sendMessage(String.format("Hey <@%s> and <@%s>! I just wanted to wish you both the happiest of birthdays! Can I have a slice of cake too? :birthday::heart:", birthdays.get(0), birthdays.get(1)));
        }
        else {
            StringBuilder announcement = new StringBuilder("Hey");
            for (int i = 0; i < birthdays.size(); i++) {
                if (i != birthdays.size() - 1) announcement.append(" <@" + birthdays.get(i) + ">, ");
                else announcement.append("and <@" + birthdays.get(i) + ">! ");
            }
            announcement.append("I just wanted to wish you all the happiest of birthdays! Can I have a slice of cake too? :birthday::heart:");
            birthdayChannel.sendMessage(announcement.toString());
        }
    }
}
