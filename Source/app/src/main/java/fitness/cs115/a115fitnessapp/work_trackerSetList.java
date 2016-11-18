package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Henry on 10/23/2016.
 */

public class work_trackerSetList extends AppCompatActivity{
    String exerciseName = "";
    String sessID = "";
    work_DBHelper work_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setlist);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker.
        Intent intent = getIntent();
        sessID = intent.getExtras().getString("sessID");
        exerciseName = intent.getExtras().getString("eName");
        setTitle(exerciseName);


        //TODO: grab information from session
        String[] setNumbers = {};
        String[] currReps = {};
        String[] targetReps = {};
        String[] weights = {};



        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //TODO: use the custom adapter to display exercise name, weight, and sets
        ListAdapter exerciseListAdapter = new work_trackerSetList_adapter(this, setNumbers, currReps, targetReps, weights);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                exerciseName = String.valueOf(parent.getItemAtPosition(position));
                Intent setIntent = new Intent(work_trackerSetList.this, work_trackerSetDetail.class);
                setIntent.putExtra("setName", exerciseName + " - Set " + String.valueOf(position));
                setIntent.putExtra("sessID", sessID);
                startActivity(setIntent);

            }
        });



    }


}
