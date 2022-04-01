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

public class RoutineTasks {
    /**
     * Schedule the routine tasks that run every day at midnight Central Time.
     */
    public static void schedule() {
        final JobDetail birthdayJob = JobBuilder.newJob(CheckForBirthdays.class).withIdentity("birthdayAnnoucement", "routineTasks").build();
        final JobDetail streakJob = JobBuilder.newJob(SendStreakWarning.class).withIdentity("streakwarning", "routineTasks").build();

        final Trigger midnightTrigger = TriggerBuilder.newTrigger().withIdentity("birthdayTrigger", "routineTasks").withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?").inTimeZone(TimeZone.getTimeZone("America/Chicago"))).build();

        try {
            final Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(birthdayJob, midnightTrigger);
            scheduler.scheduleJob(streakJob, midnightTrigger);
        }
        catch (final SchedulerException e) {
            e.printStackTrace();
        }

    }
}
