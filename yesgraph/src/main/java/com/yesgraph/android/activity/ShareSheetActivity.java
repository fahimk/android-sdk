package com.yesgraph.android.activity;

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

import com.bettervectordrawable.VectorDrawableCompat;
import com.yesgraph.android.ContactsActivity;
import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.Visual;
import com.yesgraph.android.utils.YSGTheme;

public class ShareSheetActivity extends AppCompatActivity {

    private YesGraph application;
    private Context context;
    private Toolbar toolbar;
    private TextView shareText, facebookText, twitterText, contactsText;
    private FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_sheet);
        setToolbar();

        application = (YesGraph) getApplicationContext();
        context = this;
        fontManager = FontManager.getInstance();

    }
    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(YSGTheme.getMainForegroundColor()));
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
    }

    private Drawable getColoredArrow() {
        Drawable arrowDrawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        Drawable wrapped = DrawableCompat.wrap(arrowDrawable);

        if (arrowDrawable != null && wrapped != null) {
            // This should avoid tinting all the arrows
            arrowDrawable.mutate();
            DrawableCompat.setTint(wrapped, YSGTheme.getBackArrowColor());
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
        shareText = (TextView)findViewById(R.id.shareText);
        shareText.setText(application.getShareText());
        shareText.setTextColor(YSGTheme.getDarkFontColor());
        if(!Constants.FONT.isEmpty()){
            fontManager.setFont(shareText,YSGTheme.getFont());
        }
    }

    private void setActionBar() {
        String color=String.format("#%06X", (0xFFFFFF & YSGTheme.getLightFontColor()));
        getSupportActionBar().setTitle(Html.fromHtml("<FONT color='" + color + "'>" + getSupportActionBar().getTitle() + "</FONT>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(YSGTheme.getMainForegroundColor()));
    }

    private void setShareIconsAndText()
    {
        facebookText = (TextView)findViewById(R.id.textFacebook);
        twitterText = (TextView)findViewById(R.id.textTwitter);
        contactsText = (TextView)findViewById(R.id.textContacts);

        facebookText.setTextColor(YSGTheme.getDarkFontColor());
        twitterText.setTextColor(YSGTheme.getDarkFontColor());
        contactsText.setTextColor(YSGTheme.getDarkFontColor());

        if(!Constants.FONT.isEmpty()){
            fontManager.setFont(facebookText,YSGTheme.getFont());
            fontManager.setFont(twitterText,YSGTheme.getFont());
            fontManager.setFont(contactsText,YSGTheme.getFont());
        }

        LinearLayout facebookLayout = (LinearLayout)findViewById(R.id.layoutFacebook);
        LinearLayout twitterLayout = (LinearLayout)findViewById(R.id.layoutTwitter);
        LinearLayout contactsLayout = (LinearLayout)findViewById(R.id.layoutContacts);

        LinearLayout facebookCircleLayout = (LinearLayout)findViewById(R.id.layoutFacebookCircle);
        LinearLayout twitterCircleLayout = (LinearLayout)findViewById(R.id.layoutTwitterCircle);
        LinearLayout contactsCircleLayout = (LinearLayout)findViewById(R.id.layoutContactsCircle);

        String shareButtonShape = YSGTheme.getShareButtonsShape();
        if(shareButtonShape.equals("circle")){
            facebookCircleLayout.setBackgroundResource(R.drawable.circle);
            twitterCircleLayout.setBackgroundResource(R.drawable.circle);
            contactsCircleLayout.setBackgroundResource(R.drawable.circle);
        } else if(shareButtonShape.equals("square")){
            facebookCircleLayout.setBackgroundResource(R.drawable.square_shape);
            twitterCircleLayout.setBackgroundResource(R.drawable.square_shape);
            contactsCircleLayout.setBackgroundResource(R.drawable.square_shape);
        } else if(shareButtonShape.equals("rounded_square")){
            facebookCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
            twitterCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
            contactsCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
        }


        GradientDrawable drawableF = (GradientDrawable) facebookCircleLayout.getBackground();
        drawableF.setColor(getResources().getColor(R.color.colorFacebook));

        GradientDrawable drawableT = (GradientDrawable) twitterCircleLayout.getBackground();
        drawableT.setColor(getResources().getColor(R.color.colorTwitter));

        GradientDrawable drawableC = (GradientDrawable) contactsCircleLayout.getBackground();
        drawableC.setColor(YSGTheme.getMainForegroundColor());

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
                Intent intent = new Intent(context, ContactsActivity.class);
                startActivity(intent);
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
        final TextView copyButtonText = (TextView) findViewById(R.id.textCopyButton);
        RelativeLayout copyLinkLayout = (RelativeLayout) findViewById(R.id.copyLinkLayout);

        copyLinkLayout.setBackgroundResource(R.drawable.rounded_corners);

        GradientDrawable drawable = (GradientDrawable) copyLinkLayout.getBackground();
        drawable.setStroke(Visual.getPixelsFromDp(context, 3), YSGTheme.getMainForegroundColor());
        drawable.setColor(YSGTheme.getReferralBunnerBackgroundColor());

        copyLinkText.setText(application.getCopyLinkText());
        copyLinkText.setTextColor(YSGTheme.getDarkFontColor());
        copyLinkText.setTextSize(TypedValue.COMPLEX_UNIT_SP, YSGTheme.getReferralTextSize());
        copyLinkText.setClickable(true);
        copyLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(getString(R.string.share_link_copy), copyLinkText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
            }
        });
        copyButtonText.setText(application.getCopyButtonText());
        copyButtonText.setTextColor(YSGTheme.getCopyButtonColor());
        copyButtonText.setClickable(true);
        copyButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(getString(R.string.share_link_copy), copyLinkText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
            }
        });

        if(!Constants.FONT.isEmpty()){
            fontManager.setFont(copyLinkText,YSGTheme.getFont());
            fontManager.setFont(copyButtonText,YSGTheme.getFont());
        }
    }

    private void setBackground()
    {
        RelativeLayout masterLayout = (RelativeLayout)findViewById(R.id.layoutMaster);
        masterLayout.setBackgroundColor(YSGTheme.getMainBackgroundColor());
    }


}
