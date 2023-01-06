package com.teamvoid.tmucapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    static public String Ref = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();

        ProgressDialog loading = new ProgressDialog(List.this);
        loading.setCancelable(false);
        loading.setMessage("Please Wait.");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Ref);

        TextView Heading = findViewById(R.id.Heading);
        LinearLayout list = findViewById(R.id.list);
        LayoutInflater inflater = LayoutInflater.from(this);

        Heading.setText(Ref);

        loading.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.removeAllViews();
                if (snapshot.exists()){
                    for (DataSnapshot event: snapshot.getChildren()){
                        if (event.exists()){
                            java.util.List<String> eventDetails = new ArrayList<>();
                            for (DataSnapshot value: event.getChildren()){
                                if (value.exists()){
                                    eventDetails.add(value.getValue().toString());
                                }
                            }


    }
}