package com.yesgraph.android;

import android.support.annotation.NonNull;

import com.yesgraph.android.models.Address;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Website;

import java.util.ArrayList;

/**
 * Created by Klemen on 5.1.2016.
 */
public class TestUtils {

    @NonNull
    public Contact getContact() {

        Contact contact = new Contact();
        contact.setName("John");
        contact.setEmail("email");
        contact.setPhone("331-23-12-32");
        contact.setNickname("j-on");
        contact.setTitle("title");
        contact.setCompany("ItCom");
        contact.setIs_favorite("true");
        contact.setPinned(2);
        contact.setTimes_contacted((long) 1);
        contact.setLast_message_sent_date((long) 2);
        contact.setLast_message_received_date((long) 3);

        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@gmail.com");
        emails.add("john@email.com");
        contact.setEmails(emails);

        ArrayList<String> phones = new ArrayList<>();
        phones.add("333-456-434");
        phones.add("123-232-542");
        contact.setPhones(phones);

        Address address = new Address();
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        contact.setAddresses(addresses);

        Website website = new Website();
        ArrayList<Website> websites = new ArrayList<>();
        websites.add(website);
        contact.setWebsites(websites);

        Ims ims = new Ims();
        ArrayList<Ims> imses = new ArrayList<>();
        imses.add(ims);
        contact.setIms(imses);

        return contact;
    }

    @NonNull
    public RankedContact getRankedContact() {

        RankedContact contact = new RankedContact();
        contact.setName("John");
        contact.setEmail("email");
        contact.setPhone("331-23-12-32");
        contact.setNickname("j-on");
        contact.setTitle("title");
        contact.setCompany("ItCom");
        contact.setIs_favorite("true");
        contact.setPinned(2);
        contact.setTimes_contacted((long) 1);
        contact.setLast_message_sent_date((long) 2);
        contact.setLast_message_received_date((long) 3);

        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@gmail.com");
        emails.add("john@email.com");
        contact.setEmails(emails);

        ArrayList<String> phones = new ArrayList<>();
        phones.add("333-456-434");
        phones.add("123-232-542");
        contact.setPhones(phones);

        Address address = new Address();
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        contact.setAddresses(addresses);

        Website website = new Website();
        ArrayList<Website> websites = new ArrayList<>();
        websites.add(website);
        contact.setWebsites(websites);

        Ims ims = new Ims();
        ArrayList<Ims> imses = new ArrayList<>();
        imses.add(ims);
        contact.setIms(imses);

        return contact;
    }

    @NonNull
    public ArrayList<RankedContact> getRankedContacts() {

        ArrayList<RankedContact> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            contacts.add(getRankedContact());
        }

        return contacts;
    }

    @NonNull
    public ArrayList<Contact> getContacts() {

        ArrayList<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            contacts.add(getContact());
        }

        return contacts;
    }

}
