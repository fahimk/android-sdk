package com.yesgraph.android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;

import com.yesgraph.android.activity.MainActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.CustomTheme;
import com.yesgraph.android.utils.StorageKeyValueManager;

import org.junit.Before;
import org.mockito.Mock;

import java.util.ArrayList;

/**
 * Created by Klemen on 23.12.2015.
 */
public class YesGraphUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private YesGraph yesGraph;

    @Mock
    private Context mockContext;

    private Context context;

    public YesGraphUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
        yesGraph = (YesGraph) mainActivity.getApplication();
        mockContext = new MockDelegatedContext(mainActivity.getBaseContext());

        context = mainActivity.getApplicationContext();
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

        String smsText = yesGraph.getCustomTheme().getSmsText(context);

        assertEquals(true, !smsText.isEmpty());

        String emailText = yesGraph.getCustomTheme().getEmailText(context);
        assertEquals(true, !emailText.isEmpty());

        String emailSubject = yesGraph.getCustomTheme().getEmailSubject(context);
        assertEquals(true, !emailSubject.isEmpty());

        String copyLinkText = yesGraph.getCustomTheme().getCopyLinkText(context);
        assertEquals(true, !copyLinkText.isEmpty());

        String copyButtonText = yesGraph.getCustomTheme().getCopyButtonText(context);
        assertEquals(true, !copyButtonText.isEmpty());

        String shareText = yesGraph.getCustomTheme().getShareText(context);
        assertEquals(true, !shareText.isEmpty());

    }

    /**
     * Check if connectivity manager is online
     */
    public void testCheckInternetConnection() {

        boolean isOnline = yesGraph.isOnline();

        assertTrue(isOnline);
    }

    public void testCheckSetSecretKey() {

        String secretKey = "secretKey";

        yesGraph.configureWithClientKey(secretKey);

        String savedSecretKey = new StorageKeyValueManager(context).getSecretKey();

        assertEquals(secretKey, savedSecretKey);


    }

    public void testCheckSetUserSource() {

        String userName = "john12";
        String userPhone = "123-434-534";
        String userEmail = "john@email.com";

        yesGraph.setSource(userName, userPhone, userEmail);

        String savedUserName = new StorageKeyValueManager(context).getUserName();
        String savedPhone = new StorageKeyValueManager(context).getUserPhone();
        String savedEmail = new StorageKeyValueManager(context).getUserEmail();

        assertEquals(userName, savedUserName);
        assertEquals(userPhone, savedPhone);
        assertEquals(userEmail, savedEmail);

    }

    public void testCheckRetrieverContactsData() {

        ArrayList<RankedContact> contacts = yesGraph.readContactsFromPhone();

        boolean contactNameIsEmpty = false;

        int contactsCount = contacts.size();

        if (contactsCount > 0) {

            for (int i = 0; i < contacts.size(); i++) {

                String contactName = contacts.get(i).name();

                if (TextUtils.isEmpty(contactName)) {
                    contactNameIsEmpty = true;
                }
            }
        }

        assertEquals(false, contactNameIsEmpty);

    }

    public void testCheckUpdateContactsFromPhone() {

        final Handler.Callback handler = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        };

        Message message = new Message();
        message.what = Constants.RESULT_OK;
        handler.handleMessage(message);

        yesGraph.updateContactsFromPhone(handler);

        assertEquals(Constants.RESULT_OK, message.what);
    }


    public void testUpdateSuggestionsSeen() {

        ArrayList<RankedContact> contacts = new TestUtils().getRankedContacts();

        final Handler.Callback handler = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        };

        Message message = new Message();
        message.what = Constants.RESULT_OK;
        handler.handleMessage(message);

        yesGraph.updateSuggestionsSeen(contacts, handler);

        assertEquals(Constants.RESULT_OK, message.what);

    }

    public void testInviteSentForUsers() {

        ArrayList<Contact> contacts = new TestUtils().getContacts();

        final Handler.Callback handler = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        };

        Message message = new Message();
        message.what = Constants.RESULT_OK;
        handler.handleMessage(message);

        yesGraph.inviteSentForUsers(contacts, handler);

        assertEquals(Constants.RESULT_OK, message.what);
    }

    public void testCheckIsTimeToRefresh() {

        String secretKey = "secretKey";
        yesGraph.configureWithClientKey(secretKey);

        yesGraph.checkIsTimeToRefreshAddressBook();
    }

    public void testLoadOnCreate() {

        String secretKey = "secretKey";
        yesGraph.onCreate(secretKey);

        String savedSecretKey = new StorageKeyValueManager(context).getSecretKey();
        assertEquals(secretKey, savedSecretKey);

    }

    public void testValidateTimeToRefreshMilliseconds() {

        //47 hours
        long lastUploadTwoDaysAgo = 172790000;
        new StorageKeyValueManager(context).setContactLastUpload(lastUploadTwoDaysAgo);
        boolean isTimeToRefresh = yesGraph.timeToRefreshAddressBook();
        assertTrue(isTimeToRefresh);


        long lastUploadOneDayAgo = System.currentTimeMillis() - (Constants.HOURS_BETWEEN_UPLOAD * 60 * 1000);
        new StorageKeyValueManager(context).setContactLastUpload(lastUploadOneDayAgo);

        boolean isNoTimeToRefresh = !yesGraph.timeToRefreshAddressBook();

        assertTrue(isNoTimeToRefresh);

    }

}