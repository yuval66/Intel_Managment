package my.app.yuval.intel_managment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;


public class InsertElecActivity extends Activity{

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText edtFirstName;
    EditText edtLastName;
    EditText edtWorkerID;
    EditText edtPassword;
    EditText edtMail;
    EditText edtPhone;
    Button addbtn;
    Button clearbtn;


    private static String url_add_elec = "http://yuvalt.com/android_php/add_elec.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.insert_elec);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            edtFirstName = (EditText) findViewById(R.id.EdtFirstName);
            edtLastName = (EditText) findViewById(R.id.EdtLastName);
            edtWorkerID = (EditText) findViewById(R.id.EdtElec);
            edtWorkerID.setRawInputType(Configuration.KEYBOARD_12KEY);
            edtPassword = (EditText) findViewById(R.id.EdtElecPass);
            edtMail = (EditText) findViewById(R.id.EdtMail);
            edtMail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            edtPhone = (EditText) findViewById(R.id.EdtPhone);
            edtPhone.setRawInputType(Configuration.KEYBOARD_12KEY);
            addbtn = (Button) findViewById(R.id.addButton);
            clearbtn = (Button) findViewById(R.id.clearcButton);



            addbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    String FirstName = edtFirstName.getText().toString();
                    String LastName = edtLastName.getText().toString();
                    String WorkerID = edtWorkerID.getText().toString();
                    String Password = edtPassword.getText().toString();
                    String mail = edtMail.getText().toString();
                    String phone = edtPhone.getText().toString();

                    if(FirstName.length() <= 0|| LastName.length() <= 0 || WorkerID.length() <= 0
                            || Password.length() <= 0 || mail.length() <= 0 || phone.length() <= 0 ){
                        Toast.makeText(InsertElecActivity.this, "One of the fields is empty", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(WorkerID.length() != 8 || !(WorkerID.matches("[-+]?\\d*\\.?\\d+")))
                    {
                        edtWorkerID.setText("");
                        Toast.makeText(InsertElecActivity.this, "Invalid Worker ID", Toast.LENGTH_LONG).show();
                        return;
                    }

                    new AddNewElec().execute(FirstName,LastName,WorkerID,Password,mail,phone);
                    init();

                }
            });

            clearbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    init();
                }
            });
        }

    protected void init()
    {
        edtFirstName.setText("");
        edtLastName.setText("");
        edtWorkerID.setText("");
        edtPassword.setText("");
        edtPhone.setText("");
        edtMail.setText("");
    }

    /* Background Async Task to Create new product
    * */
    class AddNewElec extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InsertElecActivity.this);
            pDialog.setMessage("Add Electrician..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String workerID = args[2];
            String first_name = args[0];
            String last_name = args[1];
            String password = args[3];
            String phone = args[5];
            String mail = args[4];
            String role = "Elec";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("WORKERID", workerID));
            params.add(new BasicNameValuePair("FNAME", first_name));
            params.add(new BasicNameValuePair("LNAME", last_name));
            params.add(new BasicNameValuePair("PASSWORD", MainActivity.md5(password)));
            params.add(new BasicNameValuePair("PHONE", phone));
            params.add(new BasicNameValuePair("MAIL", mail));
            params.add(new BasicNameValuePair("ROLE", role));


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
             if(file_url.equals("Success")) {
                 Toast.makeText(InsertElecActivity.this, "Add Succssefully", Toast.LENGTH_LONG).show();
                 pDialog.dismiss();
             }
            else
             {
                 Toast.makeText(InsertElecActivity.this, "Failed to add electrician", Toast.LENGTH_LONG).show();
                 pDialog.dismiss();
             }
        }

    }



    }

