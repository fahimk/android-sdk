package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.StorageKeyValueManager;
import com.yesgraph.android.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marko on 23/11/15.
 */
public class Authenticate extends HttpMethod {

    public void fetchRandomClientKeyWithSecretKey(Context context, String secretKey, Handler.Callback callback) {
        fetchClientKeyWithSecretKey(context, secretKey, randomId(), callback);
    }

    public void fetchClientKeyWithSecretKey(final Context context, String secretKey, String userId, final Handler.Callback callback)
    {
        JSONObject json = setUserIdToJSON(context, userId);

        super.httpAsync(secretKey, Constants.HTTP_METHOD_POST, "client-key", null, json, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();

                if (msg.what == Constants.RESULT_OK) {
                    setClientKeyAndUserId(context, msg);
                    callbackMessage.what = Constants.RESULT_OK;
                    callbackMessage.obj = msg.obj;
                    callback.handleMessage(callbackMessage);
                } else {
                    callbackMessage.what = msg.what;
                    callbackMessage.obj = msg.obj;
                    callback.handleMessage(callbackMessage);
                }
                return false;
            }
        });
    }

    /**
     * Save client key and user id to shared prefrences
     * @param msg server message response
     * @param context context
     */
    public void setClientKeyAndUserId(Context context,Message msg) {

        try {

            String secretKey = ((JSONObject) msg.obj).getString("client_key");
            new StorageKeyValueManager(context).setSecretKey(secretKey);

            String userId = ((JSONObject) msg.obj).getString("user_id");
            new StorageKeyValueManager(context).setUserId(userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert user id string to json object
     * @param context context
     * @param userId user id
     * @return user id in json object
     */
    public JSONObject setUserIdToJSON(Context context, String userId) {

        if(userId.equals("")) {
            userId = randomId();
            new StorageKeyValueManager(context).setUserId(userId);
        }

        JSONObject json=new JSONObject();
        try {
            json.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public static String randomId()
    {
        return Utility.randomUserId();
    }
}
