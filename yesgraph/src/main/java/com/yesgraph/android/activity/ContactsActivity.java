package com.yesgraph.android.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yesgraph.android.R;
import com.yesgraph.android.adapters.ContactsAdapter;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.callbacks.ITextChangedListener;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.network.AddressBook;
import com.yesgraph.android.utils.AlphabetSideIndexManager;
import com.yesgraph.android.utils.StorageKeyValueManager;
import com.yesgraph.android.utils.Constants;
import com.yesgraph.android.utils.ContactManager;
import com.yesgraph.android.utils.ContactRetriever;
import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.PermissionGrantedManager;
import com.yesgraph.android.utils.RankingContactsManager;
import com.yesgraph.android.utils.SearchBarManager;
import com.yesgraph.android.utils.SenderManager;
import com.yesgraph.android.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by marko on 17/11/15.
 */
public class ContactsActivity extends AppCompatActivity implements View.OnClickListener,ITextChangedListener {

    private YesGraph application;
    private Context context;
    private RecyclerView contactsList;
    private ContactsAdapter adapter;
    private ArrayList<Object> items;
    private ArrayList<Object> itemsOld;
    private ArrayList<RegularContact> contacts;
    private Toolbar toolbar;
    private FontManager fontManager;
    private TextView toolbarTitle;
    private LinearLayout indexLayout;
    private FrameLayout contactsListContent;

    private ContactList contactList;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private AlphabetSideIndexManager alphabeticalIndexManager;
    private SearchBarManager searchBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        init();

        checkReadContactPermission();

    }

    private void checkReadContactPermission() {

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

        searchBarManager = new SearchBarManager(this,application);
        contactsList.setBackgroundColor(application.getCustomTheme().getRowBackgroundColor());

        setProgressVisibility(true);

        if(!application.getCustomTheme().getFont().isEmpty()){
            fontManager.setFont(searchBarManager.getSearch(),application.getCustomTheme().getFont());
        }

        items = new ArrayList<>();

        alphabeticalIndexManager = new AlphabetSideIndexManager(this,application);

    }

    private Boolean checkIfUploadNeeded() {

        Boolean isContactCached = !new StorageKeyValueManager(context).getContactCache().equals("");
        Boolean isReadContactPermissionGranted = new PermissionGrantedManager(context).isReadContactsPermission();
        Boolean checkContactsReadPermission = new PermissionGrantedManager(context).checkContactsReadPermission();

        Boolean isContactsUploading = new StorageKeyValueManager(context).isContactsUploading();

        if(!isContactCached && isReadContactPermissionGranted && checkContactsReadPermission && application.isOnline() && !isContactsUploading) {

            ContactList contactList = new ContactManager().getContactList(context);

            final AddressBook addressBook =new AddressBook();

            Log.i("#YSGCONTACTS_COUNT", "1count:" + contactList.getEntries().size());

            String userId = new StorageKeyValueManager(context).getUserId();

            addressBook.updateAddressBookWithContactListForUserId(ContactsActivity.this, contactList, userId, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    new StorageKeyValueManager(context).setContactsUploading(false);

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
            findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.progressLayout).setVisibility(View.GONE);
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
        new SenderManager(items).inviteContacts(context);
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
            protected void onPreExecute() {}

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
                    setContactsByAlphabetSections();
                }

                setupRecycler();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {}
        }.execute((Void[]) null);
    }


    private void setContactsByAlphabetSections() {

        if(itemsOld==null || itemsOld.size()==0) {

            resetItems();

            items = new ContactManager().getContactsByAlphabetSections(context,contacts,application.getNumberOfSuggestedContacts());

            itemsOld = (ArrayList<Object>)items.clone();
        }
        else
        {
            items = (ArrayList<Object>)itemsOld.clone();
        }
    }

    private void setContactsByFilter(String filter) {

        resetItems();

        items = new ContactManager().getContactsByFilter(filter,contacts);
    }

    private void resetItems() {
        items = null;
        items = new ArrayList<>();
    }

    /**
     * Get contacts from application cache or from Contacts Provider
     */
    private void setContactsFromCacheOrContactsProvider() {

        Boolean isContactCached = !new StorageKeyValueManager(context).getContactCache().equals("");

        if(isContactCached)
        {
            try {

                String contactsCache = new StorageKeyValueManager(context).getContactCache();
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

    }

    private void setupRecycler() {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter = new ContactsAdapter(items, ContactsActivity.this, application);

                setOnItemClickListener();
                setOnTouchListener();

                contactsList.setAdapter(adapter);

                alphabeticalIndexManager.setIndexList(items,YesGraph.getNumberOfSuggestedContacts());

                //show alphabetical side index
                alphabeticalIndexManager.displayIndex();

                setProgressVisibility(false);
            }
        });
    }

    private void setOnTouchListener()
    {
        contactsList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    private void setOnItemClickListener() {

        adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                setSelectedClickedItem(position);
            }
        });
    }

    private void setSelectedClickedItem(int position) {

        if (items.get(position) instanceof RegularContact) {
            RegularContact contact = (RegularContact) items.get(position);
            contact.setSelected(!contact.getSelected());
            adapter.notifyDataSetChanged();
            invalidateOptionsMenu();
        }
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

    @Override
    public void afterSearchTextChanged(String searchText) {
        getContacts(searchText);
    }

    public void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
