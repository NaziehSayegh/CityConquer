package com.mohammad_nazieh_amro.cityconquer.model;

public class City {
    private String id;
    private String name;
    private String country;
    private String coverImage;
    private int totalLandmarks;

    public City() {}

    public City(String id, String name, String country, String coverImage, int totalLandmarks) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coverImage = coverImage;
        this.totalLandmarks = totalLandmarks;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public int getTotalLandmarks() { return totalLandmarks; }
    public void setTotalLandmarks(int totalLandmarks) { this.totalLandmarks = totalLandmarks; }
}