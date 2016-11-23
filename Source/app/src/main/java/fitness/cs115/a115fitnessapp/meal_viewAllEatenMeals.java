package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Matthew on 11/17/16.
 */
//this class shows all of the days that have meals eaten on them
//overall flow
//1. get all tables from eaten meals database
//2. convert table name to human readable date
//3. when a user clicks on a meal they are taken to another page that shows the total amount of stuff eaten for that meal
public class meal_viewAllEatenMeals extends AppCompatActivity {
    private static final boolean DEBUG = false;
    ListView listView;
    ArrayList<String> mealNames = new ArrayList<String>();
    ArrayList<String> readableMealNames = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_eaten_meals);
        listView = (ListView) findViewById(R.id.meals);
        listView.setLongClickable(true);
        if (DEBUG) {
            SQLiteDatabase mDatabase = openOrCreateDatabase("Eatfood.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            String TABLE_NAME = "[10/17/2016]";

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                            "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                            "satfat DECIMAL(5,1), cholesterol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                            "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
            );

            Calendar calendar = Calendar.getInstance();
            //surround date with '[' ']' like in create meal to avoid weird issues. This is the actual name for each table
            String date = "[" + Integer.toString(calendar.get(Calendar.MONTH)) + "/" + Integer.toString(calendar.get(Calendar.DATE)) + "/" + Integer.toString(calendar.get(Calendar.YEAR)) + "]";
/*
            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + date + " " +
                            "(id INTEGER PRIMARY KEY, String Date, Double servings, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                            "satfat DECIMAL(5,1), cholesterol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                            "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
            );
  */
            meal_eatFoodDBHelper mydb;
            //  meal_eatFoodDBHelper mydb2;

            mydb = new meal_eatFoodDBHelper(this, TABLE_NAME);

            mydb.insertFood("bacon", 200.0, 10.0, 20.0, 550.0, 30.0, 40.0, 60.0, 70.0, 80.0, 90.0);
            mydb.insertFood("pie", 100.0, 80.0, 70.0, 60.0, 090.0, 50.0, 40.0, 30.0, 20.0, 10.0);

            //mydb2 = new meal_eatFoodDBHelper(this, date);
            // mydb2.insertFood("burrito", 999.999, 10.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 15.0);
        }
        viewMealsInDatabase();

    }

    private void setUpClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                //we don't need a long click here
                /*
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
                //send table name to view individual eaten meal activity
                Intent intent = new Intent(meal_viewAllEatenMeals.this, meal_viewEatenMeal.class);
                intent.putExtra("TABLE", mealNames.get(position)); //send the meal name to edit_Meal as "TABLE"
                startActivity(intent);


            }
        });
    }

    //displays the meals in the database in a listview
    private void viewMealsInDatabase() {
        mealNames.clear();
        readableMealNames.clear();
        SQLiteDatabase db = openOrCreateDatabase("Eatfood.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

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
        listView.setAdapter(arrayAdapter);
        if(listView.getCount()==0){//prompt user in case they get confused
            Toast toast = Toast.makeText(getApplicationContext(),"You have not eaten any meals yet, go to the meals page and touch a meal to eat it", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0); //make toast show up in center of screen
            toast.show();
        }
        setUpClickListener();

    }


}
