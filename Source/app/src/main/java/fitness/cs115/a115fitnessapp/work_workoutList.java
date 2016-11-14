package fitness.cs115.a115fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
 */

public class work_workoutList extends AppCompatActivity{

    private work_DBHelper work_dbh;
    String programName = "";
    String newWorkoutName = "";
    String[] userWorkouts;
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

        //Get database workout entries for the current program
        if (programName != null) {
            if (work_dbh.is_taken_prog_name(programName))
                workoutsInProgram = work_dbh.get_workouts_from_prog(programName);
            else
            {
                throw new IllegalArgumentException(
                        "Error: "+programName+" does not exist!");
            }
        }

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
        //  ListView
        //======================================================================================
        //Create the list.
        workoutListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workoutsInProgram);
        workoutListView = (ListView) findViewById(R.id.lv_workoutList);
        workoutListView.setAdapter(workoutListAdapter);
        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent openWorkout = new Intent(work_workoutList.this, work_exerciseList.class);
                String workoutName = String.valueOf(parent.getItemAtPosition(position));
                openWorkout.putExtra("wName", workoutName);
                startActivity(openWorkout);

            }
        });

        //======================================================================================
        // Workout Creation / Selection Dialog Box
        //======================================================================================
        // Shown when the Floating Action Button is clicked. lists all of the user's workouts
        //for the user to choose from to add to the current program.

        //for Adding New Workout: get database entries for all user workouts
        ArrayList<String> allWorkoutsList = work_dbh.get_user_workout_list();
        allWorkoutsList.add(0,"Create New Workout");
        userWorkouts = allWorkoutsList.toArray(new String[allWorkoutsList.size()]);

        final AlertDialog.Builder newWorkoutSelection = new AlertDialog.Builder(this);
        newWorkoutSelection.setTitle("Please choose a workout to add.");
        newWorkoutSelection.setItems(userWorkouts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //check which Workout was selected.
                if (selection_id == 0){
                    NewWorkout();
                } else {
                    //add selected workout to the user's workout list.
                    String workoutToAdd = userWorkouts[selection_id];
                    work_dbh.add_work_to_prog(programName,workoutToAdd);
                    regenerateWorkoutList();
                }
            }
        });

        //======================================================================================
        //  Floating Action Button
        //======================================================================================
        //Adds a new workout to the workout List.
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addWorkout);
        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newWorkoutSelection.show();
            }
        });


    }

    private void regenerateWorkoutList(){
        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        workoutsInProgram = work_dbh.get_user_workout_list();
        workoutListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workoutsInProgram);
//        workoutListView = (ListView) findViewById(R.id.lv_workoutList);
        workoutListView.setAdapter(workoutListAdapter);
//        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                Intent openWorkout = new Intent(work_workoutList.this, work_exerciseList.class);
//                String workoutName = String.valueOf(parent.getItemAtPosition(position));
//                openWorkout.putExtra("wName", workoutName);
//                startActivity(openWorkout);
//
//            }
//        });
    }

    //======================================================================================
    //  Start Activities
    //======================================================================================
    public void OpenWorkout(String progName){
        Intent openWorkout = new Intent(work_workoutList.this, work_workoutList.class);

        openWorkout.putExtra("pName", progName);
        startActivity(openWorkout);
    }

    public void NewWorkout(){
        Intent newWorkout = new Intent(work_workoutList.this, work_newWorkoutName.class);
        startActivity(newWorkout);
    }

}
