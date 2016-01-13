package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Address;
import com.yesgraph.android.services.ContactShareService;
import com.yesgraph.android.services.FacebookShareService;
import com.yesgraph.android.services.TwitterShareService;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mock;

/**
 * Created by Klemen on 13.1.2016.
 */
public class ShareServiceUnitTest extends ApplicationTestCase<Application> {
    public ShareServiceUnitTest() {
        super(Application.class);
    }

    private Context context;

    @Override
    protected void setUp() throws Exception {
        context = getContext();
    }

    public void testCheckFacebookShareService() {

        int color = context.getResources().getColor(R.color.colorFacebook);
        Drawable icon = context.getResources().getDrawable(R.drawable.facebook);
        String title = "Facebook";

        FacebookShareService facebookShareService = new FacebookShareService(context);

        facebookShareService.setColor(color);
        facebookShareService.setTitle(title);
        facebookShareService.setIcon(icon);
        facebookShareService.setContext(context);

        assertEquals(color, facebookShareService.getColor());
        assertEquals(icon, facebookShareService.getIcon());
        assertEquals(title, facebookShareService.getTitle());
        assertNotNull(facebookShareService.getContext());

    }

    public void testCheckTwitterShareService() {

        int color = context.getResources().getColor(R.color.colorTwitter);
        Drawable icon = context.getResources().getDrawable(R.drawable.twitter);
        String title = "Twitter";

        TwitterShareService twitterShareService = new TwitterShareService(context);

        twitterShareService.setColor(color);
        twitterShareService.setTitle(title);
        twitterShareService.setIcon(icon);
        twitterShareService.setContext(context);

        assertEquals(color, twitterShareService.getColor());
        assertEquals(icon, twitterShareService.getIcon());
        assertEquals(title, twitterShareService.getTitle());
        assertNotNull(twitterShareService.getContext());

    }

}