package com.cominatyou.xp;

public class Rank {
    private int level;
    private long id;
    private String name;

    public Rank(String name, int level, long id) {
        this.level = level;
        this.name = name;
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
