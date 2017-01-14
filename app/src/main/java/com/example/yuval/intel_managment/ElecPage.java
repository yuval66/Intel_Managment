package com.example.yuval.intel_managment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class ElecPage extends Activity {

    private Activity activityReference;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReference = this;
        setContentView(R.layout.elec_page);
        Intent i = getIntent();
        final ArrayList<String> details;
        details = (ArrayList<String>) getIntent().getSerializableExtra("WorkerDetails");

        TextView firstName = (TextView) findViewById(R.id.text3);
        TextView lastName = (TextView) findViewById(R.id.text5);
        TextView WorkerID = (TextView) findViewById(R.id.text7);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        firstName.setText(details.get(1).toString());
        lastName.setText(details.get(2).toString());
        WorkerID.setText(details.get(0).toString());

        Button btnDelete = (Button) findViewById(R.id.deleteButton);

        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activityReference);
                TextView title = new TextView(activityReference);
                title.setText("Delete Electician");
                title.setBackgroundColor(Color.BLACK);
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(30);
                builder.setCustomTitle(title);

                LinearLayout layout = new LinearLayout(activityReference);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                TextView tv1 = new TextView(activityReference);
                tv1.setText("Are you Sure you want to delete this worker?");
                tv1.setPadding(50, 50, 50, 50);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextSize(20);

                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                layout.addView(tv1, tv1Params);
                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String etStr = details.get(0).toString();

                        if (etStr == null || etStr.equals("")) {
                            Toast.makeText(activityReference, "Worker ID is empty", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            String name = loginDataBaseAdapter.getName(etStr);
                            int num = loginDataBaseAdapter.deleteEntry(etStr);

                            if (num == 0) {
                                Toast.makeText(activityReference, "This Worker ID does not exist", Toast.LENGTH_LONG).show();
                                return;
                            } else if (num >= 1) {
                                Toast.makeText(activityReference, "Delete succsess: " + name + "" +
                                        " wad deleted", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ElecPage.this, ViewElecList.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


    }
}


