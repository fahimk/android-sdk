package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.utils.ContactsFilterManager;
import com.yesgraph.android.utils.CustomTheme;
import com.yesgraph.android.utils.FilterType;

import java.util.ArrayList;

/**
 * Created by Klemen on 16.12.2015.
 */
public class ContactsFilterUnitTest extends ApplicationTestCase<Application> {
    public ContactsFilterUnitTest() {
        super(Application.class);
    }


    /**
     * Validate contacts with phone number only
     */
    public void testCheckOnlyPhonesContacts() throws Exception {

        ArrayList<RegularContact> allContacts = getAllContacts();

        int filterType = FilterType.ONLY_PHONES.ordinal();

        ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);

        boolean onlyPhoneContacts = true;

        for (int i = 0; i < filteredContacts.size(); i++) {

            RegularContact contact = filteredContacts.get(i);

            if (contact.isEmail()) {
                onlyPhoneContacts = false;
                break;
            }
        }

        assertEquals(true, onlyPhoneContacts);
    }

    /**
     * Check count of contacts with phone number
     */
    public void testCheckPhonesContactsCount() throws Exception {

        ArrayList<RegularContact> contactsWithPhone = getContactsWithPhone();

        int filterType = FilterType.ONLY_PHONES.ordinal();

        ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(contactsWithPhone).getByFilterType(filterType);

        int expectedCount = contactsWithPhone.size();
        int actualCount = filteredContacts.size();

        assertEquals(expectedCount, actualCount);
    }

    /**
     * Check count of contacts with email
     */
    public void testCheckEmailsContactsCount() throws Exception {

        ArrayList<RegularContact> contactsWithEmail = getContactsWithEmails();

        int filterType = FilterType.ONLY_EMAILS.ordinal();

        ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(contactsWithEmail).getByFilterType(filterType);

        int expectedCount = contactsWithEmail.size();
        int actualCount = filteredContacts.size();

        assertEquals(expectedCount, actualCount);
    }

    /**
     * Check count of all contacts (phone and email)
     */
    public void testCheckAllContactsCount() throws Exception {

        ArrayList<RegularContact> allContacts = getAllContacts();

        int filterType = FilterType.ALL_CONTACTS.ordinal();

        ArrayList<RegularContact> nonFilteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);

        int expectedCount = allContacts.size();
        int actualCount = nonFilteredContacts.size();

        assertEquals(expectedCount, actualCount);
    }

    /**
     * Validate contacts with email only
     */
    public void testCheckOnlyEmailsContacts() throws Exception {

        ArrayList<RegularContact> allContacts = getAllContacts();

        int filterType = FilterType.ONLY_EMAILS.ordinal();

        ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);

        boolean onlyEmailContacts = true;

        for (int i = 0; i < filteredContacts.size(); i++) {

            RegularContact contact = filteredContacts.get(i);

            if (contact.isPhone()) {
                onlyEmailContacts = false;
                break;
            }
        }

        assertEquals(true, onlyEmailContacts);
    }

    /**
     * Check contacts list null pointer
     */
    public void testCheckNullContactsList() {

        ArrayList<RegularContact> allContacts = null;

        int filterType = FilterType.ONLY_EMAILS.ordinal();

        try {
            ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Check empty contacts list
     */
    public void testEmptyContactsList() {

        ArrayList<RegularContact> allContacts = new ArrayList<>();

        int filterType = FilterType.ONLY_EMAILS.ordinal();

        try {
            ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Check unknown filter type
     */
    public void testCheckUnknownFilterType() throws Exception {

        ArrayList<RegularContact> allContacts = getAllContacts();

        int filterType = -1;

        // return all contacts
        ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);

        int expectedCount = allContacts.size();
        int actualCount = filteredContacts.size();

        assertEquals(expectedCount, actualCount);

    }

    /**
     * Check return items value
     */
    public void testCheckReturnContactsList() throws Exception {

        ArrayList<RegularContact> allContacts = getAllContacts();

        int filterType = FilterType.ALL_CONTACTS.ordinal();

        // return all contacts
        ArrayList<RegularContact> filteredContacts = new ContactsFilterManager(allContacts).getByFilterType(filterType);

        boolean isNotNull = filteredContacts != null;
        boolean isNotEmpty = !filteredContacts.isEmpty();

        assertEquals(true, isNotNull);

        assertEquals(true, isNotEmpty);

    }

    /**
     * Check set custom theme with contacts filter type
     */
    public void testSetCustomThemeContactsFilterType()  {

        int onlyPhonesType = FilterType.ONLY_PHONES.ordinal();

        CustomTheme customTheme = new CustomTheme();
        customTheme.setContactsFilterType(onlyPhonesType);

        YesGraph yesGraph = new YesGraph();
        yesGraph.setCustomTheme(customTheme);

        assertEquals(onlyPhonesType,yesGraph.getCustomTheme().getContactsFilterType());

    }


    /***
     * HELPER METHODS
     ***/

    private ArrayList<RegularContact> getAllContacts() {

        ArrayList<RegularContact> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");
            contact.setPhone("123-542-534");
            contact.setEmail("john@email.com");
            contacts.add(contact);

            RegularContact contactWithPhone = new RegularContact();
            contactWithPhone.setName("John");
            contactWithPhone.setPhone("123-542-534");
            contacts.add(contactWithPhone);

            RegularContact contactWithEmail = new RegularContact();
            contactWithEmail.setName("John");
            contactWithEmail.setEmail("john@email.si");
            contacts.add(contactWithEmail);
        }

        return contacts;

    }

    private ArrayList<RegularContact> getContactsWithPhone() {

        ArrayList<RegularContact> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");
            contact.setPhone("123-542-534");
            contacts.add(contact);
        }

        return contacts;
    }

    private ArrayList<RegularContact> getContactsWithEmails() {

        ArrayList<RegularContact> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");
            contact.setEmail("john@poviolabs.com");
            contacts.add(contact);
        }

        return contacts;
    }

}