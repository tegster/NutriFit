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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Henry
 * @since 10/17/2016
 * @version ${VERSION}
 */

public class work_exerciseList extends AppCompatActivity{
    private work_DBHelper work_dbh;
    String[] userExercises;
    private ArrayList<String> exersInWorkout;
    private ListAdapter exerciseListAdapter;
    private ListView exerciseListView;
    private String workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_exercise_list);
        work_dbh = new work_DBHelper(this);

        //Get the name of the workout and set it as the title.
        Intent intent = getIntent();
        workoutName = intent.getExtras().getString("wName");
        setTitle(workoutName);

        //Get database exercise entries for the current workout
        exersInWorkout = work_dbh.get_exers_from_work(workoutName);

        //Create the list.
        //TODO: Use the custom adapter to add weight, sets, and reps to the lists.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        exerciseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exersInWorkout);
        exerciseListView = (ListView) findViewById(R.id.lv_exerciseList);
        exerciseListView.setAdapter(exerciseListAdapter);
        //TODO: remove the clickability of exercises in the list
//        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                String exercise = String.valueOf(parent.getItemAtPosition(position));
//                startExercise(exercise);
//            }
//        });

        //======================================================================================
        // Exercise Creation / Selection Dialog Box
        //======================================================================================
        // Shown when the Floating Action Button is clicked. lists all of the user's exercises
        //for the user to choose from to add to the current program.

        //for Adding New Exercise: get database entries for all user exercises
        ArrayList<String> addExercisesList = work_dbh.get_user_exercise_list();
        //remove exercises
        addExercisesList.removeAll(exersInWorkout);
        addExercisesList.add(0,"Create New Exercise");
        userExercises = addExercisesList.toArray(new String[addExercisesList.size()]);

        final AlertDialog.Builder newExerciseSelection = new AlertDialog.Builder(this);
        newExerciseSelection.setTitle("Please choose a exercise to add.");
        newExerciseSelection.setItems(userExercises, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //check which Exercise was selected.
                if (selection_id == 0){
                    newExercise();
                } else {
                    //add selected exercise to the user's exercise list.
                    String exerciseToAdd = userExercises[selection_id];
                    addExercise(exerciseToAdd);
                }
            }
        });

        //======================================================================================
        //  Buttons
        //======================================================================================
        //TODO: onClick: detect the next workout in line
        Button startTrackerBtn = (Button) findViewById(R.id.btn_trackerStart);
        startTrackerBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_exerciseList.this, work_tracker.class);
                intent.putExtra("wName", workoutName);
                startActivity(intent);
            }
        });

        //======================================================================================
        //  Floating Action Button (TODO:DOESNT BELONG HERE)
        //======================================================================================
//        //Adds a new exercise to the exercise List.
//        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addExercise);
//        fabAdd.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                newExerciseSelection.show();
//            }
//        });


    }

    private void regenerateExerciseList(){
        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        exersInWorkout = work_dbh.get_exers_from_work(workoutName);
        exerciseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exersInWorkout);
//        exerciseListView = (ListView) findViewById(R.id.lv_exerciseList);
        exerciseListView.setAdapter(exerciseListAdapter);
//        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                Intent openExercise = new Intent(work_exerciseList.this, work_exerciseList.class);
//                String exerciseName = String.valueOf(parent.getItemAtPosition(position));
//                openExercise.putExtra("wName", exerciseName);
//                startActivity(openExercise);
//
//            }
//        });
    }

    //======================================================================================
    //  Start Activities
    //======================================================================================
    public void addExercise(String exerName){
        //// TODO: 11/14/2016 figure out where to go when an exercise is pressed
        Intent openExercise = new Intent(work_exerciseList.this, work_createExercise.class);
        openExercise.putExtra("wName", workoutName);
        openExercise.putExtra("eName", exerName);
        startActivity(openExercise);

    }

    //start workout tracker
    public void startTracker(){
        Intent openExercise = new Intent(work_exerciseList.this, work_tracker.class);
        openExercise.putExtra("wName", workoutName);
        startActivity(openExercise);

    }

    public void newExercise(){
        Intent newExercise = new Intent(work_exerciseList.this, work_createExercise.class);
        newExercise.putExtra("wName", workoutName);
        startActivity(newExercise);
    }


}
