package com.mohammad_nazieh_amro.cityconquer.model;

public class Landmark {
    private String id;
    private String name;
    private String cityId;
    private String description;
    private double latitude;
    private double longitude;
    private String photoUrl;
    private int xp;
    private boolean conquered;

    public Landmark() {}

    public Landmark(String id, String name, String cityId, String description,
                    double latitude, double longitude, String photoUrl, int xp) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoUrl = photoUrl;
        this.xp = xp;
        this.conquered = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCityId() { return cityId; }
    public void setCityId(String cityId) { this.cityId = cityId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public boolean isConquered() { return conquered; }
    public void setConquered(boolean conquered) { this.conquered = conquered; }
}