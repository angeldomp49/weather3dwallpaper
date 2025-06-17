package com.example.weather3dwallpaper;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;

public class MediaResourcesHandler {
    
    private final Context context;
    private final MediaPlayer mediaPlayer = new MediaPlayer();

    public MediaResourcesHandler(Context context) {
        this.context = context;
    }

    public void prepareMedia(Surface surface, boolean isVisible) throws IOException {

        Log.i(WallPaperEntryPointClass.class.getName(), "Before open the video file");
        var assetFileDescriptor = context.getResources().openRawResourceFd(R.raw.pollito2);
        Log.i(WallPaperEntryPointClass.class.getName(), "After open the video file");


        mediaPlayer.setDataSource(
                assetFileDescriptor.getFileDescriptor(),
                assetFileDescriptor.getStartOffset(),
                assetFileDescriptor.getLength()
        );

        assetFileDescriptor.close();
        mediaPlayer.setSurface(surface);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0, 0);
        mediaPlayer.prepare();
        
        Log.i(WallPaperEntryPointClass.class.getName(), "After prepare the media player");
        
        mediaPlayer.setOnPreparedListener(sameMediaPlayer -> {
            sameMediaPlayer.start();
            Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer started");
            Log.i(MediaResourcesHandler.class.getName(), "isVisible "+ isVisible);
        });
        
        mediaPlayer.setOnErrorListener((MediaPlayer sameMediaPlayer, int what, int extra) -> {

            switch (what) {
                case MediaPlayer.MEDIA_ERROR_UNKNOWN: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error Unknown"); break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error server died"); break;
                default: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error not defined"); break;
            }

            switch (extra) {
                case MediaPlayer.MEDIA_ERROR_IO: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error IO"); break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error malformed"); break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error timed out"); break;
                default: Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer extra not defined"); break;
            }

            Log.i(WallPaperEntryPointClass.class.getName(), "MediaPlayer error: "+ what +" "+extra);
            releaseMediaPlayer();
            return false;
        });
        
    }
    
    public void pause(){
        mediaPlayer.pause();
    }
    
    public void stop(){
        mediaPlayer.stop();
    }
    
    public boolean isAnyMediaMeasureZero(){
        return true;
//        return mediaPlayer.getVideoWidth() <= 0 || mediaPlayer.getVideoHeight() <= 0;
    }
    
    public void start(){
        mediaPlayer.start();
    }

    public void releaseMediaPlayer(){

        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        
        mediaPlayer.release();

    }
    
}
