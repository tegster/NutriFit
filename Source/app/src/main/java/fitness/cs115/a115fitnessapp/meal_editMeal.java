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
    private boolean DEBUG = false;
    String mealtablename;
    private ListView lv; //used to display foods
    ArrayList<String> food_names = new ArrayList<>();  //store all foods in the foods database
    ArrayList<String> items_add = new ArrayList<>(); // items to add to this meal
    ArrayList<String> already_existing_foods = new ArrayList<>(); //items that are already present in the meal

    private meal_foodDBHelper foodDB = new meal_foodDBHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);
        Bundle extras = getIntent().getExtras();    //meal name coming in to be edited
        if (extras == null) {
            Log.e("meal_editMeal: ", "error getting table name");
            Intent intent = new Intent(meal_editMeal.this, MainActivity.class); //if weird error here, just go back to main activity
            startActivity(intent);
            return;
        }
        mealtablename =  extras.getString("TABLE"); //this is the name of the [meal] table that is being edited.
        food_names = foodDB.getAllmacrosInfo(); //food_names = arraylist of all foods and their cals'
        food_names.add("Foods in Database:");

        mydb = new meal_mealDBHelper(this, mealtablename); //myDB is the name of the meal db that is being edited
        lv = (ListView) findViewById(R.id.meal_items);
        // this is where we need to add the data from foodnames to
        // meals table
        //once the item in the listview is clicked
        //mydb.insertfoodinmeal
        //below is just debug, start at line 84

        if (DEBUG) {
            mydb.insertFoodinMeal("chicken", 200.2, 12.2, 11.2, 25.6, 11.2, 12.2, 13.2, 14.2, 15.2, 8.9);
            System.out.println(mydb.getAllFoodInfofromMeal());
        }

        Log.v("meal_editMeal:", " table name is: " + mealtablename);

        //iterate through foods database and add all foods and their calories
        if (DEBUG) {
            food_names.add("bacon"); //dummy data, not correctly formatted
        }
        already_existing_foods = mydb.getAllFoodsAndCaloriesfromMeal(); //this gets all of the foods currently in this meal

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
        food_names.add(0, "Foods in Meal:"); //explain to user what is already in meal
        if (DEBUG) {
            System.out.println("food names: " + food_names);
        }


        //this attaches the listview to the array list to display the food names and calories
        ArrayAdapter<String> foodArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food_names);

        lv.setAdapter(foodArrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  String k = food_names.get(position);

                if (DEBUG) {
                    Toast.makeText(getApplicationContext(), "position: " + position, Toast.LENGTH_SHORT).show();
                    //getApplicationContext().
                }
                if (k.equals("Foods in Meal:") || k.equals("Foods in Database:")) { //can't add this to meal, it's just a prompt
                    return;
                }
                //find the the four parameters from this position and add them to mydb table
                if (items_add.contains(food_names.get(position))) {
                    //we can update the food in meal, rather than jus deleting it
                    items_add.remove(food_names.get(position));
                    Toast.makeText(getApplicationContext(), "removed: " + food_names.get(position) + " from meal", Toast.LENGTH_SHORT).show();
                    //remove from database
                    mydb.deleteFoodinMeal(food_names.get(position));
                } else { //means it's not already in the meal
                    items_add.add(food_names.get(position));
                    System.out.println(items_add);
                    Toast.makeText(getApplicationContext(), "added: " + food_names.get(position) + " to meal", Toast.LENGTH_SHORT).show();
                    Log.d("tag", "printing after adding to items" + items_add);
                    try {
                        ArrayList<Double> item = extractIntegers(k); //extracting the double paramters from the foods
                        String name = k.substring(0, k.indexOf(','));
                        Log.d("tag", "printing after decypher " + "name[" + name + "]");
                        Log.d("tag", "printing after decypher " + "Cals[" + item.get(0) + "]");
                        Log.d("tag", "printing after decypher " + "Fals[" + item.get(1) + "]");
                        Log.d("tag", "printing after decypher " + "Carbs[" + item.get(2) + "]");
                        Log.d("tag", "printing after decypher " + "Protein[" + item.get(3) + "]");
                        mydb.insertFoodinMeal(name, item.get(0), item.get(1), item.get(2), item.get(3), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0); //inserting to meals DB
                        Log.d("tag", "total cal: " + mydb.getTotalCalories());
                    } catch (Exception e) {//this is just for debugging to stop app from crashing based off of incomplete hardcoded database entries
                        //this catch will *not* get triggered with actual values
                        System.out.println(e.toString());
                    }
                }
            }
        });

        //mydb.insertFoodinMeal(it)

        //need to load all existing food names/calories into array list to see if they exist already. if it already exists, in a meal. don't add it
        // and display error message saying
        //no need to use a map instead since at absolute most a meal will probably have 20 items in it

        Toast.makeText(this, "Tap the items you wish to add to the meal, Tap again to remove", Toast.LENGTH_LONG).show();



    }

    //extract integers from a string and return them in Arraylist of double
    public ArrayList<Double> extractIntegers(String input) {
        ArrayList<Double> result = new ArrayList<Double>();
        int index = 0;
        double v = 0;
        int l = 0;
        while (index < input.length())
        {
            char c = input.charAt(index);
            if (Character.isDigit(c)) {
                v *= 10;
                v += c - '0';
                l++;
            }
            else if (l > 0) {
                result.add(v);
                l = 0;
                v = 0;
            }
            index++;
        }
        if (l > 0) {
            result.add(v);
        }
        return result;
    }

}
