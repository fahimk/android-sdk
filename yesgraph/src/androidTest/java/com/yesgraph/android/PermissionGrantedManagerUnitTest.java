package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;

import com.yesgraph.android.utils.PermissionGrantedManager;

import org.mockito.Mock;

/**
 * Created by Klemen on 28.12.2015.
 */
public class PermissionGrantedManagerUnitTest extends ApplicationTestCase<Application> {
    public PermissionGrantedManagerUnitTest() {
        super(Application.class);
    }

    @Mock
    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new DelegatedMockContext(getContext());
    }

    public void testSetReadContactPermission() {

        new PermissionGrantedManager(mockContext).setReadContactsPermission(true);

        boolean isTrue = new PermissionGrantedManager(mockContext).isReadContactsPermission();

        assertEquals(true,isTrue);
    }

    public void testSetSendSmsPermission() {

        new PermissionGrantedManager(mockContext).setSendSmsPermission(true);

        boolean isTrue = new PermissionGrantedManager(mockContext).isSendSmsPermission();

        assertEquals(true,isTrue);
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