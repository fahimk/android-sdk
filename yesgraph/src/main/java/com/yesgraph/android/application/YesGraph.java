package com.yesgraph.android.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Authenticate;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactManager;
import com.yesgraph.android.utils.CustomTheme;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.SharedPreferencesManager;
import com.yesgraph.android.utils.StorageKeyValueManager;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class YesGraph extends Application {

    private CustomTheme customTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        checkIsTimeToRefreshAddressBook();
    }

    public void onCreate(String secretKey) {
        super.onCreate();
        setSecretKey(secretKey);
        checkIsTimeToRefreshAddressBook();
    }

    public void setSecretKey(String secretKey)
    {
        new StorageKeyValueManager(getApplicationContext()).setSecretKey(secretKey);

        final String userID = new StorageKeyValueManager(getApplicationContext()).getUserId();

        Authenticate authenticate = new Authenticate();
        authenticate.fetchClientKeyWithSecretKey(getApplicationContext(), secretKey/*"live-WzEsMCwieWVzZ3JhcGhfc2RrX3Rlc3QiXQ.COM_zw.A76PgpT7is1P8nneuSg-49y4nW8"*/, userID, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == Constants.RESULT_OK)
                {

                }
                else
                {

                }
                return false;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public CustomTheme getCustomTheme() {
        if(customTheme != null){
            return this.customTheme;
        } else {
            return new CustomTheme();
        }
    }

    private boolean timeToRefreshAddressBook() {
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
            Authenticate authenticate = new Authenticate();
            authenticate.fetchClientKeyWithSecretKey(getApplicationContext(), secretKey/*"live-WzEsMCwieWVzZ3JhcGhfc2RrX3Rlc3QiXQ.COM_zw.A76PgpT7is1P8nneuSg-49y4nW8"*/, userID, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
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
                    return false;
                }
            });
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
