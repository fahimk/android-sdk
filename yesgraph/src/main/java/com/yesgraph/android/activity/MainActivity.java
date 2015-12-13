package com.yesgraph.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.CustomTheme;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private YesGraph app;
    private CustomTheme customTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        app = (YesGraph) getApplication();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                String[] contacts = {"+38975567106","+38978206505"};
//                Intent intent = new Intent(context,SendSmsActivity.class);
//                intent.putExtra("contacts",contacts);
//                intent.putExtra("message","test");
//                startActivity(intent);

//                String[] contacts = {"d.bozinoski@gmail.com","dean.bozinoski@poviolabs.com"};
//                Intent intent = new Intent(context,SendEmailActivity.class);
//                intent.putExtra("contacts",contacts);
//                intent.putExtra("subject","test subject");
//                intent.putExtra("message","test message");
//                startActivity(intent);

//                customTheme = new CustomTheme();
//                customTheme.setThemeColor(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                customTheme.setReferralTextSize(14);
//                customTheme.setShareButtonsShape("rounded_square");
//                customTheme.setFonts("Pacifico.ttf");
//                app.setCustomTheme(customTheme);
                Intent intent = new Intent(context,com.yesgraph.android.activity.ShareSheetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
