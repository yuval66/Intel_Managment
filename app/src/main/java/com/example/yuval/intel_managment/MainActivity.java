package com.example.yuval.intel_managment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {

    Button btnSignIn;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    EditText editTextUserName ;
    EditText editTextPassword;
    private CheckBox saveLoginCheckBox;
    private Boolean saveLogin = false;


    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);

        if(loginDataBaseAdapter.getCount() == 0)
        {
            loginDataBaseAdapter.insertEntry("11410202", "0543331991", "Gilad", "Tsidkiyahu");
        }

        btnSignIn = (Button) findViewById(R.id.LoginButton);

        // get the Refferences of views
        editTextUserName = (EditText) findViewById(R.id.UserTxt);
        editTextPassword = (EditText) findViewById(R.id.PasswordTxt);

        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName = editTextUserName.getText().toString();
                String password = md5(editTextPassword.getText().toString());
                if (saveLoginCheckBox.isChecked()) {
                    saveLogin = true;
                }
                loginDataBaseAdapter.deleteEntry("12345678");
                // fetch the Password form database for respective user name
                String storedPassword = loginDataBaseAdapter.getSinlgeEntry(userName);

                if(storedPassword.equals("NOT EXIST"))
                {
                    Toast.makeText(MainActivity.this, "Worker ID doesn't exist", Toast.LENGTH_LONG).show();
                    editTextUserName.setText("");
                    editTextPassword.setText("");
                }
                else if(storedPassword.equals("Manager"))
                {
                    String pas = MainActivity.md5("0543331991");
                    if(password.equals(pas))
                    {
                        Toast.makeText(MainActivity.this, "Helllo Gilad", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, ManagerActicvity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Password does not match", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                else if (password.equals(storedPassword))
                {
                    Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                    //TODO: Strat activity for technician
                }

                else
                {
                    Toast.makeText(MainActivity.this, "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = editTextUserName.getText().toString();
        PasswordValue = editTextPassword.getText().toString();
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        editTextUserName.setText(UnameValue);
        editTextPassword.setText(PasswordValue);
        if(UnameValue.length() == 8)
        {
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (saveLogin) {
            savePreferences();
        } else {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
        }
    }
}