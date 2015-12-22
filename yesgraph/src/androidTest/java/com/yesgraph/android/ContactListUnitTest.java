package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Source;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Source;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class ContactListUnitTest extends ApplicationTestCase<Application> {
    public ContactListUnitTest() {
        super(Application.class);
    }

    /**
     * Validate mapping data from contact list to json object
     *
     * @throws JSONException
     */
    public void testValidateYSGContactListToJsonObject() throws JSONException {

        String userID = "user123";

        ContactList ysgContactList = new ContactList();
        ysgContactList.setUseSuggestions(true);

        Source ysgSource = new Source();
        ysgContactList.setSource(ysgSource);

        ArrayList<RankedContact> entries = getRankedContacts();

        ysgContactList.setEntries(entries);

        JSONObject jsonObject = ysgContactList.toJSONObject(userID);

        boolean hasSource = jsonObject.has("source");
        assertEquals(true, hasSource);

        boolean hasType = jsonObject.getJSONObject("source").has("type");
        assertEquals(true, hasType);

        boolean hasEntries = jsonObject.has("entries");
        assertEquals(true, hasEntries);

        int expectedEntriesSize = entries.size();
        int actualEntriesSize = jsonObject.getJSONArray("entries").length();
        assertEquals(expectedEntriesSize, actualEntriesSize);

        boolean hasFilterSuggestedSeen = jsonObject.has("filter_suggested_seen");
        assertEquals(true, hasFilterSuggestedSeen);

        boolean filterSuggestedSeenIsTrue = jsonObject.getInt("filter_suggested_seen") == 1;
        assertEquals(true, filterSuggestedSeenIsTrue);

        boolean hasUserId = jsonObject.has("user_id") && jsonObject.get("user_id") != null;
        assertEquals(true, hasUserId);

    }


    /**
     * HELPER METHODS
     *
     */

    @NonNull
    private ArrayList<RankedContact> getRankedContacts() {

        ArrayList<RankedContact> entries = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            RankedContact contact = new RankedContact();
            contact.setName("John");
            contact.setEmail("jonh@hotmail.com");
            contact.setPhone("4554-6567756");

            entries.add(contact);
        }
        return entries;
    }
}