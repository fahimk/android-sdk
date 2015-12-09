package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.YSGContactList;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.models.YSGSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class YSGContactListUnitTest extends ApplicationTestCase<Application> {
    public YSGContactListUnitTest() {
        super(Application.class);
    }

    /**
     * Validate mapping data from contact list to json object
     *
     * @throws JSONException
     */
    public void testValidateYSGContactListToJsonObject() throws JSONException {

        String userID = "user123";

        YSGContactList ysgContactList = new YSGContactList();
        ysgContactList.setUseSuggestions(true);

        YSGSource ysgSource = new YSGSource();
        ysgContactList.setSource(ysgSource);

        ArrayList<YSGRankedContact> entries = getRankedContacts();

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

    @NonNull
    private ArrayList<YSGRankedContact> getRankedContacts() {

        ArrayList<YSGRankedContact> entries = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            YSGRankedContact contact = new YSGRankedContact();
            contact.setName("John");
            contact.setEmail("jonh@hotmail.com");
            contact.setPhone("4554-6567756");

            entries.add(contact);
        }
        return entries;
    }
}