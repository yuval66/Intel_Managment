package my.app.yuval.intel_managment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ShowMissionsForElec extends AppCompatActivity {

    private Activity activityReference;
    Button btnSave;
    Button btnFinish;
    Button startDate;
    Button btnphoto;
    Button endDate;
    Button viewphoto;
    EditText edtNotes;
    TextView start;
    TextView end;
    TextView title;
    Button loadPhoto;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private int year2, month2, day2;

    String name;
    String workerID;
    String boardID;

    private static final int CAMERA_REQUEST = 1888;
    int w = 100, h = 100;
    Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
    private static String url_update_mission = "http://yuvalt.com/android_php/update_mission.php";
    private static String url_finish_mission = "http://yuvalt.com/android_php/done_missions.php";
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    boolean photoChanged = false;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;

    private Bitmap bitmap;
    private Uri filePath;
    String hasphotoStr = "false";
    static int TAKE_PIC = 1888;
    Uri outPutfileUri;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(ShowMissionsForElec.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ShowMissionsForElec.this, Manifest.permission.CAMERA)) {
            Toast.makeText(ShowMissionsForElec.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ShowMissionsForElec.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TAKE_PIC && resultCode == RESULT_OK) {

            verifyStoragePermissions(this);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                Global.hasPhoto = true;
                photoChanged = true;
                viewphoto.setVisibility(View.VISIBLE);
                hasphotoStr = "true";
                Global.img = bMapRotate;

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }


        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Global.hasPhoto = true;
                viewphoto.setVisibility(View.VISIBLE);
                hasphotoStr = "true";
                Global.img = bitmap;
                photoChanged = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        activityReference = this;
        setContentView(R.layout.showmissionsforelec);

        Intent i = getIntent();
        final ArrayList<String> details;
        details = (ArrayList<String>) getIntent().getSerializableExtra("MissionDetails");

        if (Global.hasPhoto)
            viewphoto.setVisibility(View.VISIBLE);


        btnSave = (Button) findViewById(R.id.saveButton);
        btnFinish = (Button) findViewById(R.id.Finish);
        startDate = (Button) findViewById(R.id.startDate);
        endDate = (Button) findViewById(R.id.EndDate);
        btnphoto = (Button) findViewById(R.id.photo);
        viewphoto = (Button) findViewById(R.id.viewPhoto);
        edtNotes = (EditText) findViewById(R.id.EdtNotes);
        start = (TextView) findViewById(R.id.textStart);
        end = (TextView) findViewById(R.id.textEnd);
        loadPhoto = (Button) findViewById(R.id.load);
        title = (TextView) findViewById(R.id.text30);


        start.setText(details.get(3).toString());
        end.setText(details.get(4).toString());
        edtNotes.setText(details.get(6).toString());
        final String hasphoto = details.get(7).toString();
        workerID = details.get(2).toString();
        name = details.get(1).toString();
        final String board = details.get(0).toString();
        boardID = details.get(8).toString();
        title.setText(details.get(9).toString());

        if (hasphoto.equals("true") || hasphoto.equals("1")) {
            viewphoto.setVisibility(View.VISIBLE);
            hasphotoStr = "true";
        }


        String d1 = start.getText().toString();
        String d2 = end.getText().toString();

        String[] arr = d1.split("/");
        String[] arr2 = d2.split("/");
        year = Integer.parseInt(arr[2]);
        month = Integer.parseInt(arr[1]);
        day = Integer.parseInt(arr[0]);
        showDate(year, month, day);

        year2 = Integer.parseInt(arr2[2]);
        month2 = Integer.parseInt(arr2[1]);
        day2 = Integer.parseInt(arr2[0]);
        showDate2(year2, month2, day2);


        btnphoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!checkPermissionForCamera()) {
                    requestPermissionForCamera();
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        loadPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        viewphoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMissionsForElec.this, ShowPhoto.class);
                if (Global.hasPhoto)
                    startActivity(intent);
                else {
                    intent.putExtra("mission_id", boardID);
                    startActivity(intent);

                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String StartDate = start.getText().toString();
                String EndDate = end.getText().toString();

                int numOfDays = 0;
                try {
                    numOfDays = diffDays(StartDate, EndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new SetAsDone().execute(boardID, Integer.toString(numOfDays));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String StartDate = start.getText().toString();
                String EndDate = end.getText().toString();

                if (!(compareDates(StartDate, EndDate))) {
                    Toast.makeText(ShowMissionsForElec.this, "End date should be after start date", Toast.LENGTH_LONG).show();
                    return;
                }

                Calendar calan = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                int y = calan.get(Calendar.YEAR);
                int m = calan.get(Calendar.MONTH) + 1;
                int d = calan.get(Calendar.DAY_OF_MONTH);
                String s = d + "/" + m + "/" + y;
                String change = "not";

                if ((compareDates(s, EndDate))) {
                    change = "yes";
                }

                String notes = edtNotes.getText().toString();

                if (notes.contains("~")) {
                    Toast.makeText(ShowMissionsForElec.this, "notes cannot contain ~", Toast.LENGTH_LONG).show();
                    return;
                }

                String photoToDB = " ";
                boolean hasphoto = Global.hasPhoto;

                if (hasphoto) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Global.img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] img = stream.toByteArray();
                    photoToDB = Base64.encodeToString(img, Base64.DEFAULT);
                }

                if (notes == null || notes.length() < 1)
                    notes = "";

                new EditMission().execute(StartDate, EndDate, notes, photoToDB, boardID, hasphotoStr, change);


            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    int diffDays(String d1, String d2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(d1);
        Date date2 = sdf.parse(d2);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.clear();
        c1.set(date1.getDay(), date1.getMonth(), date1.getYear());
        c2.clear();
        c2.set(date2.getDay(), date2.getMonth(), date2.getYear());

        long diff = c2.getTimeInMillis() - c1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setDate2(View view) {
        showDialog(9999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month - 1, day);
        } else if (id == 9999) {
            return new DatePickerDialog(this,
                    myDateListener2, year2, month2 - 1, day2);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate2(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        start.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void showDate2(int year, int month, int day) {
        end.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private boolean compareDates(String d1, String d2) {
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


    public void onBackPressed() {
        Intent intent = new Intent(ShowMissionsForElec.this, ViewMyMissions.class);
        Global.hasPhoto = false;
        intent.putExtra("WorkerID", workerID);
        intent.putExtra("Name", name);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ShowMissionsForElec Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    /* Background Async Task to Create new product
 * */
    class EditMission extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowMissionsForElec.this);
            pDialog.setMessage("Editing Mission..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String sdate = args[0];
            String edate = args[1];
            String notes = args[2];
            String photo = args[3];
            String boardID = args[4];
            String hasphotoDB = args[5];
            String needToBeChange = args[6];

            boolean hasphoto = Global.hasPhoto;

            if (hasphoto) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Global.img.compress(Bitmap.CompressFormat.JPEG, 35, stream);
                byte[] img = stream.toByteArray();
                photo = Base64.encodeToString(img, Base64.DEFAULT);
            }
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("Sdate", sdate));
            params.add(new BasicNameValuePair("Edate", edate));
            params.add(new BasicNameValuePair("Notes", notes));
            params.add(new BasicNameValuePair("ID", boardID));
            params.add(new BasicNameValuePair("HasPhoto", hasphotoDB));
            params.add(new BasicNameValuePair("change", needToBeChange));
            if (photoChanged) {
                params.add(new BasicNameValuePair("Photo", photo));
            }

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_mission,
                    "POST", params);

            try {
                if (json == null)
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
         **/
        protected void onPostExecute(String file_url) {
            if (file_url != null && file_url.equals("Success")) {
                Toast.makeText(ShowMissionsForElec.this, "Updated Succssefully", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                Intent i = new Intent(ShowMissionsForElec.this, ElecActivity.class);
                Global.hasPhoto = false;
                i.putExtra("WorkerID", workerID);
                startActivity(i);
            } else {
                Toast.makeText(ShowMissionsForElec.this, "Failed to Edit mission", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }
        }

    }


    /* Background Async Task to Create new product
  * */
    class SetAsDone extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowMissionsForElec.this);
            pDialog.setMessage("Update Mission..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String boardID = args[0];
            String numOfDays = args[1];
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", boardID));
            Log.d("777777777777", numOfDays);
            params.add(new BasicNameValuePair("DAYS", numOfDays));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_finish_mission,
                    "POST", params);

            try {
                if (json == null)
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
         **/
        protected void onPostExecute(String file_url) {
            if (file_url != null && file_url.equals("Success")) {
                Toast.makeText(ShowMissionsForElec.this, "Mission Succssefully changed to done", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                Intent i;
                i = new Intent(ShowMissionsForElec.this, ViewMyMissions.class);
                Global.hasPhoto = false;
                i.putExtra("WorkerID", workerID);
                i.putExtra("Name", name);
                startActivity(i);
            } else {
                Toast.makeText(ShowMissionsForElec.this, "Failed to Update mission", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }

    }


}
