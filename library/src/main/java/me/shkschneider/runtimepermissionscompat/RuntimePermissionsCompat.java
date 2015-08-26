package me.shkschneider.runtimepermissionscompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RuntimePermissionsCompat {

    private static final String TAG = RuntimePermissionsCompat.class.getSimpleName();
    private static final int MARSHMALLOW = Build.VERSION_CODES.M;
    private static final int REQUEST_CODE = MARSHMALLOW;

    protected RuntimePermissionsCompat() {
        // Empty
    }

    public static boolean isGranted(@NonNull final Context context, @NonNull final String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermission(@NonNull final Activity activity, @NonNull final String[] permissions) {
        if (Build.VERSION.SDK_INT < MARSHMALLOW) {
            try {
                Log.i(TAG, "No Runtime Permissions -- bridging to Activity.onRequestPermissionsResult()");
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

        int alreadyGranted = 0;
        for (final String permission : permissions) {
            alreadyGranted += (isGranted(activity, permission) ? 1 : 0);
        }
        if (alreadyGranted == permissions.length) {
            final int[] grantResults = new int[alreadyGranted];
            for (int i = 0; i < alreadyGranted; i++) {
                grantResults[i] = PackageManager.PERMISSION_GRANTED;
            }
            activity.onRequestPermissionsResult(REQUEST_CODE, permissions, grantResults);
            return ;
        }
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    public static boolean isRevocable(@NonNull final Context context, @NonNull final String permission) {
        if (Build.VERSION.SDK_INT < MARSHMALLOW) {
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
            // FIXME CHANGE_NETWORK_STATE GET_ACCOUNTS normal instead of dangerous?
            // <https://stackoverflow.com/q/32208863>
            return (protectionLevel != PermissionInfo.PROTECTION_NORMAL);
        }
        catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static Map<String, Boolean> onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            final Map<String, Boolean> results = new HashMap<>();
            for (int i = 0; i < permissions.length; i++) {
                results.put(permissions[i], ((grantResults[i] == PackageManager.PERMISSION_GRANTED) ? Boolean.TRUE : Boolean.FALSE));
            }
            return results;
        }
        return null;
    }

}
