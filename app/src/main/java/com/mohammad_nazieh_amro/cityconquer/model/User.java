package com.mohammad_nazieh_amro.cityconquer.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private int totalXP;
    private int level;
    private String profileImage;
    private List<String> friends;

    public User() {}

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.totalXP = 0;
        this.level = 1;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTotalXP() { return totalXP; }
    public void setTotalXP(int totalXP) { this.totalXP = totalXP; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public List<String> getFriends() { return friends; }
    public void setFriends(List<String> friends) { this.friends = friends; }
}