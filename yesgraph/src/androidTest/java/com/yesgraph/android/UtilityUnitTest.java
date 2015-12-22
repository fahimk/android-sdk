package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Klemen on 17.12.2015.
 */
public class UtilityUnitTest extends ApplicationTestCase<Application> {
    public UtilityUnitTest() {
        super(Application.class);
    }

    /**
     * Check generated random id
     */
    public void testCheckGenerateRandomId() {

        String randomId = Utility.randomUserId();

        boolean isNotNull = randomId != null;

        assertEquals(true, isNotNull);

    }

    /**
     * Check alpha string
     */
    public void testCheckAlphaString() {

        String name = "john";

        boolean isAlpha = Utility.isAlpha(name);

        assertEquals(true, isAlpha);

    }

    /**
     * Check non alpha string
     */
    public void testCheckNonAlphaString() {

        String name = "john123";

        boolean isAlpha = Utility.isAlpha(name);

        assertEquals(false, isAlpha);

    }

    /**
     * Check numeric string
     */
    public void testCheckNumericString() {

        String numbers = "1234";

        boolean isNumeric = Utility.isNumeric(numbers);

        assertEquals(true, isNumeric);

    }

    /**
     * Check non numeric string
     */
    public void testCheckNonNumericString() {

        String numbers = "john1234";

        boolean isNumeric = Utility.isNumeric(numbers);

        assertEquals(false, isNumeric);

    }


}