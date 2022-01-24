package com.cominatyou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static com.cominatyou.xp.XPSystemCalculator.*;

import org.junit.Test;

public class LevelTest {
    @Test
    public void testLevelFromXP() {
        assertEquals(4, determineLevelFromXP(80));
        assertNotEquals(3, determineLevelFromXP(25));
        assertEquals(1, determineLevelFromXP(45));
        assertNotEquals(6, determineLevelFromXP(86));
        assertEquals(1, determineLevelFromXP(0));
        assertEquals(1, determineLevelFromXP(-8)); // This method should never be passed a negative integer, and should return 1 if one is passed.
    }

    @Test
    public void testXPFromLevel() {
        assertEquals(0, determineMinimumXPForLevel(0));
        assertEquals(50 + 65, determineMinimumXPForLevel(3));
        assertEquals(50 + 65 + 80, determineMinimumXPForLevel(4));
    }
}
