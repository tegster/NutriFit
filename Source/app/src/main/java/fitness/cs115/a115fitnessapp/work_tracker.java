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
    private work_DBHelper work_db;
    private static work_DBHelper.WorkoutData workData;
    ListAdapter exerciseListAdapter;
    ListView exerciseListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker, if params have been passed.
        Intent intent = getIntent();

        //DEBUG
        Log.d("work_tracker", "onCreate reached with intent wName: " +
                intent.getExtras().getString("wName", "<none>") );
        Log.d("work_tracker", "onCreate reached with intent sessID: " +
                intent.getExtras().getString("sessID", "<none>") );

        if (intent.hasExtra("wName")){
            //the workout has just started and does not yet have an associated session
            //instantiate inner class
            workData = work_db.new WorkoutData();
            String workName = intent.getStringExtra("wName");
            int sessionID = work_db.create_session(workName);
            workData.load_work_session(workName, sessionID);
        }
        else {
            //the session has already begun
            workData.reload_session();
        }

         //TODO: find out why work tracker isnt updating data when returning from set list



         //recreate the session from the DB data


        Log.d("work_tracker", "workData.session_id: "+workData.get_session_id());
        Log.d("work_tracker", "workData.workout_name: "+workData.get_workout_name());

        setTitle(workData.get_workout_name());

        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //pass custom adapter the workout data needed to track each exercise.
        exerciseListAdapter = new work_tracker_adapter(this, workData);
        exerciseListView = (ListView) findViewById(R.id.lv_exerList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String exerciseName = String.valueOf(parent.getItemAtPosition(position));
                Intent setIntent = new Intent(work_tracker.this, work_trackerSetList.class);
                setIntent.putExtra("eName", exerciseName);
                setIntent.putExtra("sessID", workData.get_session_id());
                setIntent.putExtra("isSetLogged", false);
                startActivity(setIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadWorkoutData();
    }

    private void reloadWorkoutData() {
        workData.reload_session();
        exerciseListAdapter = new work_tracker_adapter(this, workData);
        exerciseListView.setAdapter(exerciseListAdapter);
    }
}
