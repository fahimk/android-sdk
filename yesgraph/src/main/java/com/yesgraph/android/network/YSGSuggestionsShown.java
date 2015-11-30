package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.yesgraph.android.models.YSGContact;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.YSGUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marko on 23/11/15.
 */
public class YSGSuggestionsShown extends HttpMethodAbstract {

    public JSONArray generateArrayOfSuggestionsFromListForUser(String userId, ArrayList<YSGRankedContact> suggestions)
    {
        JSONArray json=new JSONArray();

        Long time=System.currentTimeMillis();

        for(YSGContact contact: suggestions)
        {
            JSONObject jsonSuggestion=new JSONObject();
            try {
                jsonSuggestion.put("user_id", userId);
                jsonSuggestion.put("seen_at", YSGUtility.iso8601dateStringFromMilliseconds(time));
                jsonSuggestion.put("name", contact.name());
                if(contact.phones()!=null)
                {
                    JSONArray jsonPhones=new JSONArray();
                    for(String phone : contact.phones())
                    {
                        jsonPhones.put(phone);
                    }

                    jsonSuggestion.put("phones", jsonPhones);
                }
                else
                {
                    jsonSuggestion.put("phones", new JSONArray());
                }
                if(contact.emails()!=null)
                {
                    JSONArray jsonEmails=new JSONArray();
                    for(String email : contact.emails())
                    {
                        jsonEmails.put(email);
                    }

                    jsonSuggestion.put("emails", jsonEmails);
                }
                else
                {
                    jsonSuggestion.put("emails", new JSONArray());
                }
                json.put(jsonSuggestion);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public void updateSuggestionsSeen(Context context, ArrayList<YSGRankedContact> invites, String userId, final Handler.Callback callback)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String secretKey=sharedPreferences.getString("secret_key", "");

        JSONObject json=new JSONObject();
        try {
            json.put("entries", generateArrayOfSuggestionsFromListForUser(userId, invites));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.httpAsync(secretKey, "POST", "suggested-seen", null, json, new Handler.Callback() {
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
