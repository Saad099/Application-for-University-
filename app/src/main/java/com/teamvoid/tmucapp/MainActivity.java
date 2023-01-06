package com.teamvoid.tmucapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Intent web = new Intent(getApplicationContext(), Web.class);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        findViewById(R.id.timetable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputStream inputStream = getResources().openRawResource(R.raw.timetable);
                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "timetable.pdf");
                    try (OutputStream output = new FileOutputStream(file)) {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();

                        Uri path = Uri.fromFile(file);
                        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfOpenintent.setDataAndType(path, "application/pdf");
                        try {
                            startActivity(pdfOpenintent);
                        }
                        catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "Allow Permissions in Settings", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Allow Permissions in Settings", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Allow Permissions in Settings", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null)
                        )
                );
            }
        });
        findViewById(R.id.news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Web.url = "https://tmuc.edu.pk/college-life/#news-event";
                startActivity(web);
            }
        });
        findViewById(R.id.lms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Web.url = "https://vle.tmuc.edu.pk/login/index.php";
                startActivity(web);
            }
        });
        findViewById(R.id.notices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List.Ref = "Notices";
                startActivity(new Intent(getApplicationContext(),List.class));
            }
        });
        findViewById(R.id.events).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List.Ref = "Events";
                startActivity(new Intent(getApplicationContext(),List.class));
            }
        });






    }
}