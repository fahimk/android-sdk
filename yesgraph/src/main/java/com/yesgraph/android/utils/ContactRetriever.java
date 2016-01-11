package com.yesgraph.android.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

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
    public static ArrayList<RankedContact> readYSGContacts(final Context context){

        //just the time to get the loader spinner going
        try {
            Thread.sleep(Long.valueOf(50));
        }
        catch (Exception e)
        {

        }

        final ArrayList<RankedContact> list=new ArrayList<>();

        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        if(res != PackageManager.PERMISSION_GRANTED)
            return list;

        /*final NotifyingAsyncQueryHandler asyncQueryHandler =
                new NotifyingAsyncQueryHandler(context, new NotifyingAsyncQueryHandler.AsyncQueryListener() {
                    @Override
                    public void onQueryComplete(int token, Object cookie, Cursor cur) {
                        try {
                            if (cur.getCount() > 0) {
                                while (cur.moveToNext()) {

                                    final String typeMime = cur.getString(cur.getColumnIndex(ContactsContract.Data.MIMETYPE));

                                    final RankedContact ysgRankedContact = new RankedContact();

                                    final ArrayList<String> emails = new ArrayList<>();
                                    final ArrayList<String> phones = new ArrayList<>();
                                    final ArrayList<Address> addresses = new ArrayList<>();
                                    final ArrayList<Website> websites = new ArrayList<>();
                                    final ArrayList<Ims> imses = new ArrayList<>();

                                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    name = name != null ? name.trim() : "";

                                    for (int i = 0; i < name.length(); i++) {
                                        if (!Utility.isAlpha(name.substring(i, i + 1)) && !Utility.isNumeric(name.substring(i, i + 1))) {
                                            name = name.substring(i + 1, name.length());
                                            i--;
                                        } else
                                            break;
                                    }

                                    if (name.length() == 0) {
                                        name = context.getString(R.string.no_contact_name);
                                    }

                                    if (typeMime.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                                        String email = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                        String emailType = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                                        emails.add(email);
                                        Log.i("NAME AND EMAIL", name + " " + email);
                                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                                        String phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                        phones.add(phone);

                                        String starred = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.STARRED));
                                        Long lastContacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
                                        Long timesContacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));

                                        if (lastContacted > 0)
                                            lastContacted = lastContacted / 1000;

                                        if (starred.equals("1"))
                                            ysgRankedContact.setIs_favorite(true);
                                        if (lastContacted > 0)
                                            ysgRankedContact.setLast_message_sent_date(lastContacted);
                                        if (timesContacted > 0)
                                            ysgRankedContact.setTimes_contacted(timesContacted);

                                        Log.i("NAME AND PHONE", name + " " + phone + " s:" + starred + " c:" + lastContacted + " t:" + timesContacted);
                                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
                                        String webUrl = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
                                        Integer webTypeCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE));

                                        String webType = "";

                                        switch (webTypeCode) {
                                            case 2:
                                                webType = "blog";
                                                break;
                                            case 6:
                                                webType = "ftp";
                                                break;
                                            case 4:
                                                webType = "home";
                                                break;
                                            case 1:
                                                webType = "homepage";
                                                break;
                                            case 7:
                                                webType = "other";
                                                break;
                                            case 3:
                                                webType = "profile";
                                                break;
                                            case 5:
                                                webType = "work";
                                                break;
                                        }

                                        Website website = new Website();
                                        if (webUrl != null && webUrl.length() > 0)
                                            website.setUrl(webUrl);
                                        if (webType != null && webType.length() > 0)
                                            website.setType(webType);

                                        websites.add(website);

                                        Log.i("NAME AND WEBSITE", name + " " + website.toJSONObject());
                                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
                                        String poBox = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                                        String street = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                                        String city = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                                        String state = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                                        String postalCode = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                                        String country = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                                        Integer typeCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                                        String type = "";

                                        switch (typeCode) {
                                            case 1:
                                                type = "home";
                                                break;
                                            case 3:
                                                type = "other";
                                                break;
                                            case 2:
                                                type = "work";
                                                break;
                                        }

                                        Address address = new Address();
                                        if (poBox != null && poBox.length() > 0)
                                            address.setPo_box(poBox);
                                        if (city != null && city.length() > 0)
                                            address.setCity(city);
                                        if (country != null && country.length() > 0)
                                            address.setCountry(country);
                                        if (postalCode != null && postalCode.length() > 0)
                                            address.setPostal_code(postalCode);
                                        if (state != null && state.length() > 0)
                                            address.setState(state);
                                        if (street != null && street.length() > 0)
                                            address.setStreet(street);
                                        if (type != null && type.length() > 0)
                                            address.setType(type);

                                        addresses.add(address);

                                        Log.i("NAME AND ADDRESS", name + " " + address.toJSONObject().toString());
                                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)) {
                                        String imName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                                        Integer imTypeCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                                        Integer imProtocolCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));

                                        String imType = "";

                                        switch (imTypeCode) {
                                            case 1:
                                                imType = "home";
                                                break;
                                            case 3:
                                                imType = "other";
                                                break;
                                            case 2:
                                                imType = "work";
                                                break;
                                        }

                                        String imProtocol = "";

                                        switch (imProtocolCode) {
                                            case 0:
                                                imProtocol = "aim";
                                                break;
                                            case -1:
                                                imProtocol = "custom";
                                                break;
                                            case 5:
                                                imProtocol = "googletalk";
                                                break;
                                            case 6:
                                                imProtocol = "icq";
                                                break;
                                            case 7:
                                                imProtocol = "jabber";
                                                break;
                                            case 1:
                                                imProtocol = "msn";
                                                break;
                                            case 8:
                                                imProtocol = "netmeeting";
                                                break;
                                            case 4:
                                                imProtocol = "qq";
                                                break;
                                            case 3:
                                                imProtocol = "skype";
                                                break;
                                            case 2:
                                                imProtocol = "yahoo";
                                                break;
                                        }

                                        Ims ims = new Ims();
                                        if (imType != null && imType.length() > 0)
                                            ims.setType(imType);
                                        if (imName != null && imName.length() > 0)
                                            ims.setName(imName);
                                        if (imProtocol != null && imProtocol.length() > 0)
                                            ims.setProtocol(imProtocol);

                                        imses.add(ims);
                                        Log.i("NAME AND IMS", name + " " + ims.toJSONObject());
                                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                                        String nicknameName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));

                                        if (nicknameName != null && nicknameName.length() > 0)
                                            ysgRankedContact.setNickname(nicknameName);

                                        Log.i("NAME AND NICKNAME", name + " " + nicknameName);
                                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                                        String company = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                                        String title = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

                                        if (company != null && company.length() > 0)
                                            ysgRankedContact.setCompany(company);
                                        if (title != null && title.length() > 0)
                                            ysgRankedContact.setTitle(title);

                                        Log.i("NAME AND COMPANY", name + " " + company);
                                    }

                                    ysgRankedContact.setName(name);
                                    if (phones.size() > 0) ysgRankedContact.setPhone(phones.get(0));
                                    if (emails.size() > 0) ysgRankedContact.setEmail(emails.get(0));
                                    if (phones.size() > 0) ysgRankedContact.setPhones(phones);
                                    if (emails.size() > 0) ysgRankedContact.setEmails(emails);
                                    if (websites.size() > 0) ysgRankedContact.setWebsites(websites);
                                    if (addresses.size() > 0)
                                        ysgRankedContact.setAddresses(addresses);
                                    if (imses.size() > 0) ysgRankedContact.setIms(imses);

                                    boolean duplicate = isDuplicateRankedContact(ysgRankedContact, list);

                                    if (!duplicate) {
                                        list.add(ysgRankedContact);
                                    }
                                }
                            }
                            cur.close();

                            if (list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    RankedContact rankedContact = list.get(i);
                                    if ((rankedContact.phones() == null || rankedContact.phones().size() == 0) && (rankedContact.emails() == null || rankedContact.emails().size() == 0)) {
                                        list.remove(i);
                                        i--;

                                    } else if ((rankedContact.phones() == null || rankedContact.phones().size() == 0) && rankedContact.emails() != null && rankedContact.emails().size() == 1 && rankedContact.name().equals(rankedContact.emails().get(0))) {
                                        list.remove(i);
                                        i--;
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        Message msg=new Message();
                        msg.obj=list;
                        callback.handleMessage(msg);
                    }
                });*/
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.STARRED,
                        ContactsContract.Contacts.LAST_TIME_CONTACTED,
                        ContactsContract.Contacts.TIMES_CONTACTED,
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.Im.DATA,
                        ContactsContract.CommonDataKinds.Im.TYPE,
                        ContactsContract.CommonDataKinds.Im.PROTOCOL,
                        ContactsContract.CommonDataKinds.Website.URL,
                        ContactsContract.CommonDataKinds.Website.TYPE,
                        ContactsContract.CommonDataKinds.Organization.DATA,
                        ContactsContract.CommonDataKinds.Organization.TITLE,
                        ContactsContract.CommonDataKinds.Nickname.NAME
                },
                ContactsContract.Data.CONTACT_ID + " in 'default_directory' and" +
                        " ("+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "' OR "+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE + "' OR "+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "' OR "+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "' OR "+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE + "' OR "+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "' OR " +
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
                        "')",
                null,
                ContactsContract.Contacts._ID+" ASC");

        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {

                    final String typeMime = cur.getString(cur.getColumnIndex(ContactsContract.Data.MIMETYPE));

                    final RankedContact ysgRankedContact = new RankedContact();

                    final ArrayList<String> emails = new ArrayList<>();
                    final ArrayList<String> phones = new ArrayList<>();
                    final ArrayList<Address> addresses = new ArrayList<>();
                    final ArrayList<Website> websites = new ArrayList<>();
                    final ArrayList<Ims> imses = new ArrayList<>();

                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    name = name != null ? name.trim() : "";

                    for (int i = 0; i < name.length(); i++) {
                        if (!Utility.isAlpha(name.substring(i, i + 1)) && !Utility.isNumeric(name.substring(i, i + 1))) {
                            name = name.substring(i + 1, name.length());
                            i--;
                        } else
                            break;
                    }

                    if (name.length() == 0) {
                        name = context.getString(R.string.no_contact_name);
                    }

                    if (typeMime.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                        String email = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        emails.add(email);
                        //Log.i("NAME AND EMAIL", name + " " + email);
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        String phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phones.add(phone);

                        String starred = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.STARRED));
                        Long lastContacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
                        Long timesContacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));

                        if (lastContacted > 0)
                            lastContacted = lastContacted / 1000;

                        if (starred.equals("1"))
                            ysgRankedContact.setIs_favorite(true);
                        if (lastContacted > 0)
                            ysgRankedContact.setLast_message_sent_date(lastContacted);
                        if (timesContacted > 0)
                            ysgRankedContact.setTimes_contacted(timesContacted);

                        //Log.i("NAME AND PHONE", name + " " + phone + " s:" + starred + " c:" + lastContacted + " t:" + timesContacted);
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
                        String webUrl = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
                        Integer webTypeCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE));

                        String webType = "";

                        switch (webTypeCode) {
                            case 2:
                                webType = "blog";
                                break;
                            case 6:
                                webType = "ftp";
                                break;
                            case 4:
                                webType = "home";
                                break;
                            case 1:
                                webType = "homepage";
                                break;
                            case 7:
                                webType = "other";
                                break;
                            case 3:
                                webType = "profile";
                                break;
                            case 5:
                                webType = "work";
                                break;
                        }

                        Website website = new Website();
                        if (webUrl != null && webUrl.length() > 0)
                            website.setUrl(webUrl);
                        if (webType != null && webType.length() > 0)
                            website.setType(webType);

                        websites.add(website);

                        //Log.i("NAME AND WEBSITE", name + " " + website.toJSONObject());
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
                        String poBox = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        String street = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postalCode = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String country = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        Integer typeCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                        String type = "";

                        switch (typeCode) {
                            case 1:
                                type = "home";
                                break;
                            case 3:
                                type = "other";
                                break;
                            case 2:
                                type = "work";
                                break;
                        }

                        Address address = new Address();
                        if (poBox != null && poBox.length() > 0)
                            address.setPo_box(poBox);
                        if (city != null && city.length() > 0)
                            address.setCity(city);
                        if (country != null && country.length() > 0)
                            address.setCountry(country);
                        if (postalCode != null && postalCode.length() > 0)
                            address.setPostal_code(postalCode);
                        if (state != null && state.length() > 0)
                            address.setState(state);
                        if (street != null && street.length() > 0)
                            address.setStreet(street);
                        if (type != null && type.length() > 0)
                            address.setType(type);

                        addresses.add(address);

                        //Log.i("NAME AND ADDRESS", name + " " + address.toJSONObject().toString());
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)) {
                        String imName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        Integer imTypeCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                        Integer imProtocolCode = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));

                        String imType = "";

                        switch (imTypeCode) {
                            case 1:
                                imType = "home";
                                break;
                            case 3:
                                imType = "other";
                                break;
                            case 2:
                                imType = "work";
                                break;
                        }

                        String imProtocol = "";

                        switch (imProtocolCode) {
                            case 0:
                                imProtocol = "aim";
                                break;
                            case -1:
                                imProtocol = "custom";
                                break;
                            case 5:
                                imProtocol = "googletalk";
                                break;
                            case 6:
                                imProtocol = "icq";
                                break;
                            case 7:
                                imProtocol = "jabber";
                                break;
                            case 1:
                                imProtocol = "msn";
                                break;
                            case 8:
                                imProtocol = "netmeeting";
                                break;
                            case 4:
                                imProtocol = "qq";
                                break;
                            case 3:
                                imProtocol = "skype";
                                break;
                            case 2:
                                imProtocol = "yahoo";
                                break;
                        }

                        Ims ims = new Ims();
                        if (imType != null && imType.length() > 0)
                            ims.setType(imType);
                        if (imName != null && imName.length() > 0)
                            ims.setName(imName);
                        if (imProtocol != null && imProtocol.length() > 0)
                            ims.setProtocol(imProtocol);

                        imses.add(ims);
                        //Log.i("NAME AND IMS", name + " " + ims.toJSONObject());
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                        String nicknameName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));

                        if (nicknameName != null && nicknameName.length() > 0 /*&& !nicknameName.equals("null")*/)
                            ysgRankedContact.setNickname(nicknameName);

                        //Log.i("NAME AND NICKNAME", name + " " + nicknameName);
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                        String company = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

                        if (company != null && company.length() > 0)
                            ysgRankedContact.setCompany(company);
                        if (title != null && title.length() > 0)
                            ysgRankedContact.setTitle(title);

                        //Log.i("NAME AND COMPANY", name + " " + company);
                    }

                    ysgRankedContact.setName(name);
                    if (phones.size() > 0) ysgRankedContact.setPhone(phones.get(0));
                    if (emails.size() > 0) ysgRankedContact.setEmail(emails.get(0));
                    if (phones.size() > 0) ysgRankedContact.setPhones(phones);
                    if (emails.size() > 0) ysgRankedContact.setEmails(emails);
                    if (websites.size() > 0) ysgRankedContact.setWebsites(websites);
                    if (addresses.size() > 0)
                        ysgRankedContact.setAddresses(addresses);
                    if (imses.size() > 0) ysgRankedContact.setIms(imses);

                    boolean duplicate = isDuplicateRankedContact(ysgRankedContact, list);

                    if (!duplicate) {
                        list.add(ysgRankedContact);
                    }
                }
            }
            cur.close();

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    RankedContact rankedContact = list.get(i);
                    if ((rankedContact.phones() == null || rankedContact.phones().size() == 0) && (rankedContact.emails() == null || rankedContact.emails().size() == 0)) {
                        list.remove(i);
                        i--;

                    } else if ((rankedContact.phones() == null || rankedContact.phones().size() == 0) && rankedContact.emails() != null && rankedContact.emails().size() == 1 && rankedContact.name().equals(rankedContact.emails().get(0))) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //Log.i("SIZE 1", list.size() + "");
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

                if (currentContact.phones()!=null && currentContact.phones().size() > 0) {
                    for (String thisContact : currentContact.phones()) {
                        if (oldYsgRankedContact.phones() == null)
                            oldYsgRankedContact.setPhones(new ArrayList<String>());

                        oldYsgRankedContact.phones().add(thisContact);
                    }
                }
                if (currentContact.phones()!=null && currentContact.phones().size() > 0 && (oldYsgRankedContact.phone() == null || oldYsgRankedContact.phone().length() == 0)) {
                    oldYsgRankedContact.setPhone(currentContact.phones().get(0));
                }

                if (currentContact.emails()!=null && currentContact.emails().size() > 0) {
                    for (String thisContact : currentContact.emails()) {
                        if (oldYsgRankedContact.emails() == null)
                            oldYsgRankedContact.setEmails(new ArrayList<String>());

                        oldYsgRankedContact.emails().add(thisContact);
                    }
                }
                if (currentContact.emails()!=null && currentContact.emails().size() > 0 && (oldYsgRankedContact.email() == null || oldYsgRankedContact.email().length() == 0)) {
                    oldYsgRankedContact.setEmail(currentContact.emails().get(0));
                }

                if (currentContact.getAddresses()!=null && currentContact.getAddresses().size() > 0) {
                    for (Address thisContact : currentContact.getAddresses()) {
                        if (oldYsgRankedContact.getAddresses() == null)
                            oldYsgRankedContact.setAddresses(new ArrayList<Address>());

                        oldYsgRankedContact.getAddresses().add(thisContact);
                    }
                }

                if (currentContact.getWebsites()!=null && currentContact.getWebsites().size() > 0) {
                    for (Website thisContact : currentContact.getWebsites()) {
                        if (oldYsgRankedContact.getWebsites() == null)
                            oldYsgRankedContact.setWebsites(new ArrayList<Website>());

                        oldYsgRankedContact.getWebsites().add(thisContact);
                    }
                }

                if (currentContact.getIms()!=null && currentContact.getIms().size() > 0) {
                    for (Ims thisContact : currentContact.getIms()) {
                        if (oldYsgRankedContact.getIms() == null)
                            oldYsgRankedContact.setIms(new ArrayList<Ims>());

                        oldYsgRankedContact.getIms().add(thisContact);
                    }
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
        Cursor cur = cr.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                ContactsContract.Data.CONTACT_ID + " in 'default_directory' and" +
                        " ("+
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "' OR " +
                        ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
                        "')",
                null,
                ContactsContract.Contacts._ID+" ASC");

        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {

                    final String typeMime = cur.getString(cur.getColumnIndex(ContactsContract.Data.MIMETYPE));

                    final RegularContact newContact=new RegularContact();

                    final ArrayList<String> emails = new ArrayList<>();
                    final ArrayList<String> phones = new ArrayList<>();

                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    name = name != null ? name.trim() : "";

                    for (int i = 0; i < name.length(); i++) {
                        if (!Utility.isAlpha(name.substring(i, i + 1)) && !Utility.isNumeric(name.substring(i, i + 1))) {
                            name = name.substring(i + 1, name.length());
                            i--;
                        } else
                            break;
                    }

                    if (name.length() == 0) {
                        name = context.getString(R.string.no_contact_name);
                    }

                    if (typeMime.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                        String email = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        emails.add(email);
                        //Log.i("NAME AND EMAIL", name + " " + email);
                    } else if (typeMime.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        String phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phones.add(phone);
                    }

                    newContact.setName(name);

                    boolean duplicate = isDuplicateContact(list, emails, phones, newContact);

                    if(!duplicate)
                    {
                        if (phones.size() > 0) newContact.setPhone(phones.get(0));
                        if (emails.size() > 0) newContact.setEmail(emails.get(0));

                        list.add(newContact);
                    }
                }
            }
            cur.close();

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    RegularContact regularContact = list.get(i);
                    if (regularContact.getContact() == null || regularContact.getContact().length() == 0) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
