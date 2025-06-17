package com.example.weather3dwallpaper.weather;

import android.content.Context;
import android.net.Uri;
import com.example.weather3dwallpaper.R;
import com.example.weather3dwallpaper.preferences.AppPreferencesProxy;

import java.net.URL;

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

    public Uri getAdequateVideoToShow(){
        
        var lastInfo = weatherInformationAPI.getLastInformation();
        var flatCurrentInfo = appPreferencesProxy.get("currentWeatherInfo");
        var currentInfo = fromJson(flatCurrentInfo);
        
        if(nonDownload(lastInfo, currentInfo)){
            return Uri.parse(
                    appPreferencesProxy.get("currentWeatherVideoFile")
            );
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
    
    private Uri getFallbackVideoUri(){
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.pollito2);
    }
    
}
