package com.example.weather3dwallpaper;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);

        // Set the video URI.
        // R.raw.rainy_city points to your rainy_city.mp4 file in res/raw/
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pollito2);
        videoView.setVideoURI(videoUri);

        // Optional: Add MediaController for basic playback controls (play/pause, seek).
        // This is good for testing but usually not wanted for a background wallpaper.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set listeners for video playback lifecycle
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Called when the video is prepared for playback.
                // Start playback when prepared.
                mp.start();
                Log.d(TAG, "Video prepared and started playing.");

                // You can also loop the video when it finishes.
                mp.setLooping(true);
                mp.setVolume(0f, 0f); // Mute audio for wallpaper-like effect
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Called when the video finishes playing.
                // If not looping, you might want to restart it or do something else.
                Log.d(TAG, "Video playback completed.");
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Called if an error occurs during playback.
                // Log the error codes for debugging.
                String errorString;
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        errorString = "MEDIA_ERROR_UNKNOWN";
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        errorString = "MEDIA_ERROR_SERVER_DIED";
                        break;
                    default:
                        errorString = "UNKNOWN_ERROR_CODE(" + what + ")";
                        break;
                }

                String extraString;
                switch (extra) {
                    case MediaPlayer.MEDIA_ERROR_IO:
                        extraString = "MEDIA_ERROR_IO (File not found / network error)";
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        extraString = "MEDIA_ERROR_MALFORMED (Bad file structure)";
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        extraString = "MEDIA_ERROR_UNSUPPORTED (Codec/Format not supported)";
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        extraString = "MEDIA_ERROR_TIMED_OUT (Operation took too long)";
                        break;
                    default:
                        extraString = "UNKNOWN_EXTRA_CODE(" + extra + ")";
                        break;
                }
                Log.e(TAG, "Video playback error: what=" + errorString + " (" + what + "), extra=" + extraString + " (" + extra + ")");
                // Return true to indicate you've handled the error, false to let it propagate.
                return true;
            }
        });
        
        
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Resume video playback when the activity is resumed.
        if (!videoView.isPlaying()) {
            videoView.start();
            Log.d(TAG, "Video resumed.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause video playback when the activity is paused to save resources.
        if (videoView.isPlaying()) {
            videoView.pause();
            Log.d(TAG, "Video paused.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed.
        videoView.stopPlayback();
        Log.d(TAG, "Video playback stopped and resources released.");
    }
}