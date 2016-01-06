package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Address;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Source;
import com.yesgraph.android.models.Website;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 18.12.2015.
 */
public class ContactUnitTest extends ApplicationTestCase<Application> {
    public ContactUnitTest() {
        super(Application.class);
    }


    public void testContactModelData() {

        Contact contact = new TestUtils().getContact();

        assertTrue(!contact.name().isEmpty());
        assertTrue(!contact.phone().isEmpty());
        assertTrue(!contact.getNickname().isEmpty());
        assertEquals(contact.getPinned(), (Integer) 2);

        assertTrue(!contact.getTitle().isEmpty());
        assertTrue(!contact.getCompany().isEmpty());
        assertTrue(contact.getIs_favorite());

        assertTrue(contact.getTimes_contacted() != null);
        assertTrue(contact.getLast_message_received_date() != null);
        assertTrue(contact.getLast_message_sent_date() != null);

        assertTrue(contact.getAddresses() != null);
        assertTrue(contact.getWebsites() != null);
        assertTrue(contact.getIms() != null);

    }

    public void testConvertContactToJson() {

        Contact contact = new TestUtils().getContact();

        JSONObject jsonObject = contact.toJSONObject();

        assertTrue(jsonObject != null);

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
        data.put("suggested", "false");

        contact.setData(data);

        contact.setSuggested(false);

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

    /**
     * Check sanitized name
     */
    public void testSanitizedName() {

        Contact contact = new Contact();

        contact.setName("John");

        String sanitizedName = contact.sanitizedName();

        assertEquals(true, !sanitizedName.isEmpty());

    }


    /**
     * Check if set suggested flag to contact
     *
     * @throws JSONException
     */
    public void testCheckWasSuggestedContact() throws JSONException {

        Contact contact = new Contact();

        JSONObject data = new JSONObject();
        data.put("suggested", "true");
        contact.setData(data);
        contact.setSuggested(true);

        boolean isSuggested = contact.wasSuggested();

        assertEquals(true, isSuggested);

    }


}