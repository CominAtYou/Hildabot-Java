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
    }

    @Test
    public void testUserXPFromLevel() {
        assertEquals(50, determineMinimumUserFacingXPForLevel(2));
        assertEquals(65, determineMinimumUserFacingXPForLevel(3));
    }
}
