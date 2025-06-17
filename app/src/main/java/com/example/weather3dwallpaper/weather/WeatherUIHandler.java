package com.example.weather3dwallpaper.weather;

import android.content.Context;
import com.example.weather3dwallpaper.preferences.AppPreferencesProxy;

public class WeatherUIHandler {
    
    private final WeatherInformationAPI weatherInformationAPI;
    private final WeatherVideoDownloader videoDownloader;
    private final Context context;
    private final AppPreferencesProxy appPreferencesProxy;

    public WeatherUIHandler(WeatherInformationAPI weatherInformationAPI, WeatherVideoDownloader videoDownloader, Context context, AppPreferencesProxy appPreferencesProxy) {
        this.weatherInformationAPI = weatherInformationAPI;
        this.videoDownloader = videoDownloader;
        this.context = context;
        this.appPreferencesProxy = appPreferencesProxy;
    }

    public String getAdequateVideoToShow(){
        
        var lastInfo = weatherInformationAPI.getLastInformation();
        var flatCurrentInfo = appPreferencesProxy.get("currentWeatherInfo");
        var currentInfo = fromJson(flatCurrentInfo);
        
        if(nonDownload(lastInfo, currentInfo)){
            return appPreferencesProxy.get("currentWeatherVideoFile");
        }
        
        return videoDownloader.startDownload();
        
    }
    
    private boolean nonDownload(WeatherInformation lastInformation, WeatherInformation currentInformation){
        return false;
    }
    
    private WeatherInformation fromJson(String json){
        return new WeatherInformation(
                "",
                "",
                "",
                "",
                "",
                0L
        );
    }
    
}
