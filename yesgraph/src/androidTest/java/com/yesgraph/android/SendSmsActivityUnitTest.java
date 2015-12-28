package com.yesgraph.android;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.yesgraph.android.activity.SendEmailActivity;
import com.yesgraph.android.activity.SendSmsActivity;

/**
 * Created by Klemen on 28.12.2015.
 */
public class SendSmsActivityUnitTest extends ActivityInstrumentationTestCase2<SendSmsActivity> {


    private String message = "message";
    private String[] contacts = {"123-434-343", "544-232-3543"};


    public SendSmsActivityUnitTest() {
        super(SendSmsActivity.class);
    }

    @Override
    public void setUp() {

        Intent intent = new Intent();
        intent.setClassName("com.yesgraph.android", ".activity.SendSmsActivity");
        intent.putExtra("contacts", contacts);
        intent.putExtra("message", message);

        setActivityIntent(intent);
    }

    /**
     * Validate retrieve data from the intent
     */
    public void testCheckIntentDataValues() throws Exception {

        String[] returnedContacts = getActivity().getIntent().getStringArrayExtra("contacts");
        String returnedMessage = getActivity().getIntent().getStringExtra("message");

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