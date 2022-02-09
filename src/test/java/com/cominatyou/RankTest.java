package com.cominatyou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import com.cominatyou.xp.Rank;
import com.cominatyou.xp.RankUtil;

import org.junit.Test;

public class RankTest {
    private static final int[] rankLevels = RankUtil.getRanklevels();

    @Test
    public void ensureCorrectAmountOfRankLevels() {
        assertEquals(26, RankUtil.getRanklevels().length);
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
    public void ensureCorrectRankLevels() {
        for (final int i: rankLevels) {
            final Rank rank = RankUtil.getRankFromLevel(i);
            assertEquals(i, rank.getLevel());
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
