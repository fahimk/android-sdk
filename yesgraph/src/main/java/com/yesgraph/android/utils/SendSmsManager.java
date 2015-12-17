package com.yesgraph.android.utils;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by Klemen on 17.12.2015.
 */
public class SendSmsManager {

    private String message;
    private String[] contacts;

    public SendSmsManager(String message, String[] contacts) throws Exception {

        if (contacts == null || contacts.length == 0) {
            throw new Exception("No contacts");
        }

        this.message = message;
        this.contacts = contacts;

    }

    public Intent sendSmsTo() {

        Uri smsToUri = Uri.parse("smsto:" + getContactsSeparateSemicolon());
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", message);

        return intent;
    }

    private String getContactsSeparateSemicolon() {

        String contacts = "";

        for (int i = 0; i < this.contacts.length; i++) {
            contacts += this.contacts[i] + ";";
        }

        return contacts;
    }

    public String getMessage() {
        return message;
    }

    public String[] getContacts() {
        return contacts;
    }
}
