package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.FavouriteContacts;
import com.yesgraph.android.models.RecentlyContactedContact;

/**
 * Created by Klemen on 22.12.2015.
 */
public class FavouriteContactsUnitTest extends ApplicationTestCase<Application> {
    public FavouriteContactsUnitTest() {
        super(Application.class);
    }


    /**
     * Check favourite contact data
     */
    public void testFavouriteContactDataNotNull() {

        FavouriteContacts contact = new FavouriteContacts();

        contact.setNeme("John");
        contact.setConten_uri("Uri");

        assertEquals(true, !contact.getNeme().isEmpty());
        assertEquals(true, !contact.getConten_uri().isEmpty());


    }


}