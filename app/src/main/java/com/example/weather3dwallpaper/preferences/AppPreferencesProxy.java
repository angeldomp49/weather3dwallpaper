package com.example.weather3dwallpaper.preferences;

import android.content.Context;
import android.preference.PreferenceManager;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.PreferenceDataStore;
import androidx.datastore.preferences.core.Preferences;

public class AppPreferencesProxy {
    
    private final Context context;
    private DataStore<Preferences> preferencesDataStore;
    
    public void init(){
        
    }

    public AppPreferencesProxy(Context context) {
        this.context = context;
    }

    public void put(String key, String value){
        
    }
    
    public String get(String key){
        return null;
    }
}
