package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Henry on 10/17/2016.
 */

public class work_exerciseList extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_exercise_list);

        //Get the name of the workout and set it as the title.
        Intent intent = getIntent();
        String workoutName = intent.getExtras().getString("wName");
        setTitle(workoutName);


        //temporary list.
        String[] exercises = {"Squat", "Bench Press", "Deadlift", "Overhead Press"};



        //Create the list.
        //TODO: Use the custom adapter to add weight, sets, and reps to the lists.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter exerciseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerciseList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String exercise = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(work_exerciseList.this, "You selected " + exercise + ".\nWorkout tracker coming soon!", Toast.LENGTH_SHORT).show();

                /*
                Intent openWorkout = new Intent(work_workoutList.this, work_exerciseList.class);
                String workoutName = String.valueOf(parent.getItemAtPosition(position));
                openWorkout.putExtra("wName", workoutName);
                startActivity(openWorkout);
                */
            }
        });


    }

}
