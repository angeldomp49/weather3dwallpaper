package com.example.weather3dwallpaper.storage;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class VideoFileStorageProxy {
    
    private final Context context;
    private final File localVideoFile;

    public VideoFileStorageProxy(Context context) {
        this.context = context;
        this.localVideoFile = new File(context.getFilesDir(),"weather3dwallpapervideo.mp4");
    }

    public OutputStream destinationStream() throws FileNotFoundException {
        return new FileOutputStream(localVideoFile);
    }

    public File getLocalVideoFile() {
        return localVideoFile;
    }
    
}
