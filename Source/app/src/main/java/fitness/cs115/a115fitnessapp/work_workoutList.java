package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Henry on 10/17/2016.
 * Edited by James on 11/14/2016.
 */

public class work_workoutList extends AppCompatActivity{

    private work_DBHelper work_dbh;
    String programName = "";
    ArrayList<String> workoutsInProgram;
    ListAdapter workoutListAdapter;
    ListView workoutListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_workout_list);

        //Get the name of the program and set it as the title.
        Intent intent = getIntent();
        programName = intent.getExtras().getString("pName");
        setTitle(programName);

        work_dbh = new work_DBHelper(this);

        //invariant: program name must already exist in work DB
        if (programName == null || !work_dbh.is_taken_prog_name(programName)) {
            throw new IllegalArgumentException("Error: "+programName+" does not exist!");
        }

        //Get database workout entries for the current program
        workoutsInProgram = work_dbh.get_workouts_in_prog(programName);
        //for Quickstart Btn: get name of the next workout in the current program
        //======================================================================================
        //  Buttons
        //======================================================================================
        //TODO: onClick: detect the next workout in line
        Button quickStart_btn = (Button) findViewById(R.id.btn_quickStart);
        quickStart_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_workoutList.this, work_tracker.class);

                String quickStartWorkName = work_dbh.get_next_workout(programName);
                intent.putExtra("wName", quickStartWorkName);

                startActivity(intent);
            }
        });

        //======================================================================================
        //  ListView of Workouts in Program
        //======================================================================================
        //Create the list.
        workoutListView = (ListView) findViewById(R.id.lv_programWorkoutList);
        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            String workoutName = String.valueOf(parent.getItemAtPosition(position));
            OpenWorkout(workoutName);
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        updateProgramWorkoutList();
    }
    //======================================================================================
    //  update the Workouts in Program ListView and Adapter
    //======================================================================================
    private void updateProgramWorkoutList(){
        //Create the list.
        workoutsInProgram = work_dbh.get_workouts_in_prog(programName);
        workoutListAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, workoutsInProgram);
        workoutListView.setAdapter(workoutListAdapter);
    }

    //======================================================================================
    //  Start Activities
    //======================================================================================
    public void OpenWorkout(String workName){
        Intent openWorkout = new Intent(work_workoutList.this, work_exerciseList.class);
        openWorkout.putExtra("wName", workName);
        startActivity(openWorkout);
    }
}
