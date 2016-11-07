package fitness.cs115.a115fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Matthew on 10/24/16.
 * dialog box stuff modified from Henry's code
 */

public class meal_viewAllMeals extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrTblNames = new ArrayList<String>();
    AlertDialog.Builder programOptionMenu;
    private String selectedTable;
    private final boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_meals);
        listView = (ListView) findViewById(R.id.meals);
        listView.setLongClickable(true);


        //======================================================================================
        //  Dialog Boxes
        //======================================================================================
        // Program Deletion Dialog Box
        // - A deletion confirmation dialog box.
        final AlertDialog.Builder programOptionDelete = new AlertDialog.Builder(this);
        programOptionDelete.setTitle("Delete Program?");
        programOptionDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                //actually delete the meal here
                if (DEBUG) {
                    System.out.println("selectionId confirm delete: " + selection_id); //selection_id is meaningless
                    System.out.println("name of table to be dropped is: " + selectedTable);
                }
                SQLiteDatabase sdb;
                sdb = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                try {
                    sdb.execSQL("DROP TABLE IF EXISTS " + selectedTable);
                } catch (Exception e) {
                    sdb.execSQL("DROP TABLE IF EXISTS " + '[' + selectedTable + ']');
                }
                //now need to reload list view
                viewMealsInDatabase();
            }
        });
        programOptionDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                dialogInterface.cancel();
            }
        });

        // Program Options Menu Dialog Box
        // - Shown when an item in the ListView is long-clicked.
        final CharSequence programOptionsMenuOptions[] = new CharSequence[]{"Edit", "Delete"};
        programOptionMenu = new AlertDialog.Builder(this);
        programOptionMenu.setItems(programOptionsMenuOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selection_id) {
                if (selection_id == 0) {
                    //edit program
                    Intent intent = new Intent(meal_viewAllMeals.this, meal_editMeal.class);
                    intent.putExtra("TABLE", selectedTable);
                    startActivity(intent);

                } else {
                    //delete the program
                    programOptionDelete.show();
                }
            }
        });

        viewMealsInDatabase();

    }

    //displays the meals in the database in a listview
    private void viewMealsInDatabase() {
        arrTblNames.clear();
        SQLiteDatabase db = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        c.close();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrTblNames);

        listView.setAdapter(arrayAdapter);

        setUpClickListener();

    }

    private void setUpClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                programOptionMenu.show();
                selectedTable = arrTblNames.get(position);
                if (DEBUG) {
                    //    Toast.makeText(getApplicationContext(), "long click: " + position + " " + arrTblNames.get(position), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "long click: " + position + " " + arrTblNames.get(position) + ", " + selectedTable, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                selectedTable = arrTblNames.get(position);
                if (DEBUG) {
                    Toast.makeText(getApplicationContext(), "click: " + position + " " + arrTblNames.get(position), Toast.LENGTH_LONG).show();
                }
                //eat meal here


            }
        });
    }

}
