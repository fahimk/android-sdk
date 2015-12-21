package com.yesgraph.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.utils.SendEmailManager;

/**
 * Created by Dean Bozinoski on 11/16/2015.
 */
public class SendEmailActivity extends AppCompatActivity {

    private Context context;
    private SendEmailManager sendEmailManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        context = this;

        getData();

        showSendAlertDialog();

    }

    /**
     * Get contacts emails, subject and message data from intent bundle
     */
    private void getData() {

        String[] contacts_emails = getIntent().getStringArrayExtra("contacts");
        String subject = getIntent().getStringExtra("subject");
        String message = getIntent().getStringExtra("message");

        try {
            sendEmailManager = new SendEmailManager(this, message, subject, contacts_emails);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.no_selected_contacts_email), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showSendAlertDialog() {

        String[] contacts = sendEmailManager.getContacts_emails();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_send_email_title));
        // set dialog message
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_send_email_message)
                + " " + contacts.length + " " + context.getResources().getString(R.string.alert_send_email_contacts))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.alert_send_email_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendEmailManager.sendEmail();
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
}
