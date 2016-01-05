package com.yesgraph.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.StorageKeyValueManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marko on 23/11/15.
 */
public class AddressBook extends HttpMethod {


    /*public void fetchAddressBookForUserId(final Context context, String userId, final Handler.Callback callback) {
        super.httpAsync(new StorageKeyValueManager(context).getSecretKey(), Constants.HTTP_METHOD_GET, "address-book/" + userId, null, null, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if (msg.what == Constants.RESULT_OK) {
                    callbackMessage.what = Constants.RESULT_OK;
                    callbackMessage.obj = getContactList(context, msg);
                    callback.handleMessage(callbackMessage);
                } else {
                    callbackMessage.what = msg.what;
                    callbackMessage.obj = msg.obj;
                    callback.handleMessage(callbackMessage);
                }
                return false;
            }
        });
    }*/


    public void updateAddressBookWithContactListForUserId(final Context context, ContactList contactList, String userId, final Handler.Callback callback) {
        super.httpAsync(new StorageKeyValueManager(context).getSecretKey(), Constants.HTTP_METHOD_POST, "address-book", null, contactList.toJSONObject(userId), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if (msg.what == Constants.RESULT_OK) {
                    callbackMessage.what = Constants.RESULT_OK;
                    callbackMessage.obj = getContactList(context, msg);
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
     * Get contact list from server response
     * @param context context
     * @param msg callback message
     * @return contact list object
     */
    public ContactList getContactList(Context context,Message msg) {

        JSONObject json = (JSONObject)msg.obj;

        ContactList contactList = null;
        try
        {
            if(json.has("data") && !json.isNull("data"))
            {
                new StorageKeyValueManager(context).setContactCache(json.getJSONArray("data").toString());
                new StorageKeyValueManager(context).setContactLastUpload(System.currentTimeMillis());
                contactList = contactListFromResponse(json.getJSONArray("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public static ContactList contactListFromResponse(JSONArray jsonArray)
    {
        ArrayList<RankedContact> contacts=new ArrayList<>();

        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                JSONObject json=jsonArray.getJSONObject(i);

                RankedContact contact = new RankedContact(json);

                contacts.add(contact);

                Log.i("JSON:",json.toString()+" ");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ContactList contactList = new ContactList();

        Collections.sort(contacts, new Comparator<RankedContact>() {
            @Override
            public int compare(RankedContact p1, RankedContact p2) {
                return p1.getRank() - p2.getRank(); // Ascending
                //return p2.getRank() - p1.getRank(); // Descending
            }

        });


        /*Collections.sort(contacts, new Comparator<RankedContact>() {
            public int compare(RankedContact s1, RankedContact s2) {

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
}
