package com.yesgraph.android.utils;

import android.content.Context;

/**
 * Created by Klemen on 18.12.2015.
 */
public class CacheManager {

    private static final String CONTACTS_CACHE_KEY = "contacts_cache";
    private static final String CONTACTS_UPLOADING_KEY = "contacts_uploading";

    private Context context;

    public CacheManager(Context context) {
        this.context = context;
    }

    public void setContactCache(String contacts) {
        new SharedPreferencesManager(context).putString(CONTACTS_CACHE_KEY, contacts);
    }

    public String getContactCache() {
        return new SharedPreferencesManager(context).getString(CONTACTS_CACHE_KEY);
    }

    public void setContactsUploading(boolean isUploading) {
        new SharedPreferencesManager(context).putBoolean(CONTACTS_UPLOADING_KEY, isUploading);
    }

    public boolean isContactsUploading() {
        return new SharedPreferencesManager(context).getBoolean(CONTACTS_UPLOADING_KEY);
    }

}