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
    private Toolbar toolbar;
    private PendingIntent piSent, piDelivered;
    private String[] contacts;
    private String message;
    private Intent intent;
    private BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    private SmsManager sms;
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

        piSent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
        piDelivered = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
        sms = SmsManager.getDefault();

        Button btn = (Button) findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms(contacts);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initSmsSendReceiver();
        initSmsDeliveredReceiver();
        registerReceiver(smsSentReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(smsDeliveredReceiver, new IntentFilter("SMS_DELIVERED"));
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    private void sendSms(final String[] contacts) {
        if(contacts != null && contacts.length > 0) {
            if (YesGraph.isMarshmallow()) {
                checkForPermissions();
                if(sharedPreferences.getBoolean("send_sms_permision_granted", false)) {
                    showAlertDialog(contacts);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.enable_permissions), Toast.LENGTH_LONG).show();
                }
            } else {
                showAlertDialog(contacts);
            }
        }
    }

    private void showAlertDialog(final String[] contacts){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.alert_send_sms_title));
        // set dialog message
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.alert_send_sms_message)
                + " " + contacts.length + " " + context.getResources().getString(R.string.alert_send_sms_contacts))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.alert_send_sms_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < contacts.length; i++) {
                            sms.sendTextMessage(contacts[i], null, message, piSent, piDelivered);
                        }
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.alert_send_sms_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void initSmsSendReceiver(){
        smsSentReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS has been sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio Off", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void  initSmsDeliveredReceiver(){
        smsDeliveredReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    public void checkForPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
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
                    if(contacts != null && contacts.length > 0) {
                        showAlertDialog(contacts);
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
