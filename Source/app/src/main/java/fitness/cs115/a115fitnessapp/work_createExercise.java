package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;

/**
 * This class controls the Exercise Creation activity
 *
 * @author James Kennedy
 */
public class work_createExercise extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private work_DBHelper workDBH;
    private String workoutToAddTo;
    private String exerName;
    private boolean isEditingExistingExer;
    private TextView exerNameTextView;
    private TextView setsTextView;
    private TextView repsTextView;
    private TextView startWeightTextView;
    private TextView incrWeightTextView;
    private TextView restTimeTextView;
    //MAX_WEIGHT is the maximum allowed weight, and so on
    final int MAX_WEIGHT = 9999;
    final int MAX_REST_TIME = 999;
    final int MAX_SETS = 999;
    final int MAX_REPS = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_exercise);
        workDBH = new work_DBHelper(this);

        //Get the name of the workout this exercise should be added to.
        Intent intent = getIntent();
        workoutToAddTo = intent.getExtras().getString("wName");

        //if editing an existing exercise, exercise name will also be passed as an extra
        isEditingExistingExer = false;
        if (intent.hasExtra("eName")) {
            isEditingExistingExer = true;
            exerName = intent.getExtras().getString("eName");
            //invariant: exercise to edit must already belong to the workout
            if (!workDBH.get_exers_from_work(workoutToAddTo).contains(exerName)) {
                throw new IllegalArgumentException("Error editing exercise: \""
                        +exerName+"\" is not a part of \""+workoutToAddTo+"\"");
            }
            findViewById(R.id.et_exerciseName).setContentDescription(exerName);
        }

        exerNameTextView = (TextView) findViewById(R.id.et_exerciseName);
        setsTextView = (TextView) findViewById(R.id.et_sets);
        repsTextView = (TextView) findViewById(R.id.et_reps);
        startWeightTextView = (TextView) findViewById(R.id.et_startWeight);
        incrWeightTextView = (TextView) findViewById(R.id.et_incrWeight);
        restTimeTextView = (TextView) findViewById(R.id.et_restTime);

        //load values stored in DB if editing existing exercise
        if(isEditingExistingExer){
            HashMap<String, Integer> existingVals = workDBH.get_exer_index_entry(exerName);
            exerNameTextView.setText(exerName);
            repsTextView.setText(existingVals.get(work_DBHelper.EXER_INDEX_REPS));
            startWeightTextView.setText(existingVals.get(work_DBHelper.EXER_INDEX_START_WEIGHT));
            incrWeightTextView.setText(existingVals.get(work_DBHelper.EXER_INDEX_INC_WEIGHT));
            restTimeTextView.setText(existingVals.get(work_DBHelper.EXER_INDEX_REST_TIME));
            setsTextView.setText(existingVals.get(work_DBHelper.EXER_INDEX_GOAL_SETS));
        }

        Button confirm_button = (Button) findViewById(R.id.confirm);
        confirm_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //======================================================================================
                //retrieve user input values
                //======================================================================================
                String inputExerName = exerNameTextView.getText().toString();
                int sets = Integer.parseInt(setsTextView.getText().toString());
                int reps = Integer.parseInt(repsTextView.getText().toString());
                int startWeight = Integer.parseInt(startWeightTextView.getText().toString());
                int incrWeight = Integer.parseInt(incrWeightTextView.getText().toString());
                int restTime = Integer.parseInt(restTimeTextView.getText().toString());

                //invariants: user input must be valid
                if ( !validateUserInput(inputExerName, sets, reps, startWeight, incrWeight, restTime)) {
                    //user input an invalid entry. validateUserInput
                    return;
                }
                //add exercise to exercise table and to workout if its a new exercise
                if (!isEditingExistingExer) {
                    workDBH.create_exercise(inputExerName, sets, reps, startWeight, incrWeight, restTime);
                    workDBH.add_exer_to_work(workoutToAddTo, inputExerName);
                } else {
                    //workout already exists and is being edited
                    //TODO: edit_exercise function
                    //workDBH.edit_exercise(exerName, inputExerName, sets, reps, startWeight, incrWeight, restTime);
                }
                exerName = inputExerName;

                //return to the previous exercise list
                returnToWorkout(workoutToAddTo);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("work_createExercise Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private boolean validateUserInput(String exercise, int sets, int reps,
                                     int startWeight, int incrWeight, int restTime){
        //======================================================================================
        //test for valid inputs
        //======================================================================================
        if (exercise.isEmpty()) {
            exerNameTextView.setError("Please enter an exercise name");
            return false;
        }

        if (startWeight< 0 || startWeight > MAX_WEIGHT ) {
            startWeightTextView.setError("Please enter a start weight between 0 and "+MAX_WEIGHT);
            return false;
        }
        if (incrWeight< 0 || incrWeight > MAX_WEIGHT ) {
            incrWeightTextView.setError("Please enter an increment weight between 0 and "+MAX_WEIGHT);
            return false;
        }
        if (sets < 0 || sets  > MAX_SETS ) {
            setsTextView.setError("Please enter a set number between 0 and "+MAX_SETS);
            return false;
        }
        if (reps < 0 || reps  > MAX_REPS ) {
            repsTextView.setError("Please enter a rep number between 0 and "+MAX_REPS);
            return false;
        }
        if (restTime < 0 || restTime  > MAX_REST_TIME ) {
            restTimeTextView.setError("Please enter a rest time between 0 and "+MAX_REST_TIME);
            return false;
        }
        if (!isEditingExistingExer && !workDBH.can_add_exer_to_work(exercise,workoutToAddTo)) {
            exerNameTextView.setError("There is already an exercise with this name");
            return false;
        }
        return true;
    }

    //======================================================================================
    //  Start Activities
    //======================================================================================
    public void returnToWorkout(String workName){
        Intent openWorkout = new Intent(work_createExercise.this, work_createWorkout.class);
        openWorkout.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        openWorkout.putExtra("wName", workName);
        startActivity(openWorkout);
    }


}
