package com.example.weather3dwallpaper.weather.api;

import android.net.Uri;
import android.util.Log;
import com.example.weather3dwallpaper.storage.VideoFileStorageProxy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherVideoDownloader {
    
    private final VideoFileStorageProxy videoFileStorageProxy;

    public WeatherVideoDownloader(VideoFileStorageProxy videoFileStorageProxy) {
        this.videoFileStorageProxy = videoFileStorageProxy;
    }

    public Uri startDownload(String downloadVideoUrl){
        
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        Log.i(WeatherInformationAPI.class.getSimpleName(), "Starting weather video download...");

        try{
            
            var url =  new URL(downloadVideoUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                
                Log.e(WeatherVideoDownloader.class.getSimpleName(), "Error downloading video");
                Log.e(WeatherVideoDownloader.class.getSimpleName(), "Response code: "+connection.getResponseCode());
                Log.e(WeatherVideoDownloader.class.getName(), "Message: " + connection.getResponseMessage());
                throw new IOException("Error downloading video");
            }   
            
            inputStream = connection.getInputStream();
            outputStream = videoFileStorageProxy.destinationStream();
            
            var buffer = new byte[4096];
            int bytesRead;
            
            while((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            outputStream.flush();
            
            Log.i(WeatherVideoDownloader.class.getSimpleName(), "Finished video download");
            return Uri.fromFile(videoFileStorageProxy.getLocalVideoFile());
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            
            if(connection != null){
                connection.disconnect();
            }
            
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.w(WeatherVideoDownloader.class.getSimpleName(), "Error closing output stream");
                }
            }
            
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.w(WeatherVideoDownloader.class.getSimpleName(), "Error closing input stream");
                }
            }
            
        }
        
    }
    
}
