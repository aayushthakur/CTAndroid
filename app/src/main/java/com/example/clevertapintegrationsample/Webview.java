package com.example.clevertapintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webview extends AppCompatActivity {

    WebView simpleWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        simpleWebView = (WebView) findViewById(R.id.webview);
        simpleWebView.setWebViewClient(new MyWebViewClient());
        String url = "https://youtu.be/6kvsXhHUhSs?si=vs4a3_eXrJHBhlIs?autoplay=1";
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url); // load a web page in a web view

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}