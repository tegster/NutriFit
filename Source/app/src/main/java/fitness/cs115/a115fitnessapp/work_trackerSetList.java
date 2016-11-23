package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.HashMap;

import static fitness.cs115.a115fitnessapp.work_tracker.workData;

/**
 * Created by Henry on 10/23/2016.
 * Edited by James Kennedy on 11/21/2016.
 */

public class work_trackerSetList extends AppCompatActivity{
    String exerciseName = "";
    int sessID;
    static work_DBHelper.ExerciseData exData;
    work_DBHelper work_db;
    ListView setListView;
    work_trackerSetList_adapter setListAdapter;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setlist);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker, if params have been passed.
        Intent intent = getIntent();
        if ( !intent.getExtras().isEmpty()) {
            //TODO: add invariants for valid exercise name
            exerciseName = intent.getExtras().getString("eName");
            sessID = intent.getExtras().getInt("sessID");
            exData.load_session_and_exer(exerciseName, sessID);
        }
        else {
            //exercise has returned from trackSetDetail
            exerciseName = exData.get_name();
            sessID = exData.get_session_id();
        }

        Log.d("work_trackerSetList", "session id: "+exData.get_session_id());
        Log.d("work_trackerSetList", "exercise name: "+exData.get_name());
        //recreate the session data from the DB


        setTitle(exData.get_name());


        //Rest Timer
        //Currently will just start and never end.
        //Does not persist if you go back to work_tracker!
        timer = (Chronometer) findViewById(R.id.chronometer);
        timer.start();

        //======================================================================================
        //  Sets ListView
        //======================================================================================
        //Create the list.
        //TODO: use the custom adapter to display exercise name, weight, and sets
        setListView = (ListView) findViewById(R.id.lv_exerList);
        setListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                trackSet(position);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        exData.update_logged_entries();
    }

    private void trackSet(int setIndex) {
        Intent setIntent = new Intent(work_trackerSetList.this, work_trackerSetDetail.class);
        setIntent.putExtra("eName", exerciseName);
        setIntent.putExtra("sessID", sessID);
        setIntent.putExtra("setNum", setIndex + 1);
        setIntent.putExtra("goalWeight", exData.get_weights_used().get(setIndex));
        setIntent.putExtra("goalReps", exData.get_target_reps());
        startActivity(setIntent);
    }

}
