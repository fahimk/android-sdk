package com.yesgraph.android.network;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.yesgraph.android.utils.Constants;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by marko on 20/11/15.
 */
abstract class HttpMethodAbstract {

    private static String getParametersString(JSONObject parameters)
    {
        try
        {
            if(parameters!=null)
            {
                String stringParameters="?";
                int i=0;
                Iterator<?> keys=parameters.keys();
                while( keys.hasNext() )
                {
                    if(i!=0)
                        stringParameters+="&";

                    String key = (String)keys.next();

                    if( parameters.get(key) instanceof String){
                        stringParameters+=URLEncoder.encode(key, "utf-8") + "=" + URLEncoder.encode(parameters.getString(key), "utf-8");
                        i++;
                    }
                }

                return stringParameters;
            }
            else
            {
                return "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static void httpAsync(final String key, final String method, final String endpoint, final JSONObject headers, final JSONObject parameters, final Handler.Callback callback) {
        final Message callbackMessage = new Message();

        new AsyncTask<Handler.Callback, Object, Object>(){
            @Override
            protected Object doInBackground(Handler.Callback... callbacks) {
                try {

                    String urlString = Constants.API_DOMAIN + Constants.API_VERSION + endpoint;
                    String urlParameters = "";

                    if(method.equals(Constants.HTTP_METHOD_GET) && parameters!=null)
                    {
                        urlParameters = getParametersString(parameters);
                    }

                    URL url = new URL(urlString + urlParameters);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(Constants.TIMEOUT_READ);
                    conn.setConnectTimeout(Constants.TIMEOUT_CONNECTION);

                    if(key!=null && key.length()>0)
                        conn.setRequestProperty("Authorization", "Bearer " + key);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod(method);

                    if(method.equals(Constants.HTTP_METHOD_POST) && parameters!=null)
                    {
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setFixedLengthStreamingMode(parameters.toString().getBytes().length);
                        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                        os.write(parameters.toString().getBytes());
                        os.flush();
                    }

                    if (conn != null) {

                        InputStream in = null;

                        try {
                            try {
                                in = new BufferedInputStream(conn.getInputStream());
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            Integer responseCode = conn.getResponseCode();

                            String sResponse = (in!=null ? org.apache.commons.io.IOUtils.toString(in, "UTF-8") : "{}");

                            Log.i("WEB RESPONSE", "CODE:" + responseCode + " MESSAGE:" + sResponse.toString());
                            JSONObject jsonResponse = new JSONObject(sResponse);

                            if (responseCode != 200){
                                callbackMessage.obj = jsonResponse;
                                return null;
                            }

                            return jsonResponse;

                        } catch (IOException e) {
                            e.printStackTrace();

                            return e;
                        } finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        return "Response is null!";
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                //if result is JSONObject all is OK
                if (result != null && (result instanceof JSONObject))
                {
                    callbackMessage.what = 1;
                    callbackMessage.obj = result;
                    callback.handleMessage(callbackMessage);
                }
                else {
                    callbackMessage.what = 0;
                    if (callbackMessage.obj == null && result != null){
                        callbackMessage.obj = result;
                    }
                    callback.handleMessage(callbackMessage);
                }
            }

        }.execute(null, null, null);
    }
}
