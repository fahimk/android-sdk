package com.yesgraph.android;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.yesgraph.android.activity.MainActivity;
import com.yesgraph.android.activity.ShareSheetActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.CustomTheme;

import org.junit.Before;

/**
 * Created by Klemen on 23.12.2015.
 */
public class YesGraphUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private YesGraph yesGraph;

    public YesGraphUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
        yesGraph = (YesGraph) mainActivity.getApplication();
    }

    /**
     * Check if custom application theme is not null
     */
    public void testCustomThemeNotNull() {

        CustomTheme customTheme = new CustomTheme();
        yesGraph.setCustomTheme(customTheme);

        boolean customThemeNotNull = yesGraph.getCustomTheme() != null;

        assertEquals(true, customThemeNotNull);
    }


    /**
     * Check if global application text are not null
     */
    public void testCheckTextNotNull() {

        YesGraph yesGraph = (YesGraph) mainActivity.getApplication();

        String smsText = yesGraph.getSmsText();

        assertEquals(true, !smsText.isEmpty());

        String emailText = yesGraph.getEmailText();
        assertEquals(true, !emailText.isEmpty());

        String emailSubject = yesGraph.getEmailSubject();
        assertEquals(true, !emailSubject.isEmpty());

        String copyLinkText = yesGraph.getCopyLinkText();
        assertEquals(true, !copyLinkText.isEmpty());

        String copyButtonText = yesGraph.getCopyButtonText();
        assertEquals(true, !copyButtonText.isEmpty());

        String shareText = yesGraph.getShareText();
        assertEquals(true, !shareText.isEmpty());

    }

    /**
     * Check if connectivity manager is online
     */
    public void testCheckInternetConnection() {

        boolean isOnline = yesGraph.isOnline();

        assertTrue(isOnline);
    }
}