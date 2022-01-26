package com.cominatyou.xp;

public class Rank {
    private int level;
    private long roleID;
    private String name;

    /**
     * Create a new instance object, representing a rank in Hildacord.
     * @param name The name of the rank.
     * @param level The minimum level for the rank.
     * @param roleID The ID of the role for the rank.
     */
    protected Rank(String name, int level, long roleID) {
        this.level = level;
        this.name = name;
        this.roleID = roleID;
    }

    public int getLevel() {
        return level;
    }

    public long getId() {
        return roleID;
    }

    public String getName() {
        return name;
    }

    public int getMinimumXP() {
        return XPSystemCalculator.determineMinimumXPForLevel(level);
    }
}
