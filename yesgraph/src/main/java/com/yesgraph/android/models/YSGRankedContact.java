package com.yesgraph.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marko on 23/11/15.
 */
public class YSGRankedContact extends YSGContact {

    private Integer rank;
    private Double score;

    public YSGRankedContact()
    {

    }

    public YSGRankedContact(JSONObject json)
    {
        try {
            ArrayList<String> email = new ArrayList<>();
            if(json.has("emails") && !json.isNull("emails"))
            {
                for(int j=0;j<json.getJSONArray("emails").length();j++)
                {
                    email.add(json.getJSONArray("emails").getString(j));
                }
            }
            setEmails(email);

            ArrayList<String> phones = new ArrayList<>();
            if(json.has("phones") && !json.isNull("phones"))
            {
                for(int j=0;j<json.getJSONArray("phones").length();j++)
                {
                    phones.add(json.getJSONArray("phones").getString(j));
                }
            }
            setPhones(phones);

            if(json.has("name") && !json.isNull("name"))setName(json.getString("name"));
            if(json.has("phone") && !json.isNull("phone"))setPhone(json.getString("phone"));
            if(json.has("email") && !json.isNull("email"))setEmail(json.getString("email"));
            if(json.has("data") && !json.isNull("data"))setData(json.getJSONObject("data"));
            if(json.has("rank") && !json.isNull("rank"))setRank(json.getInt("rank"));
            if(json.has("score") && !json.isNull("score"))setScore(json.getDouble("score"));
            else
                setSuggested(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public JSONObject toJSONObjectExtended() {
        JSONObject json = super.toJSONObject();
        try {
            json.put("rank",getRank());
            json.put("score",getScore());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
