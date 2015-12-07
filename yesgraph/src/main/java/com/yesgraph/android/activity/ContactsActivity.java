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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.adapters.ContactsAdapter;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.YSGContact;
import com.yesgraph.android.models.YSGContactList;
import com.yesgraph.android.models.YSGRankedContact;
import com.yesgraph.android.models.YSGSource;
import com.yesgraph.android.network.YSGAddressBook;
import com.yesgraph.android.network.YSGInvite;
import com.yesgraph.android.network.YSGSuggestionsShown;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.FontManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

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

    private SharedPreferences sharedPreferences;
    private ArrayList<RegularContact> contacts;
    private Toolbar toolbar;
    private FontManager fontManager;
    private TextView toolbarTitle;
    private LinearLayout indexLayout;
    private FrameLayout contactsListContent;

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contactsListContent = (FrameLayout) findViewById(R.id.contactsListContent);
        contactsList = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.content_contact_list, null);
        contactsList.setLayoutManager(new GridLayoutManager(ContactsActivity.this, 1, GridLayoutManager.VERTICAL, false));
        contactsList.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_DEFAULT);
        contactsList.setVerticalScrollBarEnabled(true);
        contactsListContent.addView(contactsList);

        indexLayout = (LinearLayout) findViewById(R.id.side_index);
        indexLayout.setBackgroundColor(application.getYsgTheme().getMainBackgroundColor());
        search = (EditText)findViewById(R.id.search);
        search.setTextColor(application.getYsgTheme().getLightFontColor());
        search.getBackground().setColorFilter(application.getYsgTheme().getLightFontColor(), PorterDuff.Mode.SRC_ATOP);
        search.setHintTextColor(application.getYsgTheme().getLightFontColor());
        searchBar = (LinearLayout)findViewById(R.id.searhBar);
        searchBar.setBackgroundColor(application.getYsgTheme().getMainForegroundColor());
        //contactsList = (RecyclerView)findViewById(R.id.contactsList);
        contactsList.setBackgroundColor(application.getYsgTheme().getRowBackgroundColor());

        ((LinearLayout)findViewById(R.id.progressLayout)).setVisibility(View.VISIBLE);

        if(!application.getYsgTheme().getFont().isEmpty()){
            fontManager.setFont(search,application.getYsgTheme().getFont());
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        items = new ArrayList<>();

        if (sharedPreferences.getBoolean("contacts_permision_granted", false) && checkContactsReadPermission())
        {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        if(!checkIfUploadNeeded())
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    ((LinearLayout) findViewById(R.id.progressLayout)).setVisibility(View.GONE);
                                }
                            });
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

    LinkedHashMap<String, Integer> mapIndex;

    private void getIndexList(ArrayList<Object> contacts) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = application.getNumberOfSuggestedContacts(); i < contacts.size(); i++) {
            String stringLetter="";
            if(contacts.get(i) instanceof RegularContact)
            {
                continue;
            }
            else
            {
                HeaderContact contact = (HeaderContact)contacts.get(i);
                stringLetter=contact.getSign();
            }

            if (mapIndex.get(stringLetter) == null)
                mapIndex.put(stringLetter, i);
        }
    }

    private boolean checkContactsReadPermission()
    {
        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private Boolean checkIfUploadNeeded() {
        Boolean cached=!sharedPreferences.getString("contacts_cache","").equals("");

        if(!cached && sharedPreferences.getBoolean("contacts_permision_granted", false) && checkContactsReadPermission() && application.isOnline())
        {
            ArrayList<YSGRankedContact> list=ContactRetriever.readYSGContacts(ContactsActivity.this);

            YSGContactList contactList=new YSGContactList();
            contactList.setEntries(list);
            contactList.setSource(new YSGSource());
            contactList.setUseSuggestions(true);

            final YSGAddressBook ysgAddressBook=new YSGAddressBook();

            Log.i("#YSGCONTACTS_COUNT", "1count:"+contactList.getEntries().size());
            ysgAddressBook.updateAddressBookWithContactListForUserId(ContactsActivity.this, contactList, sharedPreferences.getString("user_id", ""), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg)
                {
                    ((LinearLayout) findViewById(R.id.progressLayout)).setVisibility(View.GONE);
                    if(msg.what== Constants.RESULT_OK)
                    {
                        try {
                            ysgContactList=(YSGContactList)msg.obj;
                            Log.i("#YSGCONTACTS_COUNT", "2count:"+ysgContactList.getEntries().size());
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

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getYsgTheme().getMainForegroundColor()));
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
        toolbarTitle.setText(getResources().getString(R.string.contacts_list));
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
                if (i == R.id.action_invite) {
                    ArrayList<YSGContact> ysgContacts=new ArrayList<>();
                    ArrayList<RegularContact> checkedEmails=getCheckedEmailContacts();
                    ArrayList<RegularContact> checkedPhones=getCheckedPhoneContacts();

                    if(checkedEmails.size()>0) {
                        String[] stringEmails=new String[checkedEmails.size()];
                        for(int x=0;x<checkedEmails.size();x++)
                        {
                            stringEmails[x]=checkedEmails.get(x).getContact();
                            YSGRankedContact ysgRankedContact=new YSGRankedContact();
                            ysgRankedContact.setName(checkedEmails.get(x).getName());
                            ysgRankedContact.setEmail(checkedEmails.get(x).getContact());
                            ysgContacts.add(ysgRankedContact);
                        }

                        Intent intent = new Intent(context, SendEmailActivity.class);
                        intent.putExtra("contacts",stringEmails);
                        intent.putExtra("subject",application.getEmailSubject());
                        intent.putExtra("message",application.getEmailText());
                        startActivity(intent);
                    }

                    if(checkedPhones.size()>0) {
                        String[] stringPhones=new String[checkedPhones.size()];
                        for(int x=0;x<checkedPhones.size();x++)
                        {
                            stringPhones[x]=checkedPhones.get(x).getContact();
                            YSGRankedContact ysgRankedContact=new YSGRankedContact();
                            ysgRankedContact.setName(checkedPhones.get(x).getName());
                            ysgRankedContact.setPhone(checkedPhones.get(x).getContact());
                            ysgContacts.add(ysgRankedContact);
                        }

                        Intent intent = new Intent(context, SendSmsActivity.class);
                        intent.putExtra("contacts",stringPhones);
                        intent.putExtra("message",application.getSmsText());
                        startActivity(intent);
                    }

                    YSGInvite ysgInvite=new YSGInvite();
                    ysgInvite.updateInvitesSentForUser(context, ysgContacts, sharedPreferences.getString("user_id", ""), new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            return false;
                        }
                    });
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
        menu.getItem(0).setEnabled(isContactChecked());
        return true;
    }

    private boolean isContactChecked() {
        for(Object contact : items)
        {
            if(contact instanceof RegularContact)
            {
                if(((RegularContact)contact).getSelected())
                    return true;
            }
        }
        return false;
    }

    private ArrayList<RegularContact> getCheckedEmailContacts() {
        ArrayList<RegularContact> checkedEmails=new ArrayList<>();
        for(Object contact : items)
        {
            if(contact instanceof RegularContact)
            {
                if(((RegularContact)contact).getSelected() && ((RegularContact)contact).getContact().contains("@"))
                    checkedEmails.add((RegularContact)contact);
            }
        }
        return checkedEmails;
    }

    private ArrayList<RegularContact> getCheckedPhoneContacts() {
        ArrayList<RegularContact> checkedPhones=new ArrayList<>();
        for(Object contact : items)
        {
            if(contact instanceof RegularContact)
            {
                if(((RegularContact)contact).getSelected() && !((RegularContact)contact).getContact().contains("@"))
                    checkedPhones.add((RegularContact)contact);
            }
        }
        return checkedPhones;
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
                if(ysgContactList==null || ysgContactList.getEntries()==null || ysgContactList.getEntries().size()==0)
                {
                    Boolean cached=!sharedPreferences.getString("contacts_cache","").equals("");
                    if(cached)
                    {
                        YSGAddressBook ysgAddressBook = new YSGAddressBook();
                        try {
                            ysgContactList = ysgAddressBook.contactListFromResponse(new JSONArray(sharedPreferences.getString("contacts_cache","")));
                            contacts = rankedContactsToRegularContacts(ysgContactList.getEntries(),application.getNumberOfSuggestedContacts(), true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            contacts = getContactsFromContactList();
                        }
                    }
                    else {
                        contacts = getContactsFromContactList();
                    }
                }
                else if(contacts==null || contacts.size()==0)
                {
                    contacts = rankedContactsToRegularContacts(ysgContactList.getEntries(),application.getNumberOfSuggestedContacts(), false);
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
                                if (!ContactRetriever.isAlpha(thisSign))
                                    thisSign = "#";

                                if (!lastSign.equals(thisSign)) {
                                    HeaderContact header = new HeaderContact();
                                    header.setSign(thisSign);
                                    items.add(header);
                                }

                                items.add(contact);

                                lastSign = thisSign;
                            }
                        }
                        itemsOld = (ArrayList<Object>)items.clone();
                    }
                    else
                    {
                        items = (ArrayList<Object>)itemsOld.clone();
                    }
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

                getIndexList(items);

                displayIndex();
            }
        });
    }

    private ArrayList<RegularContact> rankedContactsToRegularContacts(ArrayList<YSGRankedContact> ysgRankedContactArrayList, Integer suggestedCount, Boolean postSuggested)
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
                if(suggestedItemsIndex.size()==suggestedCount)
                    break;
            }
        }

        if(suggestedItemsIndex.size()<suggestedCount && ysgRankedContactArrayList.size()>=suggestedCount)
        {
            for(int i=0;i<suggestedCount-suggestedItemsIndex.size();i++)
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

                if(ysgRankedContact.phones()!=null && ysgRankedContact.phones().size()>0) {
                    for(String thisPhone : ysgRankedContact.phones()) {
                        RegularContact regularContact = new RegularContact();
                        regularContact.setName(ysgRankedContact.name());
                        regularContact.setContact(thisPhone);
                        regularContacts.add(regularContact);
                    }
                }
            }
            jsonArrayCached.put(ysgRankedContact.toJSONObjectExtended());
        }

        if(postSuggested) {
            YSGSuggestionsShown ysgSuggestionsShown = new YSGSuggestionsShown();
            ysgSuggestionsShown.updateSuggestionsSeen(context, ysgRankedContacts, sharedPreferences.getString("user_id", ""), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Message callbackMessage = new Message();
                    if (msg.what == Constants.RESULT_OK) {
                    }
                    return false;
                }
            });
        }

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

    private void displayIndex() {

        indexLayout.removeAllViews();

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            textView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            textView.setTextColor(application.getYsgTheme().getDarkFontColor());
            textView.setBackgroundColor(application.getYsgTheme().getMainBackgroundColor());
            if(!application.getYsgTheme().getFont().isEmpty()){
                fontManager.setFont(textView, application.getYsgTheme().getFont());
            }
            indexLayout.addView(textView);

        }
    }

    @Override
    public void onClick(View v) {
        TextView selectedIndex = (TextView) v;
        ((GridLayoutManager)contactsList.getLayoutManager()).scrollToPositionWithOffset(mapIndex.get(selectedIndex.getText()), 0);
    }

}
