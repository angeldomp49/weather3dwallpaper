package com.example.weather3dwallpaper.weather;

public class WeatherInformation {
    
    private final String videoUrl;
    private final String location;
    private final String temperature;
    private final String humidity;
    private final String description;
    private final long downloadTimestamp;

    public WeatherInformation(String videoUrl, String location, String temperature, String humidity, String description, long downloadTimestamp) {
        this.videoUrl = videoUrl;
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.description = description;
        this.downloadTimestamp = downloadTimestamp;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }
    
    public long getDownloadTimestamp() {return downloadTimestamp;}
}
