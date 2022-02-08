package com.cominatyou.memberevents;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;

import org.javacord.api.event.server.member.ServerMemberLeaveEvent;

public class MemberLeave {
    public static void removeDBEntries(ServerMemberLeaveEvent event) {
        final RedisUserEntry user = new RedisUserEntry(event.getUser().getId());

        final Integer monthInt = user.getInt("birthday:month");
        final Integer dayInt = user.getInt("birthday:day");

        final String month = monthInt < 10 ? "0" + monthInt : monthInt.toString();
        final String day = dayInt < 10 ? "0" + dayInt : dayInt.toString();

        RedisInstance.getInstance().lrem("birthdays" + ":" + month + ":" + day, 1, String.valueOf(user.getId()));
        RedisInstance.getInstance().del("users:" + user.getId() + ":birthday:month", "users:" + user.getId() + ":birthday:day", "users:" + user.getId() + ":birthday:string");
    }
}
