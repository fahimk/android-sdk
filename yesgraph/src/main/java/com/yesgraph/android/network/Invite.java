package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marko on 23/11/15.
 */
public class Invite extends HttpMethod {

    public JSONArray generateArrayOfInviteesFromListForUser(String userId, ArrayList<Contact> inviteesList)
    {
        JSONArray json=new JSONArray();

        Long time=System.currentTimeMillis();

        for(Contact contact: inviteesList)
        {
            JSONObject jsonInvitee=new JSONObject();
            try {
                Boolean insert=false;

                jsonInvitee.put("user_id", userId);
                jsonInvitee.put("sent_at", Utility.iso8601dateStringFromMilliseconds(time));
                if(contact.name()!=null)
                {
                    jsonInvitee.put("invitee_name",contact.name());
                    insert=true;
                }
                if(contact.phone()!=null)
                {
                    jsonInvitee.put("phone",contact.phone());
                    insert=true;
                }
                if(contact.email()!=null)
                {
                    jsonInvitee.put("email",contact.email());
                    insert=true;
                }

                if(insert)
                    json.put(jsonInvitee);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public void updateInvitesSentForUser(Context context, ArrayList<Contact> invites, String userId, final Handler.Callback callback)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String secretKey=sharedPreferences.getString("secret_key", "");

        JSONObject json=new JSONObject();
        try {
            json.put("entries", generateArrayOfInviteesFromListForUser(userId, invites));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.httpAsync(secretKey, "POST", "invites-sent", null, json, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if(msg.what == Constants.RESULT_OK)
                {
                    callbackMessage.what = Constants.RESULT_OK;
                    callbackMessage.obj = msg.obj;
                    callback.handleMessage(callbackMessage);
                }
                else
                {
                    callbackMessage.what = msg.what;
                    callbackMessage.obj = msg.obj;
                    callback.handleMessage(callbackMessage);
                }
                return false;
            }
        });
    }
}
