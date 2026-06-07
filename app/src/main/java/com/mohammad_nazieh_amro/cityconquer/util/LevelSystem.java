package com.mohammad_nazieh_amro.cityconquer.util;

/**
 * Defines the XP thresholds for each level and provides helper methods
 * for computing a user's level from their total XP.
 *
 * Level thresholds (XP required to reach each level):
 *   Level 1  –    0 XP
 *   Level 2  –  200 XP
 *   Level 3  –  500 XP
 *   Level 4  – 1000 XP
 *   Level 5  – 2000 XP
 *   Level 6  – 3500 XP
 *   Level 7  – 5500 XP
 *   Level 8  – 8000 XP
 *   Level 9  – 11000 XP
 *   Level 10 – 15000 XP
 */
public class LevelSystem {

    /** XP thresholds – index = level - 1, value = XP required to reach that level */
    private static final int[] LEVEL_THRESHOLDS = {
            0,      // Level 1
            200,    // Level 2
            500,    // Level 3
            1000,   // Level 4
            2000,   // Level 5
            3500,   // Level 6
            5500,   // Level 7
            8000,   // Level 8
            11000,  // Level 9
            15000   // Level 10 (max)
    };

    public static final int MAX_LEVEL = LEVEL_THRESHOLDS.length;

    /**
     * Returns the level (1-based) for a given total XP amount.
     */
    public static int getLevelForXP(int totalXP) {
        int level = 1;
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (totalXP >= LEVEL_THRESHOLDS[i]) {
                level = i + 1;
                break;
            }
        }
        return level;
    }

    /**
     * Returns the XP required to reach the given level (1-based).
     */
    public static int getXPForLevel(int level) {
        int idx = Math.max(0, Math.min(level - 1, LEVEL_THRESHOLDS.length - 1));
        return LEVEL_THRESHOLDS[idx];
    }

    /**
     * Returns the XP required for the NEXT level after the given total XP.
     * Returns -1 if already at max level.
     */
    public static int getXPForNextLevel(int totalXP) {
        int currentLevel = getLevelForXP(totalXP);
        if (currentLevel >= MAX_LEVEL) return -1;
        return LEVEL_THRESHOLDS[currentLevel]; // [currentLevel] is index for next level
    }

    /**
     * Returns how much XP is needed to reach the next level from the current total.
     * Returns 0 if at max level.
     */
    public static int getXPToNextLevel(int totalXP) {
        int nextLevelXP = getXPForNextLevel(totalXP);
        if (nextLevelXP < 0) return 0;
        return Math.max(0, nextLevelXP - totalXP);
    }

    /**
     * Returns progress (0.0 – 1.0) within the current level.
     */
    public static float getLevelProgress(int totalXP) {
        int currentLevel = getLevelForXP(totalXP);
        if (currentLevel >= MAX_LEVEL) return 1.0f;
        int currentLevelXP = LEVEL_THRESHOLDS[currentLevel - 1];
        int nextLevelXP    = LEVEL_THRESHOLDS[currentLevel];
        return (float)(totalXP - currentLevelXP) / (nextLevelXP - currentLevelXP);
    }
    /**
     * Returns a dynamic rank title string based on user level.
     */
    public static String getRankForLevel(int level) {
        if (level < 3) return "NOVICE EXPLORER 🧭";
        if (level < 5) return "ROOKIE PATHFINDER 🗺️";
        if (level < 7) return "SEASONED ADVENTURER 🎒";
        if (level < 9) return "ELITE VOYAGER 🚀";
        if (level < 10) return "MASTER CONQUEROR 👑";
        return "LEGENDARY EXPLORER 🌟";
    }
}
