package com.yesgraph.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marko on 23/11/15.
 */
public class Ims {

    private String name;
    private String protocol;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonIms=new JSONObject();
        try {
            if(name!=null)
                jsonIms.put("name",name);
            if(protocol!=null)
                jsonIms.put("protocol",protocol);
            if(type!=null)
                jsonIms.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonIms;
    }
}
