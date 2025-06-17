package com.example.weather3dwallpaper.media;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Objects;

public class MediaPlayerProxy {
    
    private final ExoPlayer exoPlayer;
    private final Context context;
    private final SurfaceHolder holder;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isVisible;
    private final Runnable drawRunnable = () -> {
        drawFrame();

        if(!isVisible){
            return;
        }

        handler.postDelayed(this.drawRunnable, 1000/30);
    };

    public MediaPlayerProxy(Context context, SurfaceHolder holder) {
        this.context = context;
        this.exoPlayer = new ExoPlayer.Builder(context).build();
        this.holder = holder;
    }
    
    public void init(boolean isVisible, Uri source){
        updateSource(source);
        exoPlayer.setPlayWhenReady(isVisible);
        exoPlayer.addListener(new ExoPlayerListener(handler, drawRunnable, this::releasePlayer));
    }
    
    public void updateSource(Uri source){
        exoPlayer.setVideoSurface(holder.getSurface());
        var mediaItem = MediaItem.fromUri(source);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        exoPlayer.setVolume(0);
        exoPlayer.prepare();
    }

    public void releasePlayer(){

        Log.i("releasePlayer", "releasePlayer: " + exoPlayer);

        handler.removeCallbacks(drawRunnable);

        if(Objects.isNull(exoPlayer)){
            return;
        }

        exoPlayer.release();
    }
    
    public void onVisibilityChanged(boolean visible, Uri source) {
        isVisible = visible;

        Log.i("onVisibilityChanged", "onVisibilityChanged: " + visible);

        if(!isVisible){
            handler.removeCallbacks(drawRunnable);
            return;
        }
        
        updateSource(source);

        if(Objects.nonNull(exoPlayer)){
            exoPlayer.setPlayWhenReady(visible);
        }

        handler.post(drawRunnable);
    }

    public void drawFrame(){

        if(!isVisible){
            return;
        }


        Canvas canvas = null;

        try {

            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);



        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        finally {

            if(Objects.nonNull(canvas)){
                holder.unlockCanvasAndPost(canvas);
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
