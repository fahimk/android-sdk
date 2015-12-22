package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.FullDetailsContact;

/**
 * Created by Klemen on 22.12.2015.
 */
public class FullDetailsContactUnitTest extends ApplicationTestCase<Application> {
    public FullDetailsContactUnitTest() {
        super(Application.class);
    }


    /**
     * Check full details contact data
     */
    public void testFullDetailsContactDataNotNull() {

        FullDetailsContact contact = new FullDetailsContact();

        contact.setName("John");
        contact.setPhone("123-542-543");
        contact.setEmail("john@gmail.com");
        contact.setCity("Ljubljana");
        contact.setCountry("Slovenia");
        contact.setEmail_type("type1");
        contact.setId("001");
        contact.setImType("imType");
        contact.setNote("Notes...");
        contact.setOrgName("OrgName");
        contact.setPoBox("PoBox");
        contact.setPostalCode("1000");
        contact.setState("SL");
        contact.setStreet("Street 1a");
        contact.setTitle("Title");
        contact.setImName("ImName");
        contact.setType("type1");

        assertEquals(true, !contact.getName().isEmpty());
        assertEquals(true, !contact.getPhone().isEmpty());
        assertEquals(true, !contact.getEmail().isEmpty());

        assertEquals(true, !contact.getCity().isEmpty());
        assertEquals(true, !contact.getCountry().isEmpty());
        assertEquals(true, !contact.getEmail_type().isEmpty());

        assertEquals(true, !contact.getId().isEmpty());
        assertEquals(true, !contact.getImType().isEmpty());
        assertEquals(true, !contact.getNote().isEmpty());

        assertEquals(true, !contact.getOrgName().isEmpty());
        assertEquals(true, !contact.getPoBox().isEmpty());
        assertEquals(true, !contact.getPostalCode().isEmpty());
        assertEquals(true, !contact.getType().isEmpty());

        assertEquals(true, !contact.getState().isEmpty());
        assertEquals(true, !contact.getStreet().isEmpty());
        assertEquals(true, !contact.getTitle().isEmpty());
        assertEquals(true, !contact.getImName().isEmpty());


    }


}