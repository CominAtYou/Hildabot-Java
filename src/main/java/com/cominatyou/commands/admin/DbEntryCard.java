package com.cominatyou.commands.admin;

import java.util.List;

import com.cominatyou.commands.Command;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.Values;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

public class DbEntryCard implements Command {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!message.getMessageAuthor().isBotOwner()) return;
        if (messageArgs.size() == 0) {
            message.getChannel().sendMessage("Please provide a User ID.");
            return;
        }
        if (!messageArgs.get(0).matches("[0-9]{17,19}")) {
            message.getChannel().sendMessage("Please provide a valid user ID.");
            return;
        }

        final Server hildacord = message.getApi().getServerById(Values.HILDACORD_ID).get();
        hildacord.getMemberById(messageArgs.get(0)).ifPresentOrElse(member -> {
            final RedisUserEntry userEntry = new RedisUserEntry(member);
            final int level = userEntry.getLevel();

            final EmbedBuilder embed = new EmbedBuilder().setTitle(member.getDiscriminatedName())
                    .setColor(Values.HILDA_BLUE)
                    .setThumbnail(member.getAvatar())
                    .addInlineField("Total XP", String.valueOf(userEntry.getXP()))
                    .addInlineField("Level", String.format("%d (%s)", level, RankUtil.getRankFromLevel(level).getName()))
                    .addInlineField("XP for Level Up", String.valueOf(XPSystemCalculator.determineMinimumTotalXPForLevel(level + 1)))
                    .addInlineField("Enrolled", userEntry.getBoolean("enrolled") ? "Yes" : "No")
                    .addInlineField("Level Alerts", userEntry.getBoolean("levelalertsdisabled") ? "Disabled" : "Enabled")
                    .addInlineField("Birthday", userEntry.getString("birthday:string") == null ? "None" : userEntry.getString("birthday:string"))
                    .setFooter("User ID: " + userEntry.getIdAsString())
                    .setTimestamp(member.getJoinedAtTimestamp(hildacord).get());

            message.getChannel().sendMessage(embed);
        }, () -> {
            message.getChannel().sendMessage("That doesn't look to be a valid ID, or the user that ID points to is not a member of Hildacord.");
        });
    }

    public String getName() {
        return "DbEntryCard";
    }
}
