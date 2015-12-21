package com.yesgraph.android.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.adapters.ContactsAdapter;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.Source;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.network.Invite;
import com.yesgraph.android.network.SuggestionsShown;
import com.yesgraph.android.utils.AlphabetSideIndexManager;
import com.yesgraph.android.utils.CacheManager;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.RankingContactsManager;
import com.yesgraph.android.utils.SenderManager;
import com.yesgraph.android.utils.SharedPreferencesManager;
import com.yesgraph.android.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit.http.HEAD;

/**
 * Created by marko on 17/11/15.
 */
public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private YesGraph application;
    private Context context;
    private RecyclerView contactsList;
    private ContactsAdapter adapter;
    private ArrayList<Object> items;
    private ArrayList<Object> itemsOld;
    private LinearLayout searchBar;
    private EditText search;
    private ArrayList<RegularContact> contacts;
    private Toolbar toolbar;
    private FontManager fontManager;
    private TextView toolbarTitle;
    private LinearLayout indexLayout;
    private FrameLayout contactsListContent;

    private SharedPreferences sharedPreferences;

    private ContactList contactList;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private AlphabetSideIndexManager alphabeticalIndexManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        init();

        checkUpload();

    }

    private void checkUpload() {

        boolean isReadContactPermissionGranted = new PermissionGrantedManager(context).isReadContactsPermission();
        boolean checkContactsReadPermission = new PermissionGrantedManager(context).checkContactsReadPermission();

        if (isReadContactPermissionGranted && checkContactsReadPermission)
        {
            getContactsOnThread();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void init() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        application = (YesGraph) getApplication();
        fontManager = FontManager.getInstance();
        setToolbar();
        context = this;

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contactsListContent = (FrameLayout) findViewById(R.id.contactsListContent);
        contactsList = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.content_contact_list, null);
        contactsList.setLayoutManager(new GridLayoutManager(ContactsActivity.this, 1, GridLayoutManager.VERTICAL, false));
        contactsList.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_DEFAULT);
        contactsList.setVerticalScrollBarEnabled(true);
        contactsListContent.addView(contactsList);

        indexLayout = (LinearLayout) findViewById(R.id.side_index);
        indexLayout.setBackgroundColor(application.getCustomTheme().getMainBackgroundColor());

        search = (EditText)findViewById(R.id.search);
        search.setTextColor(application.getCustomTheme().getLightFontColor());
        search.getBackground().setColorFilter(application.getCustomTheme().getLightFontColor(), PorterDuff.Mode.SRC_ATOP);
        search.setHintTextColor(application.getCustomTheme().getLightFontColor());
        searchBar = (LinearLayout)findViewById(R.id.searhBar);
        searchBar.setBackgroundColor(application.getCustomTheme().getMainForegroundColor());
        //contactsList = (RecyclerView)findViewById(R.id.contactsList);
        contactsList.setBackgroundColor(application.getCustomTheme().getRowBackgroundColor());

        setProgressVisibility(true);

        if(!application.getCustomTheme().getFont().isEmpty()){
            fontManager.setFont(search,application.getCustomTheme().getFont());
        }

        items = new ArrayList<>();

        alphabeticalIndexManager = new AlphabetSideIndexManager(this,application);

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                getContacts(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


    }

    private Boolean checkIfUploadNeeded() {

        Boolean isContactCached = !new CacheManager(context).getContactCache().equals("");
        Boolean isReadContactPermissionGranted = new PermissionGrantedManager(context).isReadContactsPermission();
        Boolean checkContactsReadPermission = new PermissionGrantedManager(context).checkContactsReadPermission();

        if(!isContactCached && isReadContactPermissionGranted && checkContactsReadPermission && application.isOnline() && !sharedPreferences.getBoolean("contacts_uploading", false))

        {
            ArrayList<RankedContact> list=ContactRetriever.readYSGContacts(ContactsActivity.this);

            ContactList contactList=new ContactList();
            contactList.setEntries(list);
            contactList.setSource(new Source());
            contactList.setUseSuggestions(true);

            final AddressBook addressBook =new AddressBook();

            Log.i("#YSGCONTACTS_COUNT", "1count:" + contactList.getEntries().size());

            String userId = new SharedPreferencesManager(context).getString("user_id");

            addressBook.updateAddressBookWithContactListForUserId(ContactsActivity.this, contactList, userId, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg)
                {
                    sharedPreferences.edit().putBoolean("contacts_uploading", false).commit();

                    if(msg.what== Constants.RESULT_OK)
                    {
                        try {
                            ContactsActivity.this.contactList =(ContactList)msg.obj;
                            getContacts("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            getContacts("");
                        }
                    }
                    else
                    {
                        getContacts("");
                    }
                    return false;
                }
            });
            return true;
        }
        else
        {
            return false;
        }
    }

    private void setProgressVisibility(boolean visible)
    {
        if(visible)
            ((LinearLayout)findViewById(R.id.progressLayout)).setVisibility(View.VISIBLE);
        else
            ((LinearLayout)findViewById(R.id.progressLayout)).setVisibility(View.GONE);
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getCustomTheme().getMainForegroundColor()));
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
        toolbarTitle.setText(getResources().getString(R.string.contacts_list));
        toolbarTitle.setTextColor(application.getCustomTheme().getBackArrowColor());
        if(!application.getCustomTheme().getFont().isEmpty()){
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
        final int invite = R.id.action_invite;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
            {
                int i = item.getItemId();
                if (i == R.id.action_invite) {
                    inviteContacts();
                    return true;
                } else {// If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
                }
            }
        }
    }

    /**
     * Send emails and sms to contacts
     */
    private void inviteContacts() {

        SenderManager senderManager = new SenderManager(items);

        senderManager.sendEmail(context, application);
        senderManager.sendSms(context, application);

        ArrayList<Contact> contacts = senderManager.getInvitedContacts();
        String userId = new SharedPreferencesManager(context).getString("user_id");

        Invite ysgInvite=new Invite();
        ysgInvite.updateInvitesSentForUser(context, contacts, userId, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);

        boolean isContactChecked = new ContactRetriever().isContactChecked(items);
        menu.getItem(0).setEnabled(isContactChecked);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return true;
    }

    private ArrayList<RegularContact> getContactsFromContactList()
    {
        return ContactRetriever.readContacts(context);
    }

    private void getContacts(final String filter) {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {

            }

            @Override
            protected Void doInBackground(Void... params)
            {
                setContactsFromCacheOrContactsProvider();

                if(filter.length()>0)
                {
                    setContactsByFilter(filter);
                }
                else
                {
                    setSuggestedContactsOnTheTop();
                }

                setupRecycler();
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {

            }
        }.execute((Void[]) null);
    }


    private void setSuggestedContactsOnTheTop() {

        if(itemsOld==null || itemsOld.size()==0) {
            items = null;
            items = new ArrayList<>();

            String lastSign = "";
            Integer suggestedIndex = 0;
            for (RegularContact contact : contacts) {
                if (suggestedIndex < application.getNumberOfSuggestedContacts()) {
                    if (suggestedIndex == 0) {
                        HeaderContact header = new HeaderContact();
                        header.setSign(getString(R.string.suggested));
                        items.add(header);
                    }

                    items.add(contact);

                    suggestedIndex++;
                } else {
                    String thisSign = contact.getName().length() < 0 ? "#" : contact.getName().substring(0, 1).toUpperCase();
                    if (!Utility.isAlpha(thisSign))
                        thisSign = "#";

                    if (!lastSign.equals(thisSign)) {
                        HeaderContact header = new HeaderContact();
                        header.setSign(thisSign);
                        items.add(header);
                    }

                    items.add(contact);

                    lastSign = thisSign;
                }

                //displayIndex();
            }
            itemsOld = (ArrayList<Object>)items.clone();
        }
        else
        {
            items = (ArrayList<Object>)itemsOld.clone();
        }
    }

    private void setContactsByFilter(String filter) {

        items = null;
        items = new ArrayList<>();

        for(RegularContact contact : contacts)
        {
            if(contact.getName().toLowerCase().contains(filter.toLowerCase()))
            {
                items.add(contact);
            }
        }
    }

    /**
     * Get contacts from application cache or from Contacts Provider
     */
    private void setContactsFromCacheOrContactsProvider() {

        Boolean isContactCached = !new CacheManager(context).getContactCache().equals("");

        if(isContactCached)
        {
            try {

                String contactsCache = new CacheManager(context).getContactCache();
                contactList = AddressBook.contactListFromResponse(new JSONArray(contactsCache));
                contacts = rankedContactsToRegularContacts(contactList.getEntries(),application.getNumberOfSuggestedContacts(), true);
            } catch (JSONException e) {
                e.printStackTrace();
                contacts = getContactsFromContactList();
            }
        }
        else {
            contacts = getContactsFromContactList();
        }

        //setupRecycler();
    }

    private void setupRecycler() {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter = new ContactsAdapter(items, ContactsActivity.this, application);

                adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        if (items.get(position) instanceof RegularContact) {
                            RegularContact contact = (RegularContact) items.get(position);
                            contact.setSelected(!contact.getSelected());
                            adapter.notifyDataSetChanged();
                            invalidateOptionsMenu();
                        }

                    }
                });

                contactsList.setAdapter(adapter);

                alphabeticalIndexManager.setIndexList(items,YesGraph.getNumberOfSuggestedContacts());

                //show alphabetical side index
                alphabeticalIndexManager.displayIndex();

                setProgressVisibility(false);
            }
        });
    }

    private ArrayList<RegularContact> rankedContactsToRegularContacts(ArrayList<RankedContact> ysgRankedContactArrayList, Integer suggestedCount, Boolean postSuggested)
    {
        return new RankingContactsManager(context).rankedContactsToRegularContacts(ysgRankedContactArrayList,suggestedCount,postSuggested);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    new PermissionGrantedManager(context).setReadContactsPermission(true);
                    ContactRetriever.readContacts(context);

                    getContactsOnThread();
                } else {
                    //Set Permission Denied
                    new PermissionGrantedManager(context).setReadContactsPermission(false);

                    Toast.makeText(ContactsActivity.this, "READ_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                    setProgressVisibility(false);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        TextView selectedIndex = (TextView) v;
        ((GridLayoutManager)contactsList.getLayoutManager()).scrollToPositionWithOffset(alphabeticalIndexManager.getIndexList().get(selectedIndex.getText()), 0);
    }

    private void getContactsOnThread(){

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if(!checkIfUploadNeeded())
                    {
                        getContacts("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
