package com.eaapps.thebesacademy.Table;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.eaapps.thebesacademy.R;

public class Table extends AppCompatActivity {

    Button addCol,addRow;
    TableLayout table;
    int row = 5;
    int col = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        addCol = (Button) findViewById(R.id.addCol);
        addRow = (Button) findViewById(R.id.addRow);

        table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);
        table.bringToFront();



        buidTable(row,col);

        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row++;
                buidTable(row,col);
            }
        });

        addCol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                col++;
                buidTable(row,col);
            }
        });


    }

    public void buidTable(int r,int c){

        table.removeAllViews();

        for(int i = 0; i < r; i++){
            TableRow tr =  new TableRow(this);
            for (int j = 0 ; j < c ; j++){
                TextView tv = new TextView(this);
                tv.setText(" ("+i+","+j+") ");
                tv.setBackgroundColor(0xfff0f000);

//                TableLayout.LayoutParams params = new TableLayout.LayoutParams(
//                        TableLayout.LayoutParams.WRAP_CONTENT,
//                        TableLayout.LayoutParams.WRAP_CONTENT
//                );
//                params.setMargins(5, 5, 5, 5);
//                tv.setLayoutParams(params);
                tr.addView(tv);
            }


            table.addView(tr);
        }

    }
}
