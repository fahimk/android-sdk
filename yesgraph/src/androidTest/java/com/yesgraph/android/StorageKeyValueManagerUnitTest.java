package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;

import com.yesgraph.android.utils.SharedPreferencesManager;
import com.yesgraph.android.utils.StorageKeyValueManager;

import org.mockito.Mock;

/**
 * Created by Klemen on 28.12.2015.
 */
public class StorageKeyValueManagerUnitTest extends ApplicationTestCase<Application> {
    public StorageKeyValueManagerUnitTest() {
        super(Application.class);
    }

    @Mock
    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new DelegatedMockContext(getContext());
    }

    /**
     * Check contacts cache stored in shared preferences
     */
    public void testCheckContactCacheValue() {

        String value = "contacts";

        new StorageKeyValueManager(mContext).setContactCache(value);

        String savedContactCache = new StorageKeyValueManager(mContext).getContactCache();

        boolean isEqual = value.equals(savedContactCache);

        assertEquals(true, isEqual);

    }

    /**
     * Check contacts last uploading time stored in shared preferences
     */
    public void testCheckContactLastUploadingTime() {

        long lastUploadingTime = System.currentTimeMillis();

        new StorageKeyValueManager(mContext).setContactLastUpload(lastUploadingTime);

        long savedLastUploadingTime = new StorageKeyValueManager(mContext).getContactLastUpload();

        boolean isEqual = (lastUploadingTime == savedLastUploadingTime);

        assertEquals(true, isEqual);
    }

    /**
     * Validate last uploading flag stored in shared preferences
     */
    public void testCheckContactUploadingFlag() {

        boolean isUploading = true;

        new StorageKeyValueManager(mContext).setContactsUploading(isUploading);

        boolean savedIsUploadingValue = new StorageKeyValueManager(mContext).isContactsUploading();

        boolean isEqual = (savedIsUploadingValue == isUploading);

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