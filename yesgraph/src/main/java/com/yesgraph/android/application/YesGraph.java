package com.yesgraph.android.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Authenticate;
import com.yesgraph.android.network.Invite;
import com.yesgraph.android.network.SuggestionsShown;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactManager;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.CustomTheme;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.SharedPreferencesManager;
import com.yesgraph.android.utils.StorageKeyValueManager;

import java.util.ArrayList;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class YesGraph extends Application {

    private CustomTheme customTheme;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void onCreate(String secretKey) {
        super.onCreate();
        setSecretKey(secretKey);
    }

    public void setSecretKey(String secretKey)
    {
        new StorageKeyValueManager(getApplicationContext()).setApiKey(secretKey);

        final String userID = new StorageKeyValueManager(getApplicationContext()).getUserId();

        Authenticate authenticate = new Authenticate();
        authenticate.fetchClientKeyWithSecretKey(getApplicationContext(), secretKey, userID, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == Constants.RESULT_OK) {
                    checkIsTimeToRefreshAddressBook();
                } else {

                }
                return false;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void setSource(String userName, String userPhone, String userEmail)
    {
        new StorageKeyValueManager(getApplicationContext()).setUserName(userName);
        new StorageKeyValueManager(getApplicationContext()).setUserPhone(userPhone);
        new StorageKeyValueManager(getApplicationContext()).setUserEmail(userEmail);
    }

    public ArrayList<RankedContact> readContactsFromPhone()
    {
        return ContactRetriever.readYSGContacts(getApplicationContext());
    }

    public void updateContactsFromPhone(Handler.Callback callback)
    {
        AddressBook addressBook =new AddressBook();
        ContactList contacts=new ContactManager().getContactList(getApplicationContext());
        addressBook.updateAddressBookWithContactListForUserId(getApplicationContext(), contacts, new StorageKeyValueManager(getApplicationContext()).getUserId(), callback);
    }

    public void updateSuggestionsSeen(ArrayList<RankedContact> contacts, Handler.Callback callback)
    {
        SuggestionsShown ysgSuggestionsShown = new SuggestionsShown();
        ysgSuggestionsShown.updateSuggestionsSeen(getApplicationContext(), contacts, new StorageKeyValueManager(getApplicationContext()).getUserId(), callback);
    }

    public void inviteSentForUsers(ArrayList<Contact> contacts, Handler.Callback callback)
    {
        Invite ysgInvite = new Invite();
        ysgInvite.updateInvitesSentForUser(getApplicationContext(), contacts, new StorageKeyValueManager(getApplicationContext()).getUserId(), callback);
    }

    public CustomTheme getCustomTheme() {
        if(customTheme != null){
            return this.customTheme;
        } else {
            return new CustomTheme();
        }
    }

    public boolean timeToRefreshAddressBook() {
        long lastContactsUpload = new StorageKeyValueManager(getApplicationContext()).getContactLastUpload();

        if(lastContactsUpload < System.currentTimeMillis() - (Constants.HOURS_BETWEEN_UPLOAD * 60 * 60 * 1000))
            return true;
        else
            return false;
    }

    public void checkIsTimeToRefreshAddressBook() {

        new StorageKeyValueManager(this).setContactsUploading(false);

        final String userID = new StorageKeyValueManager(getApplicationContext()).getUserId();
        final String secretKey = new StorageKeyValueManager(getApplicationContext()).getSecretKey();

        if(secretKey.length()>0)
        {
            boolean isReadContactsPermission = new PermissionGrantedManager(getApplicationContext()).isReadContactsPermission();
            if (timeToRefreshAddressBook() && isReadContactsPermission && isOnline()) {
                try {
                    new StorageKeyValueManager(getApplicationContext()).setContactsUploading(true);
                    ContactList contactList = new ContactManager().getContactList(getApplicationContext());
                    AddressBook addressBook = new AddressBook();
                    addressBook.updateAddressBookWithContactListForUserId(getApplicationContext(), contactList, userID, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            new StorageKeyValueManager(getApplicationContext()).setContactsUploading(false);
                            return false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), R.string.error_not_initialized_with_secret_key,Toast.LENGTH_SHORT).show();
        }

    }

    public void setCustomTheme(CustomTheme customTheme) {
        this.customTheme = customTheme;
    }

    public static boolean isMarshmallow() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.M) {
            return false;
        } else {
            return true;
        }
    }

    public static Integer getNumberOfSuggestedContacts()
    {
        return 5;
    }

    public static boolean isFacebookSignedIn() {
        return true;
    }

    public static boolean isTwitterSignedIn() {
        return true;
    }

    public String getCopyLinkText() {
        return getString(R.string.default_share_link);
    }

    public String getCopyButtonText(){
        return getString(R.string.button_copy_text);
    }

    public String getShareText() {
        return getString(R.string.default_share_text);
    }

    public String getSmsText() {
        return getString(R.string.default_share_text);
    }

    public String getEmailText() {
        return getString(R.string.default_share_text);
    }

    public String getEmailSubject() {
        return getString(R.string.default_share_text);
    }
}
