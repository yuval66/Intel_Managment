package my.app.yuval.intel_managment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    Button btnSignIn;
    Button btnForgetPassWord;
    static final String PREF_UNAME = "Username";
    static final String PREF_PASSWORD = "Password";
    static final String PREF_NAME = "Name";
    EditText editTextUserName ;
    EditText editTextPassword;
    private CheckBox saveLoginCheckBox;
    private Boolean saveLogin = false;
    private Activity activityReference;
    final String DefaultUnameValue = "";
    private String UnameValue;
    private String Uname;
    final String DefaultPasswordValue = "";
    String PasswordValue;
    String userName;
    private static String url_login = "http://yuvalt.com/android_php/login.php";
    private static String url_reset_password = "http://yuvalt.com/android_php/forget_password.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ERR = "message";
    private static final String TAG_WORKERID = "WorkerID";
    private static final String TAG_ROLE = "Role";
    private static final String TAG_FNAME = "Fname";
    private static final String TAG_LNAME = "Lname";
    private static final String TAG_PASS = "Pass";
    private static final String TAG_ELEC = "elec";
    String id_to_next_activity;
    String name_to_next_activity;
    String ans;
    String passToMail;
    String Md5Password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        UnameValue = pref.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = pref.getString(PREF_PASSWORD, DefaultPasswordValue);
        Uname = pref.getString(PREF_NAME, DefaultPasswordValue);

        if(UnameValue.length() == 8 && PasswordValue.length() > 1)
        {
            if(UnameValue.equals("11410202"))
            {
                Intent i = new Intent(MainActivity.this, ManagerActicvity.class);
                Toast.makeText(getApplicationContext(), "Hello Gilad", Toast.LENGTH_LONG).show();
                startActivity(i);
            }


            else
            {
                Intent i = new Intent(MainActivity.this, ElecActivity.class);
                i.putExtra("Name", Uname);
                Toast.makeText(getApplicationContext(), "Hello " + Uname, Toast.LENGTH_LONG).show();
                i.putExtra("WorkerID", UnameValue);
                startActivity(i);
            }
        }



        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activityReference = this;

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);

        btnSignIn = (Button) findViewById(R.id.LoginButton);
        btnForgetPassWord = (Button) findViewById(R.id.forgetbtn);

        // get the Refferences of views
        editTextUserName = (EditText) findViewById(R.id.UserTxt);
        editTextPassword = (EditText) findViewById(R.id.PasswordTxt);
        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                if(saveLoginCheckBox.isChecked()) {
                    saveLogin = true;
                }
                else
                    saveLogin = false;
                new GetLogin().execute(userName, password);
            }
        });


        btnForgetPassWord.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activityReference);
                TextView title = new TextView(activityReference);
                title.setText("Reset Password");
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
                tv1.setText("Are you Sure you want to reset your password?");
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

                        String workerID = editTextUserName.getText().toString();

                        if(workerID.length() < 1)
                        {
                            Toast.makeText(activityReference, "Worker ID is empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                        StringBuilder salt = new StringBuilder();
                        Random rnd = new Random();
                        while (salt.length() < 8) { // length of the random string.
                            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                            salt.append(SALTCHARS.charAt(index));
                        }
                        passToMail = salt.toString();
                        Md5Password = md5(passToMail);

                        new ResetPassword().execute(workerID, passToMail, Md5Password);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        // Edit and commit
        UnameValue = editTextUserName.getText().toString();
        PasswordValue = editTextPassword.getText().toString();
        Uname = name_to_next_activity;

        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.putString(PREF_NAME, Uname);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        UnameValue = pref.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = pref.getString(PREF_PASSWORD, DefaultPasswordValue);
        Uname = pref.getString(PREF_NAME, DefaultPasswordValue);

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

    }
    public void onBackPressed()
    {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }


    class GetLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Check Password. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {

            String WorkerID = args[0];
            String password = args[1];
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("workerID", WorkerID));
            params.add(new BasicNameValuePair("password", md5(password)));


                    // Check for success tag
                    int success;
                    try {

                        JSONObject json = jsonParser.makeHttpRequest(
                                url_login, "GET", params);

                        if(json == null)
                            return null;

                        success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // successfully received product details
                            JSONArray elecObj = json
                                    .getJSONArray(TAG_ELEC); // JSON Array

                            // get first product object from JSON Array
                            JSONObject elec = elecObj.getJSONObject(0);
                            String workID = elec.getString(TAG_WORKERID);
                            String role =   elec.getString(TAG_ROLE);
                            String name  =  elec.getString(TAG_FNAME) + " " + elec.getString(TAG_LNAME);
                            String pass = elec.getString(TAG_PASS);

                            id_to_next_activity = workID;
                            name_to_next_activity = name;
                            return role;


                        }else{
                            String errMsg = json.getString(TAG_ERR);
                            return errMsg;
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        return "Password dosen't match";
                    }
        }

        protected void onPostExecute(String file_url) {

            if(!(file_url.equals("Manager") || file_url.equals("Elec")))
            {
                Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }
            else {
                pDialog.dismiss();


                ans = file_url;

                runOnUiThread(new Runnable() {
                    public void run() {


                        if (ans.equals("Manager")) {
                            Intent i = new Intent(MainActivity.this, ManagerActicvity.class);
                            if (saveLogin)
                                savePreferences();
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                                    == PackageManager.PERMISSION_GRANTED)
                            {
                                Toast.makeText(getApplicationContext(), "Hello Gilad", Toast.LENGTH_LONG).show();
                            }
                            startActivity(i);

                        } else {
                            Intent i = new Intent(MainActivity.this, ElecActivity.class);
                            i.putExtra("Name", name_to_next_activity);
                            i.putExtra("WorkerID", id_to_next_activity);
                            if (saveLogin)
                                savePreferences();
                            startActivity(i);
                        }
                    }
                });
            }

        }
    }



    /* Background Async Task to Create new product
  * */
    class ResetPassword extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Change Password..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String workerID = args[0];
            String password = args[1];
            String passwordInMd5 = args[2];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("workerID", workerID));
            params.add(new BasicNameValuePair("newPass", password));
            params.add(new BasicNameValuePair("newPassMd", passwordInMd5));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_reset_password,
                    "POST", params);

            try {
                if(json == null)
                    return null;

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return "Success";
                    // closing this screen
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            if(file_url != null && file_url.equals("Success")) {
                Toast.makeText(MainActivity.this, "Changed Succssefully, a mail is on the way to you.", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Failed to change password or send mail", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }

    }




}