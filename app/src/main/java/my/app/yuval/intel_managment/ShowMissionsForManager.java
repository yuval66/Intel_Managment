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


public class ShowMissionsForManager extends Activity {

    private Activity activityReference;
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_delete_mission = "http://yuvalt.com/android_php/delete_mission.php";
    private static final String TAG_SUCCESS = "success";
    String smsMsg;
    String phone;
    String activityToBack;
    private static final int REQUEST = 112;
    String wid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activityReference = this;
        setContentView(R.layout.showmissionsformanager);
        Intent i = getIntent();
        final ArrayList<String> details;
        details = (ArrayList<String>) getIntent().getSerializableExtra("MissionDetails");
        activityToBack = i.getStringExtra("activity");


        TextView BoardNumber = (TextView) findViewById(R.id.text3);
        TextView Name = (TextView) findViewById(R.id.text5);
        TextView WorkerID = (TextView) findViewById(R.id.text7);
        TextView StartDate = (TextView) findViewById(R.id.text9);
        TextView EndDate = (TextView) findViewById(R.id.text11);
        TextView Status = (TextView) findViewById(R.id.text13);
        TextView Notes = (TextView) findViewById(R.id.text15);
        TextView title = (TextView) findViewById(R.id.text20);


        BoardNumber.setText(details.get(0).toString());
        Name.setText(details.get(1).toString());
        WorkerID.setText(details.get(2).toString());
        StartDate.setText(details.get(3).toString());
        EndDate.setText(details.get(4).toString());
        Notes.setText(details.get(6).toString());
        Status.setText(details.get(5).toString());
        title.setText(details.get(9).toString());
        final String hasphoto = details.get(7).toString();
        final String mission_id = details.get(8).toString();
        phone =  details.get(10).toString();
        LinearLayout layOfPhoto = (LinearLayout)findViewById(R.id.lay6);

        wid = details.get(2).toString();

        if(hasphoto.equals("true") || hasphoto.equals("1")) {
            layOfPhoto.setVisibility(View.VISIBLE);
        }


        Button btnDelete = (Button) findViewById(R.id.deleteButton);
        Button showPhoto = (Button) findViewById(R.id.photo);
        Button sendSmsbtn = (Button) findViewById(R.id.Sms);


        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activityReference);
                TextView title = new TextView(activityReference);
                title.setText("Delete Missions");
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
                tv1.setText("Are you Sure you want to delete this Mission?");
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
                        new DeleteMission().execute(mission_id);

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


        showPhoto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(ShowMissionsForManager.this, ShowPhoto.class);
                intent.putExtra("mission_id",mission_id);
                startActivity(intent);
            }
        });

        sendSmsbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
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
        Log.d("11111111111", phoneNumber);
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
    class DeleteMission extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowMissionsForManager.this);
            pDialog.setMessage("Delete Electrician..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String id = args[0];
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", id));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_delete_mission,
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
                Toast.makeText(ShowMissionsForManager.this, "Delete Succssefully", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                if(activityToBack.equals("viewElec"))
                {
                    Intent i = new Intent(ShowMissionsForManager.this, ViewElecMissions.class);
                    i.putExtra("sort", "status");
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(ShowMissionsForManager.this, ViewMyMissions.class);
                    i.putExtra("Name", "Manager");
                    i.putExtra("WorkerID",wid );
                    startActivity(i);
                }
            }
            else
            {
                Toast.makeText(ShowMissionsForManager.this, "Failed to delete mission", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }

    }



}

