package com.android.baidu.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

    WebView mWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        findViewById();
    }

    private void findViewById() {
        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        String url = getIntent().getStringExtra(Config.FIELD_KEYWORDS);
        mWebView.loadUrl(url);
    }

    private class WebClient extends  WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;

        }
    }
}
