package com.eaapps.thebesacademy.PDF;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaapps.thebesacademy.R;
import java.io.File;
import java.util.ArrayList;

public class PdfListActivity extends AppCompatActivity {

    private File root;
    private ArrayList<File> fileList = new ArrayList<File>();
    private ArrayList<String> nameList = new ArrayList<String>();
    private ArrayList<String> pathList = new ArrayList<String>();
//    private LinearLayout view;
    ListView pdf_list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);



//        view = (LinearLayout) findViewById(R.id.view);
        pdf_list = (ListView) findViewById(R.id.pdf_list);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,nameList);
        pdf_list.setAdapter(adapter);
        pdf_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(PdfListActivity.this,"path = "+pathList.get(i),Toast.LENGTH_LONG).show();

                Log.v("vvvvvvvvvvvvvv",
                        String.valueOf(Uri.parse(new File(fileList.get(i).getAbsolutePath()).toString())));
                Log.v("vvvvvvvvvvvvvv",fileList.get(i).getAbsolutePath());
                Log.v("vvvvvvvvvvvvvv",fileList.get(i).getAbsoluteFile().toString());
                Log.v("vvvvvvvvvvvvvv",fileList.get(i).getParentFile().toString());
                Log.v("vvvvvvvvvvvvvv",fileList.get(i).getParent());

                Intent intent = new Intent();
                intent.putExtra("filepath", pathList.get(i));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        getfile(root);

//        for (int i = 0; i < fileList.size(); i++) {
//            TextView textView = new TextView(this);
//            textView.setText(fileList.get(i).getName());
//            textView.setPadding(5, 5, 5, 5);
//
//            System.out.println(fileList.get(i).getName());
//
//            if (fileList.get(i).isDirectory()) {
//                textView.setTextColor(Color.parseColor("#FF0000"));
//            }
//            view.addView(textView);
//        }

    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    fileList.add(listFile[i]);
                    nameList.add(listFile[i].getName());
                    pathList.add(listFile[i].getPath());
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".pdf")
//                            || listFile[i].getName().endsWith(".xls")
//                            || listFile[i].getName().endsWith(".jpg")
//                            || listFile[i].getName().endsWith(".jpeg")
//                            || listFile[i].getName().endsWith(".png")
//                            || listFile[i].getName().endsWith(".doc")
                            ) {
                        fileList.add(listFile[i]);
                        nameList.add(listFile[i].getName());
                        pathList.add(listFile[i].getPath());
                    }
                }

            }
        }
        adapter.notifyDataSetChanged();

        return fileList;
    }

}