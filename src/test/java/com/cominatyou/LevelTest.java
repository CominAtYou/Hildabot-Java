package com.cominatyou;

import static org.junit.Assert.assertEquals;

import static com.cominatyou.xp.XPSystemCalculator.*;

import org.junit.Test;

public class LevelTest {
    @Test
    public void testXPFromLevel() {
        assertEquals(0, determineMinimumTotalXPForLevel(0));
        assertEquals(50, determineMinimumTotalXPForLevel(2));
        assertEquals(50 + 65, determineMinimumTotalXPForLevel(3));
        assertEquals(50 + 65 + 80, determineMinimumTotalXPForLevel(4));
        assertEquals(50 + 65 + 80 + 95, determineMinimumTotalXPForLevel(5));
        assertEquals(2065, determineMinimumTotalXPForLevel(15));
    }

    @Test
    public void testUserXPFromLevel() {
        assertEquals(50, determineMinimumUserFacingXPForLevel(2));
        assertEquals(65, determineMinimumUserFacingXPForLevel(3));
        assertEquals(620, determineMinimumUserFacingXPForLevel(40));
        assertEquals(635, determineMinimumUserFacingXPForLevel(41));
    }

    @Test
    public void testLevelFromXP() {
        assertEquals(15, determineLevelFromXP(2065));
        assertEquals(6, determineLevelFromXP(400));
        assertEquals(28, determineLevelFromXP(6914));
        assertEquals(74, determineLevelFromXP(43639));
        assertEquals(159, determineLevelFromXP(195274));
        assertEquals(181, determineLevelFromXP(252742));
    }
}
