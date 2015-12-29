package com.yesgraph.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marko on 23/11/15.
 */
public class Address {

    private String po_box;
    private String street;
    private String city;
    private String state;
    private String postal_code;
    private String country;
    private String type;

    public String getPo_box() {
        return po_box;
    }

    public void setPo_box(String po_box) {
        this.po_box = po_box;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonAddress=new JSONObject();
        try {
            if(po_box!=null)
                jsonAddress.put("po_box",po_box);
            if(street!=null)
                jsonAddress.put("street",street);
            if(city!=null)
                jsonAddress.put("city",city);
            if(state!=null)
                jsonAddress.put("state",state);
            if(postal_code!=null)
                jsonAddress.put("postal_code",postal_code);
            if(country!=null)
                jsonAddress.put("country",country);
            if(type!=null)
                jsonAddress.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonAddress;
    }
}
