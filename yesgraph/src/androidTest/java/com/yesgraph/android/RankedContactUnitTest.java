package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.RankedContact;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klemen on 5.1.2016.
 */
public class RankedContactUnitTest extends ApplicationTestCase<Application> {
    public RankedContactUnitTest() {
        super(Application.class);
    }


    public void testCheckRankedContactData() {

        RankedContact contact = new TestUtils().getRankedContact();
        contact.setRank(12);

        contact.toJSONObject();

        boolean hasEmails = contact.emails().size() > 0;
        boolean hasPhones = contact.phones().size() > 0;

        assertTrue(hasEmails);

        assertTrue(hasPhones);
    }


}