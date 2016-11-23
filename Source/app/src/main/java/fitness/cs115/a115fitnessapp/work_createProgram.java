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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Henry on 10/25/2016.
 * Edited by James Kennedy on 11/20/2016.
 */
public class work_createProgram extends AppCompatActivity {
    private work_DBHelper workDBH;
    private String programName;
    private ArrayList<String> workoutsInProgram;
    private ArrayList<String> addWorkoutsList;
    ListAdapter programWorkoutsAdapter;
    ListView programWorkoutsLV;
    AlertDialog.Builder addWorkoutSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_program);

        workDBH = new work_DBHelper(this);

        //Get the name of the program and set it as the title.
        Intent intent = getIntent();
        programName = intent.getExtras().getString("pName");
        setTitle(programName);

        //invariant: program name must already exist in work DB
        if (programName == null || !workDBH.is_taken_prog_name(programName)) {
            throw new IllegalArgumentException("Error: "+programName+" does not exist!");
        }
        
        //======================================================================================
        //  Dialog Boxes
        //======================================================================================
        // Program Deletion Dialog Box
        // - A deletion confirmation dialog box.
        final AlertDialog.Builder workoutOptionDelete = new AlertDialog.Builder(this);
        workoutOptionDelete.setTitle("Delete Workout?");
        workoutOptionDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //TODO: Delete the workout from the program's workout list.
                //delete the program
            }
        });
        workoutOptionDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                dialogInterface.cancel();
            }
        });

        // Program Options Menu Dialog Box
        // - Shown when an item in the ListView is long-clicked.
        final CharSequence workoutOptionsMenuOptions[] = new CharSequence[]{"Edit", "Delete"};
        final AlertDialog.Builder workoutOptionMenu = new AlertDialog.Builder(this);
        workoutOptionMenu.setItems(workoutOptionsMenuOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                if (selection_id == 0) {
                    //TODO: editWorkout();
                } else {
                    //delete the program
                    workoutOptionDelete.show();
                }
            }
        });

        //======================================================================================
        //  Program Workouts ListView
        //======================================================================================
        //Create the list of workouts that are currently a part of the program.
        programWorkoutsLV = (ListView) findViewById(R.id.lv_programWorkoutList);
        programWorkoutsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String workoutName = String.valueOf(parent.getItemAtPosition(position));
                //TODO: Edit workout
                Toast.makeText(getBaseContext(), workoutName  +" edit pressed",Toast.LENGTH_LONG);
                editWorkout(workoutName);
                
            }
        });
        programWorkoutsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                workoutOptionMenu.show();
                return true;
            }
        });
        
        //======================================================================================
        // Workout Creation / Selection Dialog Box
        //======================================================================================
        // Shown when the Floating Action Button is clicked. lists all of the user's workouts
        //for the user to choose from to add to the current program.
        //for Adding New Workout: get database entries for all user workouts
        addWorkoutSelection = new AlertDialog.Builder(this);
        addWorkoutSelection.setTitle("Please choose a workout to add.");

        //======================================================================================
        //  Add Workout Floating Action Button
        //======================================================================================
        //Adds a workout to the current Program.
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addWorkout);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkoutSelection.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgramWorkouts();
        updateAddWorkoutsList();
    }

    private void updateProgramWorkouts () {//Create the list.
        workoutsInProgram = workDBH.get_workouts_in_prog(programName);
        programWorkoutsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, workoutsInProgram);
        programWorkoutsLV.setAdapter(programWorkoutsAdapter);
    }
    
    //update list of workouts that can be added to the program
    private void updateAddWorkoutsList(){
        addWorkoutsList = workDBH.get_user_workout_list();
        addWorkoutsList.add(0,"Create New Workout");

        //remove workouts that are already a part of the program
        workoutsInProgram = workDBH.get_workouts_in_prog(programName);
        addWorkoutsList.removeAll(workoutsInProgram);
        addWorkoutSelection.setItems(addWorkoutsList.toArray(new String [addWorkoutsList.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection_id) {
                        //check which Workout was selected.
                        if (selection_id == 0) {
                            newWorkout();
                        } else {
                            //add selected workout to the user's workout list.
                            String workoutToAdd = addWorkoutsList.get(selection_id);
                            addWorkout(workoutToAdd);
                        }
                    }
        });
    }
    
    //==================================================================================
    //Start Activities
    //==================================================================================
    private void editWorkout(String workout) {
        Intent newWorkout = new Intent(work_createProgram.this, work_createWorkout.class);
        newWorkout.putExtra("wName", workout);
        startActivity(newWorkout);
    }

    //adds workout to current program
    private void addWorkout (String workoutToAdd) {
        workDBH.add_work_to_prog(programName, workoutToAdd);
        updateProgramWorkouts();
        updateAddWorkoutsList();
    }
    private void newWorkout(){
        Intent newWorkout = new Intent(work_createProgram.this, work_newWorkoutName.class);
        newWorkout.putExtra("pName", programName);
        startActivity(newWorkout);
    }
}
