package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
    //MAX_WEIGHT is the maximum allowed weight, and so on
    final int MAX_WEIGHT = 9999;
    final int MAX_REST_TIME = 999;
    final int MAX_SETS = 999;
    final int MAX_REPS = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_exercise);

        //Get the name of the workout this exercise should be added to.
        Intent intent = getIntent();
        workoutToAddTo = intent.getExtras().getString("wName");
        String eName = intent.getExtras().getString("eName");

        if (eName!= null && workDBH.can_add_exer_to_work(eName, workoutToAddTo)) {
            findViewById(R.id.et_exerciseName).setContentDescription(eName);
        }

        workDBH = new work_DBHelper(this);
        Button confirm_button = (Button) findViewById(R.id.confirm);
        confirm_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                //======================================================================================
                //retrieve user input values
                //======================================================================================
                TextView exerNameTextView = (TextView) findViewById(R.id.et_exerciseName);
                TextView setsTextView = (TextView) findViewById(R.id.et_sets);
                TextView repsTextView = (TextView) findViewById(R.id.et_reps);
                TextView startWeightTextView = (TextView) findViewById(R.id.et_startWeight);
                TextView incrWeightTextView = (TextView) findViewById(R.id.et_incrWeight);
                TextView restTimeTextView = (TextView) findViewById(R.id.et_restTime);

                String exerName= exerNameTextView.getText().toString();
                int sets = Integer.parseInt(setsTextView.getText().toString());
                int reps = Integer.parseInt(repsTextView.getText().toString());
                int startWeight = Integer.parseInt(startWeightTextView.getText().toString());
                int incrWeight = Integer.parseInt(incrWeightTextView.getText().toString());
                int restTime = Integer.parseInt(restTimeTextView.getText().toString());

                //======================================================================================
                //test for valid inputs
                //======================================================================================
                if (exerName.isEmpty()) {
                    exerNameTextView.setError("Please enter an exercise name");
                    return;
                }

                if (startWeight< 0 || startWeight > MAX_WEIGHT ) {
                    startWeightTextView.setError("Please enter a start weight between 0 and "+MAX_WEIGHT);
                    return;
                }
                if (incrWeight< 0 || incrWeight > MAX_WEIGHT ) {
                    incrWeightTextView.setError("Please enter an increment weight between 0 and "+MAX_WEIGHT);
                    return;
                }
                if (sets < 0 || sets  > MAX_SETS ) {
                    setsTextView.setError("Please enter a set number between 0 and "+MAX_SETS);
                    return;
                }
                if (reps < 0 || reps  > MAX_REPS ) {
                    repsTextView.setError("Please enter a rep number between 0 and "+MAX_REPS);
                    return;
                }
                if (restTime < 0 || restTime  > MAX_REST_TIME ) {
                    restTimeTextView.setError("Please enter a rest time between 0 and "+MAX_REST_TIME);
                    return;
                }
                if ( !workDBH.can_add_exer_to_work(exerName,workoutToAddTo)) {
                    exerNameTextView.setError("There is already an exercise with this name");
                    return;
                }
                else //if this point is reached, all user input fields are valid
                {
                    //// TODO: 11/15/2016 add exercise to exercise table once implemented
                    //add the new exercise to the intended workout

                    workDBH.add_exer_to_work(workoutToAddTo, exerName,
                            sets, reps, startWeight, incrWeight, restTime);

                    //return to the previous exercise list
                    returnToWorkout(workoutToAddTo);
                }
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

    //======================================================================================
    //  Start Activities
    //======================================================================================
    public void returnToWorkout(String workName){
        Intent openWorkout = new Intent(work_createExercise.this, work_exerciseList.class);

        openWorkout.putExtra("wName", workName);
        startActivity(openWorkout);
    }


}
