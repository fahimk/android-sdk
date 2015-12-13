package com.yesgraph.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marko on 23/11/15.
 */
public class ContactList {
    private Boolean useSuggestions;
    private ArrayList<RankedContact> entries;
    private Source source;


    public Boolean getUseSuggestions() {
        return useSuggestions;
    }

    public void setUseSuggestions(Boolean useSuggestions) {
        this.useSuggestions = useSuggestions;
    }

    public ArrayList<RankedContact> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<RankedContact> entries) {
        this.entries = entries;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public JSONObject toJSONObject(String userId)
    {
        JSONObject json=new JSONObject();
        JSONObject jsonSource=new JSONObject();
        JSONArray jsonEntries=new JSONArray();
        try {
            jsonSource.put("type", source.getType());

            for(RankedContact contact : entries)
            {
                jsonEntries.put(contact.toJSONObject());
            }

            json.put("source", jsonSource);
            json.put("entries", jsonEntries);
            json.put("filter_suggested_seen", useSuggestions ? 1 : 0);
            json.put("user_id",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
