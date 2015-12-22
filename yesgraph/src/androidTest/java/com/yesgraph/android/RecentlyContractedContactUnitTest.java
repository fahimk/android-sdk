package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.FullDetailsContact;
import com.yesgraph.android.models.RecentlyContactedContact;

/**
 * Created by Klemen on 22.12.2015.
 */
public class RecentlyContractedContactUnitTest extends ApplicationTestCase<Application> {
    public RecentlyContractedContactUnitTest() {
        super(Application.class);
    }


    /**
     * Check recently contracted contact data
     */
    public void testCheckRecentlyContractedContactDataNotNull() {

        RecentlyContactedContact contact = new RecentlyContactedContact();

        contact.setName("John");
        contact.setDate("22.12.2015");
        contact.setNumber("123-434-534");

        assertEquals(true, !contact.getName().isEmpty());
        assertEquals(true, !contact.getDate().isEmpty());
        assertEquals(true, !contact.getNumber().isEmpty());

    }


}