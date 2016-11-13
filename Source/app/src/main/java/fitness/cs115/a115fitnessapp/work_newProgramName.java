package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This class controls the program name text entry activity
 */
public class work_newProgramName extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private work_DBHelper workDBH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_new_program_name);

        workDBH = new work_DBHelper(this);
        Button confirm_button = (Button) findViewById(R.id.confirm);
        confirm_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView prog_name_text_view = (TextView) findViewById(R.id.et_newProgramName);

                String pname = prog_name_text_view.getText().toString();
                if (pname.isEmpty()) {
                    prog_name_text_view.setError("Please enter a program name");
                    return;
                }
                if (workDBH.is_taken_prog_name(pname)) {
                    prog_name_text_view.setError("There is already a program with this name");
                    return;
                }
                //create entry in program table
                workDBH.create_program(pname);
                //go to create program activity
                OpenProgram(pname);

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
    public void OpenProgram(String progName){
        Intent openProgram = new Intent(work_newProgramName.this, work_workoutList.class);

        openProgram.putExtra("pName", progName);
        startActivity(openProgram);
    }


}
