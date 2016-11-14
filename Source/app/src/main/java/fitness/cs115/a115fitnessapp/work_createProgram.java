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
 */

public class work_createProgram extends AppCompatActivity{
    private work_DBHelper workDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_program);

        workDBH= new work_DBHelper(this);

        //TODO: check if program name is not taken

        //TODO: Replace with database stuff.
        String[] defaultWorkouts = {"Workout A", "Workout B"};

        //======================================================================================
        //  Dialog Boxes
        //======================================================================================
        // Program Deletion Dialog Box
        // - A deletion confirmation dialog box.
        final AlertDialog.Builder workoutOptionDelete = new AlertDialog.Builder(this);
        workoutOptionDelete.setTitle("Delete Workout?");
        workoutOptionDelete.setPositiveButton("Delete",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //TODO: Delete the workout from the program's workout list.
                //delete the program
            }
        });
        workoutOptionDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                dialogInterface.cancel();
            }
        });

        // Program Options Menu Dialog Box
        // - Shown when an item in the ListView is long-clicked.
        final CharSequence workoutOptionsMenuOptions[] = new CharSequence[] {"Edit", "Delete"};
        final AlertDialog.Builder workoutOptionMenu = new AlertDialog.Builder(this);
        workoutOptionMenu.setItems(workoutOptionsMenuOptions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                if (selection_id == 0){
                    //TODO: Edit the selected workout.
                    //edit program
                } else {
                    //delete the program
                    workoutOptionDelete.show();
                }
            }
        });






        //======================================================================================
        //  Floating Action Button
        //======================================================================================
        //Adds a new program to the Program List.
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addWorkout);
        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent newWorkout = new Intent(work_createProgram.this, work_newWorkoutName.class);
                startActivity(newWorkout);
            }
        });


        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list of default workout that the user can add to their program.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter workoutListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, defaultWorkouts);
        ListView workoutListView = (ListView) findViewById(R.id.lv_workoutList);
        workoutListView.setAdapter(workoutListAdapter);
        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //TODO: Edit workout

                //Intent openWorkout = new Intent(work_createProgram.this, work_exerciseList.class);
                //String workoutName = String.valueOf(parent.getItemAtPosition(position));
                //openWorkout.putExtra("wName", workoutName);
                //startActivity(openWorkout);

            }
        });
        workoutListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                workoutOptionMenu.show();
                return true;
            }
        });
    }
}
