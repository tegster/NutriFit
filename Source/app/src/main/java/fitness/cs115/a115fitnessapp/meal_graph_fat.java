package fitness.cs115.a115fitnessapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matthew on 11/23/16.
 */

public class meal_graph_fat extends AppCompatActivity {
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    ArrayList<String> arrTblNames = new ArrayList<String>();
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
            try {
                mydb = new meal_eatFoodDBHelper(this, s);
                foodData.put(string_ISO8601_to_Date(s), mydb.getTotalCalories());//call function that gives calories associated with that specific day);
            } catch (Exception e) {
                mydb = new meal_eatFoodDBHelper(this, "[" + s + "]");
                System.out.println("test: " + string_ISO8601_to_Date(s));
                System.out.println("total fat is: " + mydb.getTotalFat());
                foodData.put(string_ISO8601_to_Date(s), mydb.getTotalFat());//call function that gives calories associated with that specific day);
            }
        }
        //foodData is the tree

        ArrayList<DataPoint> graphPoints = new ArrayList<DataPoint>(arrTblNames.size());
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
