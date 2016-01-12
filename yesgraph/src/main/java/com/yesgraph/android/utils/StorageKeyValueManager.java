package com.yesgraph.android.utils;

import android.content.Context;

/**
 * Created by Klemen on 18.12.2015.
 */
public class StorageKeyValueManager {

    private static final String CONTACTS_CACHE_KEY = "contacts_cache";
    private static final String CONTACTS_UPLOADING_KEY = "contacts_uploading";
    private static final String CONTACTS_LAST_UPLOADING_KEY = "contacts_last_upload";

    private static final String API_KEY = "api_key";
    private static final String SECRET_KEY = "secret_key";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_EMAIL = "user_email";
    private static final String INVITE_NUMBER = "invite_number";


    private Context context;

    public StorageKeyValueManager(Context context) {
        this.context = context;
    }

    public void setInviteNumber(Long inviteNumber) {
        new SharedPreferencesManager(context).putLong(INVITE_NUMBER, inviteNumber);
    }

    public Long getInviteNumber() {
        return new SharedPreferencesManager(context).getLong(INVITE_NUMBER);
    }

    public void setUserName(String useruserName) {
        new SharedPreferencesManager(context).putString(USER_NAME, useruserName);
    }

    public String getUserName() {
        return new SharedPreferencesManager(context).getString(USER_NAME);
    }

    public void setUserPhone(String useruserPhone) {
        new SharedPreferencesManager(context).putString(USER_PHONE, useruserPhone);
    }

    public String getUserPhone() {
        return new SharedPreferencesManager(context).getString(USER_PHONE);
    }

    public void setUserEmail(String userEmail) {
        new SharedPreferencesManager(context).putString(USER_EMAIL, userEmail);
    }

    public String getUserEmail() {
        return new SharedPreferencesManager(context).getString(USER_EMAIL);
    }

    public void setUserId(String userId) {
        new SharedPreferencesManager(context).putString(USER_ID, userId);
    }

    public String getUserId() {
        return new SharedPreferencesManager(context).getString(USER_ID);
    }

    public void setApiKey(String apiKey) {
        new SharedPreferencesManager(context).putString(API_KEY, apiKey);
    }

    public String getApiKey() {
        return new SharedPreferencesManager(context).getString(API_KEY);
    }

    public void setSecretKey(String secretKey) {
        new SharedPreferencesManager(context).putString(SECRET_KEY, secretKey);
    }

    public String getSecretKey() {
        return new SharedPreferencesManager(context).getString(SECRET_KEY);
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


}
