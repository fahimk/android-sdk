package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import com.yesgraph.android.models.Address;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Website;
import com.yesgraph.android.utils.ContactRetriever;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class ContactsRetrieverUnitTest extends ApplicationTestCase<Application> {
    public ContactsRetrieverUnitTest() {
        super(Application.class);
    }

    /**
     * Check if YSGContact name is empty
     */
    public void testCheckRetrieverYSGContactsData() {

        ArrayList<RankedContact> contacts = ContactRetriever.readYSGContacts(getContext());

        boolean contactNameIsEmpty = false;

        int contactsCount = contacts.size();

        if (contactsCount > 0) {

            for (int i = 0; i < contacts.size(); i++) {

                String contactName = contacts.get(i).name();

                if (TextUtils.isEmpty(contactName)) {
                    contactNameIsEmpty = true;
                }
            }
        }

        assertEquals(false, contactNameIsEmpty);

    }

    /**
     * Check if contact name is empty
     */
    public void testCheckRetrieverContactsData() {

        ArrayList<RegularContact> contacts = ContactRetriever.readContacts(getContext());

        boolean contactNameIsEmpty = false;

        int contactsCount = contacts.size();

        if (contactsCount > 0) {

            for (int i = 0; i < contacts.size(); i++) {

                String contactName = contacts.get(i).getName();

                if (TextUtils.isEmpty(contactName)) {
                    contactNameIsEmpty = true;
                }
            }
        }

        assertEquals(false, contactNameIsEmpty);
    }

    /**
     * Check if any of contact selected
     */
    public void testCheckAnyOfContactSelected() {

        ArrayList<Object> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");

            if (i == 1) {
                contact.setSelected(true);
            }
            contacts.add(contact);
        }

        boolean isSelected = new ContactRetriever().isContactChecked(contacts);

        assertEquals(true, isSelected);

    }

    /**
     * Check if all of contact are unselected
     */
    public void testCheckNonContactsSelected() {

        ArrayList<Object> contacts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            RegularContact contact = new RegularContact();
            contact.setName("John");
            contacts.add(contact);
        }

        boolean isNotSelected = !new ContactRetriever().isContactChecked(contacts);

        assertEquals(true, isNotSelected);

    }

    /**
     * Check if ranked contact already exists in the list of contacts, then update it
     */
    public void testCheckDuplicateContactAndUpdateData() {

        ArrayList<RankedContact> list = getRankedContacts(5);

        RankedContact contact = new RankedContact();
        contact.setName("John");

        ArrayList<String> emails = new ArrayList<>();
        emails.add("j@gmail.com");
        contact.setEmails(emails);
        ArrayList<String> phones = new ArrayList<>();
        phones.add("544-323-545");
        contact.setPhones(phones);

        boolean isDuplicated = ContactRetriever.isDuplicateRankedContact(contact, list);

        assertEquals(true, isDuplicated);
    }

    /**
     * Check if ranked contact not exists in the list of contacts
     */
    public void testCheckNoDuplicateContact() {

        ArrayList<RankedContact> list = getRankedContacts(5);

        RankedContact contact = new RankedContact();
        contact.setName("Michael");
        ArrayList<String> emails = new ArrayList<>();
        emails.add("michael@gmail.com");
        contact.setEmails(emails);
        ArrayList<String> phones = new ArrayList<>();
        phones.add("544-212-545");
        contact.setPhones(phones);

        boolean isNotDuplicated = !ContactRetriever.isDuplicateRankedContact(contact, list);

        assertEquals(true, isNotDuplicated);
    }

    /**
     * Check if regular contact already exists in the list of contacts, then update it
     */
    public void testCheckDuplicateRegularContact() {

        ArrayList<RegularContact> list = getRegularContact(5);

        RegularContact contact = new RegularContact();
        contact.setName("John");
        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@newEmail.com");
        ArrayList<String> phones = new ArrayList<>();
        phones.add("111-222-333");

        boolean isDuplicated = ContactRetriever.isDuplicateContact(list, emails, phones, contact);

        assertEquals(true, isDuplicated);
    }

    /**
     * Check if ranked contact not exists in the list of contacts
     */
    public void testCheckNoDuplicateRegularContact() {

        ArrayList<RegularContact> list = getRegularContact(5);

        RegularContact contact = new RegularContact();
        contact.setName("Michael");

        ArrayList<String> emails = new ArrayList<>();
        emails.add("michael@gmail.com");

        ArrayList<String> phones = new ArrayList<>();
        phones.add("544-212-545");

        boolean isNotDuplicated = !ContactRetriever.isDuplicateContact(list, emails, phones, contact);

        assertEquals(true, isNotDuplicated);
    }

    public void testValidateWebsiteData() {

        String url = "www.myurl.com";
        Integer homePage = 1;
        Integer blog = 2;
        Integer profile = 3;
        Integer home = 4;
        Integer work = 5;

        Website homePageWebsite = ContactRetriever.getWebsite(url, homePage);
        assertEquals(url, homePageWebsite.getUrl());
        assertEquals("homepage", homePageWebsite.getType());

        Website blogWebSite = ContactRetriever.getWebsite(url, blog);
        assertEquals("blog", blogWebSite.getType());

        Website profileWebSite = ContactRetriever.getWebsite(url, profile);
        assertEquals("profile", profileWebSite.getType());

        Website homeWebSite = ContactRetriever.getWebsite(url, home);
        assertEquals("home", homeWebSite.getType());

        Website workWebSite = ContactRetriever.getWebsite(url, work);
        assertEquals("work", workWebSite.getType());

    }

    public void testValidateCompanyNameAndTitle() {

        String company = "povioLabs";
        String title = "title";
        RankedContact rankedContact = new RankedContact();

        ContactRetriever.setCompanyAndTitle(rankedContact, company, title);

        assertEquals(company, rankedContact.getCompany());
        assertEquals(title, rankedContact.getTitle());

    }

    public void testValidateIms() {

        String imsName = "ims";
        Integer typeCode = 2;
        Integer protocolCode = 3;

        Ims ims = ContactRetriever.getIms(imsName, typeCode, protocolCode);

        assertEquals(imsName, ims.getName());
        assertEquals("work", ims.getType());
        assertEquals("skype", ims.getProtocol());

    }


    public void testValidateAddress() {

        String poBox = "poBox";
        String street = "street";
        String city = "city";
        String state = "state";
        String postalCode = "postalCode";
        String country = "country";
        Integer type = 2;

        Address address = ContactRetriever.getAddress(poBox,street,city,state,postalCode,country,type);

        assertEquals(poBox,address.getPo_box());
        assertEquals(street,address.getStreet());
        assertEquals(city,address.getCity());
        assertEquals(state,address.getState());
        assertEquals(postalCode,address.getPostal_code());
        assertEquals(country,address.getCountry());
        assertEquals("work",address.getType());


    }



    @NonNull
    private ArrayList<RankedContact> getRankedContacts(Integer count) {

        ArrayList<RankedContact> rankedContacts = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            String namePostFix = String.valueOf(i);

            RankedContact contact = new RankedContact();

            if (i == 3) {
                namePostFix = "";
            } else {
                ArrayList<String> emails = new ArrayList<>();
                emails.add("john@gmail.com");
                emails.add("john@email.com");
                contact.setEmails(emails);

                ArrayList<String> phones = new ArrayList<>();
                phones.add("333-456-434");
                phones.add("123-232-542");
                contact.setPhones(phones);
            }

            contact.setName("John" + namePostFix);
            contact.setPhone("040234252");
            contact.setEmail("jonh@hotmail.com");

            rankedContacts.add(contact);
        }
        return rankedContacts;
    }

    @NonNull
    private ArrayList<RegularContact> getRegularContact(Integer count) {

        ArrayList<RegularContact> contacts = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            String namePostFix = String.valueOf(i);

            if (i == 3) {
                namePostFix = "";
            }

            RegularContact contact = new RegularContact();

            contact.setName("John" + namePostFix);
            contact.setPhone("040234252");
            contact.setEmail("jonh@hotmail.com");

            contacts.add(contact);
        }
        return contacts;
    }


}