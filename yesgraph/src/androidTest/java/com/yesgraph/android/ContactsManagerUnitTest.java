package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.utils.ContactManager;

import java.util.ArrayList;

/**
 * Created by Klemen on 21.12.2015.
 */
public class ContactsManagerUnitTest extends ApplicationTestCase<Application> {
    public ContactsManagerUnitTest() {
        super(Application.class);
    }


    /**
     * Check contact list data are not null
     */
    public void testCheckContactListData() {

        ContactList contactList = new ContactManager().getContactList(getContext());

        boolean numberOfEntriesNotNull = contactList.getEntries() != null;
        assertEquals(true, numberOfEntriesNotNull);

        boolean sourceNotNull = contactList.getSource() != null;
        assertEquals(true, sourceNotNull);

        boolean isSuggestionsSet = contactList.getUseSuggestions();
        assertEquals(true, isSuggestionsSet);

    }

    /**
     * Validate search filter result
     */
    public void testCheckFilteredContactsExisted() {

        String filterText = "Lu";

        int actualNumber = getNumberOfFilteredContacts(filterText);

        int expectedNumber = 1;

        assertEquals(expectedNumber, actualNumber);

    }

    /**
     * Validate search filter result when search text not existed
     */
    public void testCheckFilteredContactsNoneExisted() {

        String filterText = "X";

        int actualNumber = getNumberOfFilteredContacts(filterText);

        int expectedNumber = 0;

        assertEquals(expectedNumber, actualNumber);

    }


    private int getNumberOfFilteredContacts(String filterText) {

        ArrayList<RegularContact> contacts = getContacts();

        ArrayList<Object> filteredContacts = new ContactManager().getContactsByFilter(filterText, contacts);

        int actualNumber = 0;

        for (Object object : filteredContacts) {
            if (object instanceof RegularContact) {
                RegularContact contact = (RegularContact) object;
                if (contact.getName().toLowerCase().contains(filterText.toLowerCase())) {
                    actualNumber++;
                }
            }
        }
        return actualNumber;
    }


    private ArrayList<RegularContact> getContacts() {

        ArrayList<RegularContact> contacts = new ArrayList<>();

        String[] names = getNames();

        for (int i = 0; i < names.length; i++) {

            RegularContact contact = new RegularContact();
            contact.setName(names[i]);
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * Check if contacts are sorted by alphabet
     */
    public void testCheckSortContactsByAlphabet() {

        int numberOfSuggestedContacts = 2;
        ArrayList<RegularContact> contacts = getContacts();

        ArrayList<Object> sortedContacts = new ContactManager().getContactsByAlphabetSections(getContext(), contacts, numberOfSuggestedContacts);

        boolean isSortedAlphabet = true;
        boolean areSuggestionsOnTheTop = true;

        for (int i = 0; i < sortedContacts.size(); i++) {

            if ((i + 2) > sortedContacts.size()) {
                break;
            }

            Object currentContact = sortedContacts.get(i);
            Object nextContact = sortedContacts.get(i + 1);

            if (i == 0) {

                if (currentContact instanceof HeaderContact) {
                    if (!((HeaderContact) currentContact).getSign().equals(getContext().getString(R.string.suggested))) {
                        areSuggestionsOnTheTop = false;
                        break;
                    }
                }
            }

            if (i > numberOfSuggestedContacts) {

                if (currentContact instanceof RegularContact && nextContact instanceof RegularContact) {

                    RegularContact current = (RegularContact) currentContact;
                    RegularContact next = (RegularContact) nextContact;

                    int asciiNameValue = (int) current.getName().charAt(0);
                    int asciiNextNameValue = (int) next.getName().charAt(0);

                    if (asciiNextNameValue <= asciiNameValue) {
                        isSortedAlphabet = false;
                        break;
                    }
                }
            }
        }

        assertEquals(true, areSuggestionsOnTheTop);
        assertEquals(true, isSortedAlphabet);
    }


    @NonNull
    private String[] getNames() {
        return new String[]{"Adam", "Eva", "John", "Michael", "Jenni", "Alma", "Lucky", "Maria"};
    }

}