package fitness.cs115.a115fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
 * Edited by James Kennedy on 10/20/2016.
 */

public class work_programList extends AppCompatActivity{
    String programName = "";
    work_DBHelper work_db;
    private ListAdapter programListAdapter;
    private ListView programListView;
    private ArrayList<String> programs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_program_list);

        work_db = new work_DBHelper(this);

        //check for user programs. If there aren't any, bring up a popup menu prompting users
        //to choose "Create New" or a pre-included beginner program. Replace with database programs.
        final CharSequence defaultPrograms[] = new CharSequence[] {"Create New Program", "Starting Strength",
                "StrongLifts", "Greyskull LP", "PPL for Beginners", "Ice Cream Fitness"};

        //get program list from database
        programs = work_db.get_user_program_list();


        //======================================================================================
        //  Dialog Boxes
        //======================================================================================
        // Program Deletion Dialog Box
        // - A deletion confirmation dialog box.
        final AlertDialog.Builder programOptionDelete = new AlertDialog.Builder(this);
        programOptionDelete.setTitle("Delete Program?");
        programOptionDelete.setPositiveButton("Delete",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //TODO: Delete the program from the user's program list.
                //work_db.delete_program();
            }
        });
        programOptionDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                dialogInterface.cancel();
            }
        });

        // Program Options Menu Dialog Box
        // - Shown when an item in the ListView is long-clicked.
        final CharSequence programOptionsMenuOptions[] = new CharSequence[] {"Edit", "Delete"};
        final AlertDialog.Builder programOptionMenu = new AlertDialog.Builder(this);
        programOptionMenu.setItems(programOptionsMenuOptions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                if (selection_id == 0){
                    //TODO: Edit the user's selected program.
                    //edit program
                } else {
                    //delete the program
                    programOptionDelete.show();
                }
            }
        });

        // Program Creation / Selection Dialog Box
        // - Shown when the Floating Action Button is clicked.
        final AlertDialog.Builder newProgramSelection = new AlertDialog.Builder(this);
        newProgramSelection.setTitle("Please choose a workout program.");
        newProgramSelection.setItems(defaultPrograms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //check which program was selected.
                if (selection_id == 0){
                    NewProgram();
                } else {
                    //TODO: add selected program to the user's program list.
                    //Currently will only open the program.
                    programName = defaultPrograms[selection_id].toString();
                    OpenProgram(programName);

                }
            }
        });

        //======================================================================================
        //  Floating Action Button
        //======================================================================================
        //Adds a new program to the Program List.
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_addProgram);
        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newProgramSelection.show();
            }
        });

        //======================================================================================
        //  ListView
        //======================================================================================
        //Create the list of user's programs.
        //TODO: maybe show frequency of the program within the list.
        programListView = (ListView) findViewById(R.id.lv_programList);
        programListView.setLongClickable(true);
        programListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                programName = String.valueOf(parent.getItemAtPosition(position));
                OpenProgram(programName);

            }
        });
        programListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                programOptionMenu.show();
                return true;
            }
        });


        //TODO: add support for database.
        //Show dialog for selecting a new program.
        //if (programs.length == 0){
        //    newProgramSelection.show();
        //}
        //Uncomment to see the dialog box.
        //newProgramSelection.show();



    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadProgramList();
    }
    //======================================================================================
    //  Start Activities
    //======================================================================================
    public void OpenProgram(String progName){
        Intent openProgram = new Intent(work_programList.this, work_workoutList.class);

        openProgram.putExtra("pName", progName);
        startActivity(openProgram);
    }

    public void NewProgram(){
        Intent newProgram = new Intent(work_programList.this, work_newProgramName.class);
        startActivity(newProgram);
    }

    private void reloadProgramList () {
        programs = work_db.get_user_program_list();
        programListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, programs);
        programListView.setAdapter(programListAdapter);
    }
}
