package com.yesgraph.android.utils;

import android.graphics.Color;
import com.yesgraph.android.R;

/**
 * Created by Dean Bozinoski on 11/19/2015.
 */
public class Constants {

    final public static String API_DOMAIN = "https://api.yesgraph.com/";
    final public static String API_VERSION = "v0/";

    final public static String HTTP_METHOD_GET = "GET";
    final public static String HTTP_METHOD_POST = "POST";

    final public static int TIMEOUT_CONNECTION = 45000;
    final public static int TIMEOUT_READ = 45000;

    final public static int RESULT_OK=1;
    final public static int RESULT_ERROR=0;

    final public static long HOURS_BETWEEN_UPLOAD=24L;

    private int MAIN_FOREGROUND_COLOR = Color.parseColor("#0078BD");
    private int MAIN_BACKGROUND_COLOR = Color.parseColor("#F5F5F5");
    private int DARK_FONT_COLOR = Color.parseColor("#212121");
    private int LIGHT_FONT_COLOR = Color.parseColor("#FFFFFF");
    private int ROW_SELECTED_COLOR = Color.parseColor("#D9D9D9");
    private int ROW_UNSELECTED_COLOR = Color.parseColor("#F5F5F5");
    private int ROW_BACKGROUND_COLOR = Color.parseColor("#F5F5F5");
    private int BACK_ARROW_COLOR = Color.parseColor("#FFFFFF");
    private int COPY_BUTTON_COLOR = 0;
    private int REFERRAL_BANNER_BACKGROUND_COLOR = Color.parseColor("#F5F5F5");

    private int REFERRAL_TEXT_FONT_SIZE = 14;

    private String SHARE_BUTTON_SHAPE = "circle";

    private String FONT = "";

    private int CONTACTS_FILTER_TYPE = FilterType.ALL_CONTACTS.ordinal();

    private Integer NUMBER_OF_SUGGESTED_CONTACTS = 5;
    private boolean IS_FACEBOOK_SIGNED_IN = true;
    private boolean IS_TWITTER_SIGNED_IN = true;
    private String COPY_LINK_TEXT = "";
    private String COPY_BUTTON_TEXT = "";
    private String SHARE_TEXT = "";
    private String SHARE_SMS_TEXT = "";
    private String SHARE_EMAIL_TEXT = "";
    private String SHARE_EMAIL_SUBJECT = "";


    public int getMAIN_FOREGROUND_COLOR() {
        return MAIN_FOREGROUND_COLOR;
    }

    public void setMAIN_FOREGROUND_COLOR(int MAIN_FOREGROUND_COLOR) {
        this.MAIN_FOREGROUND_COLOR = MAIN_FOREGROUND_COLOR;
    }

    public int getMAIN_BACKGROUND_COLOR() {
        return MAIN_BACKGROUND_COLOR;
    }

    public void setMAIN_BACKGROUND_COLOR(int MAIN_BACKGROUND_COLOR) {
        this.MAIN_BACKGROUND_COLOR = MAIN_BACKGROUND_COLOR;
    }

    public int getDARK_FONT_COLOR() {
        return DARK_FONT_COLOR;
    }

    public void setDARK_FONT_COLOR(int DARK_FONT_COLOR) {
        this.DARK_FONT_COLOR = DARK_FONT_COLOR;
    }

    public int getLIGHT_FONT_COLOR() {
        return LIGHT_FONT_COLOR;
    }

    public void setLIGHT_FONT_COLOR(int LIGHT_FONT_COLOR) {
        this.LIGHT_FONT_COLOR = LIGHT_FONT_COLOR;
    }

    public int getROW_SELECTED_COLOR() {
        return ROW_SELECTED_COLOR;
    }

    public void setROW_SELECTED_COLOR(int ROW_SELECTED_COLOR) {
        this.ROW_SELECTED_COLOR = ROW_SELECTED_COLOR;
    }

    public int getROW_UNSELECTED_COLOR() {
        return ROW_UNSELECTED_COLOR;
    }

    public void setROW_UNSELECTED_COLOR(int ROW_UNSELECTED_COLOR) {
        this.ROW_UNSELECTED_COLOR = ROW_UNSELECTED_COLOR;
    }

    public int getROW_BACKGROUND_COLOR() {
        return ROW_BACKGROUND_COLOR;
    }

    public void setROW_BACKGROUND_COLOR(int ROW_BACKGROUND_COLOR) {
        this.ROW_BACKGROUND_COLOR = ROW_BACKGROUND_COLOR;
    }

    public int getBACK_ARROW_COLOR() {
        return BACK_ARROW_COLOR;
    }

    public void setBACK_ARROW_COLOR(int BACK_ARROW_COLOR) {
        this.BACK_ARROW_COLOR = BACK_ARROW_COLOR;
    }

    public int getCOPY_BUTTON_COLOR() {
        return COPY_BUTTON_COLOR;
    }

    public void setCOPY_BUTTON_COLOR(int COPY_BUTTON_COLOR) {
        this.COPY_BUTTON_COLOR = COPY_BUTTON_COLOR;
    }

    public int getREFERRAL_BANNER_BACKGROUND_COLOR() {
        return REFERRAL_BANNER_BACKGROUND_COLOR;
    }

    public void setREFERRAL_BANNER_BACKGROUND_COLOR(int REFERRAL_BANNER_BACKGROUND_COLOR) {
        this.REFERRAL_BANNER_BACKGROUND_COLOR = REFERRAL_BANNER_BACKGROUND_COLOR;
    }

    public int getREFERRAL_TEXT_FONT_SIZE() {
        return REFERRAL_TEXT_FONT_SIZE;
    }

    public void setREFERRAL_TEXT_FONT_SIZE(int REFERRAL_TEXT_FONT_SIZE) {
        this.REFERRAL_TEXT_FONT_SIZE = REFERRAL_TEXT_FONT_SIZE;
    }

    public String getSHARE_BUTTON_SHAPE() {
        return SHARE_BUTTON_SHAPE;
    }

    public void setSHARE_BUTTON_SHAPE(String SHARE_BUTTON_SHAPE) {
        this.SHARE_BUTTON_SHAPE = SHARE_BUTTON_SHAPE;
    }

    public String getFONT() {
        return FONT;
    }

    public void setFONT(String FONT) {
        this.FONT = FONT;
    }

    public int getCONTACTS_FILTER_TYPE() {
        return CONTACTS_FILTER_TYPE;
    }

    public void setCONTACTS_FILTER_TYPE(int CONTACTS_FILTER_TYPE) {
        this.CONTACTS_FILTER_TYPE = CONTACTS_FILTER_TYPE;
    }

    public Integer getNUMBER_OF_SUGGESTED_CONTACTS() {
        return NUMBER_OF_SUGGESTED_CONTACTS;
    }

    public void setNUMBER_OF_SUGGESTED_CONTACTS(Integer NUMBER_OF_SUGGESTED_CONTACTS) {
        this.NUMBER_OF_SUGGESTED_CONTACTS = NUMBER_OF_SUGGESTED_CONTACTS;
    }

    public boolean IS_FACEBOOK_SIGNED_IN() {
        return IS_FACEBOOK_SIGNED_IN;
    }

    public void setIS_FACEBOOK_SIGNED_IN(boolean IS_FACEBOOK_SIGNED_IN) {
        this.IS_FACEBOOK_SIGNED_IN = IS_FACEBOOK_SIGNED_IN;
    }

    public boolean IS_TWITTER_SIGNED_IN() {
        return IS_TWITTER_SIGNED_IN;
    }

    public void setIS_TWITTER_SIGNED_IN(boolean IS_TWITTER_SIGNED_IN) {
        this.IS_TWITTER_SIGNED_IN = IS_TWITTER_SIGNED_IN;
    }

    public String getCOPY_LINK_TEXT() {
        return COPY_LINK_TEXT;
    }

    public void setCOPY_LINK_TEXT(String COPY_LINK_TEXT) {
        this.COPY_LINK_TEXT = COPY_LINK_TEXT;
    }

    public String getCOPY_BUTTON_TEXT() {
        return COPY_BUTTON_TEXT;
    }

    public void setCOPY_BUTTON_TEXT(String COPY_BUTTON_TEXT) {
        this.COPY_BUTTON_TEXT = COPY_BUTTON_TEXT;
    }

    public String getSHARE_TEXT() {
        return SHARE_TEXT;
    }

    public void setSHARE_TEXT(String SHARE_TEXT) {
        this.SHARE_TEXT = SHARE_TEXT;
    }

    public String getSHARE_SMS_TEXT() {
        return SHARE_SMS_TEXT;
    }

    public void setSHARE_SMS_TEXT(String SHARE_SMS_TEXT) {
        this.SHARE_SMS_TEXT = SHARE_SMS_TEXT;
    }

    public String getSHARE_EMAIL_TEXT() {
        return SHARE_EMAIL_TEXT;
    }

    public void setSHARE_EMAIL_TEXT(String SHARE_EMAIL_TEXT) {
        this.SHARE_EMAIL_TEXT = SHARE_EMAIL_TEXT;
    }

    public String getSHARE_EMAIL_SUBJECT() {
        return SHARE_EMAIL_SUBJECT;
    }

    public void setSHARE_EMAIL_SUBJECT(String SHARE_EMAIL_SUBJECT) {
        this.SHARE_EMAIL_SUBJECT = SHARE_EMAIL_SUBJECT;
    }
}
