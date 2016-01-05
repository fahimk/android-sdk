package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Ims;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klemen on 5.1.2016.
 */
public class ImsUnitTest extends ApplicationTestCase<Application> {
    public ImsUnitTest() {
        super(Application.class);
    }


    public void testCheckImsData() {

        Ims ims = new Ims();

        ims.setName("ims name");
        ims.setProtocol("protocol x");
        ims.setType("ims type");

        assertEquals("ims name", ims.getName());
        assertEquals("protocol x", ims.getProtocol());
        assertEquals("ims type", ims.getType());

    }

    public void testConvertImsToJson() throws JSONException {

        Ims ims = new Ims();

        ims.setName("ims name");
        ims.setProtocol("protocol x");
        ims.setType("ims type");

        JSONObject json = ims.toJSONObject();

        boolean hasName = json.has("name") && json.get("name") != null;
        boolean hasProtocol = json.has("protocol") && json.get("protocol") != null;
        boolean hasType = json.has("type") && json.get("type") != null;

        assertTrue(hasName);
        assertTrue(hasProtocol);
        assertTrue(hasType);

    }

}