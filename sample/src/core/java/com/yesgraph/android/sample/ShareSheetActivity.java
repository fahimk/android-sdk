package com.yesgraph.android.sample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yesgraph.android.R;
import com.yesgraph.android.activity.ContactsActivity;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.Visual;

public class ShareSheetActivity extends AppCompatActivity {

//    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "qtlq3xsjIrP0RpMznEnd3mFx0";
//    private static final String TWITTER_SECRET = " 8wwPxgwlN1CaDKyWMGCLmoRYroV0SHHEOsyWJByyutHym5yfCY";

    private YesGraph application;
    private Context context;
    private Toolbar toolbar;
    private TextView shareText, facebookText, twitterText, contactsText, toolbarTitle;
    private FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_share_sheet);
        application = (YesGraph) getApplication();
        fontManager = FontManager.getInstance();
        setToolbar();
        context = this;
    }


    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getCustomTheme().getMainForegroundColor()));
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
        toolbarTitle.setText(getResources().getString(R.string.share_sheet));
        toolbarTitle.setTextColor(application.getCustomTheme().getBackArrowColor());
        if (!application.getCustomTheme().getFont().isEmpty()) {
            fontManager.setFont(toolbarTitle, application.getCustomTheme().getFont());
        }
    }

    private Drawable getColoredArrow() {
        Drawable arrowDrawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        Drawable wrapped = DrawableCompat.wrap(arrowDrawable);

        if (arrowDrawable != null && wrapped != null) {
            // This should avoid tinting all the arrows
            arrowDrawable.mutate();
            DrawableCompat.setTint(wrapped, application.getCustomTheme().getBackArrowColor());
        }

        return wrapped;
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
    public void onResume() {
        super.onResume();

        setBackground();
        setCopyLinkText();
        setShareIconsAndText();
        setActionBar();
        setShareText();
    }

    private void setShareText() {
        shareText = (TextView) findViewById(R.id.shareText);
        shareText.setText(application.getShareText());
        shareText.setTextColor(application.getCustomTheme().getDarkFontColor());
        if (!application.getCustomTheme().getFont().isEmpty()) {
            fontManager.setFont(shareText, application.getCustomTheme().getFont());
        }
    }

    private void setActionBar() {
        String color=String.format("#%06X", (0xFFFFFF & application.getCustomTheme().getLightFontColor()));
        getSupportActionBar().setTitle(Html.fromHtml("<FONT color='" + color + "'>" + getResources().getString(R.string.share_sheet) + "</FONT>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getCustomTheme().getMainForegroundColor()));
    }

    private void setShareIconsAndText() {
        facebookText = (TextView) findViewById(R.id.textFacebook);
        twitterText = (TextView) findViewById(R.id.textTwitter);
        contactsText = (TextView) findViewById(R.id.textContacts);

        facebookText.setTextColor(application.getCustomTheme().getDarkFontColor());
        twitterText.setTextColor(application.getCustomTheme().getDarkFontColor());
        contactsText.setTextColor(application.getCustomTheme().getDarkFontColor());

        if (!application.getCustomTheme().getFont().isEmpty()) {
            fontManager.setFont(facebookText, application.getCustomTheme().getFont());
            fontManager.setFont(twitterText, application.getCustomTheme().getFont());
            fontManager.setFont(contactsText, application.getCustomTheme().getFont());
        }

        LinearLayout facebookLayout = (LinearLayout) findViewById(R.id.layoutFacebook);
        LinearLayout twitterLayout = (LinearLayout) findViewById(R.id.layoutTwitter);
        LinearLayout contactsLayout = (LinearLayout) findViewById(R.id.layoutContacts);

        LinearLayout facebookCircleLayout = (LinearLayout) findViewById(R.id.layoutFacebookCircle);
        LinearLayout twitterCircleLayout = (LinearLayout) findViewById(R.id.layoutTwitterCircle);
        LinearLayout contactsCircleLayout = (LinearLayout) findViewById(R.id.layoutContactsCircle);

        String shareButtonShape = application.getCustomTheme().getShareButtonsShape();
        if (shareButtonShape.equals("circle")) {
            facebookCircleLayout.setBackgroundResource(R.drawable.circle);
            twitterCircleLayout.setBackgroundResource(R.drawable.circle);
            contactsCircleLayout.setBackgroundResource(R.drawable.circle);
        } else if (shareButtonShape.equals("square")) {
            facebookCircleLayout.setBackgroundResource(R.drawable.square_shape);
            twitterCircleLayout.setBackgroundResource(R.drawable.square_shape);
            contactsCircleLayout.setBackgroundResource(R.drawable.square_shape);
        } else if (shareButtonShape.equals("rounded_square")) {
            facebookCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
            twitterCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
            contactsCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
        }


        GradientDrawable drawableF = (GradientDrawable) facebookCircleLayout.getBackground();
        drawableF.setColor(getResources().getColor(R.color.colorFacebook));

        GradientDrawable drawableT = (GradientDrawable) twitterCircleLayout.getBackground();
        drawableT.setColor(getResources().getColor(R.color.colorTwitter));

        GradientDrawable drawableC = (GradientDrawable) contactsCircleLayout.getBackground();
        drawableC.setColor(application.getCustomTheme().getMainForegroundColor());

        ImageView facebookImage = (ImageView) findViewById(R.id.imageFacebook);
        ImageView twitterImage = (ImageView) findViewById(R.id.imageTwitter);
        ImageView contactsImage = (ImageView) findViewById(R.id.imageContacts);

        facebookLayout.setClickable(true);
        twitterLayout.setClickable(true);
        contactsLayout.setClickable(true);


        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactsActivity.class);
                startActivity(intent);
            }
        });

        if (!application.isFacebookSignedIn()) {
            facebookLayout.setVisibility(View.GONE);
        }

        if (!application.isTwitterSignedIn()) {
            twitterLayout.setVisibility(View.GONE);
        }
    }

    private void setCopyLinkText() {
        final TextView copyLinkText = (TextView) findViewById(R.id.textCopyLink);
        final TextView copyButtonText = (TextView) findViewById(R.id.textCopyButton);
        RelativeLayout copyLinkLayout = (RelativeLayout) findViewById(R.id.copyLinkLayout);

        copyLinkLayout.setBackgroundResource(R.drawable.rounded_corners);

        GradientDrawable drawable = (GradientDrawable) copyLinkLayout.getBackground();
        drawable.setStroke(Visual.getPixelsFromDp(context, 3), application.getCustomTheme().getMainForegroundColor());
        drawable.setColor(application.getCustomTheme().getReferralBunnerBackgroundColor());

        copyLinkText.setText(application.getCopyLinkText());
        copyLinkText.setTextColor(application.getCustomTheme().getDarkFontColor());
        copyLinkText.setTextSize(TypedValue.COMPLEX_UNIT_SP, application.getCustomTheme().getReferralTextSize());
        copyLinkText.setClickable(true);
        copyLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.share_link_copy), copyLinkText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
            }
        });
        copyButtonText.setText(application.getCopyButtonText());
        if (application.getCustomTheme().getCopyButtonColor() != 0) {
            copyButtonText.setTextColor(application.getCustomTheme().getCopyButtonColor());
        } else {
            copyButtonText.setTextColor(application.getCustomTheme().getMainForegroundColor());
        }
        copyButtonText.setTextSize(TypedValue.COMPLEX_UNIT_SP, application.getCustomTheme().getReferralTextSize());
        copyButtonText.setClickable(true);
        copyButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.share_link_copy), copyLinkText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
                copyButtonText.setText(R.string.button_copied_text);
            }
        });

        if (!application.getCustomTheme().getFont().isEmpty()) {
            fontManager.setFont(copyLinkText, application.getCustomTheme().getFont());
            fontManager.setFont(copyButtonText, application.getCustomTheme().getFont());
        }
    }

    private void setBackground() {
        RelativeLayout masterLayout = (RelativeLayout) findViewById(R.id.layoutMaster);
        masterLayout.setBackgroundColor(application.getCustomTheme().getMainBackgroundColor());
    }
}