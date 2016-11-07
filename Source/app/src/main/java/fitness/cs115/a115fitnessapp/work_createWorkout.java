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

/**
 * Created by Henry on 10/25/2016.
 */

public class work_createWorkout extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_workout);

        //TODO: Replace with database stuff.
        String[] exercises = {"Squat", "Bench Press", "Deadlift", "Overhead Press"};

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
                //TODO: Delete the workout from the program's workout list.
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
        final CharSequence exerciseOptionsMenuOptions[] = new CharSequence[] {"Edit", "Delete"};
        final AlertDialog.Builder exerciseOptionMenu = new AlertDialog.Builder(this);
        exerciseOptionMenu.setItems(exerciseOptionsMenuOptions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                if (selection_id == 0){
                    //TODO: Edit the selected exercise.
                    //edit exercise
                } else {
                    //delete the exercise
                    exerciseOptionDelete.show();
                }
            }
        });

        // New Exercise Dialog Box
        // - Shown when an item in the ListView is long-clicked.
        final CharSequence newExerciseMenuOptions[] = new CharSequence[] {"Search Database", "Define New"};
        final AlertDialog.Builder newExerciseMenu = new AlertDialog.Builder(this);
        newExerciseMenu.setItems(exerciseOptionsMenuOptions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                if (selection_id == 0){
                    //TODO: Search the database

                } else {
                    //TODO: Create new exercise
                }
            }
        });


        //======================================================================================
        //  Buttons
        //======================================================================================
        Button saveNameButton = (Button) findViewById(R.id.btn_saveName);
        saveNameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Save the name of the workout
            }
        });


        //======================================================================================
        //  Floating Action Button
        //======================================================================================
        //Adds a new program to the Program List.
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addExercise);
        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newExerciseMenu.show();
            }
        });


        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter exerciseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerciseList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //TODO: Edit exercise


            }
        });
        exerciseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                exerciseOptionMenu.show();
                return true;
            }
        });
    }
}
