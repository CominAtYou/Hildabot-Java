package com.cominatyou.routinetasks;

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
     * Schedule the routine tasks that run every day at midnight.
     */
    public static void schedule() {
        final JobDetail job = JobBuilder.newJob(CheckForBirthdays.class).withIdentity("birthdayAnnoucement", "routineTasks").build();
        final Trigger midnightTrigger = TriggerBuilder.newTrigger().withIdentity("birthdayTrigger", "routineTasks").withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();

        try {
            final Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, midnightTrigger);
        }
        catch (final SchedulerException e) {
            e.printStackTrace();
        }

    }
}
