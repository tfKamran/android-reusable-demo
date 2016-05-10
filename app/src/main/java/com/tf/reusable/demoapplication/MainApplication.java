package com.tf.reusable.demoapplication;

import android.app.Application;

import com.tf.reusable.demoapplication.util.DatabaseLayer;
import com.tf.reusable.demoapplication.util.Logger;
import com.tf.reusable.demoapplication.util.Preferences;

/**
 * Created by kamran on 1/9/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.initialize(getApplicationContext(), Thread.getDefaultUncaughtExceptionHandler());

        Preferences.initialize(MainApplication.this);
        DatabaseLayer.initialize(MainApplication.this);
    }
}
