package com.tf.reusable.demoapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by kamran on 19/8/15.
 */
public class Preferences {
    private static final String KEY = "prefKey";
    private static final String THE_OTHER_KEY = "prefTheOtherKey";

    private static SharedPreferences sharedPreferences;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setValue(String value) {
        sharedPreferences.edit().putString(KEY, value).commit();
    }

    public static String getValue() {
        return sharedPreferences.getString(KEY, "");
    }

    public static void setTheOtherValue(String value) {
        sharedPreferences.edit().putString(THE_OTHER_KEY, value).commit();
    }

    public static String getTheOtherValue() {
        return sharedPreferences.getString(THE_OTHER_KEY, "");
    }
}
