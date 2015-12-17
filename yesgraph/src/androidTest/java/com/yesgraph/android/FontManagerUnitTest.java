package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.YSGUtility;

/**
 * Created by Klemen on 17.12.2015.
 */
public class FontManagerUnitTest extends ApplicationTestCase<Application> {
    public FontManagerUnitTest() {
        super(Application.class);
    }

    /**
     * Check font singleton instance not null
     */
    public void testCheckFontInstanceNotNull() {

        FontManager fontManager = FontManager.getInstance();

        boolean isNotNull = fontManager != null;

        assertEquals(true,isNotNull);

    }




}