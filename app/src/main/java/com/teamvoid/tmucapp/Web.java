package com.teamvoid.tmucapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/*
    Code & Designed By VOID Waleed (insta @voidwaleed) in 2022
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class Web extends AppCompatActivity {

    WebView webView;
    static String url = "";
    static boolean desktop = false;
    static boolean HomePressed = false;
    Button HomeBtn;
    ProgressBar progressBar;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        }

        HomeBtn = findViewById(R.id.homeBtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        class MyWebChromeClient extends WebChromeClient {
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        }
        webView = findViewById(R.id.web);
        webView.setWebChromeClient( new MyWebChromeClient());
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(url);
        String userAgent = webView.getSettings().getUserAgentString();
        if(desktop){
            try {
                String androidString = webView.getSettings().getUserAgentString().
                        substring(userAgent.indexOf("("),userAgent.indexOf(")")+ 1);

                userAgent = webView.getSettings().getUserAgentString().replace(androidString,"X11; Linux x86_64");

            }catch (Exception e){
                e.printStackTrace();
            }

        }}}

