package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Henry on 10/17/2016.
 */

public class work_workoutList extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_workout_list);

        //Get the name of the program and set it as the title.
        Intent intent = getIntent();
        String programName = intent.getExtras().getString("pName");
        setTitle(programName);

        //TODO: Get database entries
        //temporary list.
        String[] workouts = {"Workout A", "Workout B"};



        //Create the list.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter workoutListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workouts);
        ListView workoutListView = (ListView) findViewById(R.id.lv_workoutList);
        workoutListView.setAdapter(workoutListAdapter);
        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent openWorkout = new Intent(work_workoutList.this, work_exerciseList.class);
                String workoutName = String.valueOf(parent.getItemAtPosition(position));
                openWorkout.putExtra("wName", workoutName);
                startActivity(openWorkout);

            }
        });

    }

}
