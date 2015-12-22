package com.yesgraph.android.utils;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.callbacks.ITextChangedListener;

/**
 * Created by Klemen on 21.12.2015.
 */
public class SearchBarManager {

    private Activity activity;
    private LinearLayout searchBar;
    private EditText search;
    private ITextChangedListener textChangedListener;
    private YesGraph app;

    public SearchBarManager(Activity activity, YesGraph app) {
        this.activity = activity;
        this.app = app;
        this.textChangedListener = (ITextChangedListener) activity;

        initSearchEditText();

        initSearchBarLayout();

        setSearchTextChangedListener();

    }

    private void initSearchBarLayout() {
        searchBar = (LinearLayout) activity.findViewById(R.id.searhBar);
        searchBar.setBackgroundColor(app.getCustomTheme().getMainForegroundColor());
    }

    private void initSearchEditText() {
        search = (EditText) activity.findViewById(R.id.search);
        search.setTextColor(app.getCustomTheme().getLightFontColor());
        search.getBackground().setColorFilter(app.getCustomTheme().getLightFontColor(), PorterDuff.Mode.SRC_ATOP);
        search.setHintTextColor(app.getCustomTheme().getLightFontColor());
    }

    private void setSearchTextChangedListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                textChangedListener.afterSearchTextChanged(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    public EditText getSearch() {
        return search;
    }
}
