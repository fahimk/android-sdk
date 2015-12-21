package com.yesgraph.android.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.yesgraph.android.R;
import com.yesgraph.android.utils.CustomTheme;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class YesGraph extends Application {

    private CustomTheme customTheme;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public CustomTheme getCustomTheme() {
        if(customTheme != null){
            return this.customTheme;
        } else {
            return new CustomTheme();
        }
    }

    public void setCustomTheme(CustomTheme customTheme) {
        this.customTheme = customTheme;
    }

    public static boolean isMarshmallow() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.M) {
            return false;
        } else {
            return true;
        }
    }

    public static Integer getNumberOfSuggestedContacts()
    {
        return 5;
    }

    public static boolean isFacebookSignedIn() {
        return true;
    }

    public static boolean isTwitterSignedIn() {
        return true;
    }

    public String getCopyLinkText() {
        return getString(R.string.default_share_link);
    }

    public String getCopyButtonText(){
        return getString(R.string.button_copy_text);
    }

    public String getShareText() {
        return getString(R.string.default_share_text);
    }

    public String getSmsText() {
        return getString(R.string.default_share_text);
    }

    public String getEmailText() {
        return getString(R.string.default_share_text);
    }

    public String getEmailSubject() {
        return getString(R.string.default_share_text);
    }
}
