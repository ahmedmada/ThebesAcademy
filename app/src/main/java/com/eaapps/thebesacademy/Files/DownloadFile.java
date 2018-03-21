package com.eaapps.thebesacademy.Files;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eaapps.thebesacademy.Chat.ChatActivity;
import com.eaapps.thebesacademy.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class DownloadFile extends AppCompatActivity {

    ListView file_name;
    DatabaseReference ref;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> keys;
    ArrayList<String> urls;
    ArrayAdapter<String> adapter;
    String doctor_key,subject_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);

        doctor_key = getIntent().getStringExtra("doctor_key");
        subject_name = getIntent().getStringExtra("subject_name");

        file_name = (ListView) findViewById(R.id.download_list);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        file_name.setAdapter(adapter);
        file_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = urls.get(i);

                CharSequence options[] = new CharSequence[]{"Download File", "Open Online"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(DownloadFile.this);

                builder.setTitle("Select Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Click Event for each item.
                        if(i == 0){

                            new DownloadTask(DownloadFile.this, url);
                        }
//
                        if(i == 1){

                            Intent chatIntent = new Intent(DownloadFile.this, ViewPDF.class);
                            chatIntent.putExtra("url", url);
                            startActivity(chatIntent);

                        }

                    }
                });

                builder.show();

            }
        });


        keys = new ArrayList<>();
        urls = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Files").child(doctor_key).child(subject_name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                String value = dataSnapshot.getValue().toString();
                list.add(key);
                urls.add(value);
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
