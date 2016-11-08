package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Henry on 10/17/2016.
 */

public class work_workoutList extends AppCompatActivity{
    private work_DBHelper work_dbh = new work_DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_workout_list);

        //Get the name of the program and set it as the title.
        Intent intent = getIntent();
        String programName = intent.getExtras().getString("pName");
        setTitle(programName);

        //Get database entries
        ArrayList<String> workouts = new ArrayList<>();
        if (programName != null) {
            if (work_dbh.is_taken_prog_name(programName))
                workouts = work_dbh.get_workouts_from_prog(programName);
            else
            {
                throw new IllegalArgumentException(
                        "Error: "+programName+" does not exist!");
            }
        }

        //temporary list.
        //String[] workouts = {"Workout A", "Workout B"};

        //======================================================================================
        //  Buttons
        //======================================================================================
        //TODO: onClick: detect the next workout in line
        Button quickStart_btn = (Button) findViewById(R.id.btn_quickStart);
        quickStart_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_workoutList.this, work_tracker.class);

                //TODO: write function to determine what is the next workout
               //String workName = work_DBHelper.get_next_workout();
               // intent.putExtra("wName", workName);

                startActivity(intent);
            }
        });



        //======================================================================================
        //  ListView
        //======================================================================================
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
