package fitness.cs115.a115fitnessapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matthew on 11/17/16.
 */

//overall flow
//1. get all tables from eaten meals database
//2. convert table name to human readable date
//3. display human readable dates in listview
//4. when a user clicks on a meal they are taken to another page that shows the total amount of stuff eaten for that meal
public class meal_viewAllEatenMeals extends AppCompatActivity {
    private static final boolean DEBUG = true;
    ListView listView;
    ArrayList<String> mealNames = new ArrayList<String>();
    ArrayList<String> readableMealNames = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_eaten_meals);
        listView = (ListView) findViewById(R.id.meals);
        listView.setLongClickable(true);
        if (DEBUG) {
            SQLiteDatabase mDatabase = openOrCreateDatabase("foods.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            String TABLE_NAME = "car";
            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                            "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                            "satfat DECIMAL(5,1), cholesterol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                            "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
            );
            meal_eatFoodDBHelper mydb;

            mydb = new meal_eatFoodDBHelper(this, TABLE_NAME);

            mydb.insertFood("bacon", 200.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        }
        viewMealsInDatabase();

    }

    private void setUpClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                /*
                selectedTable = mealNames.get(position);
                if (DEBUG) {
                    //    Toast.makeText(getApplicationContext(), "long click: " + position + " " + mealNames.get(position), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "long click: " + position + " " + mealNames.get(position) + ", " + selectedTable, Toast.LENGTH_LONG).show();
                }
                */
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                mealNames.get(position);
                if (DEBUG) {
                    Toast.makeText(getApplicationContext(), "click: " + position + " " + mealNames.get(position), Toast.LENGTH_LONG).show();
                }
                //goto the meal here


            }
        });
    }

    //displays the meals in the database in a listview
    private void viewMealsInDatabase() {
        mealNames.clear();
        readableMealNames.clear();
        SQLiteDatabase db = openOrCreateDatabase("foods.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (!c.getString((c.getColumnIndex("name"))).equals("android_metadata")) {
                    if (DEBUG) {
                    }
                    mealNames.add(c.getString(c.getColumnIndex("name")));
                }
                c.moveToNext();
            }
        }
        c.close();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mealNames);

        //puts the data into the listview
        //convertIntoHumanReadable();
        listView.setAdapter(arrayAdapter);
        setUpClickListener();

    }


}
