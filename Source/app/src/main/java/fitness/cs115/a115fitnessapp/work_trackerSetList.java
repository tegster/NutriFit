package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Henry on 10/23/2016.
 * Edited by James Kennedy on 11/21/2016.
 */

public class work_trackerSetList extends AppCompatActivity{
    static String exerciseName = "";
    static int sessID;
    static int currSetIndex;
    work_DBHelper work_db;
    HashMap<String, Integer> exerciseData;
    ArrayList<String> setNumbers;
    ArrayList<String> currReps;
    ArrayList<String> targetReps;
    ArrayList<String> weights;
    ListView setListView;
    work_trackerSetList_adapter setListAdapter;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setlist);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker.
        Intent intent = getIntent();
        sessID = intent.getExtras().getInt("sessID");
        exerciseName = intent.getExtras().getString("eName");
        setTitle(exerciseName);

        //TODO: add invariants for valid exercise name
        initExerciseData();


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

    private void initExerciseData() {
        exerciseData = work_db.get_exer_detail(exerciseName);
        String repStr  = String.valueOf(exerciseData.get(work_DBHelper.EXER_INDEX_REPS));
        int sets = exerciseData.get(work_DBHelper.EXER_INDEX_GOAL_SETS);
        int startWeight = exerciseData.get(work_DBHelper.EXER_INDEX_START_WEIGHT);
        int incrWeight = exerciseData.get(work_DBHelper.EXER_INDEX_INC_WEIGHT);
        int restTime = exerciseData.get(work_DBHelper.EXER_INDEX_REST_TIME);
        //TODO: connect restTime to timer

        this.setNumbers = new ArrayList<>(sets);
        this.currReps = new ArrayList<>(sets);
        this.targetReps = new ArrayList<>(sets);
        this.weights = new ArrayList<>(sets);
        this.currSetIndex = 0;

        //load Arraylists for each set
        int setWeight = startWeight;
        for (int setIdx = 0; setIdx < sets; ++setIdx) {
            setNumbers.add(String.valueOf(setIdx + 1));
            currReps.add("0");
            targetReps.add(repStr);
            weights.add(String.valueOf(setWeight));
            setWeight += incrWeight;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateExerciseData();
    }

    private void updateExerciseData() {
        ArrayList<Integer> loggedSetReps = work_db.get_exer_session_reps(exerciseName, sessID);
        String currRepStr;
        currSetIndex = loggedSetReps.size();

        //update current reps with DB data for sets that have been logged
        for (int setInd = 0; setInd < currSetIndex; ++setInd) {
            currRepStr = String.valueOf(loggedSetReps.get(setInd));
            currReps.set(setInd, currRepStr);
        }

        setListAdapter = new work_trackerSetList_adapter(this, setNumbers, currReps, targetReps, weights);
        setListView.setAdapter(setListAdapter);
    }

    private void trackSet(int setIndex) {
        Intent setIntent = new Intent(work_trackerSetList.this, work_trackerSetDetail.class);
        setIntent.putExtra("eName", exerciseName);
        setIntent.putExtra("sessID", sessID);
        setIntent.putExtra("setNum", setIndex + 1);
        setIntent.putExtra("goalWeight", weights.get(setIndex));
        setIntent.putExtra("goalReps", targetReps.get(setIndex));
        startActivity(setIntent);
    }

}
