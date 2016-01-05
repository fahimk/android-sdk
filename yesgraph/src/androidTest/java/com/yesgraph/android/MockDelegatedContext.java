package com.yesgraph.android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Klemen on 5.1.2016.
 */
/**
 * Mocking class
 */
public class MockDelegatedContext extends android.test.mock.MockContext {

    private Context mDelegatedContext;
    private static final String PREFIX = "test.";

    public MockDelegatedContext(Context context) {
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
