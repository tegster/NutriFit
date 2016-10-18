package fitness.cs115.a115fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Henry on 10/10/2016.
 */

public class work_programList extends AppCompatActivity{
    String programName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_program_list);
        //programs.add("test");

        //check for user programs. If there aren't any, bring up a popup menu prompting users
        //to choose "Create New" or a pre-included beginner program. Replace with database programs.
        final CharSequence listprograms[] = new CharSequence[] {"Create New Program", "Starting Strength",
                "StrongLifts", "Greyskull LP", "PPL for Beginners", "Ice Cream Fitness"};

        //temporary list. replace with user selected programs.
        String[] programs = {"Brosplits", "Stronglifts", "Starting Strength", "Greyskull LP", "PPL for Beginners",
                "Ice Cream Fitness", "Arnold's Golden Six", "5/3/1", "PHUL", "Madcows", "Texas Method", "PHAT", "Bodyweight"};


        //Create dialog box for custom program name entry.
        final AlertDialog.Builder newProgramNameEntry = new AlertDialog.Builder(this);
        newProgramNameEntry.setTitle("Create New Program");
        final EditText editNameEntry = new EditText(this);
        editNameEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        newProgramNameEntry.setView(editNameEntry);
        newProgramNameEntry.setPositiveButton("Create", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                programName = editNameEntry.getText().toString();
                OpenProgram(programName);
            }
        });
        newProgramNameEntry.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
               dialogInterface.cancel();
           }
        });



        //create dialog box for new program selection.
        AlertDialog.Builder newProgramSelection = new AlertDialog.Builder(this);
        newProgramSelection.setTitle("Please choose a workout program.");
        newProgramSelection.setItems(listprograms, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection_id) {
                        //check which program was selected.
                        if (selection_id == 0){
                            newProgramNameEntry.show();
                        } else {
                            //TODO: add selected program to the program list.
                            //Currently will open the program.
                            programName = listprograms[selection_id].toString();
                            OpenProgram(programName);

                        }
                    }
        });



        //TODO: add support for database.
        //Show dialog for selecting a new program.
        if (programs.length == 0){
            newProgramSelection.show();
        }
        //Uncomment to see the dialog box.
        //newProgramSelection.show();


        //Create the list.
        //ListAdapter programListAdapter = new work_programList_adapter(this, programs);
        ListAdapter programListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, programs);
        ListView programListView = (ListView) findViewById(R.id.lv_programList);
        programListView.setAdapter(programListAdapter);
        programListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                programName = String.valueOf(parent.getItemAtPosition(position));
                OpenProgram(programName);

            }
        });

    }


    public void OpenProgram(String progName){
        Intent openProgram = new Intent(work_programList.this, work_workoutList.class);
        openProgram.putExtra("pName", progName);
        startActivity(openProgram);
    }

}
