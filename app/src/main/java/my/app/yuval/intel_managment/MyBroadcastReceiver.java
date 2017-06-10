package my.app.yuval.intel_managment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

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

public class MyBroadcastReceiver extends BroadcastReceiver {

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    JSONArray missions;



    ArrayList<String> MissionList;

    // url to get all products list
    private static String url_all_elecs = "http://yuvalt.com/android_php/alarm.php";
    private static String url_lates = "http://yuvalt.com/android_php/UpdateLates.php";
    private static String create_row = "http://yuvalt.com/android_php/Create_Row.php";

    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MISSIONS = "missions";
    private static final String TAG_BOARD = "board";
    private static final String TAG_NAME = "name";
    private static final String TAG_WID = "workerID";
    private static final String TAG_EDATE = "edate";
    private static final String TAG_STATUS = "status";
    private static final String TAG_PHONE= "phone";

    @Override
    public void onReceive(Context context, Intent intent) {

        new LoadAllMissions().execute();

    }


class LoadAllMissions extends AsyncTask<String, String, ArrayList<String>> {

    /**
     * Before starting background thread Show Progress Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected ArrayList<String> doInBackground(String... args) {


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
                missions = json.getJSONArray(TAG_MISSIONS);

                // looping through All Products
                for (int i = 0; i < missions.length(); i++) {
                    JSONObject c = missions.getJSONObject(i);
                    String arr;

                    String board = c.getString(TAG_BOARD);
                    String name = c.getString(TAG_NAME);
                    String board_id = c.getString(TAG_WID);
                    String phone = c.getString(TAG_PHONE);
                    String edate = c.getString(TAG_EDATE);
                    String status = c.getString(TAG_STATUS);
                    String mission_id = c.getString(TAG_ID);
                    arr = board + "~" + name + "~" + board_id + "~" + phone + "~" + edate + "~" + status + "~" + mission_id;

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
            return;
        }

        String s;

        for (int i = 0; i < elecListArr.size(); i++) {
            s = elecListArr.get(i);
            final String[] arr = s.split("~");
            String id = arr[6];

            if(arr[5].toString().equals("In Work")){
                if(!(compareDates(arr[4])))
                {
                    new SetAsLate().execute(id);
                    SmsManager sms = SmsManager.getDefault();
                    String msg = "Hey,\nYour task on asset: \n " + arr[0] + "\n is in status late.\n" +
                            "The end date is: " + arr[4] + ". Please update the status or the date of the task.\n" +
                            "Thanks, Gilad Tsidkiyahu.";

                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    if(day != Calendar.FRIDAY && day != Calendar.SATURDAY)
                        sms.sendTextMessage(arr[3], null, msg, null, null);
                }
                else
                    continue;
            }
            else if(arr[5].toString().equals("Late")){

                SmsManager sms = SmsManager.getDefault();
                String msg = "Hey,\nYour task on asset:  " + arr[0] + "\nis in status late.\n" +
                        "The end date is: " + arr[4] + ". Please update the status or the date of the task.\n" +
                        "Thanks, Gilad Tsidkiyahu.";

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if(day != Calendar.FRIDAY && day != Calendar.SATURDAY)
                    sms.sendTextMessage(arr[3], null, msg, null, null);
            }


        }

        }

}
    private boolean compareDates(String d1) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(d1);
            Calendar c = Calendar.getInstance();
            Date date2 = c.getTime();

            if (date2.before(date1)) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }



    class SetAsLate extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String id = args[0];
            // getting JSON string from URL
            params.add(new BasicNameValuePair("missionID", id));

            JSONObject json = jParser.makeHttpRequest(url_lates, "GET", params);


            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    return "true";

                } else {
                    return "false";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "false";
            }

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String value) {
            return;
        }


    }



}










