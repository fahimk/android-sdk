package com.yesgraph.android;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;

import com.yesgraph.android.activity.SendSmsActivity;
import com.yesgraph.android.utils.AlertDialogsManager;
import com.yesgraph.android.utils.PermissionGrantedManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

    }

    /**
     * Validate retrieve data from the intent
     */
    public void testCheckIntentDataValues() throws Exception {

        setIntent(false);

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

    public void testShowAlertDialogForSendSmsPermission() {

        //set intent data for M android devices
        setIntent(true);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialogsManager(getActivity()).askForPermissionAlertDialog();
            }
        });

        //grant sms permission
        onView(withId(android.R.id.button1)).perform(ViewActions.click());
    }

    public void testShowAlertDialogForSendSms() {

        //set intent data for older android devices
        setIntent(false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialogsManager(getActivity()).askForSendSmsAlertDialog(2);
            }
        });

        //accept sms send
        onView(withId(android.R.id.button2)).perform(ViewActions.click());
    }


    private void setIntent(boolean isMarshmallow) {

        Intent intent = new Intent();
        intent.setClassName("com.yesgraph.android", ".activity.SendSmsActivity");
        intent.putExtra("contacts", contacts);
        intent.putExtra("message", message);
        intent.putExtra("isMarshmallow", isMarshmallow);

        setActivityIntent(intent);
    }


}