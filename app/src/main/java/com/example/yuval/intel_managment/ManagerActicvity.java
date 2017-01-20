package com.example.yuval.intel_managment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerActicvity extends AppCompatActivity {

    private Activity activityReference;
    Button btnAdd;
    Button btnViewMissions;
    Button btnViewElec;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReference = this;
        setContentView(R.layout.activity_manager);
        btnAdd = (Button) findViewById(R.id.addElecButton);
        btnViewMissions = (Button) findViewById(R.id.viewMissions);
        btnViewElec = (Button) findViewById(R.id.viewElectrician);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ManagerActicvity.this, InsertElecActivity.class);
                startActivity(i);
            }
        });


        btnViewMissions.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


            }
        });

        btnViewElec.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(ManagerActicvity.this, ViewElecList.class);
                startActivity(i);
            }
        });

    }

    public void onBackPressed()
    {
        Intent intent = new Intent(ManagerActicvity.this, MainActivity.class);
        startActivity(intent);
    }
}
