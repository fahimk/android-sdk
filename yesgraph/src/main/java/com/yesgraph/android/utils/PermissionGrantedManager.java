package com.yesgraph.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Klemen on 17.12.2015.
 */
public class PermissionGrantedManager {

    private static int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String SEND_SMS_PERMISSION = "send_sms_permission_granted";
    private static final String READ_CONTACTS_PERMISSION = "contacts_permission_granted";


    private Context context;

    public PermissionGrantedManager(Context context) {
        this.context = context;
    }

    /**
     * SMS PERMISSION GRANTED
     */

    public void initCheckSendSmsPermission() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            new SharedPreferencesManager(context).putBoolean(SEND_SMS_PERMISSION, false);
        } else {
            new SharedPreferencesManager(context).putBoolean(SEND_SMS_PERMISSION, true);
        }
    }

    public void checkSendSmsPermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            new SharedPreferencesManager(context).putBoolean(SEND_SMS_PERMISSION, true);
        }
    }

    public boolean checkContactsReadPermission() {
        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isSendSmsPermission() {
        return new SharedPreferencesManager(context).getBoolean(SEND_SMS_PERMISSION);
    }

    public void setSendSmsPermission(boolean value) {
        new SharedPreferencesManager(context).putBoolean(SEND_SMS_PERMISSION, value);
    }

    public boolean isReadContactsPermission() {
        return new SharedPreferencesManager(context).getBoolean(READ_CONTACTS_PERMISSION);
    }

    public void setReadContactsPermission(boolean value) {
        new SharedPreferencesManager(context).putBoolean(READ_CONTACTS_PERMISSION, value);
    }
}
