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

import java.util.Objects;

public class WallPaperEntryPointClass extends WallpaperService {
    
    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }
    
    public class WallpaperEngine extends Engine {
        
        private ExoPlayer exoPlayer;
        private Handler handler = new Handler(Looper.getMainLooper());
        private Paint pencil = new Paint();
        private boolean isVisible;
        private final Runnable drawRunnable = () -> {
            drawFrame();
            
            if(!isVisible){
                return;
            }
            
            handler.postDelayed(this.drawRunnable, 1000/30);
        };

        
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            
            exoPlayer = new ExoPlayer.Builder(getApplicationContext()).build();
            exoPlayer.setVideoSurface(holder.getSurface());
            var videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pollito2);
            var mediaItem = MediaItem.fromUri(videoUri);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
            exoPlayer.setVolume(0);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(isVisible);
            
            exoPlayer.addListener(new ExoPlayerListener(handler, drawRunnable, this::releasePlayer));

        }
        
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            isVisible = visible;
            
            Log.i("onVisibilityChanged", "onVisibilityChanged: " + visible);
            
            if(Objects.nonNull(exoPlayer)){
                exoPlayer.setPlayWhenReady(visible);
            }
            
            if(!isVisible){
                handler.removeCallbacks(drawRunnable);
                return;
            }
            
            handler.post(drawRunnable);
        }
        
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            isVisible = false;
            
            Log.i("onSurfaceDestroyed", "onSurfaceDestroyed: " + isVisible);
            
            handler.removeCallbacks(drawRunnable);
            releasePlayer();
        }
        
        public void releasePlayer(){
            
            Log.i("releasePlayer", "releasePlayer: " + exoPlayer);
            
            if(Objects.isNull(exoPlayer)){
                return;
            }

            exoPlayer.release();
            exoPlayer = null;
        }
        
        public void drawFrame(){
            
            if(!isVisible){
                return;
            }
            
            
            Canvas canvas = null;
            
            try {
                
//                wallpaperSurfaceHandler.updateTexImage();
                canvas = getSurfaceHolder().lockCanvas();
                canvas.drawColor(Color.BLACK);
//                
//                var bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rainy_city);
//
//                var srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//                var destRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
//
//                // Draw the bitmap onto the canvas, scaling it to fill the screen
//                canvas.drawBitmap(bitmap, srcRect, destRect, pencil);
//
//                wallpaperSurfaceHandler.drawFrame(canvas, pencil);
//
//                if(wallpaperSurfaceHandler.isAnyZeroOrNegativeMesure(canvas) || mediaResourcesHandler.isAnyMediaMeasureZero()){
//                    Log.e(WallpaperEngine.class.getName(), "Any measure for canvas or media player is zero");
//                    return;
//                }
                
                
                
            }
            catch (RuntimeException e){
                e.printStackTrace();
            }
            finally {
                
                if(Objects.nonNull(canvas)){
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
                
            }
        }

    }

    public static class ExoPlayerListener implements Player.Listener{
        
        private final Handler handler;
        private final Runnable onRenderedFirstFrame;
        private final Runnable onPlayerError;
        
        
        public ExoPlayerListener(Handler handler, Runnable onRenderedFirstFrame, Runnable onPlayerError) {
            this.handler = handler;
            this.onRenderedFirstFrame = onRenderedFirstFrame;
            this.onPlayerError = onPlayerError;
        }

        @Override
        public void onPlayerError(PlaybackException playbackException) {
            Log.i("ExoPlayerListener", "onPlayerError: " + playbackException.getMessage());
            onPlayerError.run();
        }
        
        @Override
        public void onRenderedFirstFrame() {
            Log.i("onRenderedFirstFrame", "onRenderedFirstFrame");
            handler.post(onRenderedFirstFrame);
        }
    }
    
}
