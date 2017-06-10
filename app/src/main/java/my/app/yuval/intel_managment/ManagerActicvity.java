package my.app.yuval.intel_managment;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.util.Log;
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
import java.util.Calendar;
import java.util.List;

public class ManagerActicvity extends AppCompatActivity {

    private Activity activityReference;
    Button btnAdd;
    Button btnViewOpenMissions;
    Button btnViewCloseMissions;
    Button btnViewElec;
    Button btnUpdatePass;
    Button btnAddMission;
    Button cancle;

    JSONParser jsonParser = new JSONParser();
    JSONParser jParser = new JSONParser();

    private static String url_delete_elec = "http://yuvalt.com/android_php/num_missions.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ALL = "MISSIONS_NUM";
    private static final String TAG_INWORK = "MISSIONS_INWORK";
    private static final String TAG_LATE = "MISSIONS_LATE";
    private static final String TAG_DONE = "MISSIONS_DONE";
    private static String url_all_elecs = "http://yuvalt.com/android_php/select_weeks.php";
    private static final String TAG_WEEKS = "weeks";
    private static final String TAG_NUM = "num";
    private static final String TAG_DATE = "date";
    private static final String TAG_ID = "id";



    JSONArray weeks;
    ArrayList<String> arr = new ArrayList<>();

    private static final String TAG_ARR = "missions";
    private ProgressDialog pDialog;
    JSONArray missions;
    TextView missionsInDB;
    TextView InWorkNumber;
    TextView LateNumber;
    TextView DooneNumber;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Button logout;
    Intent intent;

    public void startAlertAtParticularTime() {


        intent = new Intent(this, MyBroadcastReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR, 8); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second
        firingCal.set(Calendar.AM_PM, Calendar.AM);

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if(intendedTime >= currentTime){

            if (pendingIntent == null) {
                pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
                        alarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
        else{
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

           // alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            if (pendingIntent == null) {
                pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
                        alarmManager.INTERVAL_DAY, pendingIntent);
            }
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

               if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(ManagerActicvity.this, new String[] {Manifest.permission.SEND_SMS}, 100);
        }

        activityReference = this;
        setContentView(R.layout.activity_manager);
        btnAdd = (Button) findViewById(R.id.addElecButton);
        btnViewOpenMissions = (Button) findViewById(R.id.viewOpenMissions);
        btnViewCloseMissions = (Button) findViewById(R.id.viewCloseMissions);
        btnViewElec = (Button) findViewById(R.id.viewElectrician);
        btnUpdatePass = (Button) findViewById(R.id.updatePassWord);
        btnAddMission = (Button) findViewById(R.id.addMission);
        cancle  = (Button) findViewById(R.id.deleteAlarm);
        Button weeks = (Button) findViewById(R.id.loadWeeks);
        logout = (Button) findViewById(R.id.logout);

         missionsInDB = (TextView)findViewById(R.id.numOfMissionsInDB);
         InWorkNumber = (TextView)findViewById(R.id.numOfInWork);
         LateNumber = (TextView)findViewById(R.id.numOfLate);
         DooneNumber = (TextView)findViewById(R.id.numOfDone);

        startAlertAtParticularTime();

        new NumMissions().execute();


        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ManagerActicvity.this, MainActivity.class);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(i);
            }
        });

        weeks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new LoadAllWeeks().execute();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ManagerActicvity.this, InsertElecActivity.class);
                startActivity(i);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(ManagerActicvity.this, MyBroadcastReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent = null;
                Toast.makeText(ManagerActicvity.this, "Alarm repeating was canceled", Toast.LENGTH_LONG).show();
                return;

            }
        });

        btnAddMission.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ManagerActicvity.this, AddMission.class);
                i.putExtra("Name","Gilad");
                i.putExtra("WorkerID","11410202");
                startActivity(i);
            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ManagerActicvity.this, ChangePasswordActivity.class);
                i.putExtra("Name","Gilad");
                i.putExtra("WorkerID","11410202");
                startActivity(i);
            }
        });

        btnViewOpenMissions.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(ManagerActicvity.this, ViewElecMissions.class);
                i.putExtra("sort", "status");
                Global.mission = "open";
                startActivity(i);
            }
        });

        btnViewCloseMissions.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(ManagerActicvity.this, ViewElecMissions.class);
                i.putExtra("sort", "status");
                Global.mission = "close";
                startActivity(i);
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
            pDialog = new ProgressDialog(ManagerActicvity.this);
            pDialog.setMessage("Loading page..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String[] doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_delete_elec,
                    "GET", params);

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
                missionsInDB.setText(numbers[0]);
                InWorkNumber.setText(numbers[1]);
                LateNumber.setText(numbers[2]);
                DooneNumber.setText(numbers[3]);
            }
            else
            {
                pDialog.dismiss();
                missionsInDB.setText("0");
                InWorkNumber.setText("0");
                LateNumber.setText("0");
                DooneNumber.setText("0");            }
        }

    }

    class LoadAllWeeks extends AsyncTask<String, String, ArrayList<String>> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ManagerActicvity.this);
            pDialog.setMessage("Loading weeks data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected ArrayList<String> doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_elecs, "GET", params);


            try {
                // Checking for SUCCESS TAG


                if(json == null)
                    return null;

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    ArrayList<String> arrayList = new ArrayList<String>();
                    weeks = json.getJSONArray(TAG_WEEKS);
                    // looping through All Products
                    for (int i = 0; i < weeks.length(); i++) {
                        String arr;
                        JSONObject c = weeks.getJSONObject(i);
                        String num = c.getString(TAG_NUM);
                        String date = c.getString(TAG_DATE);
                        String id = c.getString(TAG_ID);
                        arr = id + "~" + num + "~" + date;

                        arrayList.add(arr);
                    }
                    return arrayList;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }


        protected void onPostExecute(ArrayList<String> elecListArr) {
             pDialog.dismiss();
            arr = new ArrayList<String>();
            if (elecListArr == null) {
                Toast.makeText(ManagerActicvity.this, "Failed to load weeks", Toast.LENGTH_LONG).show();
                return;
            }
            String s;


            for (int i = 0; i < elecListArr.size(); i++) {
                s = elecListArr.get(i);
                arr.add(s);
            }
            Intent i = new Intent(ManagerActicvity.this, Chart.class);
            i.putExtra("weeks", arr);
            startActivity(i);

        }

    }


}
