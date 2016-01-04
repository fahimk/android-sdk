package com.yesgraph.android.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.yesgraph.android.R;
import com.yesgraph.android.callbacks.IPermissionGrantedListener;
import com.yesgraph.android.callbacks.ISendSmsListener;

/**
 * Created by Klemen on 4.1.2016.
 */
public class AlertDialogsManager {

    private Activity activity;

    public AlertDialogsManager(Activity activity) {
        this.activity = activity;
    }

    public void askForSendSmsAlertDialog(int contactsLength) {

        final ISendSmsListener sendSmsListener = (ISendSmsListener) activity;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getResources().getString(R.string.alert_send_sms_title));
        alertDialogBuilder.setMessage(activity.getResources().getString(R.string.alert_send_sms_message)
                + " " + contactsLength + " " + activity.getResources().getString(R.string.alert_send_sms_contacts))
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(R.string.alert_send_sms_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendSmsListener.accept();
                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.alert_send_sms_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        activity.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void askForPermissionAlertDialog() {

        final IPermissionGrantedListener listener = (IPermissionGrantedListener) activity;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getResources().getString(R.string.alert_grant_permission_title));
        alertDialogBuilder.setMessage(activity.getResources().getString(R.string.alert_grant_permission_message))
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(R.string.sms_grant), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.grantPermission();
                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.sms_deny), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
