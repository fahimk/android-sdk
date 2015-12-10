package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.network.YSGSuggestionsShown;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class YSGSuggestionsShownUnitTest extends ApplicationTestCase<Application> {
    public YSGSuggestionsShownUnitTest() {
        super(Application.class);
    }

    /**
     * Validate generated JSONArray from suggestions ranked contacts list
     *
     * @throws Exception
     */
    public void testValidateSuggestionsJSONArray() throws Exception {

        String userID = "user123";

        ArrayList<YSGRankedContact> suggestions = getRankedContacts();

        JSONArray jsonArray = new YSGSuggestionsShown().generateArrayOfSuggestionsFromListForUser(userID, suggestions);

        int jsonLength = jsonArray.length();
        int suggestionsLength = suggestions.size();

        assertEquals(suggestionsLength, jsonLength);

        boolean hasUsedId = jsonArray.getJSONObject(0).has("user_id");
        assertEquals(true, hasUsedId);

        boolean hasSeenDateTime = jsonArray.getJSONObject(0).has("seen_at");
        assertEquals(true, hasSeenDateTime);

        boolean hasName = jsonArray.getJSONObject(0).has("name");
        assertEquals(true, hasName);

        boolean hasPhones = jsonArray.getJSONObject(0).has("phones");
        assertEquals(true, hasPhones);

        int actualPhonesCount = jsonArray.getJSONObject(0).getJSONArray("phones").length();
        int expectedPhonesCount = suggestions.get(0).phones().size();

        assertEquals(expectedPhonesCount, actualPhonesCount);

        boolean hasEmails = jsonArray.getJSONObject(0).has("emails");
        assertEquals(true, hasEmails);

        int actualEmailsCount = jsonArray.getJSONObject(0).getJSONArray("emails").length();
        int expectedEmailsCount = suggestions.get(0).emails().size();

        assertEquals(expectedEmailsCount, actualEmailsCount);

    }


    /**
     * Validate generated JSONArray from suggestions ranked contacts list without emails and phones array items
     *
     * @throws Exception
     */
    public void testValidateSuggestionsJSONWithoutPhonesAndEmails() throws Exception {

        String userID = "user123";

        ArrayList<YSGRankedContact> suggestions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            YSGRankedContact contact = new YSGRankedContact();
            contact.setName("John" + String.valueOf(i));
            suggestions.add(contact);
        }

        JSONArray jsonArray = new YSGSuggestionsShown().generateArrayOfSuggestionsFromListForUser(userID, suggestions);

        int jsonLength = jsonArray.length();
        int suggestionsLength = suggestions.size();

        assertEquals(suggestionsLength, jsonLength);

        boolean hasUsedId = jsonArray.getJSONObject(0).has("user_id");
        assertEquals(true, hasUsedId);

        boolean hasSeenDateTime = jsonArray.getJSONObject(0).has("seen_at");
        assertEquals(true, hasSeenDateTime);

        boolean hasName = jsonArray.getJSONObject(0).has("name");
        assertEquals(true, hasName);

        boolean hasPhones = jsonArray.getJSONObject(0).has("phones");
        assertEquals(true, hasPhones);

        boolean hasEmails = jsonArray.getJSONObject(0).has("emails");
        assertEquals(true, hasEmails);

    }

    @NonNull
    private ArrayList<YSGRankedContact> getRankedContacts() {

        ArrayList<YSGRankedContact> suggestions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            YSGRankedContact contact = new YSGRankedContact();
            contact.setName("John" + String.valueOf(i));

            ArrayList<String> emails = new ArrayList<>();
            emails.add("john@email.com" + String.valueOf(i));
            emails.add("john@email.com" + String.valueOf(i));

            contact.setEmails(emails);

            ArrayList<String> phones = new ArrayList<>();
            phones.add("123-456-542" + String.valueOf(i));
            phones.add("123-456-542" + String.valueOf(i));

            contact.setPhones(phones);

            suggestions.add(contact);
        }
        return suggestions;
    }
}