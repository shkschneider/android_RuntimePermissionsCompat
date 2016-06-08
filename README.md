RuntimePermissionsCompat
========================

> [Runtime Permissions](https://developer.android.com/preview/features/runtime-permissions.html) helper library for pre-and-post API-23

Setup
-----

[![JitPack](https://jitpack.io/v/com.github.shkschneider/android_RuntimePermissionsCompat.svg)](https://jitpack.io/#shkschneider/android_RuntimePermissionsCompat/1.1.0)

Specifications
--------------

[![API-4+](https://img.shields.io/badge/API-4+-blue.svg?style=flat)](https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#DONUT)

[![Apache 2](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat)](https://raw.githubusercontent.com/shkschneider/android_RuntimePermissionsCompat/master/LICENSE)

**Libraries**

- [com.android.support:support-v4](https://developer.android.com/tools/support-library/features.html#v4)

**Uses**

- [Gradle 2.1.0](http://tools.android.com/tech-docs/new-build-system) (wrapper 2.11)
- [Android Studio 2.1.1](https://developer.android.com/sdk/index.html)

Usage
-----

<pre>(boolean) RuntimePermissionsCompat.isGranted(Context context, String permission)
(boolean) RuntimePermissionsCompat.isAnyGranted(Context context, String[] permissions)
(boolean) RuntimePermissionsCompat.areAllGranted(Context context, String[] permissions)
(boolean) RuntimePermissionsCompat.shouldPrompt(Activity activity, String permission)
(boolean) RuntimePermissionsCompat.shouldPrompt(Activity activity, String[] permissions)
(void) RuntimePermissionsCompat.requestPermission(Activity activity, String permission)
       calls Activity.onRequestPermissionsResult() using reflection
(void) RuntimePermissionsCompat.requestPermission(Activity activity, String[] permissions)
       calls Activity.onRequestPermissionsResult() using reflection
(boolean) RuntimePermissionsCompat.isRevocable(Context context, String permission)
(boolean) RuntimePermissionsCompat.isAnyRevocable(Context context, String[] permissions)
(boolean) RuntimePermissionsCompat.areAllRevocable(Context context, String[] permissions)
(Map&lt;String, Boolean&gt;) RuntimePermissionsCompat.onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
                       returns a Map<String, Boolean> of permissions
                       with Boolean.TRUE (PackageManager.PERMISSION_GRANTED)
                       or Boolean.FALSE (PackageManager.PERMISSION_DENIED) for simplicity</pre>

**Single permission example**

<pre>private void requestPermission() {
    if (RuntimePermissionsCompat.isGranted(context, Manifest.permission.ANY_PERMISSION)) {
        Toast.makeText(activity, "Permission already GRANTED", Toast.LENGTH_SHORT).show();
        // return ;
    }
    else if (RuntimePermissionsCompat.shouldPrompt(activity, Manifest.permission.ANY_PERMISSION)) {
        // Explain WHY you need this permission
        Toast.makeText(activity, "Explain WHY you need this permission", Toast.LENGTH_SHORT).show();
    }
    RuntimePermissionsCompat.requestPermission(activity, Manifest.permission.ANY_PERMISSION);
}

@Override
public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
    final Map&lt;String, Boolean&gt; runtimePermissionsResults = RuntimePermissionsCompat.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (runtimePermissionsResults != null) {
        final boolean granted = runtimePermissionsResults.get(permissions[0]);
        Toast.makeText(MainActivity.this, (granted ? "Permission GRANTED" : "Permission DENIED"), Toast.LENGTH_SHORT).show();
        return ;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
}</pre>

See [demo's MainActivity.java](https://github.com/shkschneider/android_RuntimePermissionsCompat/blob/master/demo/src/main/java/me/shkschneider/runtimepermissionscompat/demo/MainActivity.java).
