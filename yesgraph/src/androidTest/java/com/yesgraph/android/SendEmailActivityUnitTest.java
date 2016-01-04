package com.yesgraph.android;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.yesgraph.android.activity.SendEmailActivity;
import com.yesgraph.android.utils.PermissionGrantedManager;

/**
 * Created by Klemen on 28.12.2015.
 */
public class SendEmailActivityUnitTest extends ActivityInstrumentationTestCase2<SendEmailActivity> {

    private String message = "message";
    private String subject = "subject";
    private String[] contacts = {"john@email.com", "marie@gmail.com"};

    public SendEmailActivityUnitTest() {
        super(SendEmailActivity.class);
    }

    @Override
    public void setUp() {

        Intent intent = new Intent();
        intent.setClassName("com.yesgraph.android", ".activity.SendEmailActivity");

        intent.putExtra("contacts", contacts);
        intent.putExtra("subject", subject);
        intent.putExtra("message", message);

        setActivityIntent(intent);
    }

    /**
     * Validate retrieve data from the intent
     */
    public void testCheckIntentDataValues() {

        String[] returnedContacts = getActivity().getIntent().getStringArrayExtra("contacts");
        String returnedSubject = getActivity().getIntent().getStringExtra("subject");
        String returnedMessage = getActivity().getIntent().getStringExtra("message");

        assertEquals(subject, returnedSubject);
        assertEquals(message, returnedMessage);

        boolean isEqualContacts = true;

        for (int i = 0; i < returnedContacts.length; i++) {
            if (!returnedContacts[i].equalsIgnoreCase(contacts[i])) {
                isEqualContacts = false;
                break;
            }
        }

        assertEquals(true, isEqualContacts);
    }

}