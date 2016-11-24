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

import java.util.ArrayList;

/**
 * Created by Henry on 10/25/2016.
 * Edited by James Kennedy on 11/20/2016.
 */
//TODO: KNOWN BUG: crashes when adding several exercises to workout
public class work_createWorkout extends AppCompatActivity{
    private String workoutName;
    private ArrayList<String> exersInWorkout;
    private ArrayList<String> addExercisesList;
    private work_DBHelper workDBH;
    private AlertDialog.Builder addExerciseDialog;
    private ListAdapter exersInWorkoutAdapter;
    private ListView exersInWorkoutLV;
    private String addExercisesRawArray[];
    private AlertDialog.Builder addExerciseSelection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_workout);

        workDBH = new work_DBHelper(this);
        //Get the name of the workout and set it as the title.
        Intent intent = getIntent();
        workoutName = intent.getExtras().getString("wName");
        setTitle(workoutName);

        exersInWorkout = workDBH.get_exers_from_work(workoutName);
        //======================================================================================
        //  Dialog Boxes
        //======================================================================================
        // Program Deletion Dialog Box
        // - A deletion confirmation dialog box.
        final AlertDialog.Builder exerciseOptionDelete = new AlertDialog.Builder(this);
        exerciseOptionDelete.setTitle("Delete Exercise?");
        exerciseOptionDelete.setPositiveButton("Delete",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //TODO: Delete the exercise from the workout's exercise list.
                //delete the program
            }
        });
        exerciseOptionDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                dialogInterface.cancel();
            }
        });

        // Program Options Menu Dialog Box
        // - Shown when an item in the ListView is long-clicked.
//        final CharSequence exerciseOptionsMenuOptions[] = new CharSequence[] {"Edit", "Delete"};
//        final AlertDialog.Builder exerciseOptionMenu = new AlertDialog.Builder(this);
//        exerciseOptionMenu.setItems(exerciseOptionsMenuOptions, new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialogInterface, int selection_id) {
//                if (selection_id == 0){
//                    //editExercise();
//                } else {
//                    //delete the exercise
//                    exerciseOptionDelete.show();
//                }
//            }
//        });

        // New Exercise Dialog Box
        // - Shown when an item in the ListView is long-clicked.
        //final CharSequence addExerciseMenuOptions[] = new CharSequence[] {"Search Database", "Define New"};
//        addExerciseDialog = new AlertDialog.Builder(this);
//        addExerciseDialog.setItems(exerciseOptionsMenuOptions, new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialogInterface, int selection_id) {
//                if (selection_id == 0){
//                    //TODO: Search the database
//
//                } else {
//                    //TODO: Create new exercise
//                }
//            }
//        });

        //======================================================================================
        //  Add Exercise Dialog
        //======================================================================================
        //Adds an exercise to the workout exercise List.
        //available exercise items set in onResume()
        addExerciseDialog = new AlertDialog.Builder(this);


        //======================================================================================
        //  Floating Action Button
        //======================================================================================
        //Opens dialog to add an exercise to the workout
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addExercise);
        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addExerciseDialog.show();
            }
        });


        //======================================================================================
        //Exercises in Workout ListView
        //======================================================================================
        //Create the list of exercises currently in the program.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        exersInWorkoutAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exersInWorkout);
        exersInWorkoutLV = (ListView) findViewById(R.id.lv_workExerciseList);
        exersInWorkoutLV.setAdapter(exersInWorkoutAdapter);
        exersInWorkoutLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String exercise = String.valueOf(parent.getItemAtPosition(position));
                editExercise(exercise);
            }
        });
//        exersInWorkoutLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                exerciseOptionMenu.show();
//                return true;
//            }
//        });

        //======================================================================================
        // Exercise Creation / Selection Dialog Box
        //======================================================================================
        // Shown when the Floating Action Button is clicked. lists all of the user's exercises
        //for the user to choose from to add to the current program.
        addExerciseSelection = new AlertDialog.Builder(this);
        addExerciseSelection.setTitle("Please choose a exercise to add.");
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateWorkoutExercises();
        updateAddExercisesList();
    }


    private void updateWorkoutExercises () {//Create the list.
        exersInWorkout = workDBH.get_exers_from_work(workoutName);
        exersInWorkoutAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, exersInWorkout);
        exersInWorkoutLV.setAdapter(exersInWorkoutAdapter);
    }

    //update list of workouts that can be added to the program
    private void updateAddExercisesList(){
        addExercisesList = workDBH.get_user_exercise_list();
        addExercisesList.add(0,"Create New Exercise");
        //remove exercises that are already a part of the workout
        exersInWorkout = workDBH.get_exers_from_work(workoutName);
        addExercisesList.removeAll(exersInWorkout);
        addExercisesRawArray = addExercisesList.toArray(new String [addExercisesList.size()]);

        //bind addable exercises list to menu
        addExerciseDialog.setItems(addExercisesRawArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //check which Exercise was selected.
                if (selection_id == 0) {
                    newExercise();
                } else {
                    //add selected exercise to the user's exercise list.
                    String exerciseToAdd = addExercisesList.get(selection_id);
                    addExercise(exerciseToAdd);
                }
            }
        });
    }


    //==================================================================================
    //Start Activities
    //==================================================================================
    private void editExercise(String exercise) {
        Intent editExer = new Intent(work_createWorkout.this, work_createExercise.class);
        editExer.putExtra("wName", workoutName);
        editExer.putExtra("eName", exercise);
        startActivity(editExer);
    }

    private void newExercise(){
        Intent newExercise = new Intent(work_createWorkout.this, work_createExercise.class);
        newExercise.putExtra("wName", workoutName);
        startActivity(newExercise);
    }
    //adds exercise to current workout
    private void addExercise (String exerciseToAdd) {
        workDBH.add_exer_to_work(workoutName, exerciseToAdd);
        updateWorkoutExercises();
        updateAddExercisesList();
    }
}
