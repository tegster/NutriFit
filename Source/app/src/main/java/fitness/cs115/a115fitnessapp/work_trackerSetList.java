package fitness.cs115.a115fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Chronometer;
import android.widget.Toast;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Henry on 10/23/2016.
 * Edited by James Kennedy on 11/21/2016.
 */

public class work_trackerSetList extends AppCompatActivity
implements Chronometer.OnChronometerTickListener{
    String exerciseName = "";
    int sessID;
    private static work_DBHelper.ExerciseData exData;
    work_DBHelper work_db;
    ListView setListView;
    work_trackerSetList_adapter setListAdapter;
    Chronometer timer;
    boolean isSetLogged;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setlist);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker, if params have been passed.
        Intent intent = getIntent();
        isSetLogged = intent.getExtras().getBoolean("isSetLogged");

        if (intent.hasExtra("eName") && intent.hasExtra("sessID") ) {
            //exercise has just begun
            //TODO: add invariants for valid exercise name
            exerciseName = intent.getExtras().getString("eName");
            sessID = intent.getExtras().getInt("sessID");
            //instantiate inner class
            exData = work_db.new ExerciseData();
            exData.load_session_and_exer(exerciseName, sessID);
        }
        else {
            //exercise has returned from trackSetDetail
            exerciseName = exData.get_name();
            sessID = exData.get_session_id();
            exData.update_logged_entries();
        }

        Log.d("work_trackerSetList", "session id: "+exData.get_session_id());
        Log.d("work_trackerSetList", "exercise name: "+exData.get_name());
        //recreate the session data from the DB

        setTitle(exData.get_name());

        //Rest Timer
        //Does not persist if you go back to work_tracker!
        timer = (Chronometer) findViewById(R.id.chronometer);
        timer.setOnChronometerTickListener(this);
        timer.setBase(SystemClock.elapsedRealtime());
        //Start timer if set is logged.
        if (isSetLogged) {timer.start();}
        if ("00:00".equals(timer.getText())){
            timer.setText("READY");
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //======================================================================================
        //  Sets ListView
        //======================================================================================
        //Create the list.
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
        updateExerciseData();
    }

    private void updateExerciseData() {
        exData.update_logged_entries();
        //generate incremental numbering to label sets
        ArrayList<String> setNumbering = new ArrayList<>(exData.get_target_sets());
        int stopcount = exData.get_target_sets();
        for (int idx = 0; idx < stopcount; ++idx){
            setNumbering.add(String.valueOf(idx));
        }

        setListAdapter = new work_trackerSetList_adapter(this, exData, setNumbering);
        setListView.setAdapter(setListAdapter);
    }

    private void trackSet(int setIndex) {
        Intent setIntent = new Intent(work_trackerSetList.this, work_trackerSetDetail.class);
        setIntent.putExtra("eName", exerciseName);
        setIntent.putExtra("sessID", sessID);
        setIntent.putExtra("setIndex", setIndex);
        setIntent.putExtra("goalWeight", exData.get_weights_used().get(setIndex));
        setIntent.putExtra("goalReps", exData.get_target_reps());
        startActivity(setIntent);
    }

    public void onChronometerTick(Chronometer cm){
        int seconds = exData.get_rest_time() % 60;
        int minutes = exData.get_rest_time() / 60;
        String restTimeStr = String.format("%02d:%02d", minutes, seconds);

        if (restTimeStr.equals(cm.getText())){
            long[] vibpattern = {0,400,200,400};
            vibrator.vibrate(vibpattern, -1);
            Toast.makeText(work_trackerSetList.this, "Time's up! Begin the next set!", Toast.LENGTH_LONG).show();
            cm.setText("READY");
            cm.stop();
        }
    }

}
