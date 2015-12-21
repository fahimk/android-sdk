package com.yesgraph.android;

import android.app.Application;
import android.content.Intent;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.SendSmsManager;

import java.util.ArrayList;

/**
 * Created by Klemen on 17.12.2015.
 */
public class SendSmsManagerUnitTest extends ApplicationTestCase<Application> {
    public SendSmsManagerUnitTest() {
        super(Application.class);
    }

    /**
     * Validate null data
     * @throws Exception
     */
    public void testSmsDataNotNull() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";

        SendSmsManager sendSmsManager = new SendSmsManager(message, contacts);

        String actualMessage = sendSmsManager.getMessage();
        String[] actualContacts = sendSmsManager.getContacts();

        boolean messageNotNull = actualMessage != null;
        boolean contactsNotNull = actualContacts != null;

        assertEquals(true, messageNotNull);
        assertEquals(true, contactsNotNull);

    }

    /**
     * Validate empty contacts list exception
     */
    public void testSmsDataEmptyContactsList() {

        String[] contacts = {};
        String message = "message";

        try {
            new SendSmsManager(message, contacts);
        } catch (Exception e) {
            assertTrue(true);
        }
    }


    /**
     * Check sms message is set
     * @throws Exception
     */
    public void testValidateSmsMessage() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";

        SendSmsManager sendSmsManager = new SendSmsManager(message, contacts);

        String actualMessage = sendSmsManager.getMessage();

        boolean equalMessage = (message.equals(actualMessage));
        assertEquals(true, equalMessage);

    }

    /**
     * Checking if contacts are equals
     * @throws Exception
     */
    public void testValidateSmsContacts() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";

        SendSmsManager sendSmsManager = new SendSmsManager(message, contacts);

        String[] actualContacts = sendSmsManager.getContacts();

        boolean equalCount = actualContacts.length == contacts.length;
        assertEquals(true, equalCount);

        boolean areEqualContacts = true;

        for (int i = 0; i < contacts.length; i++) {
            String expectedContact = contacts[i];

            if (!expectedContact.equalsIgnoreCase(actualContacts[i])) {
                areEqualContacts = false;
                break;
            }
        }

        assertEquals(true, areEqualContacts);
    }

    /**
     * Check sms sending
     * @throws Exception
     */
    public void testValidateSendSmsNotNull() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";

        SendSmsManager sendSmsManager = new SendSmsManager(message, contacts);

        Intent intent = sendSmsManager.sendSmsTo();

        boolean notNull = intent != null;

        assertEquals(true, notNull);
    }


}