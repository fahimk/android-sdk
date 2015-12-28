package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;

import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.SharedPreferencesManager;

import org.mockito.Mock;

/**
 * Created by Klemen on 28.12.2015.
 */
public class SharedPreferencesManagerUnitTest extends ApplicationTestCase<Application> {
    public SharedPreferencesManagerUnitTest() {
        super(Application.class);
    }

    @Mock
    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new DelegatedMockContext(getContext());
    }

    /**
     * Check string values stored in shared preferences
     */
    public void testCheckStringSharedPreferences() {

        String key = "STRING_KEY";
        String value = "some text";

        new SharedPreferencesManager(mockContext).putString(key, value);

        String savedValue = new SharedPreferencesManager(mockContext).getString(key);

        boolean isEqual = value.equals(savedValue);

        assertEquals(true, isEqual);

    }

    /**
     * Check boolean values stored in shared preferences
     */
    public void testCheckBooleanSharedPreferences() {

        String key = "BOOLEAN_KEY";
        boolean value = true;

        new SharedPreferencesManager(mockContext).putBoolean(key, value);

        boolean savedValue = new SharedPreferencesManager(mockContext).getBoolean(key);

        boolean isEqual = (savedValue == value);

        assertEquals(true, isEqual);

    }

    /**
     * Check long values stored in shared preferences
     */
    public void testCheckLongSharedPreferences() {

        String key = "LONG_KEY";
        long value = 1234564;

        new SharedPreferencesManager(mockContext).putLong(key, value);

        long savedValue = new SharedPreferencesManager(mockContext).getLong(key);

        boolean isEqual = (savedValue == value);

        assertEquals(true, isEqual);

    }


    /**
     * Mocking context class
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