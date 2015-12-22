package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Source;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 18.12.2015.
 */
public class YSGContactUnitTest extends ApplicationTestCase<Application> {
    public YSGContactUnitTest() {
        super(Application.class);
    }


    /**
     * Check contact description when contact name not existed
     */
    public void testCheckContactDescriptionWithEmail() {

        Contact contactWithEmail = new Contact();

        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@email.si");
        emails.add("john@gmail.com");
        contactWithEmail.setEmails(emails);

        String description = contactWithEmail.description();

        boolean descriptionNotNull = description != null;

        assertEquals(true, descriptionNotNull);

    }

    /**
     * Check contact description when contact name not existed
     */
    public void testCheckContactDescriptionWithPhones() {

        Contact contactWithEmail = new Contact();

        ArrayList<String> phones = new ArrayList<>();
        phones.add("123-542-433");
        phones.add("123-542-433");
        contactWithEmail.setEmails(phones);

        String description = contactWithEmail.description();

        boolean descriptionNotNull = description != null;

        assertEquals(true, descriptionNotNull);

    }

    /**
     * Set contacts suggested to false and check it
     *
     * @throws JSONException
     */
    public void testCheckContactSuggested() throws JSONException {

        Contact contact = new Contact();

        JSONObject data = new JSONObject();
        data.put("suggested", "true");

        contact.setData(data);

        Boolean wasSuggested = false;

        contact.setSuggested(wasSuggested);

        boolean isNotSuggested = !contact.wasSuggested();

        assertEquals(true, isNotSuggested);

    }

    /**
     * Check contact phone is not null
     */
    public void testCheckContactPhones() {

        Contact contact = new Contact();

        ArrayList<String> phones = new ArrayList<>();
        phones.add("123-542-433");
        phones.add("123-542-433");
        contact.setPhones(phones);

        boolean hasPhone = contact.phone() != null;

        assertEquals(true, hasPhone);

    }

    /**
     * Check contact email is not null
     */
    public void testCheckContactEmails() {

        Contact contact = new Contact();

        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@email.si");
        emails.add("john@gmail.com");
        contact.setEmails(emails);

        boolean hasEmail = contact.email() != null;

        assertEquals(true, hasEmail);

    }

    /**
     * Check contactString method is not null when added only phones
     */
    public void testCheckPhonesContactData() {

        Contact contact = new Contact();

        ArrayList<String> phones = new ArrayList<>();
        phones.add("123-542-433");
        phones.add("123-542-433");
        contact.setPhones(phones);

        boolean contactNotNull = contact.contactString() != null;

        assertEquals(true, contactNotNull);

    }

    /**
     * Check contactString method is not null when added only emails
     */
    public void testCheckEmailsContactData() throws JSONException {

        Contact contact = new Contact();

        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@email.si");
        emails.add("john@gmail.com");
        contact.setEmails(emails);

        boolean contactNotNull = contact.contactString() != null;

        assertEquals(true, contactNotNull);

    }

}