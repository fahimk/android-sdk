package com.yesgraph.android;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;
import android.text.TextUtils;

import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.utils.ContactRetriever;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Klemen on 9.12.2015.
 */
public class ContactsRetrieverUnitTest extends ApplicationTestCase<Application> {
    public ContactsRetrieverUnitTest() {
        super(Application.class);
    }

    /**
     * Check if YSGContact name is empty
     */
    public void testCheckRetrieverYSGContactsData() {

        ArrayList<YSGRankedContact> contacts = ContactRetriever.readYSGContacts(getContext());

        boolean contactNameIsEmpty = false;

        int contactsCount = contacts.size();

        if (contactsCount > 0) {

            for (int i = 0; i < contacts.size(); i++) {

                String contactName = contacts.get(i).name();

                if (TextUtils.isEmpty(contactName)) {
                    contactNameIsEmpty = true;
                }
            }
        }

        assertEquals(false, contactNameIsEmpty);

    }

    /**
     * Check if contact name is empty
     */
    public void testCheckRetrieverContactsData() {

        ArrayList<RegularContact> contacts = ContactRetriever.readContacts(getContext());

        boolean contactNameIsEmpty = false;

        int contactsCount = contacts.size();

        if (contactsCount > 0) {

            for (int i = 0; i < contacts.size(); i++) {

                String contactName = contacts.get(i).getName();

                if (TextUtils.isEmpty(contactName)) {
                    contactNameIsEmpty = true;
                }
            }
        }

        assertEquals(false, contactNameIsEmpty);

    }

}