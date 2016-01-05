package com.yesgraph.android;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.yesgraph.android.activity.ContactsActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.CustomTheme;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Klemen on 21.12.2015.
 */
@RunWith(AndroidJUnit4.class)
public class ContactsActivityEspressoTests {

    private  YesGraph yesGraph;
    private static final String FONT_TYPE_FACE = "Pacifico.ttf";

    @Rule
    public ActivityTestRule<ContactsActivity> activityTestRule =
            new ActivityTestRule<>(ContactsActivity.class);


    @Before
    public void setUp() throws Exception {

        yesGraph = (YesGraph) activityTestRule.getActivity().getApplication();

        //set custom text font
        CustomTheme customTheme = new CustomTheme();
        customTheme.setFonts(FONT_TYPE_FACE);
        yesGraph.setCustomTheme(customTheme);

    }

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

    /**
     * Check if suggested text is shown
     */
    @Test
    public void checkSuggestedText() {

        //delay 5000 seconds
        delay(5000);

        YesGraph application = (YesGraph) activityTestRule.getActivity().getApplication();
        String suggestedText = application.getString(R.string.suggested);

        onView(withText(suggestedText)).check(matches(isDisplayed()));
    }

    /**
     * Validate recycler view item click and check if send dialog is shown
     */
    @Test
    public void validateInviteContact() {

        //TODO: set only phones or only emails contacts

        //delay 5000 seconds
        delay(5000);

        //click on item on position 1
        onView(withId(R.id.contactsList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        onView(withId(R.id.action_invite)).check(matches(isDisplayed()));
        onView(withId(R.id.action_invite)).perform(ViewActions.click());

        delay(1000);

        //check if is Marshmallow sdk
        if (YesGraph.isMarshmallow()) {
            //click on dialog OK button
            onView(withId(android.R.id.button1)).perform(ViewActions.click());
        }

        delay(1000);

        //click on dialog cancel button
        onView(withId(android.R.id.button2)).perform(ViewActions.click());

        onView(withId(R.id.contactsList)).check(matches(isDisplayed()));
    }

    private void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckCustomFontType() {

        String customTypeFace = yesGraph.getCustomTheme().getFont();
        assertEquals(customTypeFace, FONT_TYPE_FACE);
    }
}
