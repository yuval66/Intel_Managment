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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewElecMissions  extends Activity implements AdapterView.OnItemSelectedListener{

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<String> MissionList;

    // url to get all products list
    private static String url_all_elecs = "http://yuvalt.com/android_php/all_missions.php";

    // JSON Node names
    private static final String TAG_ID = "id";
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
    private static final String TAG_PHONE = "phone";



    // elecs JSONArray
    JSONArray missions;
    TableLayout tl;
    String item;
    String sort;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewelecmissions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tl = (TableLayout) findViewById(R.id.main_table);
        Spinner spinner = (Spinner)findViewById(R.id.spinSorting);
        final Intent intent = getIntent();
        sort = intent.getStringExtra("sort");
        String[] INTERPOLATORS = new String[3];

        if(Global.mission.equals("close")) {
            INTERPOLATORS = new String[2];
            INTERPOLATORS[0] = "Asset";
            INTERPOLATORS[1] = "Name";
        }
        else
        {
            INTERPOLATORS[0] = "Status";
            INTERPOLATORS[1] = "Asset";
            INTERPOLATORS[2] = "Name";
        }
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,  R.layout.spinner_item2, INTERPOLATORS);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int num = 1;
        switch(sort) {
            case "Status":
                num = 1;
                break;
            case "Asset":
                if (Global.mission.equals("close"))
                    num = 1;
                else
                    num = 2;
                break;
            case "Name":
                if (Global.mission.equals("close"))
                    num = 2;
                else
                    num = 3;
                break;
        }

        spinner.setSelection(num-1);

        Button b = (Button) findViewById(R.id.btn) ;

        // Hashmap for ListView
        MissionList = new ArrayList<String>();

        // Loading products in Background Thread
        new LoadAllMissions().execute(sort);

        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (item != null && item.length() > 1) {
                    Intent intent = getIntent();
                    intent.putExtra("sort", item);
                    finish();
                    startActivity(intent);
                }
                else
                    return;

            }
        });


    }

    public void onBackPressed()
    {
        Intent intent = new Intent(ViewElecMissions.this, ManagerActicvity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            pDialog = new ProgressDialog(ViewElecMissions.this);
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
            String sort = args[0];

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("SORT", sort));
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
                                + hasphoto + "~" + mission_id + "~" + title + "~" + phone;


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
        protected void onPostExecute(ArrayList<String> elecListArr) {

            if (elecListArr == null) {
                Toast.makeText(ViewElecMissions.this, "There is no missions", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }
            else if (elecListArr.size() == 0) {
                Toast.makeText(ViewElecMissions.this, "There is no missions", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }

            pDialog.dismiss();

            String s;

            for (int i = 0; i < elecListArr.size(); i++) {

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
                txtview1.setTextSize(12f);
                txtview1.setPadding(5,0,20,20);
                txtview1.setGravity(Gravity.CENTER);
                txtview1.setTypeface(null, Typeface.BOLD_ITALIC);

                TextView txtview2 = new TextView(getApplicationContext());
                txtview2.setTextSize(14f);
                txtview2.setPadding(10,0,20,20);
                txtview2.setGravity(Gravity.CENTER);
                txtview2.setTypeface(null, Typeface.BOLD_ITALIC);


                TextView txtview3 = new TextView(getApplicationContext());
                txtview3.setTextSize(14f);
                txtview3.setPadding(0,0,20,20);
                txtview3.setGravity(Gravity.CENTER);
                txtview3.setTypeface(null, Typeface.BOLD_ITALIC);

                s = elecListArr.get(i);
                final String[] arr = s.split("~");

                txtview1.setText(arr[0].toString());
                txtview3.setText(arr[5].toString());
                txtview2.setText(arr[1].toString());
                newRow.addView(txtview1,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.5f)));
                newRow.addView(txtview2,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.1f)));
                newRow.addView(txtview3,(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.7f)));

                if (arr[5].toString().equals("Done")) {
                    newRow.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
                else if(arr[5].toString().equals("In Work")){
                    newRow.setBackgroundColor(Color.parseColor("#03A9F4"));
                }
                else if(arr[5].toString().equals("Late")){
                    newRow.setBackgroundColor(Color.parseColor("#F44336"));
                }

                newRow.setOnClickListener(new View.OnClickListener() {
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

                        Intent i = new Intent(ViewElecMissions.this, ShowMissionsForManager.class);
                        i.putExtra("MissionDetails", details);
                        i.putExtra("activity", "viewElec");
                        startActivity(i);


                    }
                });

                tl.addView(newRow);
            }



        }



    }




    }

