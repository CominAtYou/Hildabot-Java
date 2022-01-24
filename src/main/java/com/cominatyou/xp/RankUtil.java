package com.cominatyou.xp;

import java.util.HashMap;

public class RankUtil {
    private final static int[] rankLevels = { 1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180, 200, 240, 260, 280, 300 };

    final static String[] ranksArr = { "Time Worm", "Nitten", "Tide Mouse", "Elf", "Twig", "Woff", "Vitra", "Nisse",
            "Forest Giant", "Troll", "Adventurer", "Sparrow Scout", "Johanna", "Librarian", "Marra", "Mad Scientist",
            "Mountain Giant", "Weather Spirit", "Thunder Bird", "Black Hound", "Lindworm", "Trevor", "Alfur", "David",
            "Frida", "Hilda" };

    private final static Rank[] ranks = { new Rank("Time Worm", 1, 505141598946852885L),
            new Rank("Nitten", 5, 492871026640814090L), new Rank("Tide Mouse", 10, 587846293091778560L),
            new Rank("Elf", 15, 644366736581459998L), new Rank("Twig", 20, 492870780770713602L),
            new Rank("Woff", 25, 644366895231008788L), new Rank("Vittra", 30, 644367091683688488L),
            new Rank("Nisse", 35, 587994460520972288L), new Rank("Forest Giant", 40, 492871177648472077L),
            new Rank("Troll", 45, 644367516604432384L), new Rank("Adventurer", 50, 498582087087947790L),
            new Rank("Sparrow Scout", 60, 498582101483061268L), new Rank("Johanna", 70, 547261495231119370L),
            new Rank("Librarian", 80, 547263982151401482L), new Rank("Marra", 90, 547261632703627274L),
            new Rank("Mad Scientist", 100, 587846803450363916L), new Rank("Mountain Giant", 120, 644368570352009237L),
            new Rank("Weather Spirit", 140, 547261953421082635L), new Rank("Thunder Bird", 160, 587846612391297034L),
            new Rank("Black Hound", 180, 587846612391297034L), new Rank("Lindworm", 200, 644368923331919892L)
    };

    private static final HashMap<Integer, Rank> rankMap = new HashMap<>(26);

    static {
        int i = 1;
        int index = 0;
        while (i <= 300) {
            rankMap.put(i, ranks[index]);
            index++;
            if (i == 1) i = 5;
            else if (i >= 5 && i < 50) i += 5;
            else if (i >= 50 && i <= 90) i += 10;
            else i += 20;
        }
    }

    public static HashMap<Integer, Rank> getRankMap() {
        return rankMap;
    }

    public static int[] getRanklevels() {
        return rankLevels;
    }

    public static int getRankLevelFromLevel(int level) {
        for (int i = 0; i < rankLevels.length; i++) {
            if (rankLevels[i + 1] > rankLevels[i]) return rankLevels[i];
        }
        return -1;
    }

    public static String getRankName(int level) {
        final int rankLevel = getRankLevelFromLevel(level);
        return rankMap.get(rankLevel).getName();
    }
}
