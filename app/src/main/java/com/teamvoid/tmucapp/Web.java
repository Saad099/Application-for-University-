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

        }
        webView.getSettings().setUserAgentString(userAgent);
        webView.reload();

        webView.setWebChromeClient(new MyWebChromeClient(){
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });


        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        //handle downloading
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, final String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //Checking runtime permission for devices above Marshmallow.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v("TAG", "Permission is granted");
                        downloadDialog(url, userAgent, contentDisposition, mimetype);
                    } else {
                        Log.v("TAG", "Permission is revoked");
                        //requesting permissions.
                        ActivityCompat.requestPermissions(Web.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    //Code for devices below API 23 or Marshmallow
                    Log.v("TAG", "Permission is granted");
                    downloadDialog(url, userAgent, contentDisposition, mimetype);
                }
            }
        });

        HomeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                HomePressed = true;
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(HomePressed) {
            super.onBackPressed();
            HomePressed = false;
        }
        else if(webView.canGoBack()){
            webView.goBack();
            progressBar.setVisibility(View.GONE);
        }
        else{
            super.onBackPressed();
        }
    }



    //downloadDialog Method
    public void downloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype) {
        //getting filename from url.
        final String filename;

        if(contentDisposition.split("filename=").length >= 2)
            filename = contentDisposition.split("filename=")[1].split(";")[0].replaceAll("^\"|\"$", "");
        else
            filename = URLUtil.guessFileName(url, contentDisposition, mimetype).replaceAll("^\"|\"$", "");
        //alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //title of alertdialog
        builder.setTitle("Download & Save: ");
        //message of alertdialog
        builder.setMessage("File: "+ filename);
        //if Yes button clicks.

        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DownloadManager.Request created with url.
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                //cookie
                String cookie = CookieManager.getInstance().getCookie(url);
                //Add cookie and User-Agent to request
                request.addRequestHeader("Cookie", cookie);
                request.addRequestHeader("User-Agent", userAgent);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setAllowedOverMetered(true);
                request.setVisibleInDownloadsUi(true);
                //file scanned by MediaScannar
                request.allowScanningByMediaScanner();
                //Download is visible and its progress, after completion too.
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(filename).setDescription("FDH APP");
                //DownloadManager created
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                //Saving files in Download folder
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/FDH Downloads");
                if (!f.exists()) {
                    f.mkdirs();
                }
                File file = new File(f.getAbsoluteFile()+"/"+filename);
                request.setDestinationUri(Uri.fromFile(file));
                //download enqued
                long id = downloadManager.enqueue(request);
                Toast.makeText(Web.this,"Starting Download", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel the dialog if Cancel clicks
                dialog.cancel();
            }
        });
        //alertdialog shows.
        builder.show();
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
}
