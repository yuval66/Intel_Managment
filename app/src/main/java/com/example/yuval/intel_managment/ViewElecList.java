package com.example.yuval.intel_managment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewElecList extends Activity {

    LoginDataBaseAdapter loginDataBaseAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleclist);

        TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        ArrayList<String> list = loginDataBaseAdapter.getAll();

        for (int i = 0; i < list.size(); i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            if (i % 2 == 0) {
                row.setBackgroundColor(Color.parseColor("#80DEEA"));
            } else {
                row.setBackgroundColor(Color.parseColor("#B2EBF2"));
            }
            row.setClickable(true);
            row.setMinimumHeight(80);
            TextView txtview1 = new TextView(this);
            txtview1.setHeight(80);
            txtview1.setTextSize(20);
            txtview1.setGravity(Gravity.CENTER);
            //  txtview1.setPadding(0,0,0,10);

            TextView txtview2 = new TextView(this);
            txtview2.setHeight(80);
            txtview2.setTextSize(20);
            txtview2.setGravity(Gravity.CENTER);
            //  txtview2.setPadding(0,0,0,10);

            TextView txtview3 = new TextView(this);
            txtview3.setHeight(80);
            txtview3.setTextSize(20);
            txtview3.setGravity(Gravity.CENTER);
            //   txtview3.setPadding(0,0,0,10);

            String s = list.get(i);
            final String[] arr = s.split(" ");

            if (!(arr[0].toString().equals("11410202"))) {
                txtview1.setText(arr[0].toString());
                row.addView(txtview1);
                txtview2.setText(arr[1].toString());
                row.addView(txtview2);
                txtview3.setText(arr[2].toString());
                row.addView(txtview3);
                row.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ArrayList<String> details = new ArrayList<String>();

                        details.add(0, arr[0].toString());
                        details.add(1, arr[1].toString());
                        details.add(2, arr[2].toString());

                        Intent i = new Intent(ViewElecList.this, ElecPage.class);
                        i.putExtra("WorkerDetails", details);
                        startActivity(i);
                    }
                });

                tl.addView(row);
            }
        }

    }
    public void onBackPressed()
    {
        Intent intent = new Intent(ViewElecList.this, ManagerActicvity.class);
        startActivity(intent);
    }
}
