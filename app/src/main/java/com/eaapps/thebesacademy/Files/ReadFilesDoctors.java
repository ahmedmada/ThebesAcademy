package com.eaapps.thebesacademy.Files;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ReadFilesDoctors extends AppCompatActivity {

    ListView doctor_name;
    DatabaseReference ref;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> keys;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_files_doctors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReadFilesDoctors.this, UploadFile.class));

            }
        });

        doctor_name = (ListView) findViewById(R.id.doctor_name);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        doctor_name.setAdapter(adapter);
        doctor_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String doctor_key = keys.get(i);
                startActivity(new Intent(ReadFilesDoctors.this, FileActivity.class));

                Intent intent = new Intent(ReadFilesDoctors.this, FileActivity.class);
                intent.putExtra("doctor_key", doctor_key);
                startActivity(intent);
            }
        });


        keys = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Files").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                keys.add(key);

                ref.child("Student User").child(key).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        list.add(snapshot.getValue().toString());

                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey().toString();
                keys.add(key);

                ref.child("Student User").child(key).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        list.add(snapshot.getValue().toString());

                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
