package com.yesgraph.android.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Klemen on 18.12.2015.
 */
public class AlphabetSideIndexManager {

    private LinearLayout sideIndexLayout;
    private LinkedHashMap<String, Integer> mapIndex;
    private Activity activity;
    private YesGraph yesGraph;

    public AlphabetSideIndexManager(Activity activity, YesGraph yesGraph){
        this.activity = activity;
        this.yesGraph = yesGraph;
        this.mapIndex = new LinkedHashMap<String, Integer>();
        this.sideIndexLayout = (LinearLayout) activity.findViewById(R.id.side_index);
        this.sideIndexLayout.setBackgroundColor(yesGraph.getCustomTheme().getMainBackgroundColor());
    }

    public void setIndexList(ArrayList<Object> contacts, int numberOfSuggestedContacts) {

        if (contacts != null) {

            for (int i = numberOfSuggestedContacts; i < contacts.size(); i++) {
                String stringLetter = "";
                if (contacts.get(i) instanceof RegularContact) {
                    continue;
                } else {
                    HeaderContact contact = (HeaderContact) contacts.get(i);
                    stringLetter = contact.getSign();
                }

                if (mapIndex.get(stringLetter) == null)
                    mapIndex.put(stringLetter, i);
            }
        }
    }

    public LinkedHashMap<String, Integer> getIndexList() {

        if (mapIndex == null) {
            return null;
        }

        return mapIndex;
    }

    public void displayIndex() {

        sideIndexLayout.removeAllViews();

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());

        for (String index : indexList) {
            textView = (TextView) activity.getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener((View.OnClickListener) activity);
            textView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            textView.setTextColor(yesGraph.getCustomTheme().getDarkFontColor());
            textView.setBackgroundColor(yesGraph.getCustomTheme().getMainBackgroundColor());
            if(!yesGraph.getCustomTheme().getFont().isEmpty()){
                //fontManager.setFont(textView, application.getYsgTheme().getFont());
            }
            sideIndexLayout.addView(textView);

        }
    }

    public LinearLayout getSideIndexLayout() {
        return sideIndexLayout;
    }
}
