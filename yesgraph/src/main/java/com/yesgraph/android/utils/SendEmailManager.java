package com.yesgraph.android.utils;

import android.content.Context;
import android.content.Intent;

import com.yesgraph.android.R;

/**
 * Created by Klemen on 17.12.2015.
 */
public class SendEmailManager {

    private Context context;
    private String[] contacts_emails;
    private String message;
    private String subject;


    public SendEmailManager(Context context, String message, String subject, String[] contacts_emails) throws Exception {

        if (contacts_emails == null || contacts_emails.length == 0) {
            throw new Exception("No selected contacts");
        }

        this.context = context;
        this.message = message;
        this.subject = subject;
        this.contacts_emails = contacts_emails;

    }

    /**
     * Send email
     *
     * @return true if is successful sent
     */
    public boolean sendEmail() {

        boolean isSuccess = true;

        try {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, contacts_emails);
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);
            //need this to prompts email client only
            email.setType("message/rfc822");
            context.startActivity(Intent.createChooser(email, context.getResources().getString(R.string.choose_email_client)));
        } catch (Exception ex) {
            ex.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;
    }

    public String[] getContacts_emails() {
        return contacts_emails;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }
}
