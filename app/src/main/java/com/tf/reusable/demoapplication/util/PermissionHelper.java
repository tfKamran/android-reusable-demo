package com.tf.reusable.demoapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by kamran on 8/28/16.
 */

public class PermissionHelper {

    private static final int PERMISSION_REQUEST_CODE = 0;

    public static void askForAllPermissions(Activity activity) {
        try {
            String[] permissions = getRequiredPermissions(activity);
            ArrayList<String> permissionsToBeAsked = new ArrayList<>();

            if (permissions != null) {
                for (String permission : permissions) {
                    if (!isPermissionGranted(activity, permission) && isDangerous(activity, permission)) {
                        permissionsToBeAsked.add(permission);
                    }
                }
            }

            if (permissionsToBeAsked.size() > 0) {
                askForPermission(activity, permissionsToBeAsked.toArray(new String[]{}));
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

    public static boolean areAllPermissionsGranted(Context context) {
        boolean areGranted = true;

        try {
            String[] permissions = getRequiredPermissions(context);

            for (String permission : permissions) {
                areGranted = areGranted
                        && (!isDangerous(context, permission) || isPermissionGranted(context, permission));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.printStackTrace(e);

            return false;
        }

        return areGranted;
    }

    public static void askForPermission(Activity activity, String permissions) {
        askForPermission(activity, new String[]{permissions});
    }

    public static void askForPermission(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= 22) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private static boolean isDangerous(Context context, String permission) {
        boolean isDangerous = false;

        try {
            isDangerous = context.getPackageManager().getPermissionInfo(permission, 0).protectionLevel
                    == PermissionInfo.PROTECTION_DANGEROUS;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return isDangerous;
    }
}
