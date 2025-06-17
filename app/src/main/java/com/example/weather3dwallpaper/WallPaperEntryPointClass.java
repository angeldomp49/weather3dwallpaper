package com.example.weather3dwallpaper;

import android.graphics.*;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import com.example.weather3dwallpaper.media.MediaPlayerProxy;
import com.example.weather3dwallpaper.preferences.AppPreferencesProxy;
import com.example.weather3dwallpaper.weather.WeatherInformationAPI;
import com.example.weather3dwallpaper.weather.WeatherUIHandler;
import com.example.weather3dwallpaper.weather.WeatherVideoDownloader;

import java.util.Objects;

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
                    new WeatherVideoDownloader(),
                    getApplicationContext(),
                    new AppPreferencesProxy(getApplicationContext())
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
