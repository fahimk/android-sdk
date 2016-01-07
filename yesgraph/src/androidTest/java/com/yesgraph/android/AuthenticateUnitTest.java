package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Authenticate;
import com.yesgraph.android.utils.StorageKeyValueManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mock;

import java.util.ArrayList;

/**
 * Created by Klemen on 5.1.2016.
 */
public class AuthenticateUnitTest extends ApplicationTestCase<Application> {
    public AuthenticateUnitTest() {
        super(Application.class);
    }

    @Mock
    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new MockDelegatedContext(getContext());
    }


    public void testSetClientKeyAndUserId() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("client_key", "123456");
        jsonObject.put("user_id", "98765");

        Message message = new Message();
        message.obj = jsonObject;

        new Authenticate().setClientKeyAndUserId(mockContext, message);

        String secretKey = new StorageKeyValueManager(mockContext).getSecretKey();
        assertTrue(secretKey != null);

        String userId = new StorageKeyValueManager(mockContext).getUserId();
        assertTrue(userId != null);


    }

    /**
     * Check
     */
    public void testCheckUserIdJsonObject() {

        String userId = "";

        Message message = new Message();
        message.obj = null;

        JSONObject json = new Authenticate().setUserIdToJSON(mockContext, userId);

        assertTrue(json != null);

        boolean hasUserIdTag = json.has("user_id");
        assertTrue(hasUserIdTag);

    }

    /**
     * Validate http parameters from json object
     *
     * @throws JSONException
     */
    public void testValidateHttpParameters() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("weather_key", "123456");
        jsonObject.put("city", "ljubljana");
        jsonObject.put("country", "slovenia");

        String parameters = Authenticate.getParametersString(jsonObject);

        assertTrue(!parameters.isEmpty());

        boolean hasCity = parameters.contains("city") && parameters.contains("ljubljana");
        boolean hasCountry = parameters.contains("country") && parameters.contains("slovenia");
        boolean hasWeatherKey = parameters.contains("weather_key") && parameters.contains("123456");

        assertTrue(hasCity);
        assertTrue(hasCountry);
        assertTrue(hasWeatherKey);

        JSONObject emptyParameters = null;
        String emptyParameter = Authenticate.getParametersString(emptyParameters);

        assertTrue(emptyParameter.equals(""));

    }
}