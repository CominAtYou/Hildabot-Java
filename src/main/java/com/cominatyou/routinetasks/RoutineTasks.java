package com.cominatyou.routinetasks;

import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.cominatyou.util.Values;

public class RoutineTasks {
    /**
     * Schedule the routine tasks that run every day at midnight Central Time.
     */
    public static void schedule() {
        if (!System.getProperty("os.name").equalsIgnoreCase("Linux")) return;

        final JobDetail birthdayJob = JobBuilder.newJob(CheckForBirthdays.class).withIdentity("birthdayAnnoucement", "routineTasks").build();
        final JobDetail streakJob = JobBuilder.newJob(SendStreakWarning.class).withIdentity("streakwarning", "routineTasks").build();

        final Trigger birthdayTrigger = TriggerBuilder.newTrigger().withIdentity("birthdayTrigger", "routineTasks").withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?").inTimeZone(TimeZone.getTimeZone(Values.TIMEZONE_AMERICA_CHICAGO))).build();
        final Trigger streakTrigger = TriggerBuilder.newTrigger().withIdentity("streakTrigger", "routineTasks").withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?").inTimeZone(TimeZone.getTimeZone(Values.TIMEZONE_AMERICA_CHICAGO))).build();

        try {
            final Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            scheduler.scheduleJob(birthdayJob, birthdayTrigger);
            scheduler.scheduleJob(streakJob, streakTrigger);
        }
        catch (final SchedulerException e) {
            e.printStackTrace();
        }

    }
}
