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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private final boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_meals);
        listView = (ListView) findViewById(R.id.meals);
        listView.setLongClickable(true);

        if (DEBUG) {
            Calendar calendar = Calendar.getInstance();
            String date = Integer.toString(calendar.get(Calendar.MONTH)) + '/' + Integer.toString(calendar.get(Calendar.DATE)) + '/' + Integer.toString(calendar.get(Calendar.YEAR));
            System.out.println("testing date is: " + date);
            System.out.println("length of date is: " + date.length());
        }

        //======================================================================================
        //  Dialog Boxes
        //======================================================================================
        // Once the list view is long clicked, the
        final AlertDialog.Builder programOptionDelete = new AlertDialog.Builder(this);
        programOptionDelete.setTitle("Delete Meal?");
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
        //once long clicked to delete, this cancel will abort the delete
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
                if (!c.getString((c.getColumnIndex("name"))).equals("android_metadata")) {
                    if (!arrTblNames.contains(c.getString(c.getColumnIndex("name")))) { //paranoid error checking, shouldn't be necessary
                        arrTblNames.add(c.getString(c.getColumnIndex("name")));
                    }
                }
                c.moveToNext();
            }
        }
        c.close();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
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
                    //    Toast.makeText(getApplicationContext(), "long click: " + position + " " + mealNames.get(position), Toast.LENGTH_LONG).show();
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
                eatMeal();
                Toast.makeText(meal_viewAllMeals.this, "You have eaten meal: " + arrTblNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //eats the meal by adding all of the meals foods into the eaten meal database under today's date
    private void eatMeal() {
        //selectedTable contains the name of the meal table being eaten
        SQLiteDatabase mDatabase = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        try {
            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + selectedTable + " " +
                            "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                            "satfat DECIMAL(5,1), cholestrol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                            "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
            );
        } catch (Exception e) {
            selectedTable = "[" + selectedTable + "]";
            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + selectedTable + " " +
                            "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                            "satfat DECIMAL(5,1), cholestrol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                            "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
            );
        }

        //cursor needs to iterate through mealdb, not food db. since the data we need is stored in meal
        SQLiteDatabase mealdb = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor res = mealdb.rawQuery("select * from " + selectedTable, null);
        res.moveToFirst();

        //set up eatFood db
        Calendar calendar = Calendar.getInstance();
        //surround date with '[' ']' like in create meal to avoid weird issues. This is the actual name for each table
        SQLiteDatabase eatFood = openOrCreateDatabase("Eatfood.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String date = "[" + Integer.toString(calendar.get(Calendar.MONTH)) + "/" + Integer.toString(calendar.get(Calendar.DATE)) + "/" + Integer.toString(calendar.get(Calendar.YEAR)) + "]";
        //make 100% sure that the table already exists
        eatFood.execSQL(
                "CREATE TABLE IF NOT EXISTS " + date + " " +
                        "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                        "satfat DECIMAL(5,1), cholesterol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                        "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
        );
        meal_eatFoodDBHelper mydb;
        mydb = new meal_eatFoodDBHelper(this, date);
        while (res.isAfterLast() == false) {
            //the following code is incredibly useful for debugging because it separates the foodDB from the meals DB
            //the items in the array list only correspond to the foodDB
            /*
            ArrayList<String> array_list = new ArrayList<String>();
            array_list.add("foodname " + res.getString(res.getColumnIndex(mydb.Col_2)));
            array_list.add("calories " + res.getString(res.getColumnIndex(mydb.Col_3)));
            array_list.add("totalfat " + res.getString(res.getColumnIndex(mydb.Col_4)));
            array_list.add("transfat " + res.getString(res.getColumnIndex(mydb.Col_5)));
            array_list.add("satfat " + res.getString(res.getColumnIndex(mydb.Col_6)));
            array_list.add("Cholestrol " + res.getString(res.getColumnIndex(mydb.Col_7)));
            array_list.add("sodium " + res.getString(res.getColumnIndex(mydb.Col_8)));
            array_list.add("carbs " + res.getString(res.getColumnIndex(mydb.Col_9)));
            array_list.add("fiber " + res.getString(res.getColumnIndex(mydb.Col_10)));
            array_list.add("sugar " + res.getString(res.getColumnIndex(mydb.Col_11)));
            array_list.add("protein " + res.getString(res.getColumnIndex(mydb.Col_12)));
            System.out.println(array_list);
*/
            mydb.insertFood(res.getString(res.getColumnIndex(mydb.Col_2)), res.getDouble(res.getColumnIndex(mydb.Col_3)),
                    res.getDouble(res.getColumnIndex(mydb.Col_4)), res.getDouble(res.getColumnIndex(mydb.Col_5)),
                    res.getDouble(res.getColumnIndex(mydb.Col_6)), res.getDouble(res.getColumnIndex(mydb.Col_7)),
                    res.getDouble(res.getColumnIndex(mydb.Col_8)), res.getDouble(res.getColumnIndex(mydb.Col_9)),
                    res.getDouble(res.getColumnIndex(mydb.Col_10)), res.getDouble(res.getColumnIndex(mydb.Col_11)),
                    res.getDouble(res.getColumnIndex(mydb.Col_12))
            );
            if (DEBUG) {
                System.out.println("total items in table is: " + mydb.getAllmacrosInfo());
            }
            res.moveToNext();
        }
        res.close();

    }

}
