package com.yesgraph.android.utils;

/**
 * Created by Dean Bozinoski on 11/19/2015.
 */
public class YSGTheme {

    public void setThemeColor(int mainForegroundColor, int mainBackgroundColor,
                                     int darkFontColor, int lightFontColor, int rowSelectedColor,
                                     int rowUnselectedColor, int rowBackgroundColor, int backArrowColor, int copyButtonColor,
                                     int referralBannerBackgroundColor) {
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
        if(copyButtonColor != 0){
            Constants.COPY_BUTTON_COLOR = copyButtonColor;
        }
        if(referralBannerBackgroundColor !=0){
            Constants.REFERRAL_BANNER_BACKGROUND_COLOR = referralBannerBackgroundColor;
        }
    }

    public void setReferralTextSize(int referralTextSize){
        if(referralTextSize != 0){
            Constants.REFERRAL_TEXT_FONT_SIZE = referralTextSize;
        }
    }

    public void setShareButtonsShape(String shape){
        if(shape != null && !shape.isEmpty()){
            Constants.SHARE_BUTTON_SHAPE = shape;
        }
    }

    public int getMainForegroundColor() {
        return Constants.MAIN_FOREGROUND_COLOR;
    }

    public int getMainBackgroundColor() {
        return Constants.MAIN_BACKGROUND_COLOR;
    }

    public int getDarkFontColor() {
        return Constants.DARK_FONT_COLOR;
    }

    public int getLightFontColor() {
        return Constants.LIGHT_FONT_COLOR;
    }

    public int getRowSelectedColor() {
        return Constants.ROW_SELECTED_COLOR;
    }

    public int getRowUnselectedColor() {
        return Constants.ROW_UNSELECTED_COLOR;
    }

    public static int getRowBackgroundColor() {
        return Constants.ROW_BACKGROUND_COLOR;
    }

    public int getBackArrowColor() {
        return Constants.BACK_ARROW_COLOR;
    }

    public int getCopyButtonColor(){
        return Constants.COPY_BUTTON_COLOR;
    }
    public int getReferralBunnerBackgroundColor(){
        return Constants.REFERRAL_BANNER_BACKGROUND_COLOR;
    }

    public int getReferralTextSize(){
        return Constants.REFERRAL_TEXT_FONT_SIZE;
    }

    public String getShareButtonsShape(){
        return Constants.SHARE_BUTTON_SHAPE;
    }

    public void setFonts(String font){
        Constants.FONT = font;

    }

    public String getFont(){
        return Constants.FONT;
    }
}
