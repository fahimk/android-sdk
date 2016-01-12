package com.yesgraph.android.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.yesgraph.android.activity.ContactsActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.services.BasicShareService;

/**
 * Created by marko on 12/01/16.
 */
public class MyShareService extends BasicShareService {
    public MyShareService(final Context context)
    {
        super.setContext(context);
        super.setIcon(context.getResources().getDrawable(com.yesgraph.android.R.drawable.icon));
        super.setColor(Color.parseColor("#444444"));
        super.setTitle("Yesgraph");
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
