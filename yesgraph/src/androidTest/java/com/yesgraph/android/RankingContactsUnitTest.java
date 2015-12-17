package com.yesgraph.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;
import android.test.mock.MockApplication;
import android.test.mock.MockContext;
import android.text.TextUtils;
import android.util.Log;

import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.utils.RankingContactsManager;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 9.12.2015.
 */
public class RankingContactsUnitTest extends ApplicationTestCase<Application> {
    public RankingContactsUnitTest() {
        super(Application.class);
    }

    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new DelegatedMockContext(getContext());
    }


    /**
     * Check if contacts data existed (name and contact)
     *
     * @throws Exception
     */
    public void testCheckContactsData() throws Exception {

        Integer rankedContactsCount = 10;
        Integer suggestedCount = 5;

        RankingContactsManager rankingContactsManager = new RankingContactsManager(mockContext);

        // set suggested and non suggested contacts
        ArrayList<YSGRankedContact> rankedContacts = getRankedContacts(rankedContactsCount, suggestedCount);

        //get suggested contacts on the top from ranked contacts
        ArrayList<RegularContact> regularContacts = rankingContactsManager.rankedContactsToRegularContacts(rankedContacts, suggestedCount, false);

        boolean emptyDataExist = false;

        for (int i = 0; i < suggestedCount; i++) {

            String name = regularContacts.get(i).getName();
            String contact = regularContacts.get(i).getContact();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contact)) {
                emptyDataExist = true;
                break;
            }
        }

        assertEquals(false, emptyDataExist);
    }

    /**
     * Check contacts with missing data (name and contact)
     *
     * @throws Exception
     */
    public void testCheckContactsWithMissingData() throws Exception {

        Integer rankedContactsCount = 10;
        Integer suggestedCount = 5;

        RankingContactsManager rankingContactsManager = new RankingContactsManager(mockContext);

        // set suggested and non suggested contacts
        ArrayList<YSGRankedContact> rankedContacts = getRankedContactsWithMissingData(rankedContactsCount, suggestedCount);

        //get suggested contacts on the top from ranked contacts
        ArrayList<RegularContact> regularContacts = rankingContactsManager.rankedContactsToRegularContacts(rankedContacts, suggestedCount, false);

        boolean emptyDataExist = false;

        for (int i = 0; i < suggestedCount; i++) {

            String name = regularContacts.get(i).getName();
            String contact = regularContacts.get(i).getContact();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contact)) {
                emptyDataExist = true;
                break;
            }
        }

        assertEquals(true, emptyDataExist);
    }

    /**
     * Validate if suggested contacts are first of the list
     *
     * @throws Exception
     */
    public void testValidateSuggestedContacts() throws Exception {

        Integer rankedContactsCount = 10;
        Integer suggestedCount = 5;

        RankingContactsManager rankingContactsManager = new RankingContactsManager(mockContext);

        // set suggested and non suggested contacts
        ArrayList<YSGRankedContact> rankedContacts = getRankedContacts(rankedContactsCount, suggestedCount);

        //get suggested contacts on the top from ranked contacts
        ArrayList<RegularContact> regularContacts = rankingContactsManager.rankedContactsToRegularContacts(rankedContacts, suggestedCount, false);

        boolean suggestedAreCorrect = true;

        for (int i = 0; i < suggestedCount; i++) {

            String suggestedContactName = rankedContacts.get(i).name();
            String contactName = regularContacts.get(i).getName();

            if (!suggestedContactName.equalsIgnoreCase(contactName)) {
                suggestedAreCorrect = false;
                break;
            }
        }

        assertEquals(true, suggestedAreCorrect);
    }



    /**
     * HELPER METHODS
     **/

    @NonNull
    private ArrayList<YSGRankedContact> getRankedContacts(Integer count, Integer suggestedCount) throws Exception {

        ArrayList<YSGRankedContact> rankedContacts = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            JSONObject data = new JSONObject();

            if (i < suggestedCount) {
                data.put("suggested", "true");
            }

            YSGRankedContact contact = new YSGRankedContact();

            contact.setName("John" + String.valueOf(i));
            contact.setPhone("040234252" + String.valueOf(i));
            contact.setEmail("jonh@hotmail.com" + String.valueOf(i));


            ArrayList<String> emails = new ArrayList<>();
            emails.add("john@gmail.com" + String.valueOf(i));
            emails.add("john@email.com" + String.valueOf(i));
            contact.setEmails(emails);

            ArrayList<String> phones = new ArrayList<>();
            phones.add("333-456-434" + String.valueOf(i));
            phones.add("123-232-542" + String.valueOf(i));
            contact.setPhones(phones);

            contact.setData(data);
            rankedContacts.add(contact);
        }
        return rankedContacts;
    }

    @NonNull
    private ArrayList<YSGRankedContact> getRankedContactsWithMissingData(Integer count, Integer suggestedCount) throws Exception {

        ArrayList<YSGRankedContact> rankedContacts = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            YSGRankedContact contact = new YSGRankedContact();

            JSONObject data = new JSONObject();

            if (i < suggestedCount) {
                data.put("suggested", "true");
            }

            if (i != 1) {
                contact.setName("John" + String.valueOf(i));
            }

            if (i != 3) {
                contact.setPhone("040234252" + String.valueOf(i));
            }

            contact.setEmail("jonh@gmail.com" + String.valueOf(i));
            contact.setData(data);

            rankedContacts.add(contact);
        }
        return rankedContacts;
    }

    /**
     * Mocking class
     */
    private class DelegatedMockContext extends MockContext {

        private Context mDelegatedContext;
        private static final String PREFIX = "test.";

        public DelegatedMockContext(Context context) {
            mDelegatedContext = context;
        }

        @Override
        public String getPackageName() {
            return PREFIX;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return mDelegatedContext.getSharedPreferences(name, mode);
        }
    }


}