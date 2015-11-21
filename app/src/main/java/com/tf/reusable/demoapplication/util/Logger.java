package com.tf.reusable.demoapplication.util;

import android.os.Environment;
import android.util.Log;

import com.tf.reusable.demoapplication.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by kamran on 16/12/14.
 */
public class Logger {
    private static final String APP_NAME = "DemoApplication";

    public static boolean ENABLED = true;

    public static void i(String tag, String message) {
        if (ENABLED)
            try {
                Log.i(tag, message);
            } catch (Exception e) {
                //e.printStackTrace();
            }
    }

    public static void d(String tag, String message) {
        if (ENABLED)
            try {
                Log.d(tag, message);
            } catch (Exception e) {
                //e.printStackTrace();
            }
    }

    public static void e(String tag, String message) {
        if (ENABLED)
            try {
                Log.e(tag, message);
            } catch (Exception e) {
                //e.printStackTrace();
            }
    }

    public static void v(String tag, String message) {
        if (ENABLED)
            try {
                Log.v(tag, message);
            } catch (Exception e) {
                //e.printStackTrace();
            }
    }

    public static void w(String tag, String message) {
        if (ENABLED)
            try {
                Log.w(tag, message);
            } catch (Exception e) {
                //e.printStackTrace();
            }
    }

    public static void wtf(String tag, String message) {
        if (ENABLED)
            try {
                Log.wtf(tag, message);
            } catch (Exception e) {
                //e.printStackTrace();
            }
    }

    public static void printStackTrace(Throwable e) {
        if (!ENABLED)
            return;

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        e.printStackTrace(printWriter);

        Log.wtf(e.getClass().getName(), stringWriter.toString());
    }

    public static Thread.UncaughtExceptionHandler uncaughtExceptionHandler
            = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();

            try {
                File file = new File(Environment.getExternalStorageDirectory()
                        +"/" + APP_NAME + "/log-" + Calendar.getInstance().getTime() + ".txt");
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                file.getParentFile().mkdirs();

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                ex.printStackTrace(printWriter);

                fileOutputStream.write(stringWriter.toString().getBytes());
                fileOutputStream.close();

                if (ENABLED)
                    Log.e("Uncaught", stringWriter.toString());

                MainApplication.defaultExceptionHandler.uncaughtException(thread, ex);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
