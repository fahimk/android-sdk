package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.RegularContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Klemen on 29.12.2015.
 */
public class RegularContactUnitTest extends ApplicationTestCase<Application> {
    public RegularContactUnitTest() {
        super(Application.class);
    }


    /**
     * Check regular contact data
     */
    public void testCheckRegularContactData() {

        RegularContact contact = new RegularContact();

        int position = 10;

        contact.setName("John");
        contact.setEmail("john@email.si");
        contact.setSelected(true);
        contact.setPosition(position);
        contact.setPhone("123-434-544");

        assertEquals(true, !contact.getName().isEmpty());
        assertEquals(true, !contact.getContact().isEmpty());
        assertEquals(true, contact.getSelected().booleanValue());
        assertEquals(true, contact.isEmail().booleanValue());
        assertEquals(false, contact.isPhone().booleanValue());

        assertEquals(position, contact.getPosition().intValue());

    }
}