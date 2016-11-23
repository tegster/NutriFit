package fitness.cs115.a115fitnessapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static fitness.cs115.a115fitnessapp.R.id.graph;
import static fitness.cs115.a115fitnessapp.R.id.graphs;

/**
 * Created by Matthew on 11/19/16.
 */

public class meal_graphs extends AppCompatActivity {
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    ArrayList<String> arrTblNames = new ArrayList<String>();
    ArrayList<Date> dates = new ArrayList<Date>();
    private TreeMap<Date, Integer> foodData = new TreeMap<>();
    GraphView graph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_graphs);

        SQLiteDatabase mDatabase = openOrCreateDatabase("Eatfood.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);//the database with all of the foods by day
        Cursor c = mDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (!c.getString((c.getColumnIndex("name"))).equals("android_metadata")) {
                    if (!arrTblNames.contains(c.getString(c.getColumnIndex("name")))) { //paranoid error checking, shouldn't be necessary
                        arrTblNames.add(c.getString(c.getColumnIndex("name")));
                    }
                }
                c.moveToNext();
            }
        }

        graph = (GraphView) findViewById(R.id.graph);
        SetUpGraph();

    }

    private void SetUpGraph() {
        meal_eatFoodDBHelper mydb;


        for (String s : arrTblNames) {
            //dates.add(string_ISO8601_to_Date(s));
            try {
                mydb = new meal_eatFoodDBHelper(this, s);
                foodData.put(string_ISO8601_to_Date(s), mydb.getTotalCalories());//call function that gives calories associated with that specific day);
            } catch (Exception e) {
                mydb = new meal_eatFoodDBHelper(this, "[" + s + "]");
                System.out.println("test: " + string_ISO8601_to_Date(s));
                System.out.println("total calroies is: " + mydb.getTotalCalories());
                foodData.put(string_ISO8601_to_Date(s), mydb.getTotalCalories());//call function that gives calories associated with that specific day);
            }
        }
        //foodData is the tree

        ArrayList<DataPoint> graphPoints = new ArrayList<DataPoint>(dates.size());
        for (Map.Entry<Date, Integer> dataPt : foodData.entrySet()) {
            DataPoint dp = new DataPoint(dataPt.getKey(), dataPt.getValue());
            graphPoints.add(dp);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));


        graph.getViewport().setMinX(foodData.firstKey().getTime());
        graph.getViewport().setMaxX(foodData.lastKey().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                graphPoints.toArray(new DataPoint[graphPoints.size()]));

        graph.addSeries(series);

    }

    /*
Converts ISO 8601 formatted String date representation into a Date object
 */
    private Date string_ISO8601_to_Date(String date_str) {
        java.text.DateFormat df = new java.text.SimpleDateFormat(DATE_FORMAT);
        Date date_obj;
        try {
            date_obj = df.parse(date_str);
        } catch (ParseException pe) {
            date_obj = null;
        }
        return date_obj;
    }

}
