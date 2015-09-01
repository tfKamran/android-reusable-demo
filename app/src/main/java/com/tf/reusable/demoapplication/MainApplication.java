package com.tf.reusable.demoapplication;

import android.app.Application;

import com.tf.reusable.demoapplication.util.DatabaseLayer;
import com.tf.reusable.demoapplication.util.Logger;
import com.tf.reusable.demoapplication.util.Preferences;

/**
 * Created by kamran on 1/9/15.
 */
public class MainApplication extends Application {
    public static Thread.UncaughtExceptionHandler defaultExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(Logger.uncaughtExceptionHandler);

        Preferences.initialize(MainApplication.this);
        DatabaseLayer.initialize(MainApplication.this);
    }
}
