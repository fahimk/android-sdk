package com.yesgraph.android.utils;

import android.support.annotation.NonNull;

import com.yesgraph.android.models.RegularContact;

import java.util.ArrayList;

/**
 * Created by Klemen on 16.12.2015.
 */
public class ContactsFilterManager {

    private ArrayList<RegularContact> contacts;

    public ContactsFilterManager(ArrayList<RegularContact> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<RegularContact> getByFilterType(int filterType) throws Exception {

        if (this.contacts == null || this.contacts.size() == 0) {
            throw new Exception("Empty contacts list");
        }

        ArrayList<RegularContact> filteredContacts = null;

        if (filterType == FilterType.ONLY_PHONES.ordinal()) {
            filteredContacts = getContactsWithPhones();
        } else if (filterType == FilterType.ONLY_EMAILS.ordinal()) {
            filteredContacts = getContactsWithEmails();
        } else {
            filteredContacts = contacts;
        }

        return filteredContacts;
    }

    @NonNull
    private ArrayList<RegularContact> getContactsWithPhones() {

        ArrayList<RegularContact> contactsWithPhones = new ArrayList<>();

        for (Object contact : contacts) {
            if (contact instanceof RegularContact) {
                if (((RegularContact) contact).isPhone())
                    contactsWithPhones.add((RegularContact) contact);
            }
        }
        return contactsWithPhones;
    }

    @NonNull
    private ArrayList<RegularContact> getContactsWithEmails() {

        ArrayList<RegularContact> contactsWithEmails = new ArrayList<>();

        for (Object contact : contacts) {
            if (contact instanceof RegularContact) {
                if (((RegularContact) contact).isEmail())
                    contactsWithEmails.add((RegularContact) contact);
            }
        }
        return contactsWithEmails;
    }
}
