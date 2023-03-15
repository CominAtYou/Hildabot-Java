package com.cominatyou.routinetasks;

import org.atteo.evo.inflector.English;
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
import com.cominatyou.util.logging.Log;

public class SendStreakWarning implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        Log.event("STREAKWARN", "Starting streak warnings task.");

        final ZonedDateTime now = ZonedDateTime.now(Values.TIMEZONE_AMERICA_CHICAGO);
        final ZonedDateTime midnight = now.toLocalDate().atStartOfDay(now.getZone());
        final ZonedDateTime threeDaysAhead = midnight.plus(3, ChronoUnit.DAYS);
        final ZonedDateTime tomorrow = midnight.plus(1, ChronoUnit.DAYS);

        final int threeDayMonth = threeDaysAhead.getMonthValue();
        final int threeDayDay = threeDaysAhead.getDayOfMonth();

        final List<String> expireInThreeDays = RedisInstance.getInstance().lrange(String.format("streakexpiries:%d:%d", threeDayMonth, threeDayDay), 0, -1);

        final int tomorrowMonth = tomorrow.getMonthValue();
        final int tomorrowDay = tomorrow.getDayOfMonth();

        final List<String> expireTomorrow = RedisInstance.getInstance().lrange(String.format("streakexpiries:%d:%d", tomorrowMonth, tomorrowDay), 0, -1);

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Your streak expires in 3 days!")
                .setColor(Values.HILDA_BLUE)
                .setDescription("If you want to disable this warning in the future, run `h!streakwarning` in this DM or <#" + Values.BOT_CHANNEL + ">.");

        for (String i : expireInThreeDays) {
            App.getClient().getServerById(Values.BASE_GUILD_ID).get().getMemberById(i).ifPresent(user -> {
                final RedisUserEntry userEntry = new RedisUserEntry(user);
                if (userEntry.getBoolean("streakwarningsdisabled")) return;

                user.openPrivateChannel().join().sendMessage(embed);
            });
        }

        Log.eventf("STREAKWARN", "Sent three-day streak expiry warning to %d %s.\n", expireInThreeDays.size(), English.plural("user", expireInThreeDays.size()));

        embed.setTitle("Your streak expires in 1 day!");

        for (String i : expireTomorrow) {
            App.getClient().getServerById(Values.BASE_GUILD_ID).get().getMemberById(i).ifPresent(user -> {
                final RedisUserEntry userEntry = new RedisUserEntry(user);
                if (userEntry.getBoolean("streakwarningsdisabled")) return;

                user.openPrivateChannel().join().sendMessage(embed);
            });
        }

        Log.eventf("STREAKWARN", "Sent next-day streak expiry warning to %d %s.\n", expireTomorrow.size(), English.plural("user", expireTomorrow.size()));
        Log.event("STREAKWARN", "Finished streak warnings task.");

    }
}
