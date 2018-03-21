package com.eaapps.thebesacademy.Files;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eaapps.thebesacademy.R;

public class ViewPDF extends AppCompatActivity {

    private WebView webView;
    Activity activity ;
    private ProgressDialog progDailog;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        url = getIntent().getStringExtra("url");


        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
        progDailog.setCancelable(false);
        webView = (WebView) findViewById(R.id.webview_compontent);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//following lines are to show the loader untile downloading the pdf file for view.
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });

        webView.loadUrl(url);   // webview loader to load the URL of file
    }  }