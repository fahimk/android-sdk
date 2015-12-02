package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGContactList;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.YSGUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marko on 23/11/15.
 */
public class YSGAddressBook extends HttpMethodAbstract {

    public void fetchAddressBookForUserId(Context context, String userId, final Handler.Callback callback)
    {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String secretKey=sharedPreferences.getString("secret_key","");

        super.httpAsync(secretKey, Constants.HTTP_METHOD_GET, "address-book/"+userId, null, null, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if(msg.what == Constants.RESULT_OK)
                {
                    JSONObject json = (JSONObject)msg.obj;

                    YSGContactList ysgContactList = null;
                    try
                    {
                        if(json.has("data") && !json.isNull("data"))
                        {
                            sharedPreferences.edit().putString("contacts_cache", json.getJSONArray("data").toString()).commit();
                            ysgContactList = contactListFromResponse(json.getJSONArray("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callbackMessage.what = Constants.RESULT_OK;
                    callbackMessage.obj = ysgContactList;
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

    public void updateAddressBookWithContactListForUserId(final Context context, YSGContactList contactList, String userId, final Handler.Callback callback)
    {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String secretKey=sharedPreferences.getString("secret_key","");

        super.httpAsync(secretKey, Constants.HTTP_METHOD_POST, "address-book", null, contactList.toJSONObject(userId), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if(msg.what == Constants.RESULT_OK)
                {
                    JSONObject json = (JSONObject)msg.obj;

                    YSGContactList ysgContactList = null;
                    try
                    {
                        if(json.has("data") && !json.isNull("data"))
                        {
                            sharedPreferences.edit().putString("contacts_cache", json.getJSONArray("data").toString()).commit();
                            sharedPreferences.edit().putLong("contacts_last_upload", System.currentTimeMillis()).commit();
                            ysgContactList = contactListFromResponse(json.getJSONArray("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callbackMessage.what = Constants.RESULT_OK;
                    callbackMessage.obj = ysgContactList;
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

    public YSGContactList contactListFromResponse(JSONArray jsonArray)
    {
        ArrayList<YSGRankedContact> contacts=new ArrayList<>();

        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                JSONObject json=jsonArray.getJSONObject(i);

                YSGRankedContact contact = new YSGRankedContact(json);

                contacts.add(contact);

                Log.i("JSON:",json.toString()+" ");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        YSGContactList contactList = new YSGContactList();

        Collections.sort(contacts, new Comparator<YSGRankedContact>() {
            @Override
            public int compare(YSGRankedContact p1, YSGRankedContact p2) {
                return p1.getRank() - p2.getRank(); // Ascending
                //return p2.getRank() - p1.getRank(); // Descending
            }

        });


        /*Collections.sort(contacts, new Comparator<YSGRankedContact>() {
            public int compare(YSGRankedContact s1, YSGRankedContact s2) {

                if (isAlpha(s1.name().substring(0, 1)) && !isAlpha(s2.name().substring(0, 1)))
                    return -s1.name().compareToIgnoreCase(s2.name());

                if (isAlpha(s2.name().substring(0, 1)) && !isAlpha(s1.name().substring(0, 1)))
                    return -s1.name().compareToIgnoreCase(s2.name());

                return s1.name().compareToIgnoreCase(s2.name());
            }
        });*/

        contactList.setEntries(contacts);
        contactList.setUseSuggestions(true);

        return contactList;
    }

    public static boolean isAlpha(String name) {
        boolean bool=name.matches("[a-zA-Z]+");
        return bool;
    }

    public static boolean isNumeric(String name) {
        boolean bool=name.matches("[0-9]+");
        return bool;
    }
}
