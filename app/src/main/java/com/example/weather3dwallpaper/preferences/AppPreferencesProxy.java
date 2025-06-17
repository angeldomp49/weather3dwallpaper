package com.example.weather3dwallpaper.preferences;

import android.content.Context;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.Objects;

public class AppPreferencesProxy {
    
    private final Context context;
    private final RxDataStore<Preferences> dataStore;

    public AppPreferencesProxy(Context context) {
        this.context = context;
        this.dataStore = new RxPreferenceDataStoreBuilder(context, "weather3dwallpaper_preferences").build();

    }
    

    public Single<Preferences> put(String key, String value){
        return dataStore.updateDataAsync(preferences -> {
            var mutable = preferences.toMutablePreferences();
            mutable.set(new Preferences.Key<>(key),  value);
            return Single.just(mutable);
        });
    }
    
    public Flowable<String> get(String key, String defaultValue){
        return dataStore.data().map(preferences -> {
            var key2 = new Preferences.Key<String>(key);
            
            if(Objects.isNull(preferences.get(key2))){
                return defaultValue;
            } else {
                return preferences.get(key2);
            }
            
        });
    }
}
