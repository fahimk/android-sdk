package com.yesgraph.android.utils;

import android.content.Context;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;

/**
 * Created by Dean Bozinoski on 11/19/2015.
 */
public class CustomTheme {

    private Constants constants;

    public CustomTheme(){
        constants = new Constants();
    }

    public void setThemeColor(int mainForegroundColor, int mainBackgroundColor,
                                     int darkFontColor, int lightFontColor, int rowSelectedColor,
                                     int rowUnselectedColor, int rowBackgroundColor, int backArrowColor, int copyButtonColor,
                                     int referralBannerBackgroundColor) {
        if (mainForegroundColor != 0) {
            constants.setMAIN_FOREGROUND_COLOR(mainForegroundColor);
        }
        if (mainBackgroundColor != 0) {
            constants.setMAIN_BACKGROUND_COLOR(mainBackgroundColor);
        }
        if (darkFontColor != 0) {
            constants.setDARK_FONT_COLOR(darkFontColor);
        }
        if (lightFontColor != 0) {
            constants.setLIGHT_FONT_COLOR(lightFontColor);
        }
        if (rowSelectedColor != 0) {
            constants.setROW_SELECTED_COLOR(rowSelectedColor);
        }
        if (rowUnselectedColor != 0) {
            constants.setROW_UNSELECTED_COLOR(rowUnselectedColor);
        }
        if (rowBackgroundColor != 0) {
            constants.setROW_BACKGROUND_COLOR(rowBackgroundColor);
        }
        if (backArrowColor != 0) {
            constants.setBACK_ARROW_COLOR(backArrowColor);
        }
        if(copyButtonColor != 0){
            constants.setCOPY_BUTTON_COLOR(copyButtonColor);
        }
        if(referralBannerBackgroundColor !=0){
            constants.setREFERRAL_BANNER_BACKGROUND_COLOR(referralBannerBackgroundColor);
        }
    }

    public void setReferralTextSize(int referralTextSize){
        if(referralTextSize != 0){
            constants.setREFERRAL_TEXT_FONT_SIZE(referralTextSize);
        }
    }

    public void setShareButtonsShape(String shape){
        if(shape != null && !shape.isEmpty()){
            constants.setSHARE_BUTTON_SHAPE(shape);
        }
    }

    public void setFonts(String font){
        constants.setFONT(font);

    }

    public void setContactsFilterType(int filterType) {
        if (filterType >= 0 && filterType <= 2) {
            constants.setCONTACTS_FILTER_TYPE(filterType);
        }
    }

    public void setNumberOfSuggestedContacts(Integer numberOfSuggestedContacts){
        constants.setNUMBER_OF_SUGGESTED_CONTACTS(numberOfSuggestedContacts);
    }

    public int getNumberOfSuggestedContacts() {
        return constants.getNUMBER_OF_SUGGESTED_CONTACTS();
    }

    public void setIsFacebookSignedIn(boolean isFacebookSignedIn){
        constants.setIS_FACEBOOK_SIGNED_IN(isFacebookSignedIn);
    }

    public boolean isFacebookSignedIn(){
        return constants.IS_FACEBOOK_SIGNED_IN();
    }

    public void setIsTwitterSignedIn(boolean isTwitterSignedIn){
        constants.setIS_TWITTER_SIGNED_IN(isTwitterSignedIn);
    }

    public boolean isTwitterSignedIn(){
        return constants.IS_TWITTER_SIGNED_IN();
    }

    public String getCopyLinkText(Context context) {

        String copyLinkText = constants.getCOPY_LINK_TEXT();

        if (copyLinkText.equals("")) {
            copyLinkText = context.getString(R.string.default_share_link);
        }

        return copyLinkText;
    }

    public void setCopyLinkText(String copyLinkText){
        constants.setCOPY_LINK_TEXT(copyLinkText);
    }

    public void setCopyButtonText(String copyButtonText){
        constants.setCOPY_BUTTON_TEXT(copyButtonText);
    }

    public String getCopyButtonText(Context context) {

        String text = constants.getCOPY_BUTTON_TEXT();

        if (text.equals("")) {
            text = context.getString(R.string.button_copy_text);
        }

        return text;
    }

    public void setShareText(String shareText){
        constants.setSHARE_TEXT(shareText);
    }

    public String getShareText(Context context) {

        String text = constants.getSHARE_TEXT();

        if (text.equals("")) {
            text = context.getString(R.string.default_share_text);
        }

        return text;
    }

    public void setSmsText(String smsTest){
        constants.setSHARE_SMS_TEXT(smsTest);
    }

    public String getSmsText(Context context) {

        String text = constants.getSHARE_SMS_TEXT();

        if (text.equals("")) {
            text = context.getString(R.string.default_share_text);
        }
        return text;
    }


    public void setEmailText(String emailText){
        constants.setSHARE_EMAIL_TEXT(emailText);
    }

    public String getEmailText(Context context) {

        String text = constants.getSHARE_EMAIL_TEXT();

        if (text.equals("")) {
            text = context.getString(R.string.default_share_text);
        }
        return text;

    }

    public void setEmailSubject(String emailSubject){
        constants.setSHARE_EMAIL_SUBJECT(emailSubject);
    }

    public String getEmailSubject(Context context) {

        String text = constants.getSHARE_EMAIL_SUBJECT();

        if (text.equals("")) {
            text = context.getString(R.string.default_share_text);
        }
        return text;

    }


    public int getContactsFilterType() {
        return constants.getCONTACTS_FILTER_TYPE();
    }

    public int getMainForegroundColor() {
        return constants.getMAIN_FOREGROUND_COLOR();
    }

    public int getMainBackgroundColor() {
        return constants.getMAIN_BACKGROUND_COLOR();
    }

    public int getDarkFontColor() {
        return constants.getDARK_FONT_COLOR();
    }

    public int getLightFontColor() {
        return constants.getLIGHT_FONT_COLOR();
    }

    public int getRowSelectedColor() {
        return constants.getROW_SELECTED_COLOR();
    }

    public int getRowUnselectedColor() {
        return constants.getROW_UNSELECTED_COLOR();
    }

    public int getRowBackgroundColor() {
        return constants.getROW_BACKGROUND_COLOR();
    }

    public int getBackArrowColor() {
        return constants.getBACK_ARROW_COLOR();
    }

    public int getCopyButtonColor(){
        return constants.getCOPY_BUTTON_COLOR();
    }
    public int getReferralBunnerBackgroundColor(){
        return constants.getREFERRAL_BANNER_BACKGROUND_COLOR();
    }

    public int getReferralTextSize(){
        return constants.getREFERRAL_TEXT_FONT_SIZE();
    }

    public String getShareButtonsShape(){
        return constants.getSHARE_BUTTON_SHAPE();
    }

    public String getFont(){
        return constants.getFONT();
    }
}
