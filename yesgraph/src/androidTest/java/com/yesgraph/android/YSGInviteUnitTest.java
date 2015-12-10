package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.YSGContact;
import com.yesgraph.android.network.YSGInvite;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class YSGInviteUnitTest extends ApplicationTestCase<Application> {
    public YSGInviteUnitTest() {
        super(Application.class);
    }

    /**
     * Validate generated JSON array from invite contacts list
     *
     * @throws Exception
     */
    public void testValidateJSONArrayFromInviteContactsList() throws Exception {

        String userID = "user123";

        ArrayList<YSGContact> suggestions = getContacts();

        JSONArray jsonArray = new YSGInvite().generateArrayOfInviteesFromListForUser(userID, suggestions);

        int jsonLength = jsonArray.length();
        int suggestionsLength = suggestions.size();

        assertEquals(suggestionsLength, jsonLength);

        boolean hasUsedId = jsonArray.getJSONObject(0).has("user_id");
        assertEquals(true, hasUsedId);

        boolean hasSentDateTime = jsonArray.getJSONObject(0).has("sent_at");
        assertEquals(true, hasSentDateTime);

        boolean hasInviteName = jsonArray.getJSONObject(0).has("invitee_name");
        assertEquals(true, hasInviteName);

        boolean hasPhone = jsonArray.getJSONObject(0).has("phone");
        assertEquals(true, hasPhone);

        boolean hasEmail = jsonArray.getJSONObject(0).has("email");
        assertEquals(true, hasEmail);

    }


    /**
     * Validate generated JSON array from invite contacts list with missing data
     *
     * @throws Exception
     */
    public void testValidateJSONArrayFromInviteContactListWithMissingData() throws Exception {

        String userID = "user123";

        ArrayList<YSGContact> suggestions = getContactsWithMissingData();

        JSONArray jsonArray = new YSGInvite().generateArrayOfInviteesFromListForUser(userID, suggestions);

        int jsonLength = jsonArray.length();
        int suggestionsLength = suggestions.size();

        assertEquals(suggestionsLength, jsonLength);

        boolean hasUsedId = jsonArray.getJSONObject(0).has("user_id");
        assertEquals(true, hasUsedId);

        boolean hasSentDateTime = jsonArray.getJSONObject(0).has("sent_at");
        assertEquals(true, hasSentDateTime);

        boolean hasInviteName = jsonArray.getJSONObject(0).has("invitee_name");
        assertEquals(false, hasInviteName);

        boolean hasPhone = jsonArray.getJSONObject(1).has("phone");
        assertEquals(false, hasPhone);

        boolean hasEmail = jsonArray.getJSONObject(2).has("email");
        assertEquals(false, hasEmail);

    }


    @NonNull
    private ArrayList<YSGContact> getContacts() {

        ArrayList<YSGContact> suggestions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            YSGContact contact = new YSGContact();
            contact.setName("John" + String.valueOf(i));
            contact.setPhone("123-432-453");
            contact.setEmail("john@email.com");

            suggestions.add(contact);
        }
        return suggestions;
    }


    @NonNull
    private ArrayList<YSGContact> getContactsWithMissingData() {
        ArrayList<YSGContact> suggestions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            YSGContact contact = new YSGContact();

            if (i != 0) {
                contact.setName("John" + String.valueOf(i));
            }
            if (i != 1) {
                contact.setPhone("123-432-453");
            }
            if (i != 2) {
                contact.setEmail("john@email.com");
            }

            suggestions.add(contact);
        }
        return suggestions;
    }
}