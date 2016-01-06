package com.yesgraph.android.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.yesgraph.android.R;
import com.yesgraph.android.models.Address;
import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.Website;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marko on 18/11/15.
 */
public class ContactRetriever {
    public static ArrayList<RankedContact> readYSGContacts(Context context){

        //just the time to get the loader spinner going
        try {
            Thread.sleep(Long.valueOf(50));
        }
        catch (Exception e)
        {

        }

        ArrayList<RankedContact> list=new ArrayList<>();

        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        if(res != PackageManager.PERMISSION_GRANTED)
            return list;

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);


        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                //just for testing ui thread hijacking
                /*try {
                    Thread.sleep(Long.valueOf(100));
                }
                catch (Exception e)
                {

                }*/

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String starred = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.STARRED));
                Long lastContacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
                Long timesContacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));
                //Integer pinned = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.PINNED));
                name = name!=null ? name.trim() : "";

                for(int i=0;i<name.length();i++)
                {
                    if(!Utility.isAlpha(name.substring(i, i + 1)) && !Utility.isNumeric(name.substring(i, i + 1)))
                    {
                        name = name.substring(i+1,name.length());
                        i--;
                    }
                    else
                        break;
                }

                if(name.length()==0)
                {
                    name=context.getString(R.string.no_contact_name);
                }


                ArrayList<String> emails=new ArrayList<>();
                ArrayList<String> phones=new ArrayList<>();

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    // get email and type
                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        emails.add(email);
                    }
                    emailCur.close();

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phones.add(phone);
                    }
                    pCur.close();

                    //Get Postal Address....
                    ArrayList<Address> addresses = getPostalAddresses(cr, id);

                    // Get Instant Messenger.........
                    ArrayList<Ims> imses=getInstantMessenger(cr, id);

                    // Get Organizations.........
                    RankedContact ysgRankedContact = getOrganizations(cr, id);

                    // Get nickname.........
                    String nicknameWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] nicknameWhereParams = new String[] {id, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE};
                    Cursor nicknameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, nicknameWhere, nicknameWhereParams, null);
                    while (nicknameCur.moveToNext()) {
                        String nicknameName = nicknameCur.getString(nicknameCur.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));

                        if(nicknameName!=null && nicknameName.length()>0)
                            ysgRankedContact.setNickname(nicknameName);
                    }
                    nicknameCur.close();

                    // Get Websites.........
                    ArrayList<Website> websites=getWebsites(cr, id);

                    // Get Pinned.........
                    /*String pinnedWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] pinnedWhereParams = new String[]{id,
                            String.valueOf(ContactsContract.PinnedPositions.DEMOTED)};
                    Cursor pinnedCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, pinnedWhere, pinnedWhereParams, null);
                    if (pinnedCur.moveToFirst()) {
                        //String pinned = pinnedCur.getString(orgCur.getColumnIndex(ContactsContract.PinnedPositions.));

                        //ysgRankedContact.setPinned(pinned.equals("1") ? "true" : "false");
                    }
                    webCur.close();*/

                    ysgRankedContact.setName(name);

                    RankedContact currentContact = new RankedContact();
                    currentContact.setName(name);
                    currentContact.setEmails(emails);
                    currentContact.setPhones(phones);

                    boolean duplicate = isDuplicateRankedContact(currentContact, list);

                    if(!duplicate)
                    {
                        if (phones.size() > 0) ysgRankedContact.setPhones(phones);
                        if (emails.size() > 0) ysgRankedContact.setEmails(emails);
                        if (phones.size() > 0) ysgRankedContact.setPhone(phones.get(0));
                        if (emails.size() > 0) ysgRankedContact.setEmail(emails.get(0));
                        if (starred.equals("1")) ysgRankedContact.setIs_favorite("true");
                        if (lastContacted > 0) ysgRankedContact.setLast_message_sent_date(lastContacted);
                        if (timesContacted > 0) ysgRankedContact.setTimes_contacted(timesContacted);
                        if (websites.size() > 0) ysgRankedContact.setWebsites(websites);
                        if (addresses.size() > 0) ysgRankedContact.setAddresses(addresses);
                        if (imses.size() > 0) ysgRankedContact.setIms(imses);

                        list.add(ysgRankedContact);
                    }
                }
            }
        }
        return list;
    }

    private static ArrayList<Website> getWebsites(ContentResolver cr, String id) {

        ArrayList<Website> websites=new ArrayList<>();

        String webWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] webWhereParams = new String[]{id,
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE};
        Cursor webCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null, webWhere, webWhereParams, null);
        if (webCur.moveToFirst()) {
            String webUrl = webCur.getString(webCur.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
            Integer webTypeCode = webCur.getInt(webCur.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE));

            Website website = getWebsite(webUrl, webTypeCode);

            websites.add(website);
        }
        webCur.close();

        return websites;
    }

    private static RankedContact getOrganizations(ContentResolver cr, String id) {

        RankedContact ysgRankedContact = new RankedContact();

        String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{id,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
        Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null);
        if (orgCur.moveToFirst()) {
            String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
            String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

            setCompanyAndTitle(ysgRankedContact, company, title);
        }
        orgCur.close();

        return ysgRankedContact;
    }

    private static ArrayList<Ims> getInstantMessenger(ContentResolver cr, String id) {

        ArrayList<Ims> imses = new ArrayList<>();

        String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] imWhereParams = new String[]{id,
                ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
        Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null, imWhere, imWhereParams, null);
        if (imCur.moveToFirst()) {
            String imName = imCur.getString(
                    imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
            Integer imTypeCode = imCur.getInt(
                    imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
            Integer imProtocolCode = imCur.getInt(
                    imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));

            Ims ims = getIms(imName, imTypeCode, imProtocolCode);

            imses.add(ims);
        }
        imCur.close();

        return imses;
    }

    private static ArrayList<Address> getPostalAddresses(ContentResolver cr, String id) {

        ArrayList<Address> addresses = new ArrayList<>();

        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] addrWhereParams = new String[]{id,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
        Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null, addrWhere, addrWhereParams, null);

        while(addrCur.moveToNext()) {
            String poBox = addrCur.getString(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
            String street = addrCur.getString(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            String city = addrCur.getString(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String state = addrCur.getString(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
            String postalCode = addrCur.getString(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
            String country = addrCur.getString(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
            Integer typeCode = addrCur.getInt(
                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

            Address address = getAddress(poBox, street, city, state, postalCode, country, typeCode);

            addresses.add(address);
        }
        addrCur.close();

        return addresses;
    }

    @NonNull
    public static Website getWebsite(String webUrl, Integer webTypeCode) {
        String webType="";

        switch(webTypeCode)
        {
            case 2: webType="blog";
                break;
            case 6: webType="ftp";
                break;
            case 4: webType="home";
                break;
            case 1: webType="homepage";
                break;
            case 7: webType="other";
                break;
            case 3: webType="profile";
                break;
            case 5: webType="work";
                break;
        }

        Website website=new Website();
        if(webUrl!=null && webUrl.length()>0)
        website.setUrl(webUrl);
        if(webType!=null && webType.length()>0)
        website.setType(webType);
        return website;
    }

    public static void setCompanyAndTitle(RankedContact ysgRankedContact, String company, String title) {
        if(company!=null && company.length()>0)
            ysgRankedContact.setCompany(company);
        if(title!=null && title.length()>0)
            ysgRankedContact.setTitle(title);
    }

    @NonNull
    public static Ims getIms(String imName, Integer imTypeCode, Integer imProtocolCode) {
        String imType="";

        switch(imTypeCode)
        {
            case 1: imType="home";
                break;
            case 3: imType="other";
                break;
            case 2: imType="work";
                break;
        }

        String imProtocol="";

        switch(imProtocolCode)
        {
            case 0: imProtocol="aim";
                break;
            case -1: imProtocol="custom";
                break;
            case 5: imProtocol="googletalk";
                break;
            case 6: imProtocol="icq";
                break;
            case 7: imProtocol="jabber";
            break;
            case 1: imProtocol="msn";
            break;
            case 8: imProtocol="netmeeting";
            break;
            case 4: imProtocol="qq";
            break;
            case 3: imProtocol="skype";
            break;
            case 2: imProtocol="yahoo";
            break;
        }

        Ims ims=new Ims();
        if(imType!=null && imType.length()>0)
            ims.setType(imType);
        if(imName!=null && imName.length()>0)
            ims.setName(imName);
        if(imProtocol!=null && imProtocol.length()>0)
            ims.setProtocol(imProtocol);
        return ims;
    }

    @NonNull
    public static Address getAddress(String poBox, String street, String city, String state, String postalCode, String country, Integer typeCode) {
        String type="";

        switch(typeCode)
        {
            case 1: type="home";
                break;
            case 3: type="other";
                break;
            case 2: type="work";
                break;
        }

        Address address=new Address();
        if(poBox!=null && poBox.length()>0)
            address.setPo_box(poBox);
        if(city!=null && city.length()>0)
            address.setCity(city);
        if(country!=null && country.length()>0)
            address.setCountry(country);
        if(postalCode!=null && postalCode.length()>0)
            address.setPostal_code(postalCode);
        if(state!=null && state.length()>0)
            address.setState(state);
        if(street!=null && street.length()>0)
            address.setStreet(street);
        if(type!=null && type.length()>0)
            address.setType(type);
        return address;
    }

    /**
     * Check if contact existed int the list then update it
     * @param currentContact current contact data
     * @param list list of contacts
     * @return true if contact existed otherwise return false
     */
    public static boolean isDuplicateRankedContact(RankedContact currentContact, ArrayList<RankedContact> list) {

        boolean duplicate = false;

        for (RankedContact oldYsgRankedContact : list) {
            if (currentContact.name().equals(oldYsgRankedContact.name())) {
                duplicate = true;

                if (currentContact.phones().size() > 0) {
                    for (String thisContact : currentContact.phones()) {
                        if (oldYsgRankedContact.phones() == null)
                            oldYsgRankedContact.setPhones(new ArrayList<String>());

                        oldYsgRankedContact.phones().add(thisContact);
                    }
                }
                if (currentContact.emails().size() > 0) {
                    for (String thisContact : currentContact.emails()) {
                        if (oldYsgRankedContact.emails() == null)
                            oldYsgRankedContact.setEmails(new ArrayList<String>());

                        oldYsgRankedContact.emails().add(thisContact);
                    }
                }
                if (currentContact.phones().size() > 0 && (oldYsgRankedContact.phone() == null || oldYsgRankedContact.phone().length() == 0)) {
                    oldYsgRankedContact.setPhone(currentContact.phones().get(0));
                }
                if (currentContact.emails().size() > 0 && (oldYsgRankedContact.email() == null || oldYsgRankedContact.email().length() == 0)) {
                    oldYsgRankedContact.setEmail(currentContact.emails().get(0));
                }

                break;
            }
        }
        return duplicate;
    }

    public static ArrayList<RegularContact> readContacts(Context context){

        ArrayList<RegularContact> list=new ArrayList<>();

        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        if(res != PackageManager.PERMISSION_GRANTED)
            return list;

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                name = name!=null ? name.trim() : "";

                for(int i=0;i<name.length();i++)
                {
                    if(!Utility.isAlpha(name.substring(i, i + 1)) && !Utility.isNumeric(name.substring(i, i + 1)))
                    {
                        name = name.substring(i+1,name.length());
                        i--;
                    }
                    else
                        break;
                }

                if(name.length()==0)
                {
                    name=context.getString(R.string.no_contact_name);
                }

                ArrayList<String> emails=new ArrayList<>();
                ArrayList<String> phones=new ArrayList<>();

                RegularContact newContact=new RegularContact();
                newContact.setName(name);

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    // get email and type
                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        emails.add(email);
                    }
                    emailCur.close();

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phones.add(phone);
                    }
                    pCur.close();

                    RegularContact regularContact = new RegularContact();
                    regularContact.setName(name);

                    boolean duplicate = isDuplicateContact(list, emails, phones, regularContact);

                    if(!duplicate)
                    {
                        if (phones.size() > 0) regularContact.setPhone(phones.get(0));
                        if (emails.size() > 0) regularContact.setEmail(emails.get(0));

                        list.add(regularContact);
                    }
                }
            }
        }

        Collections.sort(list, new Comparator<RegularContact>() {
            public int compare(RegularContact s1, RegularContact s2) {

                if(Utility.isAlpha(s1.getName().substring(0, 1)) && !Utility.isAlpha(s2.getName().substring(0, 1)))
                    return -s1.getName().compareToIgnoreCase(s2.getName());;

                if(Utility.isAlpha(s2.getName().substring(0, 1)) && !Utility.isAlpha(s1.getName().substring(0, 1)))
                    return -s1.getName().compareToIgnoreCase(s2.getName());;

                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        return list;
    }

    public static boolean isDuplicateContact(ArrayList<RegularContact> list, ArrayList<String> emails, ArrayList<String> phones, RegularContact regularContact) {

        boolean duplicate=false;

        for(RegularContact oldContact : list)
        {
            if(regularContact.getName().equals(oldContact.getName()))
            {
                duplicate=true;

                if(phones.size()>0)
                {
                    for(String thisContact : phones)
                    {
                        oldContact.setPhone(thisContact);
                    }
                }
                if(emails.size()>0)
                {
                    for(String thisContact : emails)
                    {
                        oldContact.setEmail(thisContact);
                    }
                }

                break;
            }
        }
        return duplicate;
    }

    /**
     * Check if any contact is checked
     * @param items contacts
     * @return true if is checked
     */
    public boolean isContactChecked(ArrayList<Object> items) {

        if(items != null) {
            for (Object contact : items) {
                if (contact instanceof RegularContact) {
                    if (((RegularContact) contact).getSelected())
                        return true;
                }
            }
        }
        return false;
    }

}
