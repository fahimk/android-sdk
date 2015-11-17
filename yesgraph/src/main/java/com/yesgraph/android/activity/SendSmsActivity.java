package com.yesgraph.android.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class SendSmsActivity extends AppCompatActivity {

    private static int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private YesGraph application;
    private Context context;
    private String[] contacts;
    private String message;
    private Intent intent;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        application = (YesGraph) getApplicationContext();
        intent = getIntent();

        contacts = intent.getStringArrayExtra("contacts");
        message = intent.getStringExtra("message");

        if (YesGraph.isMarshmallow()) {
            initCheckForPermissions();
        }
        if (YesGraph.isMarshmallow()) {
            if (sharedPreferences.getBoolean("send_sms_permision_granted", false)) {
                sendSms(contacts);
            } else {
                askForPermissionAlertDialog();
            }
        } else {
            sendSms(contacts);
        }
    }

    private void sendSms(final String[] contacts) {
        if (contacts != null && contacts.length > 0) {
            if (YesGraph.isMarshmallow()) {
                checkForPermissions();
                if (sharedPreferences.getBoolean("send_sms_permision_granted", false)) {
                    showSendAlertDialog(contacts);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.enable_permissions), Toast.LENGTH_LONG).show();
                }
            } else {
                showSendAlertDialog(contacts);
            }
        }
    }

    private void askForPermissionAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_grant_permission_title));
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_grant_permission_message))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.sms_grant), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendSms(contacts);
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

    private void showSendAlertDialog(final String[] contacts) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_send_sms_title));
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_send_sms_message)
                + " " + contacts.length + " " + context.getResources().getString(R.string.alert_send_sms_contacts))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.alert_send_sms_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String all_contacts = "";
                        for (int i = 0; i < contacts.length; i++) {
                            all_contacts += contacts[i] + ";";
                        }
                        Uri smsToUri = Uri.parse("smsto:" + all_contacts);
                        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                        String message = "hello";
                        intent.putExtra("sms_body", message);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.alert_send_sms_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void initCheckForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            sharedPreferences.edit().putBoolean("send_sms_permision_granted", false).commit();
        } else {
            sharedPreferences.edit().putBoolean("send_sms_permision_granted", true).commit();
        }
    }

    public void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sharedPreferences.edit().putBoolean("send_sms_permision_granted", true).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sharedPreferences.edit().putBoolean("send_sms_permision_granted", true).commit();
                    if (contacts != null && contacts.length > 0) {
                        showSendAlertDialog(contacts);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_selected_contacts), Toast.LENGTH_LONG).show();
                    }
                } else {
                    sharedPreferences.edit().putBoolean("send_sms_permision_granted", false).commit();
                }
                return;
            }
        }
    }
}