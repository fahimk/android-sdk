package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.utils.SenderManager;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class SenderManagerUnitTest extends ApplicationTestCase<Application> {
    public SenderManagerUnitTest() {
        super(Application.class);
    }


    /**
     * Validate number of checked contact with email
     *
     * @throws Exception
     */
    public void testValidateGetCheckedEmail() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        ArrayList<RegularContact> checkedContacts = senderManager.getCheckedEmailContacts();

        int actualContacts = checkedContacts.size();

        assertEquals(numberOfContactsWithEmail, actualContacts);
    }

    /**
     * Validate number of checked contact with phone
     *
     * @throws Exception
     */
    public void testValidateGetCheckedPhone() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        ArrayList<RegularContact> checkedContacts = senderManager.getCheckedPhoneContacts();

        int actualContacts = checkedContacts.size();

        assertEquals(numberOfContactsWithPhone, actualContacts);
    }

    /**
     * Validate number of checked contact with email from not selected contacts
     *
     * @throws Exception
     */
    public void testValidateGetCheckedEmailNotSelectedContacts() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = false;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        ArrayList<RegularContact> checkedContacts = senderManager.getCheckedEmailContacts();

        int actualContacts = checkedContacts.size();

        assertEquals(0, actualContacts);
    }

    /**
     * Validate number of checked contact with phone from not selected contacts
     *
     * @throws Exception
     */
    public void testValidateGetCheckedPhoneNotSelectedContacts() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = false;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        ArrayList<RegularContact> checkedContacts = senderManager.getCheckedPhoneContacts();

        int actualContacts = checkedContacts.size();

        assertEquals(0, actualContacts);
    }

    /**
     * Validate string emails array size from contacts with email
     *
     * @throws Exception
     */
    public void testValidateStringEmails() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        String[] stringEmails = senderManager.getEmails();

        int actualContacts = stringEmails.length;

        assertEquals(numberOfContactsWithEmail, actualContacts);
    }


    /**
     * Validate empty string array emails
     *
     * @throws Exception
     */
    public void testValidateEmptyStringEmails() throws Exception {

        int numberOfContactsWithEmail = 0;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        String[] stringEmails = senderManager.getEmails();

        boolean isNull = (stringEmails == null);

        assertEquals(true, isNull);
    }

    /**
     * Validate string phones array size from contacts with phone contact
     *
     * @throws Exception
     */
    public void testValidateStringPhones() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        String[] stringPhones = senderManager.getPhones();

        int actualContacts = stringPhones.length;

        assertEquals(numberOfContactsWithPhone, actualContacts);
    }


    /**
     * Validate empty string array phones
     *
     * @throws Exception
     */
    public void testValidateEmptyStringPhones() throws Exception {

        int numberOfContactsWithEmail = 5;
        int numberOfContactsWithPhone = 0;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        String[] stringPhones = senderManager.getPhones();

        boolean isNull = (stringPhones == null);

        assertEquals(true, isNull);
    }

    /**
     * Validate the contacts which send them the invite
     *
     * @throws Exception
     */
    public void testValidateInvitedContacts() throws Exception {

        int numberOfContactsWithEmail = 3;
        int numberOfContactsWithPhone = 5;
        boolean isSelected = true;

        ArrayList<Object> contacts = getContactsWithEmailAndPhone(numberOfContactsWithEmail, numberOfContactsWithPhone, isSelected);

        SenderManager senderManager = new SenderManager(contacts);

        String[] contactsPhonesNumber = senderManager.getPhones();
        String[] contactsEmails = senderManager.getEmails();

        ArrayList<Contact> invitedContacts = senderManager.getInvitedContacts();

        int expectedContacts = contactsPhonesNumber.length + contactsEmails.length;
        int actualContacts = invitedContacts.size();

        assertEquals(expectedContacts, actualContacts);
    }


    /**
     * HELPER METHODS
     **/

    private ArrayList<Object> getContactsWithEmailAndPhone(int numberOfContactsWithEmail, int numberOfContactsWithPhone, boolean isSelected) {

        ArrayList<Object> allContact = new ArrayList<>();

        ArrayList<Object> emailContacts = getContactsWithEmail(numberOfContactsWithEmail, isSelected);
        ArrayList<Object> phoneContacts = getContactsWithPhone(numberOfContactsWithPhone, isSelected);

        allContact.addAll(emailContacts);
        allContact.addAll(phoneContacts);

        return allContact;
    }

    @NonNull
    private ArrayList<Object> getContactsWithEmail(int size, boolean isSelected) {

        ArrayList<Object> entries = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");
            contact.setEmail("john@email.com");
            contact.setSelected(isSelected);

            entries.add(contact);
        }
        return entries;
    }

    @NonNull
    private ArrayList<Object> getContactsWithPhone(int size, boolean isSelected) {

        ArrayList<Object> entries = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");
            contact.setPhone("123-432-545");
            contact.setSelected(isSelected);

            entries.add(contact);
        }
        return entries;
    }

}