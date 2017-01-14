package com.example.yuval.intel_managment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertElecActivity extends Activity{

    EditText edtFirstName;
    EditText edtLastName;
    EditText edtWorkerID;
    EditText edtPassword;
    Button addbtn;
    Button clearbtn;
    LoginDataBaseAdapter loginDataBaseAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.insert_elec);

            loginDataBaseAdapter = new LoginDataBaseAdapter(this);
            loginDataBaseAdapter = loginDataBaseAdapter.open();
            edtFirstName = (EditText) findViewById(R.id.EdtFirstName);
            edtLastName = (EditText) findViewById(R.id.EdtLastName);
            edtWorkerID = (EditText) findViewById(R.id.EdtElec);
            edtPassword = (EditText) findViewById(R.id.EdtElecPass);
            addbtn = (Button) findViewById(R.id.addButton);
            clearbtn = (Button) findViewById(R.id.clearcButton);



            addbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    String FirstName = edtFirstName.getText().toString();
                    String LastName = edtLastName.getText().toString();
                    String WorkerID = edtWorkerID.getText().toString();
                    String Password = edtPassword.getText().toString();

                    if(FirstName.length() <= 0|| LastName.length() <= 0 || WorkerID.length() <= 0 || Password.length() <= 0){
                        Toast.makeText(com.example.yuval.intel_managment.InsertElecActivity.this, "One of the fields is empty", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(WorkerID.length() != 8 || !(WorkerID.matches("[-+]?\\d*\\.?\\d+")))
                    {
                        edtWorkerID.setText("");
                        Toast.makeText(com.example.yuval.intel_managment.InsertElecActivity.this, "Invalid Worker ID", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(!(loginDataBaseAdapter.getSinlgeEntry(WorkerID).equals("NOT EXIST")))
                    {
                        edtWorkerID.setText("");
                        Toast.makeText(com.example.yuval.intel_managment.InsertElecActivity.this, "Worker ID already exist", Toast.LENGTH_LONG).show();
                        return;
                    }

                    long num = loginDataBaseAdapter.insertEntry(WorkerID, Password,FirstName,LastName);

                    if(num == -1)
                    {
                        Toast.makeText(com.example.yuval.intel_managment.InsertElecActivity.this, "Cannot add - try again", Toast.LENGTH_LONG).show();
                        edtFirstName.setText("");
                        edtLastName.setText("");
                        edtWorkerID.setText("");
                        edtPassword.setText("");
                    }

                    else
                    {
                        Toast.makeText(com.example.yuval.intel_managment.InsertElecActivity.this, "add Successfull", Toast.LENGTH_LONG).show();
                        edtFirstName.setText("");
                        edtLastName.setText("");
                        edtWorkerID.setText("");
                        edtPassword.setText("");
                    }
                }
            });

            clearbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtWorkerID.setText("");
                    edtPassword.setText("");
                }
            });
        }


    }

