package my.app.yuval.intel_managment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ViewMyMissions  extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<String> MissionList;

    // url to get all products list
    private static String url_all_elecs = "http://yuvalt.com/android_php/view_elec_missions.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MISSIONS = "missions";
    private static final String TAG_BOARD = "board";
    private static final String TAG_NAME = "name";
    private static final String TAG_WID = "workerID";
    private static final String TAG_SDATE = "sdate";
    private static final String TAG_EDATE = "edate";
    private static final String TAG_STATUS = "status";
    private static final String TAG_NOTES = "notes";
    private static final String TAG_HASPHOTO = "hasphoto";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ID = "id";
    private static final String TAG_PHONE = "phone";


    // elecs JSONArray
    JSONArray missions;
    TableLayout tl;    private Calendar calendar;
    private String year, month, day;
    String name;
    String workerID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmymission);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        workerID = intent.getStringExtra("WorkerID");

        new LoadAllMissions().execute(workerID);

    }

    private boolean compareDates(String d1,String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            if (date1.before(date2)) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void onBackPressed()
    {
        if(name != null && name.equals("Manager"))
        {
            super.onBackPressed();

        }
        else
        {
            Intent intent = new Intent(ViewMyMissions.this, ElecActivity.class);
            intent.putExtra("WorkerID", workerID);
            intent.putExtra("Name", name);

            startActivity(intent);
        }
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllMissions extends AsyncTask<String, String, ArrayList<String>> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewMyMissions.this);
            pDialog.setMessage("Loading missions. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected ArrayList<String> doInBackground(String... args) {
            // Building Parameters
            String wid = args[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("WorkerID", wid));
            params.add(new BasicNameValuePair("MISSIONS", Global.mission));
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
                    missions = json.getJSONArray(TAG_MISSIONS);

                    // looping through All Products
                    for (int i = 0; i < missions.length(); i++) {
                        JSONObject c = missions.getJSONObject(i);
                        String arr;

                        String board = c.getString(TAG_BOARD);
                        String name = c.getString(TAG_NAME);
                        String id = c.getString(TAG_WID);
                        String sdate = c.getString(TAG_SDATE);
                        String edate = c.getString(TAG_EDATE);
                        String status = c.getString(TAG_STATUS);
                        String notes = c.getString(TAG_NOTES);
                        String hasphoto = c.getString(TAG_HASPHOTO);
                        String mission_id = c.getString(TAG_ID);
                        String title = c.getString(TAG_TITLE);
                        String phone = c.getString(TAG_PHONE);

                        arr = board + "~" + name + "~" + id + "~" + sdate + "~" + edate + "~" + status + "~" + notes + "~"
                                + hasphoto + "~" +mission_id +  "~" +title + "~" + phone;

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

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(ArrayList<String> missionListArr) {

            if (missionListArr == null) {
                Toast.makeText(ViewMyMissions.this, "There is no missions", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }

            pDialog.dismiss();

            TableLayout tl = (TableLayout) findViewById(R.id.main_table);

            String s;

            for (int i = 0; i < missionListArr.size(); i++) {

                TableRow newRow = new TableRow(getApplicationContext());
                TableLayout.LayoutParams tableRowParams=
                        new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                int leftMargin=2;
                int rightMargin=2;
                int topMargin = 0;
                int bottomMargin=2;

                if(i == 0)
                    topMargin =2;

                tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                newRow.setLayoutParams(tableRowParams);

                TextView txtview1 = new TextView(getApplicationContext());
                txtview1.setTextSize(16f);
                txtview1.setGravity(Gravity.CENTER);
                txtview1.setPadding(0,12,0,0);
                txtview1.setTypeface(null, Typeface.BOLD_ITALIC);



                TextView txtview2 = new TextView(getApplicationContext());
                txtview2.setTextSize(20f);
                txtview2.setGravity(Gravity.CENTER);
                txtview2.setPadding(0,18,25,0);
                txtview2.setTypeface(null, Typeface.BOLD_ITALIC);

                TextView txtview3 = new TextView(getApplicationContext());
                txtview3.setTextSize(15f);
                txtview3.setGravity(Gravity.LEFT);
                txtview3.setPadding(0,0,0,0);
                txtview3.setTypeface(null, Typeface.BOLD_ITALIC);

                ImageView image = new ImageView(getApplicationContext());
                image.setClickable(true);
                image.setImageDrawable(getResources().getDrawable(R.drawable.edit2));
                image.setPadding(10,0,0,10);


                s = missionListArr.get(i);
                final String[] arr = s.split("~");

                txtview1.setText(arr[0].toString());
                txtview2.setText(arr[5].toString());
                txtview3.setText("Blocked");
                newRow.addView(txtview1,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2.3f)));
                newRow.addView(txtview2,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.9f)));
                if (!(arr[5].toString().equals("Done")))
                    newRow.addView(image,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.6f)));
                else if(name != null && name.equals("Manager") && (arr[5].toString().equals("Done")))
                    newRow.addView(image,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.6f)));
                else
                    newRow.addView(txtview3,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.6f)));



                if (arr[5].toString().equals("Done")) {
                    newRow.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
                else if(arr[5].toString().equals("In Work")){
                    newRow.setBackgroundColor(Color.parseColor("#03A9F4"));
                }
                else if(arr[5].toString().equals("Late")){
                    newRow.setBackgroundColor(Color.parseColor("#F44336"));
                }

                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ArrayList<String> details = new ArrayList<String>();

                        details.add(0, arr[0].toString());
                        details.add(1, arr[1].toString());
                        details.add(2, arr[2].toString());
                        details.add(3, arr[3].toString());
                        details.add(4, arr[4].toString());
                        details.add(5, arr[5].toString());
                        details.add(6, arr[6].toString());
                        details.add(7, arr[7].toString());
                        details.add(8, arr[8].toString());
                        details.add(9, arr[9].toString());
                        details.add(10, arr[10].toString());


                        if(name != null && name.equals("Manager")) {
                            Intent i = new Intent(ViewMyMissions.this, ShowMissionsForManager.class);
                            i.putExtra("MissionDetails", details);
                            i.putExtra("activity", "viewMy");
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(ViewMyMissions.this, ShowMissionsForElec.class);
                            i.putExtra("MissionDetails", details);
                            i.putExtra("Name", name);
                            i.putExtra("WorkerID", workerID);
                            startActivity(i);
                        }
                    }
                });

                tl.addView(newRow);
            }



        }



    }

}
