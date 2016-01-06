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

    private Button btn_tryYesGraph, btn_tryYesGraph2, btn_tryYesGraph3, btn_tryYesGraph4, btn_export_contacts;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    final private int WRITE_EXTERNAL_STORAGE_PERMISSIONS = 124;
    final private int READ_CALL_LOG_PERMISSIONS = 125;
    final private int READ_CONTENT_URI_PERMISSIONS = 126;

    private SharedPreferences sharedPreferences;

    private List<RecentlyContactedContact> recContacts = null;
    private List<FavouriteContacts> favs = null;
    private AppCompatActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        activity = this;
        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        yesGraphApplication = (YesGraph) getApplicationContext();
        yesGraphApplication.setSecretKey("live-WzEsMCwieWVzZ3JhcGhfc2RrX3Rlc3QiXQ.COM_zw.A76PgpT7is1P8nneuSg-49y4nW8");
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
        btn_export_contacts = (Button) findViewById(R.id.btn_export_contacts);

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

        btn_export_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPreferences.getBoolean("contacts_permision_granted", false) && checkContactsReadPermission()) {

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }

                if (sharedPreferences.getBoolean("read_call_log_permision_granted", false) && checkContactsReadPermission()) {

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG},
                            READ_CALL_LOG_PERMISSIONS);
                }

                if (sharedPreferences.getBoolean("read_external_permissions_granted", false) && checkContactsReadPermission()) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                getRecentContacts(500, 500);
                                getFavouritesContactsFromPhone();
                                getAllContactsDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_PERMISSIONS);
                }
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
            case READ_CALL_LOG_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    sharedPreferences.edit().putBoolean("read_call_log_permision_granted", true).commit();
                } else {
                    // Permission Denied
                    sharedPreferences.edit().putBoolean("read_call_log_permision_granted", false).commit();
                    Toast.makeText(MainActivity.this, "READ_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case WRITE_EXTERNAL_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sharedPreferences.edit().putBoolean("read_external_permissions_granted", true).commit();
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                getRecentContacts(500, 500);
                                getFavouritesContactsFromPhone();
                                getAllContactsDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    // Permission Denied
                    sharedPreferences.edit().putBoolean("read_external_permissions_granted", false).commit();
                    Toast.makeText(MainActivity.this, "WRITE_EXTERNAL Denied", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean checkContactsReadPermission() {
        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private Map getFavouritesContactsFromPhone() {
        Map contactMap = new HashMap();
        favs = new LinkedList<>();

        Uri queryUri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.STARRED};

        String selection = ContactsContract.Contacts.STARRED + "='1'";

        Cursor cursor = managedQuery(queryUri, projection, selection, null, null);

        while (cursor.moveToNext()) {
            String contactID = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
            intent.setData(uri);
            String intentUriString = intent.toUri(0);

            String title = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

            contactMap.put(title, intentUriString);

            FavouriteContacts contact = new FavouriteContacts();
            contact.setNeme(title);
            contact.setConten_uri(intentUriString);
            favs.add(contact);
        }

        cursor.close();

        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";

        //CSV file header
        String FILE_HEADER = "id,intent_uri_string";

        FileWriter fileWriter = null;
        File sdCard = Environment.getExternalStorageDirectory();
        final File file = new File(sdCard, "FavouriteContacts.csv");

        try {
            fileWriter = new FileWriter(file);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (FavouriteContacts favouriteContacts : favs) {
                fileWriter.append(favouriteContacts.getNeme());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(favouriteContacts.getConten_uri());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, "CSV file with favourites: " + file.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }

        return contactMap;
    }


    private List<String> getRecentContacts(int limitAll, int limitUnique) {
        List<String> contacts = new ArrayList<>();
        recContacts = new ArrayList<>();

        String permission = Manifest.permission.READ_CONTACTS;
        int res = context.checkCallingOrSelfPermission(permission);
        if (res != PackageManager.PERMISSION_GRANTED)
            return contacts;

        Uri queryUri = android.provider.CallLog.Calls.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE};

        String sortOrder = String.format("%s limit %d ", CallLog.Calls.DATE + " DESC", limitAll);

        int count = 0;
        Cursor cursor = getContentResolver().query(queryUri, projection, null, null, sortOrder);
        Set<String> names = new HashSet<String>();
        while (cursor.moveToNext() && count < limitUnique) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String name = (cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            String date = (cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));

            if (phoneNumber == null || name == null || names.contains(name)) continue;
            names.add(name);
            contacts.add(name + ": " + phoneNumber);

            RecentlyContactedContact contact = new RecentlyContactedContact();
            contact.setName(name);
            contact.setNumber(phoneNumber);
            contact.setDate(date);
            recContacts.add(contact);

            count++;
        }
        cursor.close();
//        for (int i = 0; i < contacts.size(); i++) {
//            Log.d("NEJC CONTACTS: ", contacts.get(i));
//        }

        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";

        //CSV file header
        String FILE_HEADER = "name,phone,date";

        FileWriter fileWriter = null;
        File sdCard = Environment.getExternalStorageDirectory();
        final File file = new File(sdCard, "RecentContactedContacts.csv");

        try {
            fileWriter = new FileWriter(file);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new contact object list to the CSV file
            for (RecentlyContactedContact recentlyContactedContactContact : recContacts) {
                fileWriter.append(recentlyContactedContactContact.getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(recentlyContactedContactContact.getNumber());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(convertToDate(recentlyContactedContactContact.getDate()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, "CSV file with recent: " + file.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }

        return contacts;
    }

    private String convertToDate(String milliSeconds){
        String dateFormat = "dd-MM-yyyy hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static boolean isAlpha(String name) {
        boolean bool = name.matches("[a-zA-Z]+");
        return bool;
    }

    public static boolean isNumeric(String name) {
        boolean bool = name.matches("[0-9]+");
        return bool;
    }

    private void getAllContactsDetails() {

        ArrayList<FullDetailsContact> list = new ArrayList<>();

        FullDetailsContact newContact=null;

//        String permission = Manifest.permission.READ_CONTACTS;
//        int res = context.checkCallingOrSelfPermission(permission);
//        if(res != PackageManager.PERMISSION_GRANTED)
//            return list;

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                name = name != null ? name.trim() : "";

                for (int i = 0; i < name.length(); i++) {
                    if (!isAlpha(name.substring(i, i + 1)) && !isNumeric(name.substring(i, i + 1))) {
                        name = name.substring(i + 1, name.length());
                        i--;
                    } else
                        break;
                }

                if (name.length() == 0) {
                    name = context.getString(com.yesgraph.android.R.string.no_contact_name);
                }

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

                    newContact = new FullDetailsContact();
                    newContact.setId(id);
                    newContact.setName(name);

                    // get email and type
                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        System.out.println("Email " + email + " Email Type : " + emailType);

                        newContact.setEmail(email);
                        if(emailType.equals("null")){
                            newContact.setEmail_type("");
                        } else {
                            newContact.setEmail_type(emailType);
                        }
                    }
                    emailCur.close();

//                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("phone" + phone);

                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getId().equals(id)){
                                if(phone.equals("null")){
                                    newContact.setPhone("");
                                } else {
                                    newContact.setPhone(phone);
                                }
                            }
                        }
                    }
                    pCur.close();

                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        System.out.println("Note " + note);

                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getId().equals(id)){
                                if(note.equals("null")){
                                    newContact.setNote("");
                                } else {
                                    newContact.setNote(note);
                                }

                            }
                        }
                    }
                    noteCur.close();

                    //Get Postal Address....
                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, addrWhere, addrWhereParams, null);
                    while (addrCur.moveToNext()) {
                        String poBox = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        String street = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postalCode = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String country = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String type = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getId().equals(id)){
                                if(poBox == null){
                                    newContact.setPoBox("");
                                } else {
                                    newContact.setPoBox(poBox);
                                }
                                if(street == null){
                                    newContact.setState("");
                                } else {
                                    newContact.setState(street);
                                }
                                if(city == null){
                                    newContact.setCity("");
                                } else {
                                    newContact.setCity(city);
                                }
                                if(state == null){
                                    newContact.setState("");
                                } else {
                                    newContact.setState(state);
                                }
                                if(postalCode == null){
                                    newContact.setPostalCode("");
                                } else {
                                    newContact.setPostalCode(postalCode);
                                }
                                if(country == null){
                                    newContact.setCity("");
                                } else {
                                    newContact.setCity(country);
                                }
                                if(type == null){
                                    newContact.setType("");
                                } else {
                                    newContact.setType(type);
                                }
                            }
                        }

                        System.out.println("poBox " + poBox + "street " + street + "city " + city + "state " + state
                                + "postalCode " + postalCode + "country " + country + "type " + type);
                    }
                    addrCur.close();

                    // Get Instant Messenger.........
                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, imWhere, imWhereParams, null);
                    if (imCur.moveToFirst()) {
                        String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        String imType;
                        imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getId().equals(id)){
                                if(imName.equals("null")){
                                    newContact.setImName("");
                                } else {
                                    newContact.setImName(imName);
                                }
                                if(imType.equals("null")){
                                    newContact.setImType("");
                                } else {
                                    newContact.setImType(imType);
                                }
                            }
                        }
                        System.out.println("imName " + imName + "imType " + imType);
                    }
                    imCur.close();

                    // Get Organizations.........
                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getId().equals(id)){
                                if(orgName.equals("null")){
                                    newContact.setOrgName("");
                                } else {
                                    newContact.setOrgName(orgName);
                                }
                                if(title.equals("null")){
                                    newContact.setTitle("");
                                } else {
                                    newContact.setTitle(title);
                                }
                            }
                        }
                        System.out.println("orgName " + orgName + "title " + title);
                    }
                    orgCur.close();

                    list.add(newContact);
                }
            }

            String COMMA_DELIMITER = ",";
            String NEW_LINE_SEPARATOR = "\n";

            //CSV file header
            String FILE_HEADER = "id,name,email,email_type,phone,note,poBox,street,city," +
                    "state,postalCode,country,type,imName,imType,orgName,title,last_date_contacted,is_favourite";

            FileWriter fileWriter = null;
            File sdCard = Environment.getExternalStorageDirectory();
            final File file = new File(sdCard, "DetailContacts.csv");

            try {
                fileWriter = new FileWriter(file);

                //Write the CSV file header
                fileWriter.append(FILE_HEADER.toString());

                //Add a new line separator after the header
                fileWriter.append(NEW_LINE_SEPARATOR);


                int counter = 0;

                //Write a new student object list to the CSV file
                for (FullDetailsContact fullDetailsContact : list) {
                    fileWriter.append(fullDetailsContact.getId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(fullDetailsContact.getName());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(fullDetailsContact.getEmail());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(fullDetailsContact.getEmail_type());
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getPhone() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getPhone());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getNote() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getNote());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getPoBox() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getPoBox());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getStreet() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getStreet());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getCity() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getCity());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getState() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getState());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getPostalCode() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getPostalCode());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getCountry() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getCountry());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getType() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getType());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getImName() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getImName());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getImType() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getImName());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getOrgName() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getOrgName());
                    }
                    fileWriter.append(COMMA_DELIMITER);
                    if(fullDetailsContact.getTitle() == null){
                        fileWriter.append("");
                    } else {
                        fileWriter.append(fullDetailsContact.getTitle());
                    }
                    fileWriter.append(COMMA_DELIMITER);

                    boolean checkRec = false;
                    boolean checkFvs = false;

                    for(int i=0;i<recContacts.size();i++){
                        if(list.get(counter).getName().equals(recContacts.get(i).getName())){
                            fileWriter.append(convertToDate(recContacts.get(i).getDate()));
                            fileWriter.append(COMMA_DELIMITER);
                            checkRec = true;
                        }
                    }
                    for (int i=0;i<favs.size();i++){
                        if(list.get(counter).getName().equals(favs.get(i).getNeme())){
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append("YES");
                            fileWriter.append(NEW_LINE_SEPARATOR);
                            checkFvs = true;
                        }
                    }
//                    if(!checkRec){
//                        fileWriter.append(COMMA_DELIMITER);
//                    }
                    if(!checkFvs){
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                    counter ++;
                }

                System.out.println("CSV file was created successfully !!!");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "CSV file with contacts: " + file.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                System.out.println("Error in CsvFileWriter !!!");
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Error while flushing/closing fileWriter !!!");
                    e.printStackTrace();
                }
            }
        }
    }
}
