package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.Source;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 18.12.2015.
 */
public class SourceUnitTest extends ApplicationTestCase<Application> {
    public SourceUnitTest() {
        super(Application.class);
    }


    /**
     * Validate source data set
     * @throws JSONException
     */
    public void testValidateSourceData() throws JSONException {

        String name = "john";
        String email="jonh@email.com";
        String phone = "123-432-124";

        Source source = new Source();
        source.setName(name);
        source.setEmail(email);
        source.setPhone(phone);

        boolean isEqualName = source.getName().equals(name);
        assertEquals(true,isEqualName);

        boolean isEqualEmail = source.getEmail().equals(email);
        assertEquals(true,isEqualEmail);

        boolean isEqualPhone = source.getPhone().equals(phone);
        assertEquals(true,isEqualPhone);

    }
}