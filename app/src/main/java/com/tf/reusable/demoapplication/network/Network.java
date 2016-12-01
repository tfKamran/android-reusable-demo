package com.tf.reusable.demoapplication.network;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
import java.util.Map;
import java.util.Set;

/**
 * Created by kamran on 10/17/16.
 */

public class Network {

    private static final String TAG = "Network";
    private static final int TIMEOUT = 30000;
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";

    public static NetworkResponse getRequest(Context context, String urlString, ContentValues queryParameters) throws IOException, JSONException, NoInternetException {
        return request(context, urlString, queryParameters, null, GET);
    }

    public static NetworkResponse postRequest(Context context, String urlString, ContentValues queryParameters, JSONObject body) throws IOException, JSONException, NoInternetException {
        return request(context, urlString, queryParameters, body, POST);
    }

    public static NetworkResponse putRequest(Context context, String urlString, ContentValues queryParameters, JSONObject body) throws IOException, JSONException, NoInternetException {
        return request(context, urlString, queryParameters, body, PUT);
    }

    public static NetworkResponse request(Context context, String urlString, ContentValues queryParameters, JSONObject body, String method) throws IOException, JSONException, NoInternetException {
        if (isConnectedToInternet(context)) {
            InputStream inputStream = null;

            try {
                urlString += getQueryString(queryParameters);

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(TIMEOUT);
                conn.setConnectTimeout(TIMEOUT);
                conn.setRequestMethod(method);
                conn.setDoInput(true);

                if (body != null && body.length() > 0) {
                    conn.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(body.toString());
                    writer.flush();
                    writer.close();
                }

                // Starts the query
                conn.connect();
                int responseCode = conn.getResponseCode();

                if (responseCode < 400) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                // Convert the InputStream into a string
                String contentAsString = inputStreamToString(inputStream);

                Logger.i(TAG, urlString);
                Logger.i(TAG, body != null ? body.toString() : "");
                Logger.i(TAG, contentAsString);

                return new NetworkResponse(responseCode, contentAsString);
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

    private static String getQueryString(ContentValues queryParameters) {
        String queryString = "";

        if (queryParameters != null) {
            for (Map.Entry<String, Object> map : queryParameters.valueSet()) {
                queryString += "&" + map.getKey() + "=" + map.getValue();
            }
        }

        queryString = queryString.replaceFirst("&", "?");

        return queryString;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public static class NetworkResponse {
        private int code;
        private String response;

        public NetworkResponse(int code, String response) {
            this.code = code;
            this.response = response;
        }

        public int getCode() {
            return code;
        }

        public String getResponse() {
            return response;
        }
    }

    public static class NoInternetException extends RuntimeException {
    }
}
