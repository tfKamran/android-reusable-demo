package com.tf.reusable.demoapplication.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tf.reusable.demoapplication.model.SimpleKeyValuePair;
import com.tf.reusable.demoapplication.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kamran on 03/08/15.
 * <p/>
 * A common class with methods to communicate with the server
 */
public class Network {
    private static final String TAG = "Network";
    private static final int TIMEOUT = 30000;

    public static String sendRequest(Context context, String urlString, ArrayList<SimpleKeyValuePair> keyValuePairs) {
        return sendRequest(context, urlString, keyValuePairs, TIMEOUT);
    }

    public static String sendRequest(Context context, String urlString, ArrayList<SimpleKeyValuePair> keyValuePairs, int timeout) {
        Thread.setDefaultUncaughtExceptionHandler(Logger.uncaughtExceptionHandler);

        if (isConnectedToInternet(context)) {
            InputStream is = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(timeout);
                conn.setConnectTimeout(timeout);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                String output = getJSONObject(keyValuePairs).toString();
                writer.write(output);
                writer.flush();
                writer.close();

                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, 500);

                Logger.i(TAG, urlString);
                Logger.i(TAG, contentAsString);

                return contentAsString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "{\"errorCode\":\"-1\", \"errorMessage\":\"No internet connectivity\"}";
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static JSONObject getJSONObject(ArrayList<SimpleKeyValuePair> keyValuePairs) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (SimpleKeyValuePair keyValuePair : keyValuePairs)
            jsonObject.put(keyValuePair.getKey(), keyValuePair.getValue());

        return jsonObject;
    }

    public static String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
