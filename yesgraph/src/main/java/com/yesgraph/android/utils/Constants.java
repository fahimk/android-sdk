package com.yesgraph.android.utils;

import android.graphics.Color;

import com.yesgraph.android.R;

/**
 * Created by Dean Bozinoski on 11/19/2015.
 */
public class Constants {

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
}
