package com.yesgraph.android.services;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;

/**
 * Created by marko on 12/01/16.
 */
public class TwitterShareService extends BasicShareService {
    public TwitterShareService(final Context context)
    {
        super.setContext(context);
        super.setIcon(context.getResources().getDrawable(R.drawable.twitter));
        super.setColor(context.getResources().getColor(R.color.colorTwitter));
        super.setTitle(context.getResources().getString(R.string.twitter));
        super.setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Activity activity = (Activity) context;
                    YesGraph yesGraph=(YesGraph) activity.getApplication();

                    TweetComposer.Builder builder = new TweetComposer.Builder(context).text(yesGraph.getCustomTheme().getCopyLinkText(context));
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
