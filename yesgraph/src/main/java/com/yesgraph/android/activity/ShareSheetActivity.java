package com.yesgraph.android.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Authenticate;
import com.yesgraph.android.services.BasicShareService;
import com.yesgraph.android.services.ContactShareService;
import com.yesgraph.android.services.FacebookShareService;
import com.yesgraph.android.services.TwitterShareService;
import com.yesgraph.android.utils.StorageKeyValueManager;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactManager;
import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.SharedPreferencesManager;
import com.yesgraph.android.utils.Visual;

import java.util.ArrayList;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;

public class ShareSheetActivity extends AppCompatActivity {

//    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "qtlq3xsjIrP0RpMznEnd3mFx0";
//    private static final String TWITTER_SECRET = " 8wwPxgwlN1CaDKyWMGCLmoRYroV0SHHEOsyWJByyutHym5yfCY";

    private YesGraph application;
    private Context context;
    private Toolbar toolbar;
    private TextView shareText, facebookText, twitterText, contactsText, toolbarTitle;
    private FontManager fontManager;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private String TWITTER_KEY = "";
    private String TWITTER_SECRET = "";
    private ArrayList<Object> shareServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        checkIsTimeToRefreshAddressBook();
    }

    private void init() {

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTwitterKeys();
        setContentView(R.layout.activity_share_sheet);
        FacebookSdk.sdkInitialize(this);
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        application = (YesGraph) getApplication();
        fontManager = FontManager.getInstance();
        setToolbar();
        context = this;
        new StorageKeyValueManager(context).setInviteNumber(0L);
        shareServices=application.getShareServices();
    }

    private void checkIsTimeToRefreshAddressBook() {

        new StorageKeyValueManager(context).setContactsUploading(false);
        Boolean isReadContactsPermission = new PermissionGrantedManager(context).isReadContactsPermission();
        Boolean isContactsUploading =new StorageKeyValueManager(context).isContactsUploading();

        if (!isContactsUploading && timeToRefreshAddressBook() && isReadContactsPermission && application.isOnline()) {
            try {
                new StorageKeyValueManager(context).setContactsUploading(true);
                new AddressBook().updateAddressBookWithLimitedContacts(context, new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        new StorageKeyValueManager(context).setContactsUploading(false);
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean timeToRefreshAddressBook() {
        long lastContactsUpload = new StorageKeyValueManager(context).getContactLastUpload();

        if(lastContactsUpload < System.currentTimeMillis() - (Constants.HOURS_BETWEEN_UPLOAD * 60 * 60 * 1000))
            return true;
        else
            return false;
    }

    private void setTwitterKeys() {
        Intent intent = getIntent();
        if (intent != null) {
            TWITTER_KEY = intent.getStringExtra("twitter_key");
            TWITTER_SECRET = intent.getStringExtra("twitter_secret");
            if (TWITTER_KEY != null && TWITTER_SECRET != null) {
                if (!TWITTER_KEY.isEmpty() && !TWITTER_SECRET.isEmpty()) {
                    TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                    Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
                }
            }
        }
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
    public void onBackPressed()
    {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        super.onBackPressed();
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
        shareText.setText(application.getCustomTheme().getShareText(context));
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
        LinearLayout layoutShareArray = (LinearLayout)findViewById(R.id.layoutShareArray);

        layoutShareArray.removeAllViews();

        for(Object shareService : shareServices)
        {
            if(shareService instanceof BasicShareService)
            {
                if(shareService instanceof FacebookShareService)
                {
                    shareService = new FacebookShareService(context);
                }
                else if(shareService instanceof TwitterShareService)
                {
                    shareService = new TwitterShareService(context);
                }
                else if(shareService instanceof ContactShareService)
                {
                    shareService = new ContactShareService(context);
                }


                LayoutInflater inflater = LayoutInflater.from(context);
                View inflatedLayout = inflater.inflate(R.layout.item_share_service, null, false);

                LinearLayout ll = (LinearLayout)inflatedLayout;
                ll.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

                layoutParams.setMargins(3, 3, 3, 3);
                ll.setLayoutParams(layoutParams);

                layoutShareArray.addView(ll);

                TextView shareText = (TextView) inflatedLayout.findViewById(R.id.textShare);
                shareText.setText(((BasicShareService) shareService).getTitle());
                shareText.setTextColor(application.getCustomTheme().getDarkFontColor());
                if (!application.getCustomTheme().getFont().isEmpty()) {
                    fontManager.setFont(shareText, application.getCustomTheme().getFont());
                }

                LinearLayout shareLayout = (LinearLayout) inflatedLayout.findViewById(R.id.layoutShare);
                LinearLayout shareCircleLayout = (LinearLayout) inflatedLayout.findViewById(R.id.layoutShareCircle);

                String shareButtonShape = application.getCustomTheme().getShareButtonsShape();
                if (shareButtonShape.equals("circle")) {
                    shareCircleLayout.setBackgroundResource(R.drawable.circle);
                } else if (shareButtonShape.equals("square")) {
                    shareCircleLayout.setBackgroundResource(R.drawable.square_shape);
                } else if (shareButtonShape.equals("rounded_square")) {
                    shareCircleLayout.setBackgroundResource(R.drawable.rounded_square_shape);
                }

                GradientDrawable drawableC = (GradientDrawable) shareCircleLayout.getBackground();
                drawableC.setColor(((BasicShareService) shareService).getColor());

                ImageView shareImage = (ImageView) inflatedLayout.findViewById(R.id.imageShare);
                shareImage.setImageDrawable(((BasicShareService) shareService).getIcon());

                final Runnable runnable=((BasicShareService) shareService).getRunnable();

                shareLayout.setClickable(true);
                shareLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runnable.run();
                    }
                });

            }

        }

        /*facebookText = (TextView) findViewById(R.id.textFacebook);
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

        facebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToFacebook();
            }
        });
        twitterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposer.Builder builder = new TweetComposer.Builder(context).text(application.getCustomTheme().getCopyLinkText(context));
                builder.show();
            }
        });
        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactsActivity.class);
                startActivity(intent);
            }
        });

        if (!application.getCustomTheme().isFacebookSignedIn()) {
            facebookLayout.setVisibility(View.GONE);
        }

        if (!application.getCustomTheme().isTwitterSignedIn()) {
            twitterLayout.setVisibility(View.GONE);
        }*/
    }

    private void setCopyLinkText() {
        final TextView copyLinkText = (TextView) findViewById(R.id.textCopyLink);
        final TextView copyButtonText = (TextView) findViewById(R.id.textCopyButton);
        RelativeLayout copyLinkLayout = (RelativeLayout) findViewById(R.id.copyLinkLayout);

        copyLinkLayout.setBackgroundResource(R.drawable.rounded_corners);

        GradientDrawable drawable = (GradientDrawable) copyLinkLayout.getBackground();
        drawable.setStroke(Visual.getPixelsFromDp(context, 3), application.getCustomTheme().getMainForegroundColor());
        drawable.setColor(application.getCustomTheme().getReferralBunnerBackgroundColor());

        copyLinkText.setText(application.getCustomTheme().getCopyLinkText(context));
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
        copyButtonText.setText(application.getCustomTheme().getCopyButtonText(context));
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

    private void shareToFacebook() {
        try {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        //.setContentTitle("Content Title")
                        //.setContentDescription("Content Description")
                        .setContentUrl(Uri.parse(application.getCustomTheme().getCopyLinkText(context)))
                        .build();

                shareDialog.show(linkContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.initialize_facebook_sdk), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 64207) {
            if (resultCode == RESULT_OK)
                Toast.makeText(context, context.getResources().getString(R.string.facebook_share_success), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, context.getResources().getString(R.string.facebook_share_failure), Toast.LENGTH_SHORT).show();
        }
    }
}
