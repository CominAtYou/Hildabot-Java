package com.cominatyou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;

import org.junit.Test;

public class RankTest {
    private static final Integer[] rankLevels = RankUtil.getRanklevels();

    @Test
    public void ensureCorrectAmountOfRankLevels() {
        assertEquals(26, rankLevels.length);
    }

    @Test
    public void ensureUniqueRankRoleIDs() {
        final ArrayList<Long> seenIDs = new ArrayList<>(26);
        for (final int i : rankLevels) {
            final Rank rank = RankUtil.getRankFromLevel(i);
            if (seenIDs.contains(rank.getId())) {
                fail(String.format("Level %d: ID %d is present at index %d", i, rank.getId(), seenIDs.indexOf(rank.getId())));
            }
            else {
                seenIDs.add(rank.getId());
            }
        }
    }

    @Test
    public void ensureCorrectRankRoleIdFormat() {
        for (final int i : rankLevels) {
            final Rank rank = RankUtil.getRankFromLevel(i);
            if (!String.valueOf(rank.getId()).matches("^[0-9]{17,19}$")) {
                fail(String.format("Level %d: ID %d is not a Discord snowflake!", i, rank.getId()));
            }
        }
    }

    @Test
    public void ensureCorrectRankLevels() {
        for (final int i: rankLevels) {
            final Rank rank = RankUtil.getRankFromLevel(i);
            assertEquals(i, rank.getLevel());
        }
    }

    @Test
    public void ensureSortedRankLevels() {
        for (int i = 0; i < rankLevels.length - 1; i++) {
            if (rankLevels[i] > rankLevels[i + 1]) {
                fail(String.format("Level %d: next level is %d, which is less a lower level", rankLevels[i], rankLevels[i + 1]));
            }
        }
    }

    @Test
    public void ensureUniqueRankNames() {
        final ArrayList<String> seenNames = new ArrayList<>(26);
        for (final int i : rankLevels) {
            final Rank rank = RankUtil.getRankFromLevel(i);
            if (seenNames.contains(rank.getName())) {
                fail(String.format("Level %d: Name %s is already present at index %d", i, rank.getName(), seenNames.indexOf(rank.getName())));
            }
            else {
                seenNames.add(rank.getName());
            }
        }
    }
}
