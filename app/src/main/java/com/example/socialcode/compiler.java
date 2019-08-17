    package com.example.socialcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

    public class compiler extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compiler);

        webView = findViewById(R.id.compiler_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://ide.codingblocks.com");
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url){
//                view.loadUrl("https://www.google.com");
//                return true;
//            }
//        });
    }
}
