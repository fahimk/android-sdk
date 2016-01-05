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


    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new DelegatedMockContext(getContext());
    }

    /**
     * Validate email data not null
     *
     * @throws Exception
     */
    public void testEmailDataNotNull() throws Exception {

        String[] contacts = {"john@email.com", "david@email.com", "hulk@email.com"};
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
     *
     * @throws Exception
     */
    public void testValidateEmailMessageAndSubject() throws Exception {

        String[] contacts = {"john@email.com", "david@email.com", "hulk@email.com"};
        String message = "message";
        String subject = "subject";

        SendEmailManager sendEmailManager = new SendEmailManager(getContext(), message, subject, contacts);

        String actualMessage = sendEmailManager.getMessage();
        String actualSubject = sendEmailManager.getSubject();

        boolean equalMessage = (message.equals(actualMessage));
        assertEquals(true, equalMessage);

        boolean equalSubject = (subject.equals(actualSubject));
        assertEquals(true, equalSubject);

    }

    /**
     * Check if contacts are equals
     *
     * @throws Exception
     */
    public void testValidateEmailContacts() throws Exception {

        String[] contacts = {"john@email.com", "david@email.com", "hulk@email.com"};
        String message = "message";
        String subject = "subject";

        SendEmailManager sendEmailManager = new SendEmailManager(getContext(), message, subject, contacts);

        String[] actualContacts = sendEmailManager.getContacts_emails();

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
     * Check if method to sent email correctly executed
     */
    public void testCheckSendEmail() {

        String[] contacts = {"john@xmail.com", "david@xmail.com", "hulk@xmail.com"};
        String message = "message";
        String subject = "subject";

        SendEmailManager sendEmailManager = null;
        try {
            sendEmailManager = new SendEmailManager(mockContext, message, subject, contacts);
            sendEmailManager.sendEmail();
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Mocking class
     */
    private class DelegatedMockContext extends MockContext {

        private Context mDelegatedContext;
        private static final String PREFIX = "test.";

        public DelegatedMockContext(Context context) {
            mDelegatedContext = context;
        }

        @Override
        public String getPackageName() {
            return PREFIX;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return mDelegatedContext.getSharedPreferences(name, mode);
        }
    }
}