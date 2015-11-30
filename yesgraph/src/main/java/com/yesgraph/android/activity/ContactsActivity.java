package com.yesgraph.android.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.adapters.ContactsAdapter;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGContactList;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.models.YSGSource;
import com.yesgraph.android.network.YSGAddressBook;
import com.yesgraph.android.network.YSGSuggestionsShown;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.FontManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by marko on 17/11/15.
 */
public class ContactsActivity extends AppCompatActivity {

    private YesGraph application;
    private Context context;
    private RecyclerView contactsList;
    private ContactsAdapter adapter;
    private ArrayList<Object> items;
    private LinearLayout searchBar;
    private EditText search;

    private SharedPreferences sharedPreferences;
    private ArrayList<RegularContact> contacts;
    private Toolbar toolbar;
    private FontManager fontManager;
    private TextView toolbarTitle;

    private YSGContactList ysgContactList;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        application = (YesGraph) getApplication();
        fontManager = FontManager.getInstance();
        setToolbar();


        context = this;

        search = (EditText)findViewById(R.id.search);
        search.setTextColor(application.getYsgTheme().getLightFontColor());
        search.getBackground().setColorFilter(application.getYsgTheme().getLightFontColor(), PorterDuff.Mode.SRC_ATOP);
        search.setHintTextColor(application.getYsgTheme().getLightFontColor());
        searchBar = (LinearLayout)findViewById(R.id.searhBar);
        searchBar.setBackgroundColor(application.getYsgTheme().getMainForegroundColor());
        contactsList = (RecyclerView)findViewById(R.id.contactsList);
        contactsList.setBackgroundColor(application.getYsgTheme().getRowBackgroundColor());

        ((LinearLayout)findViewById(R.id.progressLayout)).setVisibility(View.VISIBLE);

        if(!application.getYsgTheme().getFont().isEmpty()){
            fontManager.setFont(search,application.getYsgTheme().getFont());
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        items = new ArrayList<>();

        if (sharedPreferences.getBoolean("contacts_permision_granted", false))
        {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        if(!checkIfUploadNeeded())
                        {
                            ((LinearLayout) findViewById(R.id.progressLayout)).setVisibility(View.GONE);
                            getContacts("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                getContacts(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    private Boolean checkIfUploadNeeded() {
        Boolean cached=!sharedPreferences.getString("contacts_cache","").equals("");

        if(!cached && sharedPreferences.getBoolean("contacts_permision_granted", false) && application.isOnline())
        {
            ArrayList<YSGRankedContact> list=ContactRetriever.readYSGContacts(ContactsActivity.this);

            YSGContactList contactList=new YSGContactList();
            contactList.setEntries(list);
            contactList.setSource(new YSGSource());
            contactList.setUseSuggestions(true);

            YSGAddressBook ysgAddressBook=new YSGAddressBook();
            ysgAddressBook.updateAddressBookWithContactListForUserId(ContactsActivity.this, contactList, sharedPreferences.getString("user_id", ""), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg)
                {
                    ((LinearLayout) findViewById(R.id.progressLayout)).setVisibility(View.GONE);
                    if(msg.what== Constants.RESULT_OK)
                    {
                        try {
                            ysgContactList=(YSGContactList)msg.obj;
                            getContacts("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getYsgTheme().getMainForegroundColor()));
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
        toolbarTitle.setText(getResources().getString(R.string.app_name));
        toolbarTitle.setTextColor(application.getYsgTheme().getBackArrowColor());
        if(!application.getYsgTheme().getFont().isEmpty()){
            fontManager.setFont(toolbarTitle, application.getYsgTheme().getFont());
        }
    }

    private Drawable getColoredArrow() {
        Drawable arrowDrawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        Drawable wrapped = DrawableCompat.wrap(arrowDrawable);

        if (arrowDrawable != null && wrapped != null) {
            // This should avoid tinting all the arrows
            arrowDrawable.mutate();
            DrawableCompat.setTint(wrapped, application.getYsgTheme().getBackArrowColor());
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
                if (i == R.id.action_invite) {// User chose the "Invite" item


                    return true;
                } else {// If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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

    private void getContacts(String filter) {
        if(ysgContactList==null)
        {
            Boolean cached=!sharedPreferences.getString("contacts_cache","").equals("");
            if(cached)
            {
                YSGAddressBook ysgAddressBook = new YSGAddressBook();
                try {
                    ysgContactList = ysgAddressBook.contactListFromResponse(new JSONArray(sharedPreferences.getString("contacts_cache","")));
                    contacts = rankedContactsToRegularContacts(ysgContactList.getEntries());
                } catch (JSONException e) {
                    e.printStackTrace();
                    contacts = getContactsFromContactList();
                }
            }
            else {
                contacts = getContactsFromContactList();
            }
        }
        else
        {
            contacts = rankedContactsToRegularContacts(ysgContactList.getEntries());
        }

        if(filter.length()>0)
        {
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
        else
        {
            items = null;
            items = new ArrayList<>();

            String lastSign="";
            Integer suggestedIndex=0;
            for(RegularContact contact : contacts)
            {
                if(suggestedIndex<application.getNumberOfSuggestedContacts())
                {
                    if(suggestedIndex==0)
                    {
                        HeaderContact header=new HeaderContact();
                        header.setSign(getString(R.string.suggested));
                        items.add(header);
                    }

                    items.add(contact);

                    suggestedIndex++;
                }
                else
                {
                    String thisSign=contact.getName().length()<0 ? "#" : contact.getName().substring(0,1).toUpperCase();
                    if(!ContactRetriever.isAlpha(thisSign))
                        thisSign="#";

                    if(!lastSign.equals(thisSign))
                    {
                        HeaderContact header=new HeaderContact();
                        header.setSign(thisSign);
                        items.add(header);
                    }

                    items.add(contact);

                    lastSign = thisSign;
                }
            }
        }

        setupRecycler();
    }

    private void setupRecycler() {
        runOnUiThread(new Runnable() {
            public void run() {
                GridLayoutManager manager = new GridLayoutManager(ContactsActivity.this, 1, GridLayoutManager.VERTICAL, false);

                contactsList.setLayoutManager(manager);
                adapter = new ContactsAdapter(items, ContactsActivity.this, application);

                adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (items.get(position) instanceof RegularContact) {
                            RegularContact contact = (RegularContact) items.get(position);
                            contact.setSelected(!contact.getSelected());
                            adapter.notifyDataSetChanged();
                        }

                    }
                });

                contactsList.setAdapter(adapter);
            }
        });
    }

    private ArrayList<RegularContact> rankedContactsToRegularContacts(ArrayList<YSGRankedContact> ysgRankedContactArrayList)
    {
        ArrayList<RegularContact> regularContacts=new ArrayList<>();
        ArrayList<RegularContact> suggestedContacts=new ArrayList<>();
        ArrayList<YSGRankedContact> ysgRankedContacts=new ArrayList<>();

        JSONArray jsonArrayCached=new JSONArray();

        ArrayList<Integer> suggestedItemsIndex=new ArrayList<>();

        for(int i=0;i<ysgRankedContactArrayList.size();i++)
        {
            YSGRankedContact ysgRankedContact = ysgRankedContactArrayList.get(i);

            if(!ysgRankedContact.wasSuggested())
            {
                suggestedItemsIndex.add(i);
                if(suggestedItemsIndex.size()==application.getNumberOfSuggestedContacts())
                    break;
            }
        }

        if(suggestedItemsIndex.size()<application.getNumberOfSuggestedContacts() && ysgRankedContactArrayList.size()>=application.getNumberOfSuggestedContacts())
        {
            for(int i=0;i<application.getNumberOfSuggestedContacts()-suggestedItemsIndex.size();i++)
            {
                suggestedItemsIndex.add(i);
            }

            for(int i=0;i<ysgRankedContactArrayList.size();i++)
            {
                YSGRankedContact ysgRankedContact = ysgRankedContactArrayList.get(i);
                ysgRankedContact.setSuggested(false);
            }
        }

        for(int i=0;i<ysgRankedContactArrayList.size();i++)
        {
            YSGRankedContact ysgRankedContact = ysgRankedContactArrayList.get(i);

            if(suggestedItemsIndex.contains(i))
            {
                ysgRankedContact.setSuggested(true);
                ysgRankedContacts.add(ysgRankedContact);

                RegularContact regularContact=new RegularContact();
                regularContact.setName(ysgRankedContact.name());
                if(ysgRankedContact.email()!=null && ysgRankedContact.email().length()>0)
                {
                    regularContact.setContact(ysgRankedContact.email());
                }
                else if(ysgRankedContact.phone()!=null && ysgRankedContact.phone().length()>0)
                {
                    regularContact.setContact(ysgRankedContact.phone());
                }
                suggestedContacts.add(regularContact);
            }
            else
            {
                if(ysgRankedContact.emails()!=null && ysgRankedContact.emails().size()>0)
                {
                    for(String thisEmail : ysgRankedContact.emails()) {
                        RegularContact regularContact = new RegularContact();
                        regularContact.setName(ysgRankedContact.name());
                        regularContact.setContact(thisEmail);
                        regularContacts.add(regularContact);
                    }
                }
                else if(ysgRankedContact.phones()!=null && ysgRankedContact.phones().size()>0)
                {
                    for(String thisPhone : ysgRankedContact.phones()) {
                        RegularContact regularContact = new RegularContact();
                        regularContact.setName(ysgRankedContact.name());
                        regularContact.setContact(thisPhone);
                        regularContacts.add(regularContact);
                    }
                }
            }
            Log.i("JSON_WRITE", ysgRankedContact.toJSONObjectExtended().toString());
            jsonArrayCached.put(ysgRankedContact.toJSONObjectExtended());
        }

        YSGSuggestionsShown ysgSuggestionsShown=new YSGSuggestionsShown();
        ysgSuggestionsShown.updateSuggestionsSeen(context,ysgRankedContacts,sharedPreferences.getString("user_id", ""),new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Message callbackMessage = new Message();
                if(msg.what == Constants.RESULT_OK)
                {
                }
                return false;
            }
        });

        sharedPreferences.edit().putString("contacts_cache", jsonArrayCached.toString()).commit();

        Collections.sort(regularContacts, new Comparator<RegularContact>() {
            public int compare(RegularContact s1, RegularContact s2) {

                if (isAlpha(s1.getName().substring(0, 1)) && !isAlpha(s2.getName().substring(0, 1)))
                    return -s1.getName().compareToIgnoreCase(s2.getName());

                if (isAlpha(s2.getName().substring(0, 1)) && !isAlpha(s1.getName().substring(0, 1)))
                    return -s1.getName().compareToIgnoreCase(s2.getName());

                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        for(RegularContact contact : regularContacts)
        {
            suggestedContacts.add(contact);
        }

        return suggestedContacts;
    }

    public static boolean isAlpha(String name) {
        boolean bool=name.matches("[a-zA-Z]+");
        return bool;
    }

    public static boolean isNumeric(String name) {
        boolean bool=name.matches("[0-9]+");
        return bool;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    sharedPreferences.edit().putBoolean("contacts_permision_granted", true).commit();
                    ContactRetriever.readContacts(context);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if(!checkIfUploadNeeded())
                                        {
                                            ((LinearLayout) findViewById(R.id.progressLayout)).setVisibility(View.GONE);
                                            getContacts("");
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    // Permission Denied
                    sharedPreferences.edit().putBoolean("contacts_permision_granted", false).commit();
                    Toast.makeText(ContactsActivity.this, "READ_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                    ((LinearLayout)findViewById(R.id.progressLayout)).setVisibility(View.GONE);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
