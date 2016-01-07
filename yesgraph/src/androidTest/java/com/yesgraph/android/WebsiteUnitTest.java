package com.yesgraph.android;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Ims;
import com.yesgraph.android.models.Website;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Klemen on 5.1.2016.
 */
public class WebsiteUnitTest extends ApplicationTestCase<Application> {
    public WebsiteUnitTest() {
        super(Application.class);
    }


    public void testCheckWebsiteData() {

        Website website = getWebsite();

        assertEquals("news", website.getType());
        assertEquals("www.newsurl.com", website.getUrl());

    }


    public void testConvertWebsiteToJson() throws JSONException {

        Website website = getWebsite();

        JSONObject json = website.toJSONObject();

        boolean hasType = json.has("type") && json.get("type") != null;
        boolean hasUrl = json.has("url") && json.get("url") != null;

        assertTrue(hasType);
        assertTrue(hasUrl);


    }

    @NonNull
    private Website getWebsite() {
        Website website = new Website();

        website.setType("news");
        website.setUrl("www.newsurl.com");
        return website;
    }

}