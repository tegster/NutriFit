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
    String exerciseName = "";
    int sessID = -1;
    String workoutName = "";
    work_DBHelper work_db;
    work_DBHelper.Workout_data workData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker.

        Intent intent = getIntent();

        workoutName = intent.getExtras().getString("wName");
        sessID = work_db.create_session(workoutName);
        setTitle(workoutName);

        //grab information from session
        workData = work_db.get_work_detail(workoutName);



        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //TODO: use the custom adapter to display exercise name, weight, and sets
        ListAdapter exerciseListAdapter = new work_tracker_adapter(this, workData);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                exerciseName = String.valueOf(parent.getItemAtPosition(position));
                Intent setIntent = new Intent(work_tracker.this, work_trackerSetList.class);
                setIntent.putExtra("eName", exerciseName);
                setIntent.putExtra("sessID", String.valueOf(sessID));
                startActivity(setIntent);
            }
        });




    }


}
