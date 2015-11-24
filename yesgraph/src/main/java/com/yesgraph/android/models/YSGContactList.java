package com.yesgraph.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marko on 23/11/15.
 */
public class YSGContactList {
    private Boolean useSuggestions;
    private ArrayList<YSGContact> entries;
    private YSGSource source;


    public Boolean getUseSuggestions() {
        return useSuggestions;
    }

    public void setUseSuggestions(Boolean useSuggestions) {
        this.useSuggestions = useSuggestions;
    }

    public ArrayList<YSGContact> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<YSGContact> entries) {
        this.entries = entries;
    }

    public YSGSource getSource() {
        return source;
    }

    public void setSource(YSGSource source) {
        this.source = source;
    }

    public JSONObject toJSONObject(String userId)
    {
        JSONObject json=new JSONObject();
        JSONObject jsonSource=new JSONObject();
        JSONArray jsonEntries=new JSONArray();
        try {
            jsonSource.put("type", source.getType());

            for(YSGContact contact : entries)
            {
                jsonEntries.put(contact.toJSONObject());
            }
            json.put("filter_suggested_seen", useSuggestions ? 1 : 0);
            json.put("user_id",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
