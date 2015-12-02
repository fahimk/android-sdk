package com.yesgraph.android.models;

/**
 * Created by marko on 23/11/15.
 */
public class YSGSource {

    private String name;
    private String email;
    private String phone;
    private String type;

    public YSGSource()
    {
        type="android";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }
}
