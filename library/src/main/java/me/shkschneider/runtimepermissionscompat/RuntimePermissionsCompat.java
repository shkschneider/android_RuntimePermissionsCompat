package me.shkschneider.runtimepermissionscompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Method;

public class RuntimePermissionsCompat {

    private static final String TAG = RuntimePermissionsCompat.class.getSimpleName();
    private static final int MARSHMALLOW = Build.VERSION_CODES.M;
    private static final int REQUEST_CODE = MARSHMALLOW;

    protected RuntimePermissionsCompat() {
        // Empty
    }

    public static boolean isGranted(@NonNull final Context context, @NonNull final String permission) {
        if (Build.VERSION.SDK_INT < MARSHMALLOW) {
            final PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            return (packageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED);
        }
        return (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermission(@NonNull final Activity activity, @NonNull final String[] permissions) {
        if (Build.VERSION.SDK_INT < MARSHMALLOW) {
            Log.i(TAG, "No Runtime Permissions");
            try {
                Log.i(TAG, "Trying to call Activity.onRequestPermissionsResult()");
                final Method method = activity.getClass().getMethod("onRequestPermissionsResult", int.class, String[].class, int[].class);
                method.setAccessible(true);
                final int[] grantResults = new int[permissions.length];
                for (int i = 0; i < permissions.length; i++) {
                    grantResults[i] = (isGranted(activity, permissions[i]) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED);
                }
                method.invoke(activity, REQUEST_CODE, permissions, grantResults);
            }
            catch (final Exception e) {
                Log.w(TAG, "Calling Activity must implement onRequestPermissionsResult()", e);
            }
            return ;
        }
        activity.requestPermissions(permissions, REQUEST_CODE);
    }

    public static boolean areAllGranted(@NonNull final int[] grantResults) {
        for (final int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGranted(@NonNull final Context context, @NonNull final String permission, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (Build.VERSION.SDK_INT < MARSHMALLOW) {
            Log.i(TAG, "No Runtime Permissions -- bridging to PackageManager.checkPermission()");
            return isGranted(context, permission);
        }
        int index = -1;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(permission)) {
                index = i;
                break ;
            }
        }
        if (index == -1) {
            return isGranted(context, permission);
        }
        return (grantResults[index] == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isRevocable(@NonNull final Context context, @NonNull final String permission) {
        if (Build.VERSION.SDK_INT < MARSHMALLOW) {
            Log.d(TAG, "No Runtime Permissions -- assuming NOT REVOCABLE");
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        try {
            final PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
            @SuppressLint("InlinedApi") // API-16+
            final int protectionLevel = (permissionInfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE);
            return (protectionLevel != PermissionInfo.PROTECTION_NORMAL);
        }
        catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

}
