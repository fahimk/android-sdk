package com.yesgraph.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.network.YSGSuggestionsShown;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Klemen on 10.12.2015.
 */
public class RankingContactsManager {

    private SharedPreferences sharedPreferences;
    private Context context;

    public RankingContactsManager(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Add suggested contacts on the top of list
     *
     * @param ysgRankedContactArrayList list of ranked contats
     * @param suggestedCount            number of suggested contacts
     * @param postSuggested             http post flag
     * @return list of regular contacts
     */
    public ArrayList<RegularContact> rankedContactsToRegularContacts(ArrayList<YSGRankedContact> ysgRankedContactArrayList, Integer suggestedCount, Boolean postSuggested) {

        ArrayList<RegularContact> regularContacts = new ArrayList<>();
        ArrayList<RegularContact> suggestedContacts = new ArrayList<>();
        ArrayList<YSGRankedContact> ysgRankedContacts = new ArrayList<>();
        JSONArray jsonArrayCached = new JSONArray();

        ArrayList<Integer> suggestedItemsIndex = getSuggestedItemIndexes(ysgRankedContactArrayList, suggestedCount);

        // set suggested and regular contacts from ranked contacts
        for (int i = 0; i < ysgRankedContactArrayList.size(); i++) {

            YSGRankedContact ysgRankedContact = ysgRankedContactArrayList.get(i);

            if (suggestedItemsIndex.contains(i)) {
                ysgRankedContact.setSuggested(true);
                ysgRankedContacts.add(ysgRankedContact);

                RegularContact regularContact = new RegularContact();
                regularContact.setName(ysgRankedContact.name());
                if (ysgRankedContact.email() != null && ysgRankedContact.email().length() > 0) {
                    regularContact.setContact(ysgRankedContact.email());
                } else if (ysgRankedContact.phone() != null && ysgRankedContact.phone().length() > 0) {
                    regularContact.setContact(ysgRankedContact.phone());
                }
                suggestedContacts.add(regularContact);
            } else {
                if (ysgRankedContact.emails() != null && ysgRankedContact.emails().size() > 0) {
                    for (String thisEmail : ysgRankedContact.emails()) {
                        RegularContact regularContact = new RegularContact();
                        regularContact.setName(ysgRankedContact.name());
                        regularContact.setContact(thisEmail);
                        regularContacts.add(regularContact);
                    }
                }

                if (ysgRankedContact.phones() != null && ysgRankedContact.phones().size() > 0) {
                    for (String thisPhone : ysgRankedContact.phones()) {
                        RegularContact regularContact = new RegularContact();
                        regularContact.setName(ysgRankedContact.name());
                        regularContact.setContact(thisPhone);
                        regularContacts.add(regularContact);
                    }
                }
            }

            jsonArrayCached.put(ysgRankedContact.toJSONObjectExtended());
        }

        postSuggestedContacts(postSuggested, ysgRankedContacts);

        sharedPreferences.edit().putString("contacts_cache", jsonArrayCached.toString()).commit();

        sortRegularContacts(regularContacts);

        for (RegularContact contact : regularContacts) {
            suggestedContacts.add(contact);
        }

        return suggestedContacts;
    }

    /**
     * Sort regular contacts asc
     *
     * @param regularContacts array of contacts
     */
    private void sortRegularContacts(ArrayList<RegularContact> regularContacts) {

        Collections.sort(regularContacts, new Comparator<RegularContact>() {
            public int compare(RegularContact s1, RegularContact s2) {

                if (YSGUtility.isAlpha(s1.getName().substring(0, 1)) && !YSGUtility.isAlpha(s2.getName().substring(0, 1)))
                    return -s1.getName().compareToIgnoreCase(s2.getName());

                if (YSGUtility.isAlpha(s2.getName().substring(0, 1)) && !YSGUtility.isAlpha(s1.getName().substring(0, 1)))
                    return -s1.getName().compareToIgnoreCase(s2.getName());

                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
    }

    /**
     * Post suggestions contacts to web API
     *
     * @param postSuggested     check if post action is required
     * @param ysgRankedContacts list of ranked suggestions contacts
     */
    private void postSuggestedContacts(Boolean postSuggested, ArrayList<YSGRankedContact> ysgRankedContacts) {

        if (postSuggested) {

            YSGSuggestionsShown ysgSuggestionsShown = new YSGSuggestionsShown();
            ysgSuggestionsShown.updateSuggestionsSeen(context, ysgRankedContacts, sharedPreferences.getString("user_id", ""), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Message callbackMessage = new Message();
                    if (msg.what == Constants.RESULT_OK) {
                    }
                    return false;
                }
            });
        }
    }


    /**
     * Get list indexes from suggested contacts
     *
     * @param ysgRankedContactArrayList list of ranked contacts
     * @param suggestedCount            number of suggested contacts
     * @return Suggested items position
     */
    private ArrayList<Integer> getSuggestedItemIndexes(ArrayList<YSGRankedContact> ysgRankedContactArrayList, Integer suggestedCount) {

        ArrayList<Integer> suggestedItemsIndex = new ArrayList<>();

        for (int i = 0; i < ysgRankedContactArrayList.size(); i++) {
            YSGRankedContact ysgRankedContact = ysgRankedContactArrayList.get(i);

            if (!ysgRankedContact.wasSuggested()) {
                suggestedItemsIndex.add(i);
                if (suggestedItemsIndex.size() == suggestedCount)
                    break;
            }
        }

        if (suggestedItemsIndex.size() < suggestedCount && ysgRankedContactArrayList.size() >= suggestedCount) {
            for (int i = 0; i < suggestedCount - suggestedItemsIndex.size(); i++) {
                suggestedItemsIndex.add(i);
            }

            for (int i = 0; i < ysgRankedContactArrayList.size(); i++) {
                YSGRankedContact ysgRankedContact = ysgRankedContactArrayList.get(i);
                ysgRankedContact.setSuggested(false);
            }
        }
        return suggestedItemsIndex;
    }

}
