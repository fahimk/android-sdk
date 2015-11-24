package com.yesgraph.android.utils;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dean Bozinoski on 11/23/2015.
 */
public class FontManager {
    private static FontManager sInstance;
    private Map<String, Typeface> mCache;

    private FontManager() {
        mCache = new HashMap<String, Typeface>();
    }

    public static FontManager getInstance() {
        if (sInstance == null) {
            sInstance = new FontManager();
        }

        return sInstance;
    }

    public void setFont(TextView textView, String fontName) {
        Typeface typeface = mCache.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(textView.getContext().getAssets(), fontName);
            mCache.put(fontName, typeface);
        }

        textView.setTypeface(typeface);
    }

    public void setFont(Button button, String fontName) {
        Typeface typeface = mCache.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(button.getContext().getAssets(), fontName);
            mCache.put(fontName, typeface);
        }

        button.setTypeface(typeface);
    }

    public void setFont(EditText editText, String fontName) {
        Typeface typeface = mCache.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(editText.getContext().getAssets(), fontName);
            mCache.put(fontName, typeface);
        }

        editText.setTypeface(typeface);
    }
}