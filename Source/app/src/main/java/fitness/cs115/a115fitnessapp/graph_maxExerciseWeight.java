package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Maaz on 11/22/2016.
 */

public class graph_maxExerciseWeight extends AppCompatActivity{
    String exerciseName = "";
    String workoutName = "";
    work_DBHelper work_db;
    private TreeMap<Date,Integer> exerciseData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_exercise_weight);

        work_db = new work_DBHelper(this);
        exerciseData = work_db.get_weight_logs_for_exer("Bench Press");
        ArrayList<DataPoint> graphPoints = new ArrayList<DataPoint>(exerciseData.size());

        //retrieve Dates and Max Weights used for the selected exercise
        for (Entry<Date, Integer> dataPt:exerciseData.entrySet() ) {
            DataPoint dp = new DataPoint(dataPt.getKey(), dataPt.getValue());
            graphPoints.add(dp);
        }

        EditText exerNameEditText = (EditText) findViewById(R.id.et_graphExerciseName);
        String inputExerName = exerNameEditText.getText().toString();
        //// TODO: 11/22/2016 connect change exercise button 

        GraphView graph = (GraphView) findViewById(R.id.graph);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(exerciseData.firstKey().getTime());
        graph.getViewport().setMaxX(exerciseData.lastKey().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                graphPoints.toArray(new DataPoint[graphPoints.size()]));
        graph.addSeries(series);
    }
}
