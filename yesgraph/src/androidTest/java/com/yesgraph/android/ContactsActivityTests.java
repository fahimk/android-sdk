package com.yesgraph.android;

import android.support.test.rule.ActivityTestRule;

import com.yesgraph.android.activity.ContactsActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Klemen on 21.12.2015.
 */
public class ContactsActivityTests {

    @Rule
    public ActivityTestRule<ContactsActivity> activityTestRule =
            new ActivityTestRule<>(ContactsActivity.class);

    @Test
    public void validateLayoutsAreShown() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutMaster)).check(matches(isDisplayed()));
        onView(withId(R.id.searhBar)).check(matches(isDisplayed()));
        onView(withId(R.id.contactsListContent)).check(matches(isDisplayed()));
        onView(withId(R.id.side_index)).check(matches(isDisplayed()));
    }

    @Test
    public void validateTextViewsAreShown(){
        onView(withId(R.id.toolbarTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.action_invite)).check(matches(isDisplayed()));


    }

    @Test
    public void validateEditTextIsShown(){
        onView(withId(R.id.search)).check(matches(isDisplayed()));
    }

    @Test
    public void validateRecyclerViewIsShown(){
        onView(withId(R.id.contactsList)).check(matches(isDisplayed()));
    }
}
