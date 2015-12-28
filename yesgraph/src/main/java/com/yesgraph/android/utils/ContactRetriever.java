package com.yesgraph.android.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.yesgraph.android.R;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.RegularContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marko on 18/11/15.
 */
public class ContactRetriever {
    public static ArrayList<RankedContact> readYSGContacts(Context context){

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

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

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

                        System.out.println("Email " + email + " Email Type : " + emailType);

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
                        System.out.println("phone" + phone);

                        phones.add(phone);
                    }
                    pCur.close();

                    RankedContact ysgRankedContact = new RankedContact();
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

                        list.add(ysgRankedContact);
                    }
/*
                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        System.out.println("Note " + note);
                    }
                    noteCur.close();

                    //Get Postal Address....

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, null, null, null);
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
                        String type = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                        // Do something with these....

                    }
                    addrCur.close();

                    // Get Instant Messenger.........
                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, imWhere, imWhereParams, null);
                    if (imCur.moveToFirst()) {
                        String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        String imType;
                        imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                    }
                    imCur.close();

                    // Get Organizations.........
                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    }
                    orgCur.close();*/
                }
            }
        }

        return list;
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
                    System.out.println("name : " + name + ", ID : " + id);

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

                        System.out.println("Email " + email + " Email Type : " + emailType);

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
                        System.out.println("phone" + phone);

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
/*
                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        System.out.println("Note " + note);
                    }
                    noteCur.close();

                    //Get Postal Address....

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, null, null, null);
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
                        String type = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                        // Do something with these....

                    }
                    addrCur.close();

                    // Get Instant Messenger.........
                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, imWhere, imWhereParams, null);
                    if (imCur.moveToFirst()) {
                        String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        String imType;
                        imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                    }
                    imCur.close();

                    // Get Organizations.........
                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    }
                    orgCur.close();*/
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
