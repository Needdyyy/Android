package com.needyyy.app.Modules.Home.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.utils.Constant;


public class WebViewActivity extends AppCompatActivity {
    ProgressDialog progressBar;
    String TAG = WebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView webView = (WebView) findViewById(R.id.webView);

        progressBar = ProgressDialog.show(WebViewActivity.this, "", "loading...");

        String url = getIntent().getExtras().getString(Constants.URL);
        String data = getIntent().getExtras().getString(Constant.kData);
        String title = getIntent().getExtras().getString(Constant.Title);

        Log.e(TAG, "url is " + url);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        if (!TextUtils.isEmpty(title))
            toolbar.setTitle(title);
        else toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        //Add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arow);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        this.setProgressBarVisibility(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient() {


            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });

        if (url != null && url.contains(".pdf")) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
        } else if (!TextUtils.isEmpty(data)) {
            webView.loadData(data, "text/html", "UTF-8");
        } else {
            webView.loadUrl(url);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e(TAG, "the item id is " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}