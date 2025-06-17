package com.example.weather3dwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;

public class WallpaperSurfaceHandler {
    
    private final SurfaceTexture surfaceTexture;
    private final Surface surface;
    
    public WallpaperSurfaceHandler(){
        surfaceTexture = new SurfaceTexture(0);
        surface = new Surface(surfaceTexture);
    }
    
    public void setOnFrameAvailableListener(SurfaceTexture.OnFrameAvailableListener listener){
        surfaceTexture.setOnFrameAvailableListener(listener);
    }
    
    public void release(){
        surfaceTexture.release();
        surface.release();
    }

    public Surface getSurface() {
        return surface;
    }
    
    public void drawFrame(Canvas canvas, Paint pencil){
        Log.i("drawFrameWallpaperSurfaceHandler", "Before Video Wallpaper Playing");
        surfaceTexture.updateTexImage();
        canvas.drawColor(Color.BLACK);

        surfaceTexture.setDefaultBufferSize(canvas.getWidth(), canvas.getHeight());

        pencil.setColor(Color.WHITE);
        pencil.setTextSize(80);
        pencil.setTextAlign(Paint.Align.CENTER);
        Log.i("drawFrameWallpaperSurfaceHandler", "Video Wallpaper Playing");
        canvas.drawText("Video Wallpaper Playing", canvas.getWidth() / 2, canvas.getHeight() / 2, pencil);
    }

    public boolean isAnyZeroOrNegativeMesure(Canvas canvas){
        return canvas.getWidth() <= 0 ||
                canvas.getHeight() <= 0;
    }
    
    
}
