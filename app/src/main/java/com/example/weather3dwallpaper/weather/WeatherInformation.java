package com.example.weather3dwallpaper.weather;

public class WeatherInformation {
    
    private final String videoUrl;
    private final String location;
    private final long temperature;
    private final String temperatureUnitOfMesurement;
    private final long humidity;
    private final String description;
    private final long downloadTimestamp;

    public WeatherInformation(String videoUrl, String location, long temperature, String temperatureUnitOfMesurement, long humidity, String description, long downloadTimestamp) {
        this.videoUrl = videoUrl;
        this.location = location;
        this.temperature = temperature;
        this.temperatureUnitOfMesurement = temperatureUnitOfMesurement;
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

    public long getTemperature() {
        return temperature;
    }

    public long getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }
    
    public long getDownloadTimestamp() {return downloadTimestamp;}

    public String getTemperatureUnitOfMesurement() {
        return temperatureUnitOfMesurement;
    }
}
