package com.yesgraph.android;

import android.content.Context;
import android.graphics.Typeface;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yesgraph.android.activity.ShareSheetActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.CustomTheme;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Dean Bozinoski on 11/30/2015.
 */
@RunWith(AndroidJUnit4.class)
public class ShareSheetActivityTest extends ActivityInstrumentationTestCase2<ShareSheetActivity> {

    private YesGraph yesGraph;
    private Context context;
    private static final String FONT_TYPE_FACE = "Pacifico.ttf";

    public ShareSheetActivityTest() {
        super(ShareSheetActivity.class);
    }

    @Rule
    public ActivityTestRule<ShareSheetActivity> activityTestRule =
            new ActivityTestRule<>(ShareSheetActivity.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        yesGraph = (YesGraph) activityTestRule.getActivity().getApplication();
        context = yesGraph.getBaseContext();

        //set custom text font
        CustomTheme customTheme = new CustomTheme();
        customTheme.setFonts(FONT_TYPE_FACE);
        yesGraph.setCustomTheme(customTheme);

    }

    @Test
    public void validateLayoutsAreShown() {

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutMaster)).check(matches(isDisplayed()));
        onView(withId(R.id.copyLinkLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void validateTexViewsAreShown() {

        onView(withId(R.id.toolbarTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.shareText)).check(matches(isDisplayed()));
        onView(withId(R.id.textCopyLink)).check(matches(isDisplayed()));
        onView(withId(R.id.textCopyButton)).check(matches(isDisplayed()));
    }

    @Test
    public void checkTextViewHaveRightStrings() {
        YesGraph application = (YesGraph) activityTestRule.getActivity().getApplication();
        onView(withId(R.id.toolbarTitle)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.share_sheet))));
        onView(withId(R.id.shareText)).check(matches(withText(application.getCustomTheme().getShareText(context))));
        onView(withId(R.id.textCopyLink)).check(matches(withText(application.getCustomTheme().getCopyLinkText(context))));
        onView(withId(R.id.textCopyButton)).check(matches(withText(application.getCustomTheme().getCopyButtonText(context))));
    }

    @Test
    public void checkCopyButtonTextChange() {

        onView(withId(R.id.textCopyButton)).perform(ViewActions.click());
        //check is button text set to copied
        onView(withId(R.id.textCopyButton)).check(matches(withText(activityTestRule.getActivity().getResources().getString(R.string.button_copied_text))));

    }

    @Test
    public void checkCopyToClickBoard() {

        onView(withId(R.id.copyLinkLayout)).perform(ViewActions.click());

        //check is toast with text is shown
        onView(withText(R.string.copy_to_clipboard)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    /**
     * Check if share button shape is square
     */
    @Test
    public void testCheckShareButtonShapeIsSquare() {

        CustomTheme customTheme = new CustomTheme();
        customTheme.setShareButtonsShape("square");
        yesGraph.setCustomTheme(customTheme);
        assertEquals("square", yesGraph.getCustomTheme().getShareButtonsShape());
    }

    /**
     * Check if share button shape is rounded square
     */
    @Test
    public void testCheckShareButtonShapeIsRoundedSquare() {

        CustomTheme customTheme = new CustomTheme();
        customTheme.setShareButtonsShape("rounded_square");
        yesGraph.setCustomTheme(customTheme);
        assertEquals("rounded_square", yesGraph.getCustomTheme().getShareButtonsShape());
    }

    @Test
    public void testClickOnContactsButton() {

        onView(withId(R.id.toolbarTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testCheckCustomFontType() {

        String customTypeFace = yesGraph.getCustomTheme().getFont();

        assertEquals(customTypeFace, FONT_TYPE_FACE);
    }
}