package com.cominatyou.routinetasks;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.cominatyou.App;
import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;

public class SendStreakWarning implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        final ZonedDateTime now = ZonedDateTime.now(Values.BOT_TIME_ZONE);
        final ZonedDateTime midnight = now.toLocalDate().atStartOfDay(now.getZone());
        final ZonedDateTime threeDaysAhead = midnight.plus(3, ChronoUnit.DAYS);
        final ZonedDateTime tomorrow = midnight.plus(1, ChronoUnit.DAYS);

        int month = midnight.getMonthValue();
        int day = midnight.getDayOfMonth();

        month = threeDaysAhead.getMonthValue();
        day = threeDaysAhead.getDayOfMonth();

        final List<String> expireInThreeDays = RedisInstance.getInstance().lrange(String.format("streakexpiries:%d:%d", month, day), 0, -1);

        month = tomorrow.getMonthValue();
        day = tomorrow.getMonthValue();

        final List<String> expireTomorrow = RedisInstance.getInstance().lrange(String.format("streakexpiries:%d:%d", month, day), 0, -1);

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Your streak expires in 3 days!")
                .setColor(Values.HILDA_BLUE)
                .setDescription("If you want to disable this warning in the future, run `h!streakwarning` in this DM or <#" + Values.BOT_CHANNEL + ">.");

        for (String i : expireInThreeDays) {
            App.getClient().getServerById(Values.HILDACORD_ID).get().getMemberById(i).ifPresent(user -> {
                final RedisUserEntry userEntry = new RedisUserEntry(user);
                if (userEntry.getBoolean("streakwarningsdisabled")) return;

                user.openPrivateChannel().join().sendMessage(embed);
            });
        }

        embed.setTitle("Your streak expires in 1 day!");

        for (String i : expireTomorrow) {
            App.getClient().getServerById(Values.HILDACORD_ID).get().getMemberById(i).ifPresent(user -> {
                final RedisUserEntry userEntry = new RedisUserEntry(user);
                if (userEntry.getBoolean("streakwarningsdisabled")) return;

                user.openPrivateChannel().join().sendMessage(embed);
            });
        }

    }
}
