package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Henry on 10/23/2016.
 * Edited by James Kennedy on 11/23/2016
 */

public class work_tracker extends AppCompatActivity{
    work_DBHelper work_db;
    private String workoutName;
    private int sessID;
    static work_DBHelper.WorkoutData workData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker, if params have been passed.
        Intent intent = getIntent();
         if ( !intent.getExtras().isEmpty()) {
             //if anything is passed as extra, workoutName must also be passed in as extra
             workoutName = intent.getExtras().getString("wName");

             if (intent.hasExtra("sessID")){
                 sessID = intent.getExtras().getInt("sessID");
             } else {
                 //the workout does not yet have an associated session
                 sessID = work_db.create_session(workoutName);
             }

             //recreate the session from the DB data
             workData.load_work_session(workoutName, sessID);
         }

        Log.d("work_tracker", "session id: "+workData.get_session_id());
        Log.d("work_tracker", "workout name: "+workData.get_workout_name());

        setTitle(workData.get_workout_name());

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
