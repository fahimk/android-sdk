package com.yesgraph.android.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;

/**
 * Created by marko on 12/01/16.
 */
public class FacebookShareService extends BasicShareService {
    public FacebookShareService(final Context context)
    {
        super.setContext(context);
        super.setIcon(context.getResources().getDrawable(R.drawable.facebook));
        super.setColor(context.getResources().getColor(R.color.colorFacebook));
        super.setTitle(context.getResources().getString(R.string.facebook));
        super.setRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Activity activity = (Activity) context;
                    YesGraph yesGraph=(YesGraph) activity.getApplication();

                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(yesGraph.getCustomTheme().getCopyLinkText(context)))
                                .build();

                        ShareDialog shareDialog;
                        shareDialog = new ShareDialog(activity);
                        shareDialog.show(linkContent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, context.getResources().getString(R.string.initialize_facebook_sdk), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
