package com.yesgraph.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yesgraph.android.adapters.ContactsAdapter;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.utils.ContactRetriever;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(application.getMainForegroundColor()));

        application = (YesGraph) getApplicationContext();
        context = this;

        search = (EditText)findViewById(R.id.search);
        search.setTextColor(application.getLightFontColor());
        search.getBackground().setColorFilter(application.getLightFontColor(), PorterDuff.Mode.SRC_ATOP);
        searchBar = (LinearLayout)findViewById(R.id.searhBar);
        searchBar.setBackgroundColor(application.getMainForegroundColor());
        contactsList = (RecyclerView)findViewById(R.id.contactsList);
        contactsList.setBackgroundColor(application.getRowBackgroundColor());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        items = new ArrayList<>();

        if (sharedPreferences.getBoolean("contacts_permision_granted", false))
        {
            getContacts("");
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

        /*for(int i=0;i<4;i++)
        {
            if(i==0)
            {
                HeaderContact header=new HeaderContact();
                header.setSign("Suggested");
                items.add(header);
            }
            else if(i==6)
            {
                HeaderContact header=new HeaderContact();
                header.setSign("A");
                items.add(header);

            }
            else if(i==20)
            {

                HeaderContact header=new HeaderContact();
                header.setSign("B");
                items.add(header);
            }
            else if(i==30)
            {

                HeaderContact header=new HeaderContact();
                header.setSign("C");
                items.add(header);
            }
            else if(i==50)
            {

                HeaderContact header=new HeaderContact();
                header.setSign("D");
                items.add(header);
            }
            else if(i==80)
            {

                HeaderContact header=new HeaderContact();
                header.setSign("E");
                items.add(header);
            }
            else
            {
                RegularContact contact=new RegularContact();
                contact.setName("Name Of Contact");
                contact.setContact("1234123125");
                items.add(contact);
            }


        }*/

    }

    private ArrayList<RegularContact> getContactsFromContactList()
    {
        return ContactRetriever.readContacts(context);
    }

    private void getContacts(String filter) {
        if(contacts==null)
            contacts = getContactsFromContactList();

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
            for(RegularContact contact : contacts)
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

        setupRecycler();
    }

    private void setupRecycler() {
        GridLayoutManager manager = new GridLayoutManager(this, 1,GridLayoutManager.VERTICAL, false);

        contactsList.setLayoutManager(manager);
        adapter = new ContactsAdapter(items, this, application);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    sharedPreferences.edit().putBoolean("contacts_permision_granted", true).commit();
                    ContactRetriever.readContacts(context);
                } else {
                    // Permission Denied
                    sharedPreferences.edit().putBoolean("contacts_permision_granted", false).commit();
                    Toast.makeText(ContactsActivity.this, "READ_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
