package com.yesgraph.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yesgraph.android.R;

/**
 * Created by Dean Bozinoski on 11/16/2015.
 */
public class SendEmailActivity extends AppCompatActivity {

    private Context context;
    private Intent intent;
    private String[] contacts_emails;
    private String message;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        context = this;

        intent = getIntent();

        contacts_emails = intent.getStringArrayExtra("contacts");
        subject = intent.getStringExtra("subject");
        message = intent.getStringExtra("message");
        showSendAlertDialog(contacts_emails);

    }

    private void showSendAlertDialog(final String[] contacts) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_send_email_title));
        // set dialog message
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_send_email_message)
                + " " + contacts.length + " " + context.getResources().getString(R.string.alert_send_email_contacts))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.alert_send_email_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (contacts_emails != null && contacts_emails.length > 0) {
                            sendEmail(contacts_emails, subject, message);
                        } else {
                            Toast.makeText(context,context.getResources().getString(R.string.no_selected_contacts_email),Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.alert_send_email_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void sendEmail(String[] contacts_emails, String subject, String message) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, contacts_emails);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        //need this to prompts email client only
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, context.getResources().getString(R.string.choose_email_client)));
    }

}
