package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matthew on 10/13/16.
 */

public class meal_editMeal extends AppCompatActivity {
    private meal_mealDBHelper mydb;
    private boolean DEBUG = true;
    String table;
    private ListView lv;
    ArrayList<String> food_names = new ArrayList<>();
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
        ArrayAdapter<String> foodArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food_names);

        lv.setAdapter(foodArrayAdapter);

        Toast.makeText(this, "Tap the items you wish to add to the meal, Tap again to move", Toast.LENGTH_LONG).show();

    }

}
