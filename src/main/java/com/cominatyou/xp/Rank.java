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

    public int getLevel() {
        return level;
    }

    public long getId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public int getMinimumXP() {
        return XPSystemCalculator.determineMinimumTotalXPForLevel(level);
    }
}
