package com.yesgraph.android.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class YesGraph extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static boolean isMarshmallow(){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.M){
            return false;
        } else{
            return true;
        }
    }

    public static boolean isFacebookSignedIn()
    {
        return true;
    }

    public static boolean isTwitterSignedIn()
    {
        return true;
    }

    public static String getCopyLinkText()
    {
        return "http://www.google.com";
    }

    public static String getShareText()
    {
        return "Demo our SDK by sharing YesGraph with your contacts";
    }

    public static int getMainForegroundColor()
    {
        return Color.BLUE;
    }

    public static int getMainBackgroundColor()
    {
        return Color.WHITE;
    }

    public static int getDarkFontColor()
    {
        return Color.BLACK;
    }

    public static int getLightFontColor()
    {
        return Color.WHITE;
    }
}
