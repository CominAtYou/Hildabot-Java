package com.cominatyou.commands.staff;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.cominatyou.TextCommand;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.CommandPermissions;
import com.cominatyou.util.Values;
import com.cominatyou.util.Pair;
import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;
import com.cominatyou.xp.XPSystemCalculator;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class InitializeUser extends TextCommand {
    public void execute(MessageCreateEvent message, List<String> messageArgs) {
        if (!CommandPermissions.canRunSensitiveCommands(message.getMessageAuthor().asUser().get())) return;
        if (messageArgs.size() < 2) {
            message.getChannel().sendMessage("A user ID and/or a level must be provided.");
            return;
        }
        final int level;

        try {
            level = Integer.parseInt(messageArgs.get(1));
        } catch (NumberFormatException e) {
            message.getChannel().sendMessage("Invalid level provided.");
            return;
        }

        if (level < 0) {
            message.getChannel().sendMessage("Level must be a positive number.");
            return;
        }

        message.getServer().get().getMemberById(messageArgs.get(0)).ifPresentOrElse(user -> {
            final RedisUserEntry dbUser = new RedisUserEntry(user);

            if (dbUser.getLevel() >= 10 && !messageArgs.contains("--force")) {
                final EmbedBuilder warningMessage = new EmbedBuilder()
                    .setTitle("Hold up! You might have typed something wrong.")
                    .setDescription(String.format("You're attempting to initialize <@%d>, but they're already level %d. **You may have specified the wrong user.**\n\nIf you're sure that this is the user you wanted, re-run your command with `--force` at the end.", user.getId(), dbUser.getLevel()))
                    .setColor(Values.WARNING_YELLOW);

                message.getChannel().sendMessage(warningMessage);
                return;
            }

            final Integer xp = XPSystemCalculator.determineMinimumTotalXPForLevel(level);
            dbUser.set("xp", xp.toString());

            final int rankLevel = RankUtil.getRankFromLevel(dbUser.getLevel()).getLevel();
            final int[] rankLevels = RankUtil.getRanklevels();
            final int index = Arrays.binarySearch(rankLevels, rankLevel);

            final ArrayList<Pair<String, Long>> unknownRoleIds = new ArrayList<>();

            // Give roles
            for (int i = index; i > 0; i--) {
                final Rank rank = RankUtil.getRankFromLevel(rankLevels[i]);
                message.getServer().get().getRoleById(rank.getId()).ifPresentOrElse(role -> {
                    role.addUser(user);
                }, () -> {
                    unknownRoleIds.add(new Pair<String, Long>(rank.getName(), rank.getId()));
                });
            }

            if (unknownRoleIds.isEmpty()) {
                message.addReactionToMessage("👍");
            }
            else {
                final StringBuilder unknownRolesMessage = new StringBuilder()
                    .append("The user has been initized, but the following role(s) were unable to be added to the user:\n");

                for (final Pair<String, Long> unknownRole : unknownRoleIds) {
                    unknownRolesMessage.append(String.format("- %s (ID: %d)\n", unknownRole.getFirst(), unknownRole.getSecond()));
                }

                message.getChannel().sendMessage(unknownRolesMessage.toString().trim());
            }
        }, () -> {
            message.getChannel().sendMessage("Unable to locate a user in this server with the provided ID.");
        });
    }
}
