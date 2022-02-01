package com.cominatyou.xp;

public class XPSystemCalculator {
    /**
     * Determine the level for an XP value.
     *
     * @param xp The XP value, represented by a <b>positive</b> integer.
     * @return The level associated with the XP value.
     * @throws IllegalArgumentException If a negative value is passed.
    */
    public static int determineLevelFromXP(int xp) throws IllegalArgumentException {
        if (xp < 0) throw new IllegalArgumentException("XP must be a positive value.");
        if (xp < 50) return 1;

        // TODO: Find a less stupid way for this later.
        for (int i = 1; i < 500; i++) {
            final int possibleNextLevelXP = determineMinimumTotalXPForLevel(i + 1);
            if (possibleNextLevelXP > xp) return i;
        }
        return 1;
    }

    /**
     * Determine the minimum total XP required to attain a level.
     *
     * @param level The level, represented by a <b>positive</b> integer.
     * @return The minimum total XP required to attain the level.
     * @throws IllegalArgumentException If a negative value is passed.
    */
    public static int determineMinimumTotalXPForLevel(int level) throws IllegalArgumentException {
        if (level < 0) throw new IllegalArgumentException("XP must be a positive value.");
        if (level < 2) return 0;
        int xp = 50;
        for (int i = 2; i < level; i++) {
            xp += 50 + 15 * (i - 1);
        }
        return xp;
    }

    /**
     * Determine the minimum XP required to level up from the previous level.
     *
     * @param level The level, represented by a <b>positive</b> integer.
     * @return The minimum XP required to attain the level from the previous level.
     * @throws IllegalArgumentException If a negative value is passed.
    */
    public static int determineMinimumUserFacingXPForLevel(int level) throws IllegalArgumentException {
        if (level < 0) throw new IllegalArgumentException("XP must be a positive value.");
        if (level < 2) return 0;
        return 50 + 15 * (level - 2);
    }
}
