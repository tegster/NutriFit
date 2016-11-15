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
 * Created by Henry on 10/23/2016.
 */

public class work_tracker extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker);

        //TODO: grab exercises
        String[] exercises = {};


        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list.
        //TODO: maybe show frequency of the program within the list.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter exerciseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        ListView exerciseListView = (ListView) findViewById(R.id.lv_exerList);
        exerciseListView.setAdapter(exerciseListAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //programName = String.valueOf(parent.getItemAtPosition(position));
                //OpenProgram(programName);

            }
        });




    }


}
