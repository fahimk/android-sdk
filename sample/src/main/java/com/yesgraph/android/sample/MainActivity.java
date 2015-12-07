package com.yesgraph.android.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.YSGTheme;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "qtlq3xsjIrP0RpMznEnd3mFx0";
//    private static final String TWITTER_SECRET = " 8wwPxgwlN1CaDKyWMGCLmoRYroV0SHHEOsyWJByyutHym5yfCY";


    private Context context;
    private YesGraph yesGraphApplication;
    private YSGTheme ysgTheme;
    private TextView description5_3, description5_4, description5_1, description5_2;
    private Button btn_tryYesGraph, btn_tryYesGraph2, btn_tryYesGraph3, btn_tryYesGraph4;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        context = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        yesGraphApplication = (YesGraph) getApplicationContext();

//        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        initUI();

//        ysgTheme = new YSGTheme();
//        ysgTheme.setThemeColor(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//        ysgTheme.setReferralTextSize(14);
//        ysgTheme.setShareButtonsShape("rounded_square");
//        ysgTheme.setFonts("Pacifico.ttf");
//        yesGraphApplication.setYsgTheme(ysgTheme);
    }

    private void initUI() {

        btn_tryYesGraph = (Button) findViewById(R.id.btn_tryYesGraph);
        btn_tryYesGraph2 = (Button) findViewById(R.id.btn_tryYesGraph2);
        btn_tryYesGraph3 = (Button) findViewById(R.id.btn_tryYesGraph3);
        btn_tryYesGraph4 = (Button) findViewById(R.id.btn_tryYesGraph4);

        description5_1 = (TextView) findViewById(R.id.description5_1);
        description5_2 = (TextView) findViewById(R.id.description5_2);

        description5_3 = (TextView) findViewById(R.id.description5_3);
        description5_3.setText(Html.fromHtml(context.getResources().getString(R.string.description5_3)));
        description5_3.setMovementMethod(LinkMovementMethod.getInstance());

        description5_4 = (TextView) findViewById(R.id.description5_4);
        description5_4.setText(Html.fromHtml(context.getResources().getString(R.string.description5_4)));
        description5_4.setMovementMethod(LinkMovementMethod.getInstance());

        description5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        description5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        description5_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        description5_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        btn_tryYesGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.activity.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
        btn_tryYesGraph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.activity.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
        btn_tryYesGraph3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.activity.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
        btn_tryYesGraph4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.activity.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
    }
}
