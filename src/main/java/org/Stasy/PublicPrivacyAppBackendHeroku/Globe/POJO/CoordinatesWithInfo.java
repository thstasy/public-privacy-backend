package org.Stasy.PublicPrivacyAppBackendHeroku.Globe.POJO;

public class CoordinatesWithInfo {
    public double latitude;
    public double longitude;
    public String category;
    public String title;

    public CoordinatesWithInfo(double latitude, double longitude, String category, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.title = title;
    }
}