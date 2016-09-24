package com.tf.reusable.demoapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by kamran on 8/28/16.
 */

public class PermissionHelper {

    private static final int PERMISSION_REQUEST_CODE = 0;

    public static void askForAllPermissions(Activity activity) {
        try {
            String[] permissions = getRequiredPermissions(activity);

            if (permissions != null) {
                for (String permission : permissions) {
                    if (!isPermissionGranted(activity, permission)) {
                        askForPermission(activity, permission);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String[] getRequiredPermissions(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
                        .requestedPermissions;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return Build.VERSION.SDK_INT <= 22
                || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForPermission(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= 22) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
        }
    }
}
