package my.app.yuval.intel_managment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChangePasswordActivity extends AppCompatActivity {

    private Activity activityReference;
    Button btnSave;
    EditText pass1;
    EditText pass2;

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    String name;
    String workerID;
    private static String url_add_elec = "http://yuvalt.com/android_php/change_password.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        activityReference = this;
        setContentView(R.layout.change_password);


        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        workerID = intent.getStringExtra("WorkerID");
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);

        btnSave = (Button) findViewById(R.id.saveButton);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Pass1 = pass1.getText().toString();
                String Pass2 = pass2.getText().toString();

                if(Pass1.length() < 1 || Pass1.equals(""))
                {
                    Toast.makeText(ChangePasswordActivity.this, "The first field is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if(Pass2.length() < 1 || Pass2.equals(""))
                {
                    Toast.makeText(ChangePasswordActivity.this, "The second field is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!(Pass1.equals(Pass2)))
                {
                    Toast.makeText(ChangePasswordActivity.this, "The passwords don't match", Toast.LENGTH_LONG).show();
                    return;
                }

                new ChangePassword().execute(workerID,MainActivity.md5(Pass2) );

            }

        });

    }
    public void onBackPressed()
    {
        Intent i;
        if(workerID.equals("11410202"))
            i = new Intent(ChangePasswordActivity.this, ManagerActicvity.class);
        else {
            i = new Intent(ChangePasswordActivity.this, ElecActivity.class);
            i.putExtra("Name", name);
            i.putExtra("WorkerID", workerID);
        }
        startActivity(i);
    }


    /* Background Async Task to Create new product
  * */
    class ChangePassword extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
            pDialog.setMessage("Change Password..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String workerID = args[0];
            String password = args[1];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("workerID", workerID));
            params.add(new BasicNameValuePair("newPass", password));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_elec,
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
                Toast.makeText(ChangePasswordActivity.this, "Changed Succssefully", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                Intent i;
                if(workerID.equals("11410202"))
                    i = new Intent(ChangePasswordActivity.this, ManagerActicvity.class);
                else {
                    i = new Intent(ChangePasswordActivity.this, ElecActivity.class);
                    i.putExtra("Name", name);
                    i.putExtra("WorkerID", workerID);
                }
                startActivity(i);
            }
            else
            {
                Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }

    }



}

