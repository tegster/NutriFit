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
import java.util.Collections;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by Matthew on 10/13/16.
 */

public class meal_editMeal extends AppCompatActivity {
    private meal_mealDBHelper mydb;
    private boolean DEBUG = true;
    String table;
    private ListView lv; //used to display foods
    ArrayList<String> food_names = new ArrayList<>();  //store all foods in the foods database
    ArrayList<String> items_add = new ArrayList<>(); // items to add to this meal
    ArrayList<String> already_existing_foods = new ArrayList<>(); //items that are already present in the meal

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
        mydb = new meal_mealDBHelper(this, table); //myDB is the name of the db that is being edited
        if (DEBUG) {
            mydb.insertFood("chicken", 400.3);
            System.out.println(mydb.getAllFoodInfo());
        }

        Log.v("meal_editMeal:", " table name is: " + table);
        lv = (ListView) findViewById(R.id.meal_items);
        //iterate through foods database and add all foods and their calories
        if (DEBUG) {
            food_names.add("bacon"); //dummy data, not correctly formatted
        }
        already_existing_foods = mydb.getAllFoodsAndCalories(); //this gets all of the foods currently in this meal

        food_names.removeAll(already_existing_foods);//in the list of all foods, get rid of elements that are already in the meal, they will be inserted at the front
        //put both lists in lexographical order
        Collections.sort(food_names);
        Collections.sort(already_existing_foods);

        if (DEBUG) {
            System.out.println("food names: " + food_names);
            System.out.println("already_existing_foods in meal: " + already_existing_foods);
        }

        for (String temp_food : already_existing_foods) { //iterate through the list, adding each item to the beginning of the list
            food_names.add(0, temp_food);
        }

        if (DEBUG) {
            System.out.println("food names: " + food_names);
        }

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
