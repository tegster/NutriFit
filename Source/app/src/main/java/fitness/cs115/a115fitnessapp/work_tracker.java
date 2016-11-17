package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Henry on 10/23/2016.
 */

public class work_tracker extends AppCompatActivity{
    String exerciseName = "";
    String sessID = "";
    String workoutName = "";
    work_DBHelper work_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker);

        work_db = new work_DBHelper(this);

        //Get parameters that are passed into the tracker.
        Intent intent = getIntent();
        sessID = intent.getExtras().getString("sessID");
        workoutName = intent.getExtras().getString("wName");
        setTitle(workoutName);


        //TODO: grab exercises
        ArrayList<String> userworkAL = work_db.get_exers_from_work(workoutName);
        String[] exercises = userworkAL.toArray(new String[userworkAL.size()]);




        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //TODO: use the custom adapter to display exercise name, weight, and sets
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter exerciseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                exerciseName = String.valueOf(parent.getItemAtPosition(position));
                Intent setIntent = new Intent(work_tracker.this, work_trackerSetList.class);
                setIntent.putExtra("eName", exerciseName);
                setIntent.putExtra("sessID", sessID);
                startActivity(setIntent);

            }
        });




    }


}
