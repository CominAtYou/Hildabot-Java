package com.cominatyou.xp;

public class Rank {
    private int level;
    private long roleId;
    private String name;

    /**
     * Create a new instance object, representing a rank in Hildacord.
     * @param name The name of the rank.
     * @param level The minimum level for the rank.
     * @param roleId The ID of the role for the rank.
     */
    protected Rank(String name, int level, long roleId) {
        this.level = level;
        this.name = name;
        this.roleId = roleId;
    }

    /**
     * Get the minimum level of the rank.
     * @return The rank's minimum level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the ID of the role for the rank.
     * @return The ID of the rank's role.
     */
    public long getId() {
        return roleId;
    }

    /**
     * Get the name of the rank.
     * @return The name of the rank.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the minimum amount of XP required for the rank.
     * @return The minimum amount of XP needed to attain the rank.
     */
    public int getMinimumXP() {
        return XPSystemCalculator.determineMinimumTotalXPForLevel(level);
    }
}
