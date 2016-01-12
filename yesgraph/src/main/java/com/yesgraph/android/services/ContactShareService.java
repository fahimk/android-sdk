package com.yesgraph.android.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.yesgraph.android.R;
import com.yesgraph.android.activity.ContactsActivity;
import com.yesgraph.android.application.YesGraph;

/**
 * Created by marko on 12/01/16.
 */
public class ContactShareService extends BasicShareService {
    public ContactShareService(final Context context)
    {
        YesGraph yesGraph;

        try {
            Activity activity = (Activity) context;
            yesGraph = (YesGraph) activity.getApplication();
        }
        catch (Exception e)
        {
            yesGraph = (YesGraph) context;
        }
        super.setContext(context);
        super.setIcon(context.getResources().getDrawable(R.drawable.phone));
        super.setColor(yesGraph.getCustomTheme().getMainForegroundColor());
        super.setTitle(context.getResources().getString(R.string.contacts));
        super.setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(context, ContactsActivity.class);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
