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
    private String nickname;
    private String title;
    private String company;
    private String is_favorite;
    private Integer pinned;
    private Long times_contacted;
    private Long last_message_sent_date;
    private Long last_message_received_date;
    private ArrayList<Address> addresses;
    private ArrayList<Website> websites;
    private ArrayList<Ims> ims;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public Integer getPinned() {
        return pinned;
    }

    public void setPinned(Integer pinned) {
        this.pinned = pinned;
    }

    public Long getTimes_contacted() {
        return times_contacted;
    }

    public void setTimes_contacted(Long times_contacted) {
        this.times_contacted = times_contacted;
    }

    public Long getLast_message_sent_date() {
        return last_message_sent_date;
    }

    public void setLast_message_sent_date(Long last_message_sent_date) {
        this.last_message_sent_date = last_message_sent_date;
    }

    public Long getLast_message_received_date() {
        return last_message_received_date;
    }

    public void setLast_message_received_date(Long last_message_received_date) {
        this.last_message_received_date = last_message_received_date;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(ArrayList<Website> websites) {
        this.websites = websites;
    }

    public ArrayList<Ims> getIms() {
        return ims;
    }

    public void setIms(ArrayList<Ims> ims) {
        this.ims = ims;
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
            if(nickname!=null)
                jsonContact.put("nickname",nickname);
            if(title!=null)
                jsonContact.put("title",title);
            if(company!=null)
                jsonContact.put("company",company);
            if(is_favorite!=null)
                jsonContact.put("is_favorite",is_favorite);
            if(pinned!=null)
                jsonContact.put("pinned",pinned);
            if(times_contacted!=null)
                jsonContact.put("times_contacted",times_contacted);
            if(last_message_sent_date!=null)
                jsonContact.put("last_message_sent_date",last_message_sent_date);
            if(last_message_received_date!=null)
                jsonContact.put("last_message_received_date",last_message_received_date);
            if(addresses!=null)
            {
                JSONArray jsonAddresses=new JSONArray();
                for(Address address : addresses)
                {
                    jsonAddresses.put(address.toJSONObject());
                }
                jsonContact.put("addresses",jsonAddresses);
            }
            if(websites!=null)
            {
                JSONArray jsonWebsites=new JSONArray();
                for(Website website : websites)
                {
                    jsonWebsites.put(website.toJSONObject());
                }
                jsonContact.put("websites",jsonWebsites);
            }
            if(ims!=null)
            {
                JSONArray jsonIms=new JSONArray();
                for(Ims imsItem : ims)
                {
                    jsonIms.put(imsItem.toJSONObject());
                }
                jsonContact.put("ims",jsonIms);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonContact;
    }
}
