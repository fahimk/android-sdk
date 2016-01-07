package com.yesgraph.android.utils;

/**
 * Created by Klemen on 16.12.2015.
 */
public enum FilterType {

    ALL_CONTACTS("All contacts",0),
    ONLY_PHONES("Phones",1),
    ONLY_EMAILS("Emails",2);

    private String stringValue;
    private int intValue;

    private FilterType(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
