package my.app.yuval.intel_managment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewElecList extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<String> elecList;

    // url to get all products list
    private static String url_all_elecs = "http://yuvalt.com/android_php/all_elec.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ELECS = "elecs";
    private static final String TAG_WID = "workerID";
    private static final String TAG_FNAME = "Fname";
    private static final String TAG_LNAME = "Lname";
    private static final String TAG_MAIL = "mail";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_MISSIONS = "numMissions";


    // elecs JSONArray
    JSONArray elecs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleclist);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hashmap for ListView
        elecList = new ArrayList<String>();


        // Loading products in Background Thread
        new LoadAllElecs().execute();

    }

    public void onBackPressed() {
        Intent intent = new Intent(ViewElecList.this, ManagerActicvity.class);
        startActivity(intent);
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllElecs extends AsyncTask<String, String, ArrayList<String>> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewElecList.this);
            pDialog.setMessage("Loading elec list. Please wait...");
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
                    elecs = json.getJSONArray(TAG_ELECS);

                    // looping through All Products
                    for (int i = 0; i < elecs.length(); i++) {
                        JSONObject c = elecs.getJSONObject(i);
                        String arr;

                        String workerid = c.getString(TAG_WID);
                        String f_name = c.getString(TAG_FNAME);
                        String l_name = c.getString(TAG_LNAME);
                        String mail = c.getString(TAG_MAIL);
                        String phone = c.getString(TAG_PHONE);
                        String numOfMissions = c.getString(TAG_MISSIONS);
                        arr = workerid + "~" + f_name + "~" + l_name + "~" + mail + "~" + phone + "~" + numOfMissions;

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

            if (elecListArr == null || elecListArr.size() < 1) {
                Toast.makeText(ViewElecList.this, "Failed to view electricians", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }

            pDialog.dismiss();

            TableLayout tl = (TableLayout) findViewById(R.id.main_table);

            for (int i = 0; i < elecListArr.size(); i++) {

                TextView txtview1 = new TextView(getApplicationContext());
                txtview1.setTextSize(20f);
                txtview1.setGravity(Gravity.CENTER);

                TextView txtview2 = new TextView(getApplicationContext());
                txtview2.setTextSize(20f);
                txtview2.setGravity(Gravity.CENTER);

                TextView txtview3 = new TextView(getApplicationContext());
                txtview3.setTextSize(20f);
                txtview3.setGravity(Gravity.CENTER);

                String s = elecListArr.get(i);
                final String[] arr = s.split("~");


                if (!(arr[0].equals("11410202"))) {

                    TableRow row = new TableRow(getApplicationContext());
                    TableLayout.LayoutParams tableRowParams =
                            new TableLayout.LayoutParams
                                    (TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    int leftMargin = 2;
                    int rightMargin = 2;
                    int topMargin = 0;
                    int bottomMargin = 2;

                    if (i == 0)
                        topMargin = 2;

                    tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    row.setLayoutParams(tableRowParams);

                    if (i % 2 == 0) {
                        row.setBackgroundColor(Color.parseColor("#1565C0"));
                    } else {
                        row.setBackgroundColor(Color.parseColor("#0D47A1"));
                    }
                    row.setClickable(true);
                    row.setMinimumHeight(70);

                    txtview1.setText(arr[0].toString());
                    txtview2.setText(arr[1].toString());
                    txtview3.setText(arr[2].toString());
                    row.addView(txtview1, (new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)));
                    row.addView(txtview2, (new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)));
                    row.addView(txtview3, (new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)));

                    row.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            ArrayList<String> details = new ArrayList<String>();
                            details.add(0, arr[0].toString());
                            details.add(1, arr[1].toString());
                            details.add(2, arr[2].toString());
                            details.add(3, arr[3].toString());
                            details.add(4, arr[4].toString());
                            details.add(5, arr[5].toString());

                            Intent i = new Intent(ViewElecList.this, ElecPage.class);
                            i.putExtra("WorkerDetails", details);
                            startActivity(i);
                        }
                    });

                    tl.addView(row);
                }
            }

        }



    }
}
