package com.yesgraph.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.SendSmsManager;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class SendSmsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private Context context;
    private SendSmsManager sendSmsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        context = this;

        getContactsAndMessage();

        checkAndroidVersionAndSendSms();
    }

    private void getContactsAndMessage() {

        String[] contacts = getIntent().getStringArrayExtra("contacts");
        String message = getIntent().getStringExtra("message");

        try {
            sendSmsManager = new SendSmsManager(message, contacts);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.no_selected_contacts), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void checkAndroidVersionAndSendSms() {

        if (YesGraph.isMarshmallow()) {
            new PermissionGrantedManager(this).initCheckSendSmsPermission();
        }

        if (YesGraph.isMarshmallow()) {

            boolean isSendSmsPermissionGranted = new PermissionGrantedManager(this).isSendSmsPermission();

            if (isSendSmsPermissionGranted) {
                sendSms();
            } else {
                askForPermissionAlertDialog();
            }

        } else {
            sendSms();
        }
    }

    private void sendSms() {

        if (YesGraph.isMarshmallow()) {
            new PermissionGrantedManager(getBaseContext()).checkSendSmsPermission(this);
            boolean isSendSmsPermissionGranted = new PermissionGrantedManager(this).isSendSmsPermission();
            if (isSendSmsPermissionGranted) {
                showSendAlertDialog();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.enable_permissions), Toast.LENGTH_LONG).show();
            }
        } else {
            showSendAlertDialog();
        }

    }

    private void askForPermissionAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_grant_permission_title));
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_grant_permission_message))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.sms_grant), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendSms();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.sms_deny), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showSendAlertDialog() {

        String[] contacts = sendSmsManager.getContacts();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_send_sms_title));
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_send_sms_message)
                + " " + contacts.length + " " + context.getResources().getString(R.string.alert_send_sms_contacts))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.alert_send_sms_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = sendSmsManager.sendSmsTo();
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.alert_send_sms_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new PermissionGrantedManager(this).setSendSmsPermission(true);

                    showSendAlertDialog();

                } else {
                    new PermissionGrantedManager(this).setSendSmsPermission(false);
                }
                return;
            }
        }
    }
}