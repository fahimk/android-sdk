package com.yesgraph.android;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.LinearLayout;

import com.yesgraph.android.activity.ShareSheetActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.CustomTheme;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Dean Bozinoski on 11/30/2015.
 */
public class ShareSheetActivityTest {

    @Rule
    public ActivityTestRule<ShareSheetActivity> activityTestRule =
            new ActivityTestRule<>(ShareSheetActivity.class);

    @Test
    public void validateLayoutsAreShown() {

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutMaster)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutFacebook)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutTwitter)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutContacts)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutFacebookCircle)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutTwitterCircle)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutContactsCircle)).check(matches(isDisplayed()));
        onView(withId(R.id.copyLinkLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void validateTexViewsAreShown() {

        onView(withId(R.id.toolbarTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.shareText)).check(matches(isDisplayed()));
        onView(withId(R.id.textFacebook)).check(matches(isDisplayed()));
        onView(withId(R.id.textTwitter)).check(matches(isDisplayed()));
        onView(withId(R.id.textContacts)).check(matches(isDisplayed()));
        onView(withId(R.id.textCopyLink)).check(matches(isDisplayed()));
        onView(withId(R.id.textCopyButton)).check(matches(isDisplayed()));
    }

    @Test
    public void validateImageViewsAreShown() {

        onView(withId(R.id.imageFacebook)).check(matches(isDisplayed()));
        onView(withId(R.id.imageTwitter)).check(matches(isDisplayed()));
        onView(withId(R.id.imageContacts)).check(matches(isDisplayed()));
    }

    @Test
    public void checkTextViewHaveRightStrings() {
        YesGraph application = (YesGraph) activityTestRule.getActivity().getApplication();
        onView(withId(R.id.toolbarTitle)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.share_sheet))));
        onView(withId(R.id.shareText)).check(matches(withText(application.getShareText())));
        onView(withId(R.id.textFacebook)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.facebook))));
        onView(withId(R.id.textTwitter)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.twitter))));
        onView(withId(R.id.textContacts)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.contacts))));
        onView(withId(R.id.textCopyLink)).check(matches(withText(application.getCopyLinkText())));
        onView(withId(R.id.textCopyButton)).check(matches(withText(application.getCopyButtonText())));
    }

    @Test
    public void checkCopyButtonTextChange() {

        onView(withId(R.id.textCopyButton)).perform(ViewActions.click());

        //check is button text set to copied
        onView(withId(R.id.textCopyButton)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.button_copied_text))));

    }
}