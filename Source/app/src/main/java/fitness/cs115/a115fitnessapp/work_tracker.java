package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Henry on 10/23/2016.
 */

public class work_tracker extends AppCompatActivity{
    work_DBHelper work_db;
    static work_DBHelper.Workout_data workData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker, if params have been passed.
        Bundle intentExtras = getIntent().getExtras();
         if ( !intentExtras.isEmpty()) {
             String workoutName = intentExtras.getString("wName");
             workData.set_workout_name(workoutName);
             int sessID = work_db.create_session(workoutName);
             workData.set_session_id(sessID);
         }

        setTitle(workoutName);
        //grab information from session
        workData = work_db.get_work_detail(workoutName);

        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //pass custom adapter the workout data needed to track each exercise.
        ListAdapter exerciseListAdapter = new work_tracker_adapter(this, workData);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String exerciseName = String.valueOf(parent.getItemAtPosition(position));
                Intent setIntent = new Intent(work_tracker.this, work_trackerSetList.class);
                setIntent.putExtra("eName", exerciseName);
                setIntent.putExtra("sessID", sessID);
                startActivity(setIntent);
            }
        });
    }
}
