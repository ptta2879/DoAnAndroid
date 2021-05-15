package com.IUH.FastEvent.Model;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesFastEvent {
    private static final String SHARED_PREFERENCES = "MY_PREFERENCES";
    private Context context;

    public SharedPreferencesFastEvent(Context context) {
        this.context = context;
    }
    public void putFirstInstallApp(String key, boolean val){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES
                ,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,val);
        editor.apply();
    }
    public boolean getSharedPreferences(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
