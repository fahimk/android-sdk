package com.yesgraph.android.utils;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dean Bozinoski on 11/19/2015.
 */
public class YSGTheme {

    public static void setThemeColor(int mainForegroundColor, int mainBackgroundColor,
                                     int darkFontColor, int lightFontColor, int rowSelectedColor,
                                     int rowUnselectedColor, int rowBackgroundColor, int backArrowColor) {
        if (mainForegroundColor != 0) {
            Constants.MAIN_FOREGROUND_COLOR = mainForegroundColor;
        }
        if (mainBackgroundColor != 0) {
            Constants.MAIN_BACKGROUND_COLOR = mainBackgroundColor;
        }
        if (darkFontColor != 0) {
            Constants.DARK_FONT_COLOR = darkFontColor;
        }
        if (lightFontColor != 0) {
            Constants.LIGHT_FONT_COLOR = lightFontColor;
        }
        if (rowSelectedColor != 0) {
            Constants.ROW_SELECTED_COLOR = rowSelectedColor;
        }
        if (rowUnselectedColor != 0) {
            Constants.ROW_UNSELECTED_COLOR = rowUnselectedColor;
        }
        if (rowBackgroundColor != 0) {
            Constants.ROW_BACKGROUND_COLOR = rowBackgroundColor;
        }
        if (backArrowColor != 0) {
            Constants.BACK_ARROW_COLOR = backArrowColor;
        }
    }

    public static int getMainForegroundColor() {
        return Constants.MAIN_FOREGROUND_COLOR;
    }

    public static int getMainBackgroundColor() {
        return Constants.MAIN_BACKGROUND_COLOR;
    }

    public static int getDarkFontColor() {
        return Constants.DARK_FONT_COLOR;
    }

    public static int getLightFontColor() {
        return Constants.LIGHT_FONT_COLOR;
    }

    public static int getRowSelectedColor() {
        return Constants.ROW_SELECTED_COLOR;
    }

    public static int getRowUnselectedColor() {
        return Constants.ROW_UNSELECTED_COLOR;
    }

    public static int getRowBackgroundColor() {
        return Constants.ROW_BACKGROUND_COLOR;
    }

    public static int getBackArrowColor() {
        return Constants.BACK_ARROW_COLOR;
    }


}
