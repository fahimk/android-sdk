package com.yesgraph.android;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.Utility;

/**
 * Created by Klemen on 17.12.2015.
 */
public class FontManagerUnitTest extends ApplicationTestCase<Application> {
    public FontManagerUnitTest() {
        super(Application.class);
    }

    /**
     * Check font singleton instance not null
     */
    public void testCheckFontInstanceNotNull() {

        FontManager fontManager = FontManager.getInstance();

        boolean isNotNull = fontManager != null;

        assertEquals(true, isNotNull);

    }

    /*public void testCheckSetTextViewTypeFace() {

        FontManager fontManager = FontManager.getInstance();

        TextView textView = new TextView(getContext());

        fontManager.setFont(textView,"Pacifico.ttf");

        boolean typeFaceNotNull = textView.getTypeface() != null;

        assertEquals(true,typeFaceNotNull);

    }

    public void testCheckSetEditTextTypeFace() {

        FontManager fontManager = FontManager.getInstance();

        EditText editText = new EditText(getContext());

        fontManager.setFont(editText,"AlexBrush-Regular.ttf");

        boolean typeFaceNotNull = editText.getTypeface() != null;

        assertEquals(true,typeFaceNotNull);

    }

    public void testCheckSetButtonTypeFace() {

        FontManager fontManager = FontManager.getInstance();

        Button btn = new Button(getContext());

        fontManager.setFont(btn,"GoodDog.otf");

        boolean typeFaceNotNull = btn.getTypeface() != null;

        assertEquals(true,typeFaceNotNull);

    }*/




}