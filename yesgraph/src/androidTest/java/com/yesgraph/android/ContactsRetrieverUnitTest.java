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

    /*public void testValidateWebsiteData() {

        String url = "www.myurl.com";
        Integer homePage = 1;
        Integer blog = 2;
        Integer profile = 3;
        Integer home = 4;
        Integer work = 5;
        Integer ftp = 6;

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

        Website ftpWebsite = ContactRetriever.getWebsite(url, ftp);
        assertEquals("ftp", ftpWebsite.getType());

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
        Integer typeHome = 1;
        Integer typeWork = 2;
        Integer typeOther = 3;

        Integer protocolCustom = -1;
        Integer protocolAim = 0;
        Integer protocolMsn = 1;
        Integer protocolYahoo = 2;
        Integer protocolSkype = 3;
        Integer protocolQq = 4;
        Integer protocolGoogleTalk = 5;
        Integer protocolIcq = 6;
        Integer protocolJabber = 7;
        Integer protocolNetMeeting = 8;

        Ims ims = ContactRetriever.getIms(imsName, typeHome, protocolCustom);
        assertEquals(imsName, ims.getName());
        assertEquals("home", ims.getType());
        assertEquals("custom", ims.getProtocol());

        Ims imsAim = ContactRetriever.getIms(imsName, typeWork, protocolAim);
        assertEquals(imsName, imsAim.getName());
        assertEquals("work", imsAim.getType());
        assertEquals("aim", imsAim.getProtocol());

        Ims imsMsn = ContactRetriever.getIms(imsName, typeOther, protocolMsn);
        assertEquals(imsName, imsMsn.getName());
        assertEquals("other", imsMsn.getType());
        assertEquals("msn", imsMsn.getProtocol());

        Ims imsYahoo = ContactRetriever.getIms(imsName, typeOther, protocolYahoo);
        assertEquals(imsName, imsYahoo.getName());
        assertEquals("other", imsYahoo.getType());
        assertEquals("yahoo", imsYahoo.getProtocol());

        Ims imsSkype = ContactRetriever.getIms(imsName, typeOther, protocolSkype);
        assertEquals(imsName, imsSkype.getName());
        assertEquals("other", imsSkype.getType());
        assertEquals("skype", imsSkype.getProtocol());

        Ims imsQq = ContactRetriever.getIms(imsName, typeOther, protocolQq);
        assertEquals(imsName, imsQq.getName());
        assertEquals("other", imsQq.getType());
        assertEquals("qq", imsQq.getProtocol());

        Ims imsGTalk = ContactRetriever.getIms(imsName, typeOther, protocolGoogleTalk);
        assertEquals(imsName, imsGTalk.getName());
        assertEquals("other", imsGTalk.getType());
        assertEquals("googletalk", imsGTalk.getProtocol());

        Ims imsIcq = ContactRetriever.getIms(imsName, typeOther, protocolIcq);
        assertEquals(imsName, imsIcq.getName());
        assertEquals("other", imsIcq.getType());
        assertEquals("icq", imsIcq.getProtocol());

        Ims imsJabber = ContactRetriever.getIms(imsName, typeOther, protocolJabber);
        assertEquals(imsName, imsJabber.getName());
        assertEquals("other", imsJabber.getType());
        assertEquals("jabber", imsJabber.getProtocol());

        Ims imsNetMeeting = ContactRetriever.getIms(imsName, typeOther, protocolNetMeeting);
        assertEquals(imsName, imsNetMeeting.getName());
        assertEquals("other", imsNetMeeting.getType());
        assertEquals("netmeeting", imsNetMeeting.getProtocol());

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

*/

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