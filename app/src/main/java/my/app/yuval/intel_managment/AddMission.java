package my.app.yuval.intel_managment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddMission extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Activity activityReference;
    JSONParser jsonParser = new JSONParser();
    Button btnAdd;
    String smsMsg;
    Button btnClear;
    Button startDate;
    Button btnphoto;
    Button endDate;
    Button viewphoto;
    Button loadPhoto;
    EditText edtNotes;
    EditText title;
    TextView start;
    TextView end;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private int year2, month2, day2;
    String name;
    String workerID;
    String Assigner;
    String phone;
    boolean flag = false;
    ArrayList<String> phones = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> IDS = new ArrayList<>();
    String ID;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public static final String KEY = "Key";
    Spinner spinner;
    String board;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    String path;
    ArrayAdapter<String> dataAdapterName;
    ArrayAdapter<String> dataAdapterPlace;
    ArrayAdapter<String> dataAdapterFloor;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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



    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<String> elecList;
    ArrayList<String> assetsName = new ArrayList<>();
    ArrayList<String> assetsPlace = new ArrayList<>();
    ArrayList<String> assetsFloor = new ArrayList<>();

    // url to get all products list
    private static String url_all_elecs = "http://yuvalt.com/android_php/all_elec.php";
    private static String url_add_mission = "http://yuvalt.com/android_php/add_mission.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ELECS = "elecs";
    private static final String TAG_WID = "workerID";
    private static final String TAG_FNAME = "Fname";
    private static final String TAG_LNAME = "Lname";
    private static final String TAG_MAIL = "mail";
    private static final String TAG_PHONE = "phone";
    ArrayList<String> categories2 = new ArrayList<String>();

    // elecs JSONArray
    JSONArray elecs;



    int w = 100, h = 100;
    Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
    Bitmap bmp = Bitmap.createBitmap(w, h, conf);
    String encodedImage;
    Bitmap photo;
    private static final int REQUEST = 112;
    private int PICK_IMAGE_REQUEST = 1;
    Activity act;
    private Bitmap bitmap;
    private Uri filePath;
    String hasphotoStr = "false";
    String mCurrentPhotoPath;
    static int TAKE_PIC = 1888;
    Uri outPutfileUri;
    String place;
    String floor;

    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(AddMission.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForCamera(){
       if (ActivityCompat.shouldShowRequestPermissionRationale(AddMission.this, Manifest.permission.CAMERA)){
            Toast.makeText(AddMission.this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddMission.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TAKE_PIC && resultCode==RESULT_OK) {

            verifyStoragePermissions(this);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                Global.hasPhoto = true;
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(AddMission.this, new String[] {Manifest.permission.CAMERA}, 100);
        }

        activityReference = this;
        setContentView(R.layout.add_mission);
        act= this;

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        workerID = intent.getStringExtra("WorkerID");

        btnAdd = (Button) findViewById(R.id.addButton);
        btnClear = (Button) findViewById(R.id.clearcButton);
        startDate = (Button) findViewById(R.id.startDate);
        endDate = (Button) findViewById(R.id.EndDate);
        btnphoto = (Button) findViewById(R.id.photo);
        loadPhoto = (Button) findViewById(R.id.load);
        viewphoto = (Button) findViewById(R.id.viewPhoto);
        edtNotes = (EditText) findViewById(R.id.EdtNotes);
        title = (EditText) findViewById(R.id.Edttitle);
        start = (TextView)findViewById(R.id.textStart);
        end = (TextView)findViewById(R.id.textEnd);
        init();

        if(workerID.equals("11410202"))
        {
            flag = true;
            LinearLayout l1 = (LinearLayout) findViewById(R.id.layAssign);
            l1.setVisibility(View.VISIBLE);
            new LoadAllElecs().execute();
            spinner = (Spinner) findViewById(R.id.spinSorting);
            spinner.setOnItemSelectedListener(this);
            elecList = new ArrayList<String>();
        }
        else
        {
            spinner = (Spinner) findViewById(R.id.spinAssetPlace);
            initAssetPlaceArr();
            dataAdapterPlace = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, assetsPlace);
            dataAdapterPlace.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapterPlace);
            spinner.setOnItemSelectedListener(AddMission.this);

        }


        btnphoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(AddMission.this, new String[] {Manifest.permission.CAMERA}, 100);
                }
                else
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStorageDirectory(),
                                "MyPhoto.jpg");
                        outPutfileUri = Uri.fromFile(file);
                        path = file.getPath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                        startActivityForResult(intent, TAKE_PIC);
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
                Intent intent = new Intent(AddMission.this,ShowPhoto.class);
                startActivity(intent);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String StartDate = start.getText().toString();
                String EndDate = end.getText().toString();
                String taskTitle = title.getText().toString();

                if(!(compareDates(StartDate,EndDate )))
                {
                    Toast.makeText(AddMission.this, "End date should be after start date", Toast.LENGTH_LONG).show();
                    return;
                }

                Calendar calan = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                int y = calan.get(Calendar.YEAR);
                int m = calan.get(Calendar.MONTH) + 1;
                int d = calan.get(Calendar.DAY_OF_MONTH);
                String s = d+ "/" + m  + "/" + y;

                if((compareDates(EndDate,s)))
                {
                    Toast.makeText(AddMission.this, "End date already passed.", Toast.LENGTH_LONG).show();
                    return;
                }


                if(taskTitle == null || taskTitle.length() < 1 || taskTitle.equals(""))
                {
                    Toast.makeText(AddMission.this, "Task title is needed.", Toast.LENGTH_LONG).show();
                    return;
                }

                String notes = edtNotes.getText().toString();



                String photoToDB = " ";

                if(notes == null || notes.length() < 1)
                    notes = "  ";


                if(flag)
                {
                    new AddNewMission().execute(board, name, ID, StartDate, EndDate,"In Work" ,notes, photoToDB, hasphotoStr,taskTitle);

                }
                else
                {
                    new AddNewMission().execute(board, name, workerID, StartDate, EndDate, "In Work" ,notes, photoToDB, hasphotoStr,taskTitle);

                }
            }
        });

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void init() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        calendar.add(Calendar.DATE, 14); //Default time of two weeks
        year2 = calendar.get(Calendar.YEAR);
        month2 = calendar.get(Calendar.MONTH);
        day2 = calendar.get(Calendar.DAY_OF_MONTH);
        showDate2(year2, month2+1, day2);

        edtNotes.setText("");
        title.setText("");
        viewphoto.setVisibility(View.GONE);
        encodedImage = null;
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
                    myDateListener, year, month, day);
        }
        else if(id == 9999)
        {
            return new DatePickerDialog(this,
                    myDateListener2, year2, month2, day2);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate2(arg1, arg2+1, arg3);
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
        if(flag)
        {
            Intent i = new Intent(AddMission.this, ManagerActicvity.class);
            startActivity(i);
        }
        else {
            Intent intent = new Intent(AddMission.this, ElecActivity.class);
            intent.putExtra("WorkerID",workerID);
            intent.putExtra("Name", name);
            startActivity(intent);
        }
    }

    private void sendSMS(String phoneNumber, String message) {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
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
                    Global.hasPhoto = false;
                    Intent i = new Intent(AddMission.this, ManagerActicvity.class);
                    startActivity(i);
                } else {
                    Global.hasPhoto = false;
                    Intent i = new Intent(AddMission.this, ManagerActicvity.class);
                    startActivity(i);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int a = 1;
        if(workerID.equals("11410202") && parent.getId() == R.id.spinSorting)
        {
            Assigner = parent.getItemAtPosition(position).toString();
            phone = phones.get(position);
            name = names.get(position);
            ID = IDS.get(position);
            Spinner spinner = (Spinner) parent;
        }
        else if(parent.getId() == R.id.spinAssetPlace)
        {
            place = parent.getItemAtPosition(position).toString();
            Spinner spinner = (Spinner) parent;

            if(place.equals("area1"))
                a = 1;
            else if(place.equals("area2"))
                a = 2;
            else if(place.equals("area3"))
                a = 3;

            spinner = (Spinner) findViewById(R.id.spinAssetFloor);
            initAssetFloorArr(a);
            dataAdapterFloor = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, assetsFloor);
            dataAdapterFloor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            dataAdapterFloor.notifyDataSetChanged();
            spinner.setAdapter(dataAdapterFloor);
            spinner.setOnItemSelectedListener(AddMission.this);

        }
        else if(parent.getId() == R.id.spinAssetName)
        {
            board = parent.getItemAtPosition(position).toString();
            Spinner spinner = (Spinner) parent;
        }
        else if(parent.getId() == R.id.spinAssetFloor)
        {
            floor = parent.getItemAtPosition(position).toString();
            Spinner spinner = (Spinner) parent;

            if(floor.equals("1"))
                a = 1;
            else if(floor.equals("2"))
                a = 2;
            else if(floor.equals("3"))
                a = 3;
            else if(floor.equals("4"))
                a = 4;

            spinner = (Spinner) findViewById(R.id.spinAssetName);
            initAssetArr(a);
            dataAdapterName = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, assetsName);
            dataAdapterName.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            dataAdapterName.notifyDataSetChanged();
            spinner.setAdapter(dataAdapterName);
            spinner.setOnItemSelectedListener(AddMission.this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            pDialog = new ProgressDialog(AddMission.this);
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
                        arr = workerid + "~" + f_name + "~" + l_name + "~" + mail + "~" + phone;

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
                Toast.makeText(AddMission.this, "Failed to view electricians", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }
            pDialog.dismiss();

            List<String> categories = new ArrayList<String>();
            String arr[];
            String s;

            for(int i = 0; i < elecListArr.size(); i ++)
            {
                s = elecListArr.get(i);
                arr = s.split("~");
                if(!(arr[0].equals("11410202")))
                {
                    categories.add(arr[1] + " " + arr[2]);
                    names.add(arr[1] + " " + arr[2]);
                    phones.add(arr[4]);
                    IDS.add(arr[0]);
                }
            }

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);
            dataAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter2);
            spinner = (Spinner) findViewById(R.id.spinAssetPlace);
            initAssetPlaceArr();
            dataAdapterPlace = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, assetsPlace);
            dataAdapterPlace.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapterPlace);
            spinner.setOnItemSelectedListener(AddMission.this);
            return;
        }
    }


    /* Background Async Task to Create new product
   * */
    class AddNewMission extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddMission.this);
            pDialog.setMessage("Add Mission..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            board = args[0];
            String name = args[1];
            String workerID = args[2];
            String sdate = args[3];
            String edate = args[4];
            String status = args[5];
            String notes = args[6];
            String photo = args[7];
            String hasphotoDB = args[8];
            String tit = args[9];

            boolean hasphoto = Global.hasPhoto;

            if(hasphoto) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Global.img.compress(Bitmap.CompressFormat.JPEG, 35, stream);
                byte[] img = stream.toByteArray();
                photo = Base64.encodeToString(img, Base64.DEFAULT);
            }
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Board", board));
            params.add(new BasicNameValuePair("Name", name));
            params.add(new BasicNameValuePair("Wid", workerID));
            params.add(new BasicNameValuePair("Sdate", sdate));
            params.add(new BasicNameValuePair("Edate", edate));
            params.add(new BasicNameValuePair("Status", status));
            params.add(new BasicNameValuePair("Notes", notes));
            params.add(new BasicNameValuePair("Photo", photo));
            params.add(new BasicNameValuePair("HasPhoto", hasphotoDB));
            params.add(new BasicNameValuePair("Title", tit));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_mission,
                    "POST", params);

            try {
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
            if(file_url == null || !(file_url.equals("Success")))
            {
                Toast.makeText(AddMission.this, "Failed to add mission", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                return;
            }
            if(file_url.equals("Success")) {
               // Toast.makeText(AddMission.this, "Add Succssefully", Toast.LENGTH_LONG).show();
                pDialog.dismiss();

                if (flag) {

                    smsMsg = "Hi,\nYou got a new task, on asset: \n" + board + " .\n" +
                            "Please see more information in Yugi app.\nThanks,\nGilad Tsidkiyahu.";

                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] PERMISSIONS = {android.Manifest.permission.SEND_SMS};
                        if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                            ActivityCompat.requestPermissions(act, PERMISSIONS, REQUEST);
                        } else {
                            sendSMS(phone, smsMsg);
                            Global.hasPhoto = false;
                            Intent intent = new Intent(AddMission.this, ManagerActicvity.class);
                            startActivity(intent);
                        }
                    } else {
                        sendSMS(phone, smsMsg);
                        Global.hasPhoto = false;
                        Intent intent = new Intent(AddMission.this, ManagerActicvity.class);
                        startActivity(intent);
                    }

                } else {
                    Intent i = new Intent(AddMission.this, ElecActivity.class);
                    Global.hasPhoto = false;
                    i.putExtra("WorkerID", workerID);
                    i.putExtra("Name", name);
                    startActivity(i);
                }
            }

        }

    }

    public void initAssetArr(int num)
    {
        assetsName = new ArrayList<>();
        switch (num) {

            case 1:
                assetsName.add("F28-ABC1563");
                assetsName.add("F28-ACD1563");
                assetsName.add("F28-AFGH1563");
                assetsName.add("F28-ABCHJHGF563");
                assetsName.add("F28-KLKL12");
                break;
            case 2:
                assetsName.add("F25-ERT785");
                assetsName.add("F25-AJK-LP,K-56");
                assetsName.add("F25-RTYPA");
                assetsName.add("F25-PANEL52");
                assetsName.add("F25-PALKERM321");
                assetsName.add("F25-PANEL3654");
                break;

            case 3:
                assetsName.add("F30-ABC1563");
                assetsName.add("F30-ACD1563");
                assetsName.add("F30-AFGH1563");
                assetsName.add("F30-ABCHJHGF563");
                assetsName.add("F30-KLKL12");
                break;
            case 4:
                assetsName.add("F40-ERT785");
                assetsName.add("F40-AJK-LP,K-56");
                assetsName.add("F40-RTYPA");
                assetsName.add("F40-PANEL52");
                assetsName.add("F40-PALKERM321");
                assetsName.add("F40-PANEL3654");
                break;
        }

    }

    public  void initAssetFloorArr(int num)
    {
        assetsFloor = new ArrayList<>();
        switch (num) {
            case 1:
                assetsFloor.add("1");
                assetsFloor.add("2");
                assetsFloor.add("3");
                break;
            case 2:
                assetsFloor.add("1");
                assetsFloor.add("2");
                break;
            case 3:
                assetsFloor.add("1");
                assetsFloor.add("2");
                assetsFloor.add("3");
                assetsFloor.add("4");
                break;

        }

    }

    public void initAssetPlaceArr()
    {
        assetsPlace.add("area1");
        assetsPlace.add("area2");
        assetsPlace.add("area3");

    }
}


