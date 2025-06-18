package com.example.weather3dwallpaper.weather.api;

import android.util.Log;
import com.example.weather3dwallpaper.weather.WeatherInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

public class WeatherInformationAPI {
    
    private static final String GET_INFO_URL = "http://localhost:8080/weather-api/last-information";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public WeatherInformation getLastInformation(){
        
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        
        Log.i(WeatherInformationAPI.class.getSimpleName(), "Starting weather information API call...");
        
        try{
            
            var url = new URL(GET_INFO_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                Log.e(WeatherVideoDownloader.class.getSimpleName(), "Error getting weather information");
                Log.e(WeatherVideoDownloader.class.getSimpleName(), "Response code: "+connection.getResponseCode());
                Log.e(WeatherVideoDownloader.class.getName(), "Message: " + connection.getResponseMessage());
                throw new IOException("Error getting weather information");
            }
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            
            while((line = reader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            
            Log.i(WeatherVideoDownloader.class.getSimpleName(), "Finished weather information request");
            Log.i(WeatherVideoDownloader.class.getSimpleName(), stringBuilder.toString());
            
            return fromJson(stringBuilder.toString());
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

            if(connection != null){
                connection.disconnect();
            }

            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w(WeatherVideoDownloader.class.getSimpleName(), "Error closing input stream");
                }
            }
            
        }
        
    }
    
    private WeatherInformation fromJson(String json) throws JsonProcessingException {
        
        var tree = MAPPER.readTree(json);
        
        return new WeatherInformation(
                tree.get("videoUrl").asText(),
                tree.get("location").asText(),
                tree.get("temperature").asLong(),
                tree.get("temperatureUnitOfMeasurement").asText(),
                tree.get("humidity").asLong(),
                tree.get("description").asText(),
                System.currentTimeMillis()
        );
    }
    
}
