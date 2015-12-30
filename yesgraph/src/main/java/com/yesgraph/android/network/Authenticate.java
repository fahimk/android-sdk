package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.StorageKeyValueManager;
import com.yesgraph.android.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marko on 23/11/15.
 */
public class Authenticate extends HttpMethod {

    public void fetchRandomClientKeyWithSecretKey(Context context, String secretKey, Handler.Callback callback)
    {
        fetchClientKeyWithSecretKey(context, secretKey, randomId(), callback);
    }

    public void fetchClientKeyWithSecretKey(final Context context, String secretKey, String userId, final Handler.Callback callback)
    {
        if(userId.equals("")) {
            userId = randomId();

            try {
                new StorageKeyValueManager(context).setUserId(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONObject json=new JSONObject();
        try {
            json.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.httpAsync(secretKey, Constants.HTTP_METHOD_POST, "client-key", null, json, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if(msg.what == Constants.RESULT_OK)
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    try {
                        new StorageKeyValueManager(context).setSecretKey(((JSONObject) msg.obj).getString("client_key"));
                        new StorageKeyValueManager(context).setUserId(((JSONObject)msg.obj).getString("user_id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    public static String randomId()
    {
        return Utility.randomUserId();
    }
}
