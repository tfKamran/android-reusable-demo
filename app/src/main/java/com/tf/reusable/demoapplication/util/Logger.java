package com.tf.reusable.demoapplication.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by kamran on 16/12/14.
 */
public class Logger {
    public static Thread.UncaughtExceptionHandler defaultExceptionHandler;

    private static String applicationName;
    public static boolean ENABLED = true;

    public static void initialize(Context context,
                                  Thread.UncaughtExceptionHandler exceptionHandler) {
        defaultExceptionHandler = exceptionHandler;

        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

        applicationName = (String) context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

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
                        +"/" + applicationName + "/log-" + getTimeStamp() + ".txt");

                file.getParentFile().mkdirs();
                
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                ex.printStackTrace(printWriter);

                fileOutputStream.write(stringWriter.toString().getBytes());
                fileOutputStream.close();

                if (ENABLED)
                    Log.e("Uncaught", stringWriter.toString());

                defaultExceptionHandler.uncaughtException(thread, ex);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private static String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR)
                + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                + "-" + calendar.get(Calendar.MINUTE)
                + "-" + calendar.get(Calendar.SECOND)
                + "-" + calendar.get(Calendar.MILLISECOND);
    }
}
