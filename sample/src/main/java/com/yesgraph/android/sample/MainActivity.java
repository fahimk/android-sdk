package com.yesgraph.android.sample;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.FavouriteContacts;
import com.yesgraph.android.models.FullDetailsContact;
import com.yesgraph.android.models.RecentlyContactedContact;
import com.yesgraph.android.utils.CustomTheme;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "qtlq3xsjIrP0RpMznEnd3mFx0";
//    private static final String TWITTER_SECRET = " 8wwPxgwlN1CaDKyWMGCLmoRYroV0SHHEOsyWJByyutHym5yfCY";


    private Context context;
    private YesGraph yesGraphApplication;
    private CustomTheme customTheme;
    private TextView description5_3, description5_4, description5_1, description5_2;

    private Button btn_tryYesGraph, btn_tryYesGraph2, btn_tryYesGraph3, btn_tryYesGraph4;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private SharedPreferences sharedPreferences;

    private List<RecentlyContactedContact> recContacts = null;
    private List<FavouriteContacts> favs = null;
    private AppCompatActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        setContentView(R.layout.activity_main);
        activity = this;
        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        yesGraphApplication = (YesGraph) getApplicationContext();
        yesGraphApplication.configureWithUserId("<USER_ID>");
        yesGraphApplication.configureWithClientKey("live-WzEsMCwic2FtcGxlX2FuZHJvaWRfMyJd.CTO9TA.FNjfXP89EMykC-t20NytFVJv-Zg");

        yesGraphApplication.setSource("Name", "+1 123 123 123", "Email");
        CustomTheme customTheme=new CustomTheme();
        customTheme.setNumberOfSuggestedContacts(5);
        customTheme.setCopyLinkText("www.yesgraph.com/#android");
        customTheme.setEmailSubject("We should check out YesGraph");
        customTheme.setEmailText("Check out YesGraph, they help apps grow: www.yesgraph.com/#android");
        customTheme.setSmsText("Check out YesGraph, they help apps grow: www.yesgraph.com/#android");

        yesGraphApplication.setCustomTheme(customTheme);
//        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        initUI();

//        customTheme = new CustomTheme();
//        customTheme.setThemeColor(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//        customTheme.setReferralTextSize(14);
//        customTheme.setShareButtonsShape("rounded_square");
//        customTheme.setFonts("Pacifico.ttf");
//        yesGraphApplication.setCustomTheme(customTheme);
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
                Intent intent = new Intent(context, com.yesgraph.android.sample.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
        btn_tryYesGraph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.sample.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
        btn_tryYesGraph3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.sample.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
        btn_tryYesGraph4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.yesgraph.android.sample.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    sharedPreferences.edit().putBoolean("contacts_permision_granted", true).commit();
                } else {
                    // Permission Denied
                    sharedPreferences.edit().putBoolean("contacts_permision_granted", false).commit();
                    Toast.makeText(MainActivity.this, "READ_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
