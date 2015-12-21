package com.yesgraph.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Klemen on 17.12.2015.
 */
public class SharedPreferencesManager {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesManager(Context context){
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putBoolean(String key,boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key,String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key, "");
    }

}
