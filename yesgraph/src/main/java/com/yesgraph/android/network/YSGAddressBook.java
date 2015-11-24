package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.yesgraph.android.models.YSGContactList;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.YSGUtility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marko on 23/11/15.
 */
public class YSGAddressBook extends HttpMethodAbstract {

    public void fetchAddressBookForUserId(Context context, String userId, final Handler.Callback callback)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String secretKey=sharedPreferences.getString("secret_key","");

        super.httpAsync(secretKey, Constants.HTTP_METHOD_GET, "address-book/"+userId, null, null, new Handler.Callback() {
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

    public void updateAddressBookWithContactListForUserId(Context context, YSGContactList contactList, String userId, final Handler.Callback callback)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String secretKey=sharedPreferences.getString("secret_key","");

        super.httpAsync(secretKey, Constants.HTTP_METHOD_POST, "address-book/"+userId, null, contactList.toJSONObject(userId), new Handler.Callback() {
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
