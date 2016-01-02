package com.yesgraph.android.utils;

import android.content.Context;

import com.yesgraph.android.R;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.Source;

import java.util.ArrayList;

/**
 * Created by Klemen on 21.12.2015.
 */
public class ContactManager {


    /**
     * Read contacts from content provider and set it to YSGContactList object
     *
     * @param context context
     * @return YSGContact object
     */
    public ContactList getContactList(Context context) {

        ArrayList<RankedContact> list = ContactRetriever.readYSGContacts(context);

        ContactList contactList = new ContactList();

        Source source = new Source();
        if(new StorageKeyValueManager(context).getUserName().length()>0)
            source.setName(new StorageKeyValueManager(context).getUserName());
        if(new StorageKeyValueManager(context).getUserPhone().length()>0)
            source.setName(new StorageKeyValueManager(context).getUserPhone());
        if(new StorageKeyValueManager(context).getUserEmail().length()>0)
            source.setName(new StorageKeyValueManager(context).getUserEmail());

        contactList.setEntries(list);
        contactList.setSource(source);
        contactList.setUseSuggestions(true);

        return contactList;
    }

    /**
     * Get contacts by filter text
     *
     * @param filter   search text
     * @param contacts list of contacts
     * @return filtered list of contacts
     */
    public ArrayList<Object> getContactsByFilter(String filter, ArrayList<RegularContact> contacts) {

        ArrayList<Object> items = new ArrayList<>();

        for (RegularContact contact : contacts) {
            if (contact.getName().toLowerCase().contains(filter.toLowerCase())) {
                items.add(contact);
            }
        }

        return items;
    }

    /**
     * Get contacts sorted by alphabetically
     *
     * @param context                   context
     * @param contacts                  list of contacts
     * @param numberOfSuggestedContacts number of suggested contacts added on the top of list
     * @return list of contacts sorted by alphabetically
     */
    public ArrayList<Object> getContactsByAlphabetSections(Context context, ArrayList<RegularContact> contacts, int numberOfSuggestedContacts) {

        ArrayList<Object> items = new ArrayList<>();

        String lastSign = "";
        Integer suggestedIndex = 0;

        for (RegularContact contact : contacts) {
            if (suggestedIndex < numberOfSuggestedContacts) {
                if (suggestedIndex == 0) {
                    HeaderContact header = new HeaderContact();
                    header.setSign(context.getString(R.string.suggested));
                    items.add(header);
                }

                items.add(contact);
                suggestedIndex++;

            } else {

                String thisSign = contact.getName().length() < 0 ? "#" : contact.getName().substring(0, 1).toUpperCase();

                if (!Utility.isAlpha(thisSign)) {
                    thisSign = "#";
                }

                if (!lastSign.equals(thisSign)) {
                    HeaderContact header = new HeaderContact();
                    header.setSign(thisSign);
                    items.add(header);
                }

                items.add(contact);
                lastSign = thisSign;
            }
        }

        return items;
    }

}
