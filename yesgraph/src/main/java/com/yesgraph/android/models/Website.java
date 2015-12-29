package com.yesgraph.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marko on 23/11/15.
 */
public class Website {

    private String url;
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonWebsite=new JSONObject();
        try {
            if(url!=null)
                jsonWebsite.put("url",url);
            if(type!=null)
                jsonWebsite.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonWebsite;
    }
}
