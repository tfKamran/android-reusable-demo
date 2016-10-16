package com.tf.reusable.demoapplication.network;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.io.CharStreams;
import com.tf.reusable.demoapplication.model.SimpleKeyValuePair;
import com.tf.reusable.demoapplication.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by kamran on 03/08/15.
 * <p/>
 * A common class with methods to communicate with the server
 */
public class Network {
    private static final String TAG = "Network";
    private static final int TIMEOUT = 30000;
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";

    public static String getRequest(Context context, String urlString, ContentValues values) throws IOException, JSONException, NoInternetException {
        return request(context, urlString, values, GET);
    }

    public static String postRequest(Context context, String urlString, ContentValues values) throws IOException, JSONException, NoInternetException {
        return request(context, urlString, values, POST);
    }

    public static String putRequest(Context context, String urlString, ContentValues values) throws IOException, JSONException, NoInternetException {
        return request(context, urlString, values, PUT);
    }

    public static String request(Context context, String urlString, ContentValues values, String method) throws IOException, JSONException, NoInternetException {
        if (isConnectedToInternet(context)) {
            InputStream inputStream = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(TIMEOUT);
                conn.setConnectTimeout(TIMEOUT);
                conn.setRequestMethod(method);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                String output = getJSONObject(values).toString();
                writer.write(output);
                writer.flush();
                writer.close();

                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                inputStream = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = inputStreamToString(inputStream);

                Logger.i(TAG, urlString);
                Logger.i(TAG, contentAsString);

                return contentAsString;
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new NoInternetException();
        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static JSONObject getJSONObject(ContentValues values) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Set<Map.Entry<String, Object>> keyValues = values.valueSet();
        for (Map.Entry<String, Object> keyValue : keyValues)
            jsonObject.put(keyValue.getKey(), keyValue.getValue());

        return jsonObject;
    }

    public static String inputStreamToString(InputStream stream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String read;

        while ((read = bufferedReader.readLine()) != null) {
            stringBuilder.append(read);
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static class NoInternetException extends RuntimeException {
    }
}
