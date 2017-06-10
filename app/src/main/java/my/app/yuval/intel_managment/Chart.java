package my.app.yuval.intel_managment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;
import java.util.List;

import javax.microedition.khronos.opengles.GL;


/**
 * Created by Yuval on 26/05/2017.
 */

public class Chart extends Activity {

    ArrayList<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.charts);

        arr = new ArrayList<>();
        arr = (ArrayList<String>) getIntent().getSerializableExtra("weeks");
        final TextView txt = (TextView) findViewById(R.id.text1);
        Global.additionalToGraph = Integer.parseInt(arr.get(0).split("~")[0]);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Done tasks per week");
        graph.getGridLabelRenderer().setNumHorizontalLabels(arr.size() +1);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(true);

        SecondScale s = new SecondScale(graph);

        DataPoint[] points = new DataPoint[arr.size() + 1];
        points[0] = new DataPoint(0, 0);

        for (int i = 0; i < arr.size(); i++) {
            String[] array = arr.get(i).split("~");
            points[i+1] = new DataPoint(i+1, Integer.parseInt(array[1]));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                String s = dataPoint.toString();
                String num= "";
                for(int i = 1; i < s.length(); i++)
                {
                   if(s.charAt(i) == '.')
                       break;
                   num += s.charAt(i);
                }
                int index = Integer.parseInt(num);
                int number  = index+ Global.additionalToGraph;
                String [] array = arr.get(index-1).split("~");
                String[] date = array[2].split("/");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) , Integer.parseInt(date[0]));

                cal.add(Calendar.DATE, 7);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String date2 = day + "/" + month + "/" + year;

                txt.setText("Week: " + number + "\nNumber of Tasks: " + array[1] + "\nDates: " + array[2] + " - " + date2);
            }
        });

        // set manual X bounds
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(100);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//       graph.getViewport().setMinX(1);
//        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
     //   graph.getViewport().setScalable(true);
    //    graph.getViewport().setScalableY(true);

        graph.addSeries(series);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
     //   graph.getViewport().setScrollableY(true); // enables vertical scrolling
    //    graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
      //  graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

    }







}
