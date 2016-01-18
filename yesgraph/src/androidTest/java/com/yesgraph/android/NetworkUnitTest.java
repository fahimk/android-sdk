package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Authenticate;
import com.yesgraph.android.network.Invite;
import com.yesgraph.android.network.SuggestionsShown;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * Created by Klemen on 5.1.2016.
 */
public class NetworkUnitTest extends ApplicationTestCase<Application> {
    public NetworkUnitTest() {
        super(Application.class);
    }

    @Mock
    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new MockDelegatedContext(getContext());
    }


    public void testUpdateAddressBookWithContactListForUserId() {

        String userID = "1234";

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        ContactList contactList = Mockito.mock(ContactList.class);

        new AddressBook().updateAddressBookWithLimitedContacts(mockContext, callback);

    }

    public void testFetchClientKeyWithSecretKey() {

        String userID = "1234";
        String secretKey = "secret_key";

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        new Authenticate().fetchClientKeyWithSecretKey(mockContext, secretKey, userID, callback);

    }

    public void testUpdateInvitesSentForUser() {

        String userID = "1234";

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        ArrayList<Contact> invites = new ArrayList<>();

        new Invite().updateInvitesSentForUser(mockContext, invites, userID, callback);

    }

    public void testUpdateSuggestionsSeen() {

        String userID = "1234";

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        ArrayList<RankedContact> invites = new ArrayList<>();

        new SuggestionsShown().updateSuggestionsSeen(mockContext, invites, userID, callback);

    }




}
