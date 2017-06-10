package my.app.yuval.intel_managment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElecActivity extends AppCompatActivity {

    private Activity activityReference;
    Button btnAdd;
    Button btnViewMissions;
    Button btnViewCloseMissions;
    Button btnUpdatePass;
    JSONParser jsonParser = new JSONParser();
    private static String url_mission_numer = "http://yuvalt.com/android_php/num_missions_elec.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ALL = "MISSIONS_NUM";
    private static final String TAG_INWORK = "MISSIONS_INWORK";
    private static final String TAG_LATE = "MISSIONS_LATE";
    private static final String TAG_DONE = "MISSIONS_DONE";
    private static final String TAG_ARR = "missions";
    private ProgressDialog pDialog;
    JSONArray missions;
    TextView missionsDB;
    TextView InWorkNumber;
    TextView LateNumber;
    TextView DooneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        activityReference = this;
        setContentView(R.layout.activity_elec);

        missionsDB = (TextView)findViewById(R.id.numOfMissionsInDBElec);
        InWorkNumber = (TextView)findViewById(R.id.numOfInWork);
        LateNumber = (TextView)findViewById(R.id.numOfLate);
        DooneNumber = (TextView)findViewById(R.id.numOfDone);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("Name");
        final String WorkerID = intent.getStringExtra("WorkerID");

        Toast.makeText(getApplicationContext(), "Hello " + name, Toast.LENGTH_LONG).show();



        btnAdd = (Button) findViewById(R.id.addMissionButton);
        btnViewMissions = (Button) findViewById(R.id.viewMyMissions);
        btnViewCloseMissions = (Button) findViewById(R.id.viewMyCloseMissions);
        btnUpdatePass = (Button) findViewById(R.id.updatePassWord);
        Button logout = (Button) findViewById(R.id.logout);

        new NumMissions().execute(WorkerID);

        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ElecActivity.this, MainActivity.class);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ElecActivity.this, AddMission.class);
                i.putExtra("Name",name);
                i.putExtra("WorkerID",WorkerID);
                startActivity(i);
            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ElecActivity.this, ChangePasswordActivity.class);
                i.putExtra("Name",name);
                i.putExtra("WorkerID",WorkerID);
                startActivity(i);
            }
        });



        btnViewMissions.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ElecActivity.this, ViewMyMissions.class);
                i.putExtra("Name",name);
                i.putExtra("WorkerID",WorkerID);
                Global.mission = "open";
                startActivity(i);

            }
        });

        btnViewCloseMissions.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ElecActivity.this, ViewMyMissions.class);
                i.putExtra("Name",name);
                i.putExtra("WorkerID",WorkerID);
                Global.mission = "close";
                startActivity(i);

            }
        });


    }
    public void onBackPressed()
    {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    /* Background Async Task to Create new product
* */
    class NumMissions extends AsyncTask<String, String, String[]> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ElecActivity.this);
            pDialog.setMessage("Loading page..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String[] doInBackground(String... args) {

            String id = args[0];
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("workerID", id));

            JSONObject json = jsonParser.makeHttpRequest(url_mission_numer,
                    "POST", params);

            try {
                if(json == null)
                    return null;

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    missions = json.getJSONArray(TAG_ARR);

                    // looping through All Products
                    JSONObject c = missions.getJSONObject(0);
                    String[] arr = {"0","0","0","0"};

                    String all = c.getString(TAG_ALL);
                    String inwork = c.getString(TAG_INWORK);
                    String late = c.getString(TAG_LATE);
                    String done = c.getString(TAG_DONE);

                    arr[0] = all ;
                    arr[1] = inwork ;
                    arr[2] = late ;
                    arr[3] = done ;

                    return arr;
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
        protected void onPostExecute(String[] numbers) {

            if(numbers != null && numbers.length > 0) {
                pDialog.dismiss();
                missionsDB.setText(numbers[0]);
                InWorkNumber.setText(numbers[1]);
                LateNumber.setText(numbers[2]);
                DooneNumber.setText(numbers[3]);
            }
            else
            {
                pDialog.dismiss();
                missionsDB.setText("0");
                InWorkNumber.setText("0");
                LateNumber.setText("0");
                DooneNumber.setText("0");            }
        }

    }
}
