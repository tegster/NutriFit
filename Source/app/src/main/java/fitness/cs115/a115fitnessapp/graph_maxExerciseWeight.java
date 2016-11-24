package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
    AutoCompleteTextView exerNameTextView;
    GraphView graph;

    private TreeMap<Date,Integer> exerciseData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_exercise_weight);

        work_db = new work_DBHelper(this);


        //exercise name entry box
        ArrayList<String> exers = work_db.get_user_exercise_list();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, exers);
        exerNameTextView = (AutoCompleteTextView)
                findViewById(R.id.tv_graphExerciseName);
        exerNameTextView.setAdapter(adapter);

        // EditText exerNameEditText = (EditText) findViewById(R.id.et_graphExerciseName);
        // String inputExerName = exerNameEditText.getText().toString();
        //// TODO: 11/22/2016 connect change exercise button 




        Button confirm_button = (Button) findViewById(R.id.changeGraphExerciseBtn);
        confirm_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //======================================================================================
                //retrieve user input values
                //======================================================================================
                String inputExerName = exerNameTextView.getText().toString();
                exerciseName = inputExerName;
                //invariant exercise name must exist
                if (!work_db.is_taken_exer_name(exerciseName)) {
                    Log.e("weightGraph","bad exercise name: " + exerciseName);
                    exerNameTextView.setError("Error: This exercise does not exist!");
                }
                else {
                    updateGraphExercise(exerciseName);
                }
            }
        });
    }

    public void updateGraphExercise(String exerciseName){
        graph = (GraphView) findViewById(R.id.graph);

        exerciseData = work_db.get_weight_logs_for_exer(exerciseName);
        ArrayList<DataPoint> graphPoints = new ArrayList<DataPoint>(exerciseData.size());

        //retrieve Dates and Max Weights used for the selected exercise
        for (Entry<Date, Integer> dataPt:exerciseData.entrySet() ) {
            DataPoint dp = new DataPoint(dataPt.getKey(), dataPt.getValue());
            graphPoints.add(dp);
        }



        // set manual x bounds to have nice steps

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
