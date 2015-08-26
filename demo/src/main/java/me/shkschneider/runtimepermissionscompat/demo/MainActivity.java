package me.shkschneider.runtimepermissionscompat.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import me.shkschneider.runtimepermissionscompat.RuntimePermissionsCompat;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Permission> mArrayAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(getResources().getDrawable(R.drawable.ic_launcher));
        toolbar.setTitle("Demo");
        toolbar.setSubtitle(getResources().getString(R.string.app_label));
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/shkschneider/android_RuntimePermissionsCompat")));
                return true;
            }

        });

        final ListView listView = (ListView) findViewById(R.id.listView);
        mArrayAdapter = new ArrayAdapter<Permission>(MainActivity.this, android.R.layout.simple_list_item_2) {

            {
                add(new Permission(Manifest.permission.ACCESS_COARSE_LOCATION, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.ACCESS_FINE_LOCATION, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.ACCESS_NETWORK_STATE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.ACCESS_WIFI_STATE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.BLUETOOTH, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.BLUETOOTH_ADMIN, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.BROADCAST_STICKY, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.CALL_PHONE, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.CAMERA, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.CHANGE_NETWORK_STATE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.CHANGE_WIFI_STATE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.DISABLE_KEYGUARD, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.EXPAND_STATUS_BAR, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.FLASHLIGHT, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.GET_ACCOUNTS, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.GET_PACKAGE_SIZE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.INTERNET, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.MODIFY_AUDIO_SETTINGS, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.READ_CALENDAR, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.READ_CONTACTS, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.READ_PHONE_STATE, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.READ_SMS, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.READ_SYNC_STATS, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.RECEIVE_BOOT_COMPLETED, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.RECEIVE_MMS, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.RECEIVE_SMS, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.RECEIVE_WAP_PUSH, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.RECORD_AUDIO, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.REORDER_TASKS, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.SEND_SMS, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.SET_TIME_ZONE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.SET_WALLPAPER, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.SET_WALLPAPER_HINTS, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.VIBRATE, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.WAKE_LOCK, PermissionInfo.PROTECTION_NORMAL));
                add(new Permission(Manifest.permission.WRITE_CALENDAR, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.WRITE_CONTACTS, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionInfo.PROTECTION_DANGEROUS));
                add(new Permission(Manifest.permission.WRITE_SYNC_SETTINGS, PermissionInfo.PROTECTION_NORMAL));
            }

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_2, null);
                }
                final Permission permission = getItem(position);
                final TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                textView1.setText(permission.name());
                textView1.setTextColor((RuntimePermissionsCompat.isGranted(MainActivity.this, permission.name) ? Color.BLACK : Color.RED));
                final TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                textView2.setText(permission.protectionLevel());
                textView2.setTextColor((RuntimePermissionsCompat.isRevocable(MainActivity.this, permission.name) ? Color.RED : Color.BLACK));
                return convertView;
            }

        };
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
                final Permission permission = mArrayAdapter.getItem(position);
                final boolean shouldPrompt = RuntimePermissionsCompat.shouldPrompt(MainActivity.this, permission.name);
                if (RuntimePermissionsCompat.isGranted(MainActivity.this, permission.name)) {
                    Toast.makeText(MainActivity.this, "Permission already GRANTED", Toast.LENGTH_SHORT).show();
                    // return ;
                }
                RuntimePermissionsCompat.requestPermission(MainActivity.this, new String[] { permission.name });
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        final Map<String, Boolean> runtimePermissionsResults = RuntimePermissionsCompat.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (runtimePermissionsResults != null) {
            final boolean granted = runtimePermissionsResults.get(permissions[0]);
            Toast.makeText(MainActivity.this, (granted ? "Permission GRANTED" : "Permission DENIED"), Toast.LENGTH_SHORT).show();
            return ;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
