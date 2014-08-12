package io.oskm.helloandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private static final String PROPERTY_REG_ID = "registraion_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String SENDER_ID = "533693650635";
    private WebView webView = null;
    private GoogleCloudMessaging gcm;
    private String regId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "hi");

        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        this.initializeWebView();

        if (checkPlayServices()) {
            // if this check succeeds, proceed with normal processing.
            // otherwise, prompt user to get valid Play Services APK.
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(context);

            Log.i(TAG, "[registration id]" + regId);

            if (regId.isEmpty()) {
                registerInBackground();
            }

        } else {
            Log.i(TAG, "No valid Google Play Service APK found");
        }
    }

    private void registerInBackground() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                Log.i(TAG, "doInBackground...");
                String msg = "";
                try {
                    Log.i(TAG, "[gcm object] : " + gcm);
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;
                    Log.i(TAG, "[msg] : " + msg);
                    sendRegistrationIdtoBackend();

                    storeRegistrationId(context, regId);

                } catch (IOException ex) {
                    Log.i(TAG, "error : " + ex.getMessage());
                    ex.printStackTrace();
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //Toast.makeText(context, )
                Log.i(TAG, "onPostExecute : " + msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreference(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version" + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void sendRegistrationIdtoBackend() {
        // Your implementation here.
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreference(context);
        String regId = prefs.getString(PROPERTY_REG_ID, "");

        if (regId.isEmpty()) {
            Log.i(TAG, "registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MAX_VALUE);
        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }

        return regId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);

        }

    }

    private SharedPreferences getGCMPreference(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_room_list) {
            Intent intentRoomListActivity = new Intent(MainActivity.this, RoomListActivity.class);
            startActivity(intentRoomListActivity);
            return true;
        } else if (id == R.id.action_login) {
            Intent intentLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLoginActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeWebView() {
        webView = (WebView) findViewById(R.id.webView);
        //webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);
        webView.loadUrl("http://m.naver.com");

    }

    public void onClick(View v) {
        Log.d("oskm", "onClick....");


        switch (v.getId()) {
            case R.id.goButton:
                EditText editText = (EditText) findViewById(R.id.urlText);
                Log.d("oskm", editText.getText().toString());
                webView.loadUrl(editText.getText().toString());
                break;
            case R.id.bottomButton:
                Toast.makeText(MainActivity.this, "open using intent", Toast.LENGTH_SHORT).show();
                Intent intentFullScreenActivity = new Intent(MainActivity.this, FullscreenActivity.class);
                startActivity(intentFullScreenActivity);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
