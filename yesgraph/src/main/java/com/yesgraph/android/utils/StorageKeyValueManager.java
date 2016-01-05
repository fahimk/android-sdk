package com.yesgraph.android.utils;

import android.content.Context;

/**
 * Created by Klemen on 18.12.2015.
 */
public class StorageKeyValueManager {

    private static final String CONTACTS_CACHE_KEY = "contacts_cache";
    private static final String CONTACTS_UPLOADING_KEY = "contacts_uploading";
    private static final String CONTACTS_LAST_UPLOADING_KEY = "contacts_last_upload";
    private static final String SECRET_KEY_KEY = "secret_key";
    private static final String USER_ID_KEY = "user_id";

    private Context context;

    public StorageKeyValueManager(Context context) {
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

    public void setContactLastUpload(long time) {
        new SharedPreferencesManager(context).putLong(CONTACTS_LAST_UPLOADING_KEY, time);
    }

    public long getContactLastUpload() {
        return new SharedPreferencesManager(context).getLong(CONTACTS_LAST_UPLOADING_KEY);
    }

    public String getSecretKey() {
        return new SharedPreferencesManager(context).getString(SECRET_KEY_KEY);
    }

    public void setSecretKey(String secretKey) {
        new SharedPreferencesManager(context).putString(SECRET_KEY_KEY, secretKey);
    }

    public String getUserId() {
        return new SharedPreferencesManager(context).getString(USER_ID_KEY);
    }

    public void setUserId(String userId) {
        new SharedPreferencesManager(context).putString(USER_ID_KEY, userId);
    }

}
