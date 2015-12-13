package com.yesgraph.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marko on 23/11/15.
 */
public class Contact {

    final private static String YSGContactSuggestedKey = "suggested";

    private String name;
    private String email;
    private String phone;
    private ArrayList<String> emails;
    private ArrayList<String> phones;
    private JSONObject data;

    public ArrayList<String> emails()
    {
        return emails;
    }

    public ArrayList<String> phones()
    {
        return phones;
    }

    public String name()
    {
        return name;
    }

    public String description()
    {
        return name!=null ? name : "" + " - " + contactString();
    }

    public String phone()
    {
        if(phone!=null)
            return phone;

        if(phones!=null && phones.size()>0)
            return phones.get(0);
        else
            return null;
    }

    public String email()
    {
        if(email!=null)
            return email;

        if(emails!=null && emails.size()>0)
            return emails.get(0);
        else
            return null;
    }

    public String contactString() {
        if(emails!=null && emails.size()>0)
            return emails.get(0);
        else if(phones!=null && phones.size()>0)
            return phones.get(0);
        else
            return null;
    }

    public String sanitizedName()
    {
        return name!=null ? name.trim() : "";
    }

    public Boolean wasSuggested()
    {
        try {
            if(data!=null && data.has(YSGContactSuggestedKey) && !data.isNull(YSGContactSuggestedKey) && data.get(YSGContactSuggestedKey) instanceof Boolean)
                return data.getBoolean(YSGContactSuggestedKey);
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setSuggested(Boolean suggested)
    {
        try {
            if (suggested) {
                if(data!=null)
                {
                    data.put(YSGContactSuggestedKey, suggested);
                }
            }
            else
            {
                if(data!=null && data.has(YSGContactSuggestedKey) && !data.isNull(YSGContactSuggestedKey) && data.get(YSGContactSuggestedKey) instanceof Boolean)
                {
                    data.remove(YSGContactSuggestedKey);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonContact=new JSONObject();
        JSONArray jsonEmail=new JSONArray();
        JSONArray jsonPhones=new JSONArray();
        try {
            jsonContact.put("name", name);
            jsonContact.put("email", email());
            jsonContact.put("phone", phone());
            if(emails!=null)
            {
                for(String email : emails)
                {
                    jsonEmail.put(email);
                }
            }
            if(jsonEmail.length()>0)
                jsonContact.put("emails",jsonEmail);
            if(phones!=null)
            {
                for(String phone : phones)
                {
                    jsonPhones.put(phone);
                }
            }
            if(jsonPhones.length()>0)
                jsonContact.put("phones",jsonPhones);
            if(data!=null)
                jsonContact.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonContact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
