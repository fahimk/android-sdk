package com.yesgraph.android;

import android.support.annotation.NonNull;

import com.yesgraph.android.models.Address;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Website;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        contact.setIs_favorite(true);
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
        contact.setIs_favorite(true);
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
    public ArrayList<RankedContact> getRankedContacts(int size) {

        ArrayList<RankedContact> contacts = new ArrayList<>();

        for (int i = 0; i < size; i++) {
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

    @NonNull
    public JSONArray getJsonArray() throws JSONException {

        JSONArray jsonContacts = new JSONArray();

        for (int i = 0; i < 5; i++) {

            JSONObject jsonContact = new JSONObject();

            jsonContact.put("name", "John");
            jsonContact.put("phone", "123-432-533");
            jsonContact.put("email", "john@poviolabs.com");

            jsonContact.put("rank", 7 + (i + 1));
            jsonContact.put("score", 12.5);

            JSONArray emails = new JSONArray();

            JSONObject email1 = new JSONObject();
            email1.put("emails", "john@gmail.com");

            JSONObject email2 = new JSONObject();
            email2.put("emails", "john@email.com");

            emails.put(email1);
            emails.put(email2);

            jsonContact.put("emails", emails);

            JSONArray phones = new JSONArray();

            JSONObject phone1 = new JSONObject();
            email1.put("phones", "124-321-533");

            JSONObject phone2 = new JSONObject();
            email2.put("phones", "435-235-523");

            phones.put(phone1);
            phones.put(phone2);

            jsonContact.put("phones", phones);

            jsonContacts.put(jsonContact);

        }

        return jsonContacts;
    }



    @NonNull
    public Website getWebsite() {
        Website website = new Website();

        website.setType("news");
        website.setUrl("www.newsurl.com");
        return website;
    }

    @NonNull
    public Address getAddress() {

        Address address = new Address();

        address.setPo_box("po_box");
        address.setStreet("street");
        address.setState("state");
        address.setCity("city");
        address.setPostal_code("postalCode");
        address.setCountry("country");
        address.setType("type");
        return address;
    }

    @NonNull
    public Ims getIms() {
        Ims ims = new Ims();

        ims.setName("ims name");
        ims.setProtocol("protocol x");
        ims.setType("ims type");
        return ims;
    }

}
