package com.example.weather3dwallpaper.weather;

import android.content.Context;
import android.net.Uri;
import com.example.weather3dwallpaper.R;
import com.example.weather3dwallpaper.preferences.AppPreferencesProxy;
import com.example.weather3dwallpaper.weather.api.WeatherInformationAPI;
import com.example.weather3dwallpaper.weather.api.WeatherVideoDownloader;

public class WeatherUIHandler {
    
    private static final int TEMPERATURE_THRESHOLD = 3;
    private static final int HUMIDITY_THRESHOLD = 3;
    
    private final WeatherInformationAPI weatherInformationAPI;
    private final WeatherVideoDownloader videoDownloader;
    private final AppPreferencesProxy appPreferencesProxy;
    private final String fallbackVideoUri;

    public WeatherUIHandler(WeatherInformationAPI weatherInformationAPI, WeatherVideoDownloader videoDownloader, AppPreferencesProxy appPreferencesProxy, Context context) {
        this.weatherInformationAPI = weatherInformationAPI;
        this.videoDownloader = videoDownloader;
        this.appPreferencesProxy = appPreferencesProxy;
        this.fallbackVideoUri = "android.resource://" + context.getPackageName() + "/" + R.raw.pollito2;
    }

    public Uri getAdequateVideoToShow(){
        
        var lastInfo = weatherInformationAPI.getLastInformation();
        var currentInfo = localInformation();
        
        if(nonDownload(lastInfo, currentInfo)){
            return Uri.parse(currentInfo.getVideoUrl());
        }
        
        storeUpdatedInformation(lastInfo);
        return videoDownloader.startDownload(lastInfo.getVideoUrl());
        
    }
    
    private boolean nonDownload(WeatherInformation lastInformation, WeatherInformation currentInformation){
        
        var hasChangedLocation = lastInformation.getLocation().equals(currentInformation.getLocation());
        var isPassedTemperatureThreshold = Math.abs(
                (lastInformation.getTemperature() - currentInformation.getTemperature())
        ) > TEMPERATURE_THRESHOLD;
        var isPassedHumidityThreshold = Math.abs(
                (lastInformation.getHumidity() - currentInformation.getHumidity())
        ) > HUMIDITY_THRESHOLD;
        
        return !hasChangedLocation && !isPassedTemperatureThreshold && !isPassedHumidityThreshold;
    }
    
    private WeatherInformation localInformation(){
        return new WeatherInformation(
                appPreferencesProxy.get("weather.current.video-url", fallbackVideoUri).blockingFirst(),
                appPreferencesProxy.get("weather.current.location", "Unknown").blockingFirst(),
                Long.parseLong(
                        appPreferencesProxy.get("weather.current.temperature.quantity", "0").blockingFirst()
                ),
                appPreferencesProxy.get("weather.current.temperature.unit-of-measurement", "Celsius").blockingFirst(),
                Long.parseLong(
                        appPreferencesProxy.get("weather.current.humidity", "0").blockingFirst()
                ),
                appPreferencesProxy.get("weather.current.description", "Unknown").blockingFirst(),
                Long.parseLong(
                        appPreferencesProxy.get("weather.current.timestamp", "0").blockingFirst()
                )
        );
    }
    
    private void storeUpdatedInformation(WeatherInformation weatherInformation){
        appPreferencesProxy.put("weather.current.video-url", weatherInformation.getVideoUrl());
        appPreferencesProxy.put("weather.current.location", weatherInformation.getLocation());
        appPreferencesProxy.put("weather.current.temperature.quantity", String.valueOf(weatherInformation.getTemperature()));
        appPreferencesProxy.put("weather.current.temperature.unit-of-measurement", weatherInformation.getTemperatureUnitOfMesurement());
        appPreferencesProxy.put("weather.current.humidity", String.valueOf(weatherInformation.getHumidity()));
        appPreferencesProxy.put("weather.current.description", weatherInformation.getDescription());
        appPreferencesProxy.put("wheather.current.timestamp", String.valueOf(weatherInformation.getDownloadTimestamp()));
    }
    
}
