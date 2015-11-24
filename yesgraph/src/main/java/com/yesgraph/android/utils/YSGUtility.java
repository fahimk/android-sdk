package com.yesgraph.android.utils;

import android.util.Base64;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by marko on 23/11/15.
 */
public class YSGUtility {
    public static String randomUserId()
    {
        Random rand = new Random();
        int r = rand.nextInt();
        Double random = Double.valueOf(r) / Double.valueOf(2147483647);
        Long randomLong = Math.abs(random.longValue()) * (9999999 - 1000000) + 1000000;;

        String randomString = System.currentTimeMillis()/1000 + "_" + randomLong;

        byte[] data = new byte[0];
        try {
            data = randomString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return "anon_" + base64;
    }

    public static String iso8601dateStringFromMilliseconds(Long milliseconds)
    {
        Date date = new Date(milliseconds); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // the format of your date
        return sdf.format(date);
    }
}
