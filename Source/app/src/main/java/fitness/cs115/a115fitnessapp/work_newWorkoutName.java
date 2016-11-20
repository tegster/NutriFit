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
 * This class controls the Workout name text entry activity
 */
public class work_newWorkoutName extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private work_DBHelper workDBH;
    private String programToAddTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_new_workout_name);

        //Get the name of the program this workout should be added to.
        Intent intent = getIntent();
        programToAddTo = intent.getExtras().getString("pName");


        workDBH = new work_DBHelper(this);
        Button confirm_button = (Button) findViewById(R.id.confirm);
        confirm_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView workNameTextView = (TextView) findViewById(R.id.et_newWorkoutName);

                String wName= workNameTextView.getText().toString();
                if (wName.isEmpty()) {
                    workNameTextView.setError("Please enter a workout name");
                    return;
                }
                if (workDBH.is_taken_work_name(wName)) {
                    workNameTextView.setError("There is already a workout with this name");
                    return;
                }
                //create entry in Workout table and add the new workout to the intended program
                workDBH.create_workout(wName);
                workDBH.add_work_to_prog(programToAddTo,wName);

                //go to create workout activity
                OpenWorkout(wName);

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
                .setName("work_newProgramName Page") // TODO: Define a title for the content shown.
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
    public void OpenWorkout(String workName){
        Intent openWorkout = new Intent(work_newWorkoutName.this, work_exerciseList.class);

        openWorkout.putExtra("wName", workName);
        startActivity(openWorkout);
    }


}
