package com.yesgraph.android;

import android.support.annotation.NonNull;
import android.test.ActivityInstrumentationTestCase2;

import com.yesgraph.android.activity.ContactsActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.utils.AlphabetSideIndexManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemen on 19.12.2015.
 */
public class AlphabeticalSideIndexUnitTest extends ActivityInstrumentationTestCase2<ContactsActivity> {

    private ContactsActivity contactsActivity;

    public AlphabeticalSideIndexUnitTest() {
        super(ContactsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        contactsActivity = getActivity();

    }


    /**
     * Validate count of alphabetical
     */
    public void testValidateAlphabeticalSideIndexSize() {

       YesGraph application = (YesGraph) getActivity().getApplication();

        AlphabetSideIndexManager alphabetSideIndexManager
                = new AlphabetSideIndexManager(contactsActivity, application);

        ArrayList<Object> contacts = getContacts();
        String[] signs = getSigns();

        alphabetSideIndexManager.setIndexList(contacts, 0);

        List<String> indexList = new ArrayList<String>(alphabetSideIndexManager.getIndexList().keySet());

        //check size
        int expectedSize = signs.length;
        int actualSize = indexList.size();

        assertEquals(expectedSize, actualSize);
    }

    /**
     * Validate if all letters are added to index list
     */
    public void testValidateAlphabeticalSizeIndexData() {

       YesGraph application = (YesGraph) getActivity().getApplication();

        AlphabetSideIndexManager alphabetSideIndexManager
                = new AlphabetSideIndexManager(contactsActivity, application);

        ArrayList<Object> contacts = getContacts();
        String[] signs = getSigns();

        alphabetSideIndexManager.setIndexList(contacts, 0);

        List<String> indexList = new ArrayList<String>(alphabetSideIndexManager.getIndexList().keySet());

        boolean isAdded = true;

        for (int i = 0; i < indexList.size(); i++) {
            if (!indexList.contains(signs[i])) {
                isAdded = false;
            }
        }

        assertEquals(true, isAdded);

    }
    
    /**
     * Helper methods
     */

    private ArrayList<Object> getContacts() {

        ArrayList<Object> contacts = new ArrayList<>();

        String[] signs = getSigns();

        for (int i = 0; i < signs.length; i++) {

            HeaderContact headerContact = new HeaderContact();
            headerContact.setSign(signs[i]);
            contacts.add(headerContact);

            RegularContact contact = new RegularContact();
            contacts.add(contact);

        }

        return contacts;
    }

    @NonNull
    private String[] getSigns() {
        return new String[]{"A", "C", "G", "K", "V", "H", "U"};
    }
}