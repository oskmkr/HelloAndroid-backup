package io.oskm.helloandroid;

import android.app.Activity;
import android.content.Intent;
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

public class MainActivity extends Activity {

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("hello log", "hi");

        setContentView(R.layout.activity_main);

        this.initializeWebView();

        /*

        Button goButton = (Button)findViewById(R.id.goButton);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        */
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
}
