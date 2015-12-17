package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;

import com.yesgraph.android.utils.SendEmailManager;
import com.yesgraph.android.utils.SendSmsManager;

/**
 * Created by Klemen on 17.12.2015.
 */
public class SendEmailManagerUnitTest extends ApplicationTestCase<Application> {
    public SendEmailManagerUnitTest() {
        super(Application.class);
    }

    /**
     * Validate email data not null
     * @throws Exception
     */
    public void testEmailDataNotNull() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";
        String subject = "subject";

        SendEmailManager emailManager = new SendEmailManager(getContext(), message, subject, contacts);

        String actualMessage = emailManager.getMessage();
        String actualSubject = emailManager.getSubject();
        String[] actualContacts = emailManager.getContacts_emails();

        boolean messageNotNull = actualMessage != null;
        boolean contactsNotNull = actualContacts != null;
        boolean subjectNotNull = actualSubject != null;

        assertEquals(true, messageNotNull);
        assertEquals(true, contactsNotNull);
        assertEquals(true, subjectNotNull);

    }

    /**
     * Check empty contacts list exception thrown
     */
    public void testEmailDataEmptyContactsList() {

        String[] contacts = {};
        String message = "message";
        String subject = "subject";

        try {
            new SendEmailManager(getContext(), message, subject, contacts);
        } catch (Exception e) {
            assertTrue(true);
        }
    }


    /**
     * Check if message and subject are set
     * @throws Exception
     */
    public void testValidateEmailMessageAndSubject() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";
        String subject = "subject";

        SendEmailManager sendSmsManager = new SendEmailManager(getContext(), message, subject, contacts);

        String actualMessage = sendSmsManager.getMessage();
        String actualSubject = sendSmsManager.getSubject();

        boolean equalMessage = (message.equals(actualMessage));
        assertEquals(true, equalMessage);

        boolean equalSubject = (subject.equals(actualSubject));
        assertEquals(true, equalSubject);

    }

    /**
     * Check if contacts are equals
     * @throws Exception
     */
    public void testValidateEmailContacts() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";
        String subject = "subject";

        SendEmailManager sendSmsManager = new SendEmailManager(getContext(), message, subject, contacts);

        String[] actualContacts = sendSmsManager.getContacts_emails();

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

    //TODO: need to be mock
    public void testValidateSendEmail() throws Exception {

        String[] contacts = {"123-431-233", "998-323-434", "655-434-655"};
        String message = "message";
        String subject = "subject";

        SendEmailManager sendSmsManager = new SendEmailManager(getContext(), message, subject, contacts);

        boolean isSuccessSent = sendSmsManager.sendEmail();

        assertEquals(false, isSuccessSent);
    }


}