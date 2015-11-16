package com.yesgraph.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by marko on 15/11/15.
 */
public class Visual {

    public static int getPixelsFromDp(Context context, int dp)
    {
        Resources r = context.getResources();
        int px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        return px;
    }
}
