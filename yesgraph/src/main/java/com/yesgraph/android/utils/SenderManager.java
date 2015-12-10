package com.yesgraph.android.utils;

import android.content.Context;
import android.content.Intent;

import com.yesgraph.android.activity.SendEmailActivity;
import com.yesgraph.android.activity.SendSmsActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGContact;
import com.yesgraph.android.models.YSGRankedContact;

import java.util.ArrayList;

/**
 * Created by Klemen on 10.12.2015.
 */
public class SenderManager {

    private ArrayList<YSGContact> invitedContacts;
    private ArrayList<Object> items;


    public SenderManager(ArrayList<Object> items) {
        this.items = items;
        this.invitedContacts = new ArrayList<>();
    }

    /**
     * Get emails from checked contacts
     *
     * @return array of emails
     */
    public String[] getEmails() {

        ArrayList<RegularContact> checkedEmails = getCheckedEmailContacts();

        String[] stringEmails = null;

        if (checkedEmails.size() > 0) {

            stringEmails = new String[checkedEmails.size()];

            for (int x = 0; x < checkedEmails.size(); x++) {
                stringEmails[x] = checkedEmails.get(x).getContact();
                YSGRankedContact ysgRankedContact = new YSGRankedContact();
                ysgRankedContact.setName(checkedEmails.get(x).getName());
                ysgRankedContact.setEmail(checkedEmails.get(x).getContact());
                invitedContacts.add(ysgRankedContact);
            }
        }

        return stringEmails;
    }

    /**
     * Send email with contacts emails
     *
     * @param context     context
     * @param application application
     */
    public void sendEmail(Context context, YesGraph application) {

        String[] stringEmails = getEmails();

        if (stringEmails != null && stringEmails.length > 0) {

            Intent intent = new Intent(context, SendEmailActivity.class);
            intent.putExtra("contacts", stringEmails);
            intent.putExtra("subject", application.getEmailSubject());
            intent.putExtra("message", application.getEmailText());
            context.startActivity(intent);
        }
    }

    /**
     * Get checked contact with email
     *
     * @return list of checked contact
     */
    public ArrayList<RegularContact> getCheckedEmailContacts() {

        ArrayList<RegularContact> checkedEmails = new ArrayList<>();

        for (Object contact : items) {
            if (contact instanceof RegularContact) {
                if (((RegularContact) contact).getSelected() && ((RegularContact) contact).getContact().contains("@"))
                    checkedEmails.add((RegularContact) contact);
            }
        }

        return checkedEmails;
    }


    /**
     * Get phones from checked contacts
     *
     * @return array of phones
     */
    public String[] getPhones() {

        ArrayList<RegularContact> checkedPhones = getCheckedPhoneContacts();

        String[] stringPhones = null;

        if (checkedPhones.size() > 0) {
            stringPhones = new String[checkedPhones.size()];

            for (int x = 0; x < checkedPhones.size(); x++) {
                stringPhones[x] = checkedPhones.get(x).getContact();
                YSGRankedContact ysgRankedContact = new YSGRankedContact();
                ysgRankedContact.setName(checkedPhones.get(x).getName());
                ysgRankedContact.setPhone(checkedPhones.get(x).getContact());
                invitedContacts.add(ysgRankedContact);
            }
        }

        return stringPhones;
    }

    /**
     * Send sms to checked contacts
     *
     * @param context     context
     * @param application application
     */
    public void sendSms(Context context, YesGraph application) {

        String[] stringPhones = getPhones();

        if (stringPhones != null && stringPhones.length > 0) {

            Intent intent = new Intent(context, SendSmsActivity.class);
            intent.putExtra("contacts", stringPhones);
            intent.putExtra("message", application.getSmsText());
            context.startActivity(intent);

        }
    }


    /**
     * Get checked contact
     *
     * @return list of checked contact
     */
    public ArrayList<RegularContact> getCheckedPhoneContacts() {

        ArrayList<RegularContact> checkedPhones = new ArrayList<>();

        for (Object contact : items) {
            if (contact instanceof RegularContact) {
                if (((RegularContact) contact).getSelected() && !((RegularContact) contact).getContact().contains("@"))
                    checkedPhones.add((RegularContact) contact);
            }
        }
        return checkedPhones;
    }

    public ArrayList<YSGContact> getInvitedContacts() {
        return invitedContacts;
    }
}
