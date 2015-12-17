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

    public void checkForSendSmsPermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            new SharedPreferencesManager(context).putBoolean(SEND_SMS_PERMISSION, true);
        }
    }

    public boolean getSendSmsPermission() {
        return new SharedPreferencesManager(context).getBoolean(SEND_SMS_PERMISSION);
    }

    public void putSendSmsPermission(boolean value) {
         new SharedPreferencesManager(context).putBoolean(SEND_SMS_PERMISSION,value);
    }
}
