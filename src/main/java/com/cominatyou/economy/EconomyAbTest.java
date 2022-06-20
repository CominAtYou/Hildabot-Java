package com.cominatyou.economy;

import java.util.concurrent.ThreadLocalRandom;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import com.cominatyou.db.RedisInstance;
import com.cominatyou.db.RedisUserEntry;
import com.cominatyou.util.logging.Log;

/*
 * An A/B test for the economy functionality.
 *
 * Only 25 of the most active submitters will be able to participate.
 *
 * Eligibility critieria: The user must have a streak of at least 40, and be at least adventurer.
 */

public class EconomyAbTest {
    private static final int MAXIMUM_TEST_PARTICIPANTS = 10 + 1; // 25, excluding me

    /**
     * Possibly register the user for the A/B test, based on random chance.
     * @param user The user to register.
     */
    public static void register(MessageAuthor user) {
        final RedisUserEntry entry = new RedisUserEntry(user);

        // Check if they are eligible
        if (isParticipating(user)) return;
        if (entry.getLevel() < 50) return;
        if (entry.getInt("streak") < 25) return;

        if (ThreadLocalRandom.current().nextInt(0, 4) != 1) return; // 25% chance for being selected; the bound of ThreadLocalRandom is exclusive

        final long participantsCount = RedisInstance.getInstance().llen("config:abtests:economy");
        if (participantsCount > MAXIMUM_TEST_PARTICIPANTS) return;

        // Add the user's Discord ID to the list of participants
        RedisInstance.getInstance().rpush("config:abtests:economy", user.getIdAsString());
        Log.eventf("ECONOMY_TEST", "%s (%d) was added to the economy A/B test, %d slots remaining\n", user.getDiscriminatedName(), entry.getId(), MAXIMUM_TEST_PARTICIPANTS - participantsCount);
    }

    /**
     * Check if the user is already participating in the A/B test.
     * @param user The user to check.
     */
    public static boolean isParticipating(MessageAuthor user) {
        return RedisInstance.getInstance().lrange("config:abtests:economy", 0, -1).contains(user.getIdAsString());
    }

    /**
     * Check if the user is already participating in the A/B test.
     * @param user The user to check.
     */
    public static boolean isParticipating(User user) {
        return RedisInstance.getInstance().lrange("config:abtests:economy", 0, -1).contains(user.getIdAsString());
    }
}
