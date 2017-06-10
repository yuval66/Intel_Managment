package my.app.yuval.intel_managment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ElecPage extends Activity {

    private Activity activityReference;
    String workerID;
    String phone;
    String smsMsg;
    private static final int REQUEST = 112;
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_delete_elec = "http://yuvalt.com/android_php/delete_elec.php";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        activityReference = this;
        setContentView(R.layout.elec_page);
        Intent i = getIntent();
        final ArrayList<String> details;
        details = (ArrayList<String>) getIntent().getSerializableExtra("WorkerDetails");

        TextView firstName = (TextView) findViewById(R.id.text3);
        TextView lastName = (TextView) findViewById(R.id.text5);
        final TextView WorkerID = (TextView) findViewById(R.id.text7);
        TextView phoneTxt = (TextView) findViewById(R.id.textPhone);
        TextView mailTxt = (TextView) findViewById(R.id.textMail);
        TextView textMissions = (TextView) findViewById(R.id.numOfMissionsInDB);

        firstName.setText(details.get(1).toString());
        lastName.setText(details.get(2).toString());
        WorkerID.setText(details.get(0).toString());
        phoneTxt.setText(details.get(4).toString());
        mailTxt.setText(details.get(3).toString());

        phone = details.get(4).toString();
        workerID = details.get(0).toString();

        String strLong = details.get(5).toString();
        textMissions.setText(strLong);

        Button btnDelete = (Button) findViewById(R.id.deleteButton);
        Button btnSms = (Button) findViewById(R.id.Sms);
        Button btnMail = (Button) findViewById(R.id.mail);
        Button ViewMis = (Button) findViewById(R.id.ViewMis);


        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activityReference);
                TextView title = new TextView(activityReference);
                title.setText("Delete Electician");
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
                tv1.setText("Are you Sure you want to delete this worker?");
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
                        String etStr = details.get(0).toString();

                        new DeleteElec().execute(workerID);
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

        btnMail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(ElecPage.this, SendMail.class);
                intent.putExtra("WorkerID",workerID);
                intent.putExtra("Mail",details.get(3).toString());
                startActivity(intent);
            }
        });

        ViewMis.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent i = new Intent(ElecPage.this, ViewMyMissions.class);
                i.putExtra("Name","Manager");
                i.putExtra("WorkerID",workerID);
                Global.mission = "all";
                startActivity(i);

            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                LayoutInflater li = LayoutInflater.from(activityReference);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        activityReference);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        smsMsg = userInput.getText().toString();

                                        if(smsMsg.length() < 1 || smsMsg.equals(""))
                                        {
                                            Toast.makeText(activityReference, "Message is empty.", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        if (Build.VERSION.SDK_INT >= 23) {
                                            String[] PERMISSIONS = {android.Manifest.permission.SEND_SMS};
                                            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                                                ActivityCompat.requestPermissions(activityReference, PERMISSIONS, REQUEST );
                                            } else {
                                                sendSMS(phone, smsMsg);
                                            }
                                        } else {
                                            sendSMS(phone, smsMsg);
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }
    private void sendSMS(String phoneNumber, String message) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        Log.d("1111", phoneNumber + " " + message);
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getApplicationContext(),
                "SMS sent.", Toast.LENGTH_LONG).show();
        return;
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(phone, smsMsg);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }



    /* Background Async Task to Create new product
    * */
    class DeleteElec extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ElecPage.this);
            pDialog.setMessage("Delete Electrician..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String workerID = args[0];
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", workerID));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_delete_elec,
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
                Toast.makeText(ElecPage.this, "Delete Succssefully", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                Intent intent = new Intent(ElecPage.this, ViewElecList.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(ElecPage.this, "Failed to delete electrician", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }

    }


}


