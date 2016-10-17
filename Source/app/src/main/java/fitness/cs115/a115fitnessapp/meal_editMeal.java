package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by Matthew on 10/13/16.
 */

public class meal_editMeal extends AppCompatActivity {
    private meal_mealDBHelper mydb;
    private boolean DEBUG = true;
    String table;
    private ListView lv;
    ArrayList<String> food_names = new ArrayList<>();
    ArrayList<String> items_add = new ArrayList<>();
    ArrayList<String> already_existing_foods = new ArrayList<>();

    private meal_foodDBHelper foodDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("meal_editMeal: ", "error getting table name");
            Intent intent = new Intent(meal_editMeal.this, MainActivity.class); //if weird error here, just go back to main activity
            startActivity(intent);
            return;
        }

        table = extras.getString("TABLE"); //this is the name of the table that is being edited.
        foodDB = new meal_foodDBHelper(this);
        food_names = foodDB.getAllFoodsAndCalories();
        mydb = new meal_mealDBHelper(this, table);
        if (DEBUG) {
            mydb.insertFood("chicken", 400.3);
            System.out.println(mydb.getAllFoodInfo());
        }

        Log.v("meal_editMeal:", " table name is: " + table);
        lv = (ListView) findViewById(R.id.meal_items);
        //iterate through foods database and add all foods and their calories
        if (DEBUG) {
            food_names.add("bacon");
        }
        already_existing_foods = mydb.getAllFoodsAndCalories();
        ArrayAdapter<String> foodArrayAdapter = //this attaches the listview to the array list to display the food names and calories
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food_names);

        lv.setAdapter(foodArrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (DEBUG) {
                    Toast.makeText(getApplicationContext(), "position: " + position, Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "item: " + food_names.get(position), Toast.LENGTH_SHORT).show();
                if (items_add.contains(food_names.get(position))) {
                    items_add.remove(food_names.get(position));
                    Toast.makeText(getApplicationContext(), "removed: " + food_names.get(position) + " from meal", Toast.LENGTH_SHORT).show();
                    //remove from database
                } else { //means it's not already in the meal
                    items_add.add(food_names.get(position));
                    Toast.makeText(getApplicationContext(), "added: " + food_names.get(position) + " to meal", Toast.LENGTH_SHORT).show();
                    //add to database
                }
            }
        });

        //need to load all existing food names/calories into array list to see if they exist already. if it already exists, in a meal. don't add it
        // and display error message saying
        //no need to use a map instead since at absolute most a meal will probably have 20 items in it

        Toast.makeText(this, "Tap the items you wish to add to the meal, Tap again to move", Toast.LENGTH_LONG).show();

    }

}
