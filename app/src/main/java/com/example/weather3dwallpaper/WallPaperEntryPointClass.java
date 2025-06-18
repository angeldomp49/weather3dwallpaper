package com.example.weather3dwallpaper;

import android.graphics.*;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import com.example.weather3dwallpaper.media.MediaPlayerProxy;
import com.example.weather3dwallpaper.preferences.AppPreferencesProxy;
import com.example.weather3dwallpaper.storage.VideoFileStorageProxy;
import com.example.weather3dwallpaper.weather.api.WeatherInformationAPI;
import com.example.weather3dwallpaper.weather.WeatherUIHandler;
import com.example.weather3dwallpaper.weather.api.WeatherVideoDownloader;

public class WallPaperEntryPointClass extends WallpaperService {
    
    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }
    
    public class WallpaperEngine extends Engine {
        
        private Paint pencil = new Paint();
        private boolean isVisible;
        private WeatherUIHandler weatherUIHandler;
        private MediaPlayerProxy mediaPlayerProxy;

        
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            
            
            this.weatherUIHandler = new WeatherUIHandler(
                    new WeatherInformationAPI(),
                    new WeatherVideoDownloader(
                            new VideoFileStorageProxy(getApplicationContext())
                    ),
                    new AppPreferencesProxy(getApplicationContext()),
                    getApplicationContext()
            );
            
            this.mediaPlayerProxy = new MediaPlayerProxy(getApplicationContext(), holder);
            
            mediaPlayerProxy.init(isVisible, weatherUIHandler.getAdequateVideoToShow());

        }
        
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            isVisible = visible;
            
            mediaPlayerProxy.onVisibilityChanged(isVisible, weatherUIHandler.getAdequateVideoToShow());
        }
        
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            isVisible = false;
            
            mediaPlayerProxy.releasePlayer();
        }
        

    }
    
}
