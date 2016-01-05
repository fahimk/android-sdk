package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Address;
import com.yesgraph.android.models.Ims;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klemen on 5.1.2016.
 */
public class AddressUnitTest extends ApplicationTestCase<Application> {
    public AddressUnitTest() {
        super(Application.class);
    }


    public void testCheckAddresModelData() {

        Address address = new Address();

        address.setPo_box("po_box");
        address.setStreet("street");
        address.setState("state");
        address.setCity("city");
        address.setPostal_code("postalCode");
        address.setCountry("country");
        address.setType("type");

        assertTrue(!address.getPo_box().isEmpty());
        assertTrue(!address.getStreet().isEmpty());
        assertTrue(!address.getCity().isEmpty());
        assertTrue(!address.getCountry().isEmpty());
        assertTrue(!address.getPostal_code().isEmpty());
        assertTrue(!address.getState().isEmpty());
        assertTrue(!address.getType().isEmpty());

    }

    public void testConvertAddressDataToJson() throws JSONException {

        Address address = new Address();

        address.setPo_box("po_box");
        address.setStreet("street");
        address.setState("state");
        address.setPostal_code("postalCode");
        address.setCountry("country");
        address.setType("type");


        JSONObject json = address.toJSONObject();

        boolean hasPoBox = json.has("po_box") && json.get("po_box") != null;
        boolean hasStreet = json.has("street") && json.get("street") != null;
        boolean hasState = json.has("state") && json.get("state") != null;
        boolean hasPostalCode = json.has("postal_code") && json.get("postal_code") != null;
        boolean hasCountry = json.has("country") && json.get("country") != null;
        boolean hasType = json.has("type") && json.get("type") != null;

        assertTrue(hasPoBox);
        assertTrue(hasStreet);
        assertTrue(hasState);

        assertTrue(hasPostalCode);
        assertTrue(hasCountry);
        assertTrue(hasType);

    }

}