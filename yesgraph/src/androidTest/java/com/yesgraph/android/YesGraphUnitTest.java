package com.yesgraph.android;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;

import com.yesgraph.android.activity.SendSmsActivity;
import com.yesgraph.android.activity.ShareSheetActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.services.ContactShareService;
import com.yesgraph.android.services.FacebookShareService;
import com.yesgraph.android.services.TwitterShareService;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.CustomTheme;
import com.yesgraph.android.utils.StorageKeyValueManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.mockito.Mock;

import java.util.ArrayList;

/**
 * Created by Klemen on 23.12.2015.
 */
public class YesGraphUnitTest extends ActivityInstrumentationTestCase2<ShareSheetActivity> {

    private ShareSheetActivity mainActivity;
    private YesGraph yesGraph;

    private Context context;

    public YesGraphUnitTest() {
        super(ShareSheetActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
        yesGraph = (YesGraph) mainActivity.getApplication();
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

    public void testCheckConfigureClientKey() {

        String apiKey = "apiKey";

        new StorageKeyValueManager(context).setApiKey(apiKey);

        yesGraph.configureWithClientKey(apiKey);

        String savedApiKey = new StorageKeyValueManager(context).getApiKey();

        assertEquals(apiKey, savedApiKey);


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

        ArrayList<RankedContact> contacts = new TestUtils().getRankedContacts(5);

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

        String key = "secret_key";

        new StorageKeyValueManager(context).setSecretKey(key);

        yesGraph.checkIsTimeToRefreshAddressBook();

        String savedKey = new StorageKeyValueManager(context).getSecretKey();

        String secretKey = "secretKey";
        yesGraph.onCreate();
        yesGraph.configureWithClientKey(secretKey);

        assertEquals(key, savedKey);

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

    public void testCheckContactsFromCache() throws JSONException {

        JSONArray jsonContacts = new TestUtils().getJsonArray();

        //set contacts to cache
        new StorageKeyValueManager(context).setContactCache(jsonContacts.toString());

        ArrayList<RankedContact> contacts = yesGraph.getContactsFromCache();

        assertTrue(contacts != null);

        assertTrue(!contacts.isEmpty());

        assertEquals(jsonContacts.length(), contacts.size());

    }

    public void testCheckNoContactsFromCache() throws JSONException {

        //reset contacts in cache
        new StorageKeyValueManager(context).setContactCache(null);

        ArrayList<RankedContact> contacts = yesGraph.getContactsFromCache();

        assertTrue(contacts == null);

    }

    public void testCheckNumberOfInvitesContacts() throws JSONException {

        Long number = 12L;

        new StorageKeyValueManager(context).setInviteNumber(number);

        Long invitedContacts = yesGraph.getLastInvitedContactsNumber();

        assertEquals(number, invitedContacts);

    }

    public void testCheckConfigureWithUserId() {

        String userId = "user_id";

        yesGraph.configureWithUserId(userId);

        String savedUserId = new StorageKeyValueManager(context).getUserId();

        assertEquals(userId, savedUserId);

    }

    public void testCheckCustomSmsIntent() {

        Intent intent = new Intent(context, SendSmsActivity.class);
        yesGraph.setCustomSmsIntent(intent);

        Intent savedSmsIntent = yesGraph.getCustomSmsIntent();

        assertNotNull(savedSmsIntent);
    }

    public void testCheckCustomEmailIntent() {

        Intent intent = new Intent(context, SendSmsActivity.class);
        yesGraph.setCustomEmailIntent(intent);

        Intent savedEmailIntent = yesGraph.getCustomEmailIntent();

        assertNotNull(savedEmailIntent);
    }

    public void testCheckShareServices() {

        ArrayList<Object> shareServices = yesGraph.getShareServices();

        assertNotNull(shareServices);
    }

    public void testSetShareServices() {

        ArrayList<Object> shareServices = new ArrayList<>();

        FacebookShareService facebookShareService = new FacebookShareService(context);
        TwitterShareService twitterShareService = new TwitterShareService(context);
        ContactShareService contactShareService = new ContactShareService(context);

        shareServices.add(facebookShareService);
        shareServices.add(twitterShareService);
        shareServices.add(contactShareService);

        yesGraph.setShareServices(shareServices);

        int shareServicesSize = yesGraph.getShareServices().size();

        assertEquals(3,shareServicesSize);

        assertNotNull(yesGraph.getShareServices());

    }

}