package com.yesgraph.android.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.callbacks.IPermissionGrantedListener;
import com.yesgraph.android.callbacks.ISendSmsListener;
import com.yesgraph.android.utils.AlertDialogsManager;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.SendSmsManager;

/**
 * Created by Dean Bozinoski on 11/13/2015.
 */
public class SendSmsActivity extends AppCompatActivity implements IPermissionGrantedListener,ISendSmsListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private Context context;
    private SendSmsManager sendSmsManager;

    private boolean isMarshmallow;


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
        isMarshmallow = getIntent().getBooleanExtra("isMarshmallow",false);

        try {
            sendSmsManager = new SendSmsManager(message, contacts);
        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.no_selected_contacts), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void checkAndroidVersionAndSendSms() {

        if (isMarshmallow) {
            new PermissionGrantedManager(this).initCheckSendSmsPermission();
        }

        if (isMarshmallow) {

            boolean isSendSmsPermissionGranted = new PermissionGrantedManager(this).isSendSmsPermission();

            if (isSendSmsPermissionGranted) {
                sendSms();
            } else {
                new AlertDialogsManager(this).askForPermissionAlertDialog();
            }
        } else {
            sendSms();
        }
    }

    private void sendSms() {

        if (isMarshmallow) {
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

    private void showSendAlertDialog() {
        if (sendSmsManager!= null) {
            new AlertDialogsManager(this).askForSendSmsAlertDialog(sendSmsManager.getContacts().length);
        }
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

    @Override
    public void grantPermission() {
        sendSms();
    }
    @Override
    public void accept() {
        startActivity(sendSmsManager.sendSmsTo());
        finish();
    }
}