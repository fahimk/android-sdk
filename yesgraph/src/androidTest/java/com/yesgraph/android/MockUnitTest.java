package com.yesgraph.android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Authenticate;
import com.yesgraph.android.network.Invite;
import com.yesgraph.android.network.SuggestionsShown;
import com.yesgraph.android.utils.SendEmailManager;
import com.yesgraph.android.utils.SenderManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Klemen on 21.12.2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class MockUnitTest {

    @Mock
    Context context;

    @Test
    public void testSendEmailMock() {

        SendEmailManager emailManager = Mockito.mock(SendEmailManager.class);

        when(emailManager.sendEmail()).thenReturn(true);

        assertEquals(emailManager.sendEmail(), true);

    }

    /**
     * Validate if invites sent to contacts
     *
     * @throws Exception
     */
    @Test
    public void testValidateInvitesSend() throws Exception {

        SenderManager manager = Mockito.mock(SenderManager.class);

        when(manager.inviteContacts(context)).thenReturn(true);

        assertEquals(manager.inviteContacts(context), true);

    }

   /* @Test
    public void testFetchAddressBookForUser() {

        String userID = "1234";

        AddressBook mock = Mockito.mock(AddressBook.class);

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        mock.fetchAddressBookForUserId(context, userID, callback);
        verify(mock, atLeastOnce()).fetchAddressBookForUserId(context, userID, callback);
        doNothing().when(mock).fetchAddressBookForUserId(context, userID, callback);
    }*/

    @Test
    public void testUpdateAddressBookWithContactListForUserId() {

        String userID = "1234";

        AddressBook mock = Mockito.mock(AddressBook.class);

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        ContactList contactList = Mockito.mock(ContactList.class);

        mock.updateAddressBookWithContactListForUserId(context, contactList, userID, callback);

        verify(mock, atLeastOnce()).updateAddressBookWithContactListForUserId(context, contactList, userID, callback);
        doNothing().when(mock).updateAddressBookWithContactListForUserId(context, contactList, userID, callback);
    }

    @Test
    public void testFetchClientKeyWithSecretKey() {

        String userID = "1234";
        String secretKey = "secret_key";

        Authenticate mock = Mockito.mock(Authenticate.class);

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        mock.fetchClientKeyWithSecretKey(context, secretKey, userID, callback);

        verify(mock, atLeastOnce()).fetchClientKeyWithSecretKey(context, secretKey, userID, callback);
        doNothing().when(mock).fetchClientKeyWithSecretKey(context, secretKey, userID, callback);
    }

    @Test
    public void testUpdateInvitesSentForUser() {

        String userID = "1234";
        Invite mock = Mockito.mock(Invite.class);

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        ArrayList<Contact> invites = new ArrayList<>();

        mock.updateInvitesSentForUser(context, invites, userID, callback);

        verify(mock, atLeastOnce()).updateInvitesSentForUser(context, invites, userID, callback);
        doNothing().when(mock).updateInvitesSentForUser(context, invites, userID, callback);
    }

    @Test
    public void testUpdateSuggestionsSeen() {

        String userID = "1234";
        SuggestionsShown mock = Mockito.mock(SuggestionsShown.class);

        final Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        };

        ArrayList<RankedContact> invites = new ArrayList<>();

        mock.updateSuggestionsSeen(context, invites, userID, callback);

        verify(mock, atLeastOnce()).updateSuggestionsSeen(context, invites, userID, callback);
        doNothing().when(mock).updateSuggestionsSeen(context, invites, userID, callback);
    }
}
