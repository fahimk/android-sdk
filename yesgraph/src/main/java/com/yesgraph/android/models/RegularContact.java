package com.yesgraph.android.models;

/**
 * Created by marko on 16/11/15.
 */
public class RegularContact {
    private String name;
    private String email;
    private String phone;
    private Integer position;
    private Boolean selected=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getContact() {
        return email!=null ? email : phone;
    }

    public Boolean isEmail() {
        return email!=null ? true : false;
    }

    public Boolean isPhone() {
        return email!=null ? false : true;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
