package com.example.weather3dwallpaper;

import android.graphics.*;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Objects;

public class WallPaperEntryPointClass extends WallpaperService {
    
    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }
    
    public class WallpaperEngine extends Engine {
        
        private Paint pencil = new Paint();
        private boolean isVisible;
        private final MediaResourcesHandler mediaResourcesHandler = new MediaResourcesHandler(getApplicationContext());
        private WallpaperSurfaceHandler wallpaperSurfaceHandler;
        
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            
            try{
                
                wallpaperSurfaceHandler = new WallpaperSurfaceHandler();
                wallpaperSurfaceHandler.setOnFrameAvailableListener(texture -> {
                    
                    Log.i("onFrameAvailableListener", "isVisible: " + isVisible);
                    
                    if(!isVisible){
                        return;
                    }

                    drawFrame();
                } );
                
//                mediaResourcesHandler.prepareMedia(wallpaperSurfaceHandler.getSurface(), isVisible);
                
            } catch (Exception e) {
                mediaResourcesHandler.releaseMediaPlayer();
                e.printStackTrace();
            }
        }
        
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            isVisible = visible;
            
            Log.i("onVisibilityChanged", "onVisibilityChanged: " + visible);
            
            if(!isVisible){
//                mediaResourcesHandler.stop();
                return;
            }
            
//            mediaResourcesHandler.start();
            drawFrame();
        }
        
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            isVisible = false;
            
            Log.i("onSurfaceDestroyed", "onSurfaceDestroyed: " + isVisible);
            
//            mediaResourcesHandler.releaseMediaPlayer();
            wallpaperSurfaceHandler.release();
        }
        
        public void drawFrame(){
            
            Canvas canvas = null;
            
            try {
                
                canvas = getSurfaceHolder().lockCanvas();
                
                var bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rainy_city);

                var srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                var destRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Draw the bitmap onto the canvas, scaling it to fill the screen
                canvas.drawBitmap(bitmap, srcRect, destRect, pencil);

                wallpaperSurfaceHandler.drawFrame(canvas, pencil);

                if(wallpaperSurfaceHandler.isAnyZeroOrNegativeMesure(canvas) || mediaResourcesHandler.isAnyMediaMeasureZero()){
                    Log.e(WallpaperEngine.class.getName(), "Any measure for canvas or media player is zero");
                    return;
                }
                
                
                
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
    
}
