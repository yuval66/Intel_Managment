package my.app.yuval.intel_managment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowPhoto extends AppCompatActivity {

    ImageView img;
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_getPhoto = "http://yuvalt.com/android_php/get_photo.php";

    // JSON Node names
    private static final String TAG_PHOTO= "photo";
    private static final String TAG_SUCCESS = "success";

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.show_photo);
        if(Global.hasPhoto) {
            img = (ImageView) findViewById(R.id.backgroundphoto);
            img.setImageBitmap(Global.img);
        }
        else
        {
            Intent intent = getIntent();
            String id = intent.getStringExtra("mission_id");
            new getPhoto().execute(id);

        }
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class getPhoto extends AsyncTask<String, String,Bitmap> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowPhoto.this);
            pDialog.setMessage("Loading photo. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected Bitmap doInBackground(String... args) {
            // Building Parameters
            String id = args[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", id));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_getPhoto, "POST", params);


            try {
                // Checking for SUCCESS TAG
                if(json == null)
                    return null;

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    String photo = json.getString(TAG_PHOTO);
                    byte [] encodeByte=Base64.decode(photo,Base64.DEFAULT);
                    Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    return bitmap;

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
        protected void onPostExecute(Bitmap bmp) {

            pDialog.dismiss();

            if(bmp == null)
                Toast.makeText(ShowPhoto.this, "Failed to laod photo from server", Toast.LENGTH_LONG).show();
            else
            {
                img = (ImageView) findViewById(R.id.backgroundphoto);
                img.setImageBitmap(bmp);
            }


            }




    }



}
