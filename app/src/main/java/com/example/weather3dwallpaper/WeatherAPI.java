package com.example.weather3dwallpaper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WeatherAPI {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String OPEN_WEATHER_URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/weather?lat={{lat}}&lon={{lon}}&APPID={{apikey}}";
    private static final String OPEN_WEATHER_API_KEY = "434a74cf495d6919c9bea33c0cd74ca9";
    private static final String DUMMY_WEATHER_DATA = "{\"location\":\"Mexico City, Azcapotzalco\", \"temperature\":\"16°C\", \"weatherCondition\":\"Partially Sunny\"}"; 
    
    private final Context context;

    public WeatherAPI(Context context) {
        this.context = context;
    }

    public String getLastUpdate(){
        
        var params = new HashMap<String, String>();
        var location = attemptToGetLocation();
        
        params.put("lat",  String.valueOf(location.getLatitude()));
        params.put("lon",  String.valueOf(location.getLongitude()));
        
        var dynamicUri = setParameters(OPEN_WEATHER_URL_TEMPLATE, params);
        
        HttpURLConnection client = null;

        try {
            client =(HttpURLConnection) (new URL(dynamicUri)).openConnection();
            
            client.setRequestMethod("GET");
            
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(Objects.nonNull(client)){
                client.disconnect();
            }
        }

        return DUMMY_WEATHER_DATA;
    }
    
    private String setParameters(String uri, Map<String, String> params){
        return uri.replace("{{lat}}", Objects.requireNonNull(params.get("lat")))
                .replace("{{lon}}", Objects.requireNonNull(params.get("lon")))
                .replace("{{apikey}}", OPEN_WEATHER_API_KEY);
    }
    
    private Location attemptToGetLocation(){
        boolean isAccessFineLocationPermissionGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean isAccessCoarseLocationPermissionGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        
        if(isAccessFineLocationPermissionGranted && isAccessCoarseLocationPermissionGranted){
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        
        throw new RuntimeException("Missing location permission");
    }
    
}
