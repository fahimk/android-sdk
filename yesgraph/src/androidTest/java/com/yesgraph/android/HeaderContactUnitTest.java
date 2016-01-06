package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.Ims;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klemen on 5.1.2016.
 */
public class HeaderContactUnitTest extends ApplicationTestCase<Application> {
    public HeaderContactUnitTest() {
        super(Application.class);
    }

    public void testCheckImsData() {

        Integer position = 7;
        String sign = "A";

        HeaderContact headerContact = new HeaderContact();

        headerContact.setSign(sign);
        headerContact.setPosition(position);

        assertEquals(sign, headerContact.getSign());
        assertEquals(position, headerContact.getPosition());
    }
}