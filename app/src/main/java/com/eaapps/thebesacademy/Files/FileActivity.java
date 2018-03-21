package com.eaapps.thebesacademy.Files;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eaapps.thebesacademy.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {


    ListView file_name;
    DatabaseReference ref;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> keys;
    ArrayAdapter<String> adapter;
    String doctor_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        doctor_key = getIntent().getStringExtra("doctor_key");

        file_name = (ListView) findViewById(R.id.file_name);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        file_name.setAdapter(adapter);
        file_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String subject_name = list.get(i);
//                startActivity(new Intent(ReadFilesDoctors.this, FileActivity.class));

                Intent intent = new Intent(FileActivity.this, DownloadFile.class);
                intent.putExtra("doctor_key", doctor_key);
                intent.putExtra("subject_name", subject_name);
                startActivity(intent);
            }
        });


        keys = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Files").child(doctor_key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                list.add(key);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey().toString();
                list.add(key);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
