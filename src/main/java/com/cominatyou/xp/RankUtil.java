package com.cominatyou.xp;

import java.util.HashMap;

public class RankUtil {
    private final static int[] rankLevels = { 1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300 };
    // private final static int[] rankLevels = {1, 5, 10, 15}; // TODO: Change for testing

    // private final static String[] ranksArr = { "Time Worm", "Nitten", "Tide Mouse", "Elf", "Twig", "Woff", "Vitra", "Nisse",
    //         "Forest Giant", "Troll", "Adventurer", "Sparrow Scout", "Johanna", "Librarian", "Marra", "Mad Scientist",
    //         "Mountain Giant", "Weather Spirit", "Thunder Bird", "Black Hound", "Lindworm", "Trevor", "Alfur", "David",
    //         "Frida", "Hilda" };

    private final static Rank[] ranks = { new Rank("Time Worm", 1, 505141598946852885L),
            new Rank("Nitten", 5, 492871026640814090L), new Rank("Tide Mouse", 10, 587846293091778560L),
            new Rank("Elf", 15, 644366736581459998L), new Rank("Twig", 20, 492870780770713602L),
            new Rank("Woff", 25, 644366895231008788L), new Rank("Vittra", 30, 644367091683688488L),
            new Rank("Nisse", 35, 587994460520972288L), new Rank("Forest Giant", 40, 492871177648472077L),
            new Rank("Troll", 45, 644367516604432384L), new Rank("Adventurer", 50, 498582087087947790L),
            new Rank("Sparrow Scout", 60, 498582101483061268L), new Rank("Johanna", 70, 547261495231119370L),
            new Rank("Librarian", 80, 547263982151401482L), new Rank("Marra", 90, 547261632703627274L),
            new Rank("Mad Scientist", 100, 587846803450363916L), new Rank("Mountain Giant", 120, 644368570352009237L),
            new Rank("Weather Spirit", 140, 547261953421082635L), new Rank("Thunder Bird", 160, 520057898731307009L),
            new Rank("Black Hound", 180, 587846612391297034L), new Rank("Lindworm", 200, 644368923331919892L),
            new Rank("Trevor", 220, 644369091901259776L), new Rank("Alfur", 240, 644369272474173440L),
            new Rank("David", 260, 644369376274939916L), new Rank("Frida", 280, 644369526129033217L),
            new Rank("Hilda", 300, 644369712108666903L) };

    // TODO: Change for testing

    // private final static Rank[] ranks = { new Rank("Time Worm", 1, 936499728970043453L),
    // new Rank("Nitten", 5, 936499765569548308L), new Rank("Tide Mouse", 10, 936499819290177556L),
    // new Rank("Elf", 15, 936499850101522452L) };


    private static final HashMap<Integer, Rank> ranksMap = new HashMap<>(26);

    static {
        for (int i = 0; i < ranks.length; i++) {
            ranksMap.put(rankLevels[i], ranks[i]);
        }
    }

    private static int getRankLevelFromLevel(int level) {
        for (int i = 0; i < rankLevels.length; i++) {
            if (level == rankLevels[i]) return level;
            if (level < rankLevels[i + 1]) return rankLevels[i];
        }
        return -1;
    }

    public static Rank getRankFromLevel(int level) {
        return ranksMap.get(getRankLevelFromLevel(level));
    }

    public static boolean isLevelRankLevel(int level) {
        return ranksMap.containsKey(level);
    }

    public static int[] getRanklevels() {
        return rankLevels;
    }

    public static String getRankName(int level) {
        final int rankLevel = getRankLevelFromLevel(level);
        return ranksMap.get(rankLevel).getName();
    }
}
