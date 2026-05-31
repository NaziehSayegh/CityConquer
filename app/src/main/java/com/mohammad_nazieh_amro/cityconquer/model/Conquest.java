package com.mohammad_nazieh_amro.cityconquer.model;

import java.util.List;

public class Conquest {
    private String userId;
    private String cityId;
    private List<String> completedLandmarks;
    private int cityXP;
    private String rank;

    public Conquest() {}

    public Conquest(String userId, String cityId) {
        this.userId = userId;
        this.cityId = cityId;
        this.cityXP = 0;
        this.rank = "Explorer";
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCityId() { return cityId; }
    public void setCityId(String cityId) { this.cityId = cityId; }

    public List<String> getCompletedLandmarks() { return completedLandmarks; }
    public void setCompletedLandmarks(List<String> completedLandmarks) {
        this.completedLandmarks = completedLandmarks;
    }

    public int getCityXP() { return cityXP; }
    public void setCityXP(int cityXP) { this.cityXP = cityXP; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String calculateRank(int completedCount, int totalCount) {
        double percentage = (double) completedCount / totalCount * 100;
        if (percentage >= 100) return "Legend";
        else if (percentage >= 75) return "Conqueror";
        else if (percentage >= 50) return "Adventurer";
        else return "Explorer";
    }
}