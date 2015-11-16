package com.yesgraph.android;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bettervectordrawable.VectorDrawableCompat;
import com.yesgraph.android.utils.Visual;

public class ShareSheetActivity extends AppCompatActivity {

    private YesGraph application;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        application = (YesGraph) getApplicationContext();
        context = this;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        VectorDrawableCompat.enableResourceInterceptionFor(getResources(),
                R.drawable.facebook,
                R.drawable.twitter,
                R.drawable.phone);

        setBackground();
        setCopyLinkText();
        setShareIconsAndText();
        setActionBar();
        setShareText();
    }

    private void setShareText() {
        TextView shareText = (TextView)findViewById(R.id.shareText);
        shareText.setText(application.getShareText());
        shareText.setTextColor(application.getDarkFontColor());
    }

    private void setActionBar() {
        String color=String.format("#%06X", (0xFFFFFF & application.getLightFontColor()));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='"+color+"'>"+getSupportActionBar().getTitle()+"</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getMainForegroundColor()));
    }

    private void setShareIconsAndText()
    {
        TextView facebookText = (TextView)findViewById(R.id.textFacebook);
        TextView twitterText = (TextView)findViewById(R.id.textTwitter);
        TextView contactsText = (TextView)findViewById(R.id.textContacts);

        facebookText.setTextColor(application.getDarkFontColor());
        twitterText.setTextColor(application.getDarkFontColor());
        contactsText.setTextColor(application.getDarkFontColor());

        LinearLayout facebookLayout = (LinearLayout)findViewById(R.id.layoutFacebook);
        LinearLayout twitterLayout = (LinearLayout)findViewById(R.id.layoutTwitter);
        LinearLayout contactsLayout = (LinearLayout)findViewById(R.id.layoutContacts);

        LinearLayout facebookCircleLayout = (LinearLayout)findViewById(R.id.layoutFacebookCircle);
        LinearLayout twitterCircleLayout = (LinearLayout)findViewById(R.id.layoutTwitterCircle);
        LinearLayout contactsCircleLayout = (LinearLayout)findViewById(R.id.layoutContactsCircle);

        facebookCircleLayout.setBackgroundResource(R.drawable.circle);
        GradientDrawable drawableF = (GradientDrawable) facebookCircleLayout.getBackground();
        drawableF.setColor(getResources().getColor(R.color.colorFacebook));

        twitterCircleLayout.setBackgroundResource(R.drawable.circle);
        GradientDrawable drawableT = (GradientDrawable) twitterCircleLayout.getBackground();
        drawableT.setColor(getResources().getColor(R.color.colorTwitter));

        contactsCircleLayout.setBackgroundResource(R.drawable.circle);
        GradientDrawable drawableC = (GradientDrawable) contactsCircleLayout.getBackground();
        drawableC.setColor(application.getMainForegroundColor());

        ImageView facebookImage = (ImageView)findViewById(R.id.imageFacebook);
        ImageView twitterImage = (ImageView)findViewById(R.id.imageTwitter);
        ImageView contactsImage = (ImageView)findViewById(R.id.imageContacts);

        facebookLayout.setClickable(true);
        twitterLayout.setClickable(true);
        contactsLayout.setClickable(true);

        facebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        twitterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(!application.isFacebookSignedIn())
        {
            facebookLayout.setVisibility(View.GONE);
        }

        if(!application.isTwitterSignedIn())
        {
            twitterLayout.setVisibility(View.GONE);
        }
    }

    private void setCopyLinkText()
    {
        final TextView copyLinkText = (TextView)findViewById(R.id.textCopyLink);

        copyLinkText.setBackgroundResource(R.drawable.rounded_corners);

        GradientDrawable drawable = (GradientDrawable) copyLinkText.getBackground();
        drawable.setStroke(Visual.getPixelsFromDp(context, 3), application.getMainForegroundColor());

        copyLinkText.setText(application.getCopyLinkText());
        copyLinkText.setTextColor(application.getDarkFontColor());
        copyLinkText.setClickable(true);
        copyLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(getString(R.string.share_link_copy), copyLinkText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.copy_to_clipboard,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBackground()
    {
        RelativeLayout masterLayout = (RelativeLayout)findViewById(R.id.layoutMaster);
        masterLayout.setBackgroundColor(application.getMainBackgroundColor());
    }


}
