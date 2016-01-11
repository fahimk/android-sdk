package com.yesgraph.android.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.Source;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactManager;
import com.yesgraph.android.utils.StorageKeyValueManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


    /**
     * Check if contacts is over 2000 and then execute post request for first 2000 contacts otherwise upload all contacts in one piece.
     *
     * @param context  context
     * @param callback callback handler
     */
    public void updateAddressBookWithLimitedContacts(Context context, Handler.Callback callback) {

        final int LIMIT_CONTACTS = 2000;

        String userID = new StorageKeyValueManager(context).getUserId();
        List<ContactList> contactLists;

        ContactList contactList = new ContactManager().getContactList(context);

        int numberOfContacts = contactList.getEntries().size();

        // check if contacts is over 2000
        if (numberOfContacts > LIMIT_CONTACTS) {

            // set first 2000 contacts
            ContactList firstTwoHundredContacts = new ContactList();
            firstTwoHundredContacts.setUseSuggestions(contactList.getUseSuggestions());
            firstTwoHundredContacts.setSource(contactList.getSource());
            firstTwoHundredContacts.setEntries(new ArrayList<RankedContact>(contactList.getEntries().subList(0, LIMIT_CONTACTS)));

            // get others contacts splitted into list
            contactLists = getLimitedContactsList(LIMIT_CONTACTS, contactList);

            // post first 2000 contacts to web api and set contacts to cache
            updateAddressBookWithContactListForUserId(context, firstTwoHundredContacts, userID, callback);

            // post others contacts to web api in async task
            new UpdateAddressBookAsyncTask(context, contactLists, userID).execute();

        } else {
            updateAddressBookWithContactListForUserId(context, contactList, userID, callback);
        }
    }

    /**
     * Split contacts to more limited list size
     *
     * @param LIMIT_CONTACTS limit of each list
     * @param contactList    all contacts items
     * @return limited contacts
     */
    private List<ContactList> getLimitedContactsList(int LIMIT_CONTACTS, ContactList contactList) {

        List<ContactList> contactLists = new ArrayList<>();

        ArrayList<RankedContact> entries = new ArrayList<>();
        int limitCounter = 0;

        for (int i = LIMIT_CONTACTS; i < contactList.getEntries().size(); i++) {

            if (limitCounter == LIMIT_CONTACTS || i == contactList.getEntries().size() - 1) {
                ContactList list = new ContactList();
                list.setUseSuggestions(contactList.getUseSuggestions());
                list.setSource(contactList.getSource());
                list.setEntries(entries);
                contactLists.add(list);
                limitCounter = 0;
                entries = new ArrayList<>();
            } else {
                entries.add(contactList.getEntries().get(i));
                limitCounter++;
            }
        }

        return contactLists;
    }

    private class UpdateAddressBookAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private List<ContactList> contactLists;
        private String userID;

        public UpdateAddressBookAsyncTask(Context context, List<ContactList> contactLists, String userID) {
            this.context = context;
            this.contactLists = contactLists;
            this.userID = userID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean isSuccess = true;

            try {
                for (int i = 0; i < contactLists.size(); i++) {
                    ContactList list = contactLists.get(i);
                    updateAddressBookWithContactsIgnoreResponse(context, list, userID);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                isSuccess = false;
            }

            return isSuccess;
        }
    }


    private void updateAddressBookWithContactsIgnoreResponse(final Context context, ContactList contactList, String userId) {
        super.httpAsync(new StorageKeyValueManager(context).getSecretKey(), Constants.HTTP_METHOD_POST, "address-book", null, contactList.toJSONObject(userId), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
    }


    private void updateAddressBookWithContactListForUserId(final Context context, ContactList contactList, String userId, final Handler.Callback callback) {
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
