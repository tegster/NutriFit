package fitness.cs115.a115fitnessapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matthew on 11/17/16.
 */
//this class shows all the nutrition info in 1 meal summed up
//1.get the table name passed into the intent
//2. set up database helper
//3. call database helper getMacronutrients functions for each nutrient
//4. populate list view with each item in it
public class meal_viewEatenMeal extends AppCompatActivity {
    private String TABLE_NAME;
    private meal_eatFoodDBHelper mydb;
    private static final boolean DEBUG = true;
    ListView listView;

    ArrayList<String> foodNames = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view_eaten_meal);
        listView = (ListView) findViewById(R.id.foodList);
        try {
            Bundle extras = getIntent().getExtras();    //meal name coming in to be edited
            TABLE_NAME = extras.getString("TABLE"); //this is the name of the [meal] table that is being edited.
        } catch (Exception e) {
            Toast.makeText(this, "There was an error retrieving this meal", Toast.LENGTH_SHORT).show();
        }
        TABLE_NAME = "[" + TABLE_NAME + "]";
        mydb = new meal_eatFoodDBHelper(this, TABLE_NAME);
        //remove [ and ] from table name
        Bundle extras = getIntent().getExtras();    //meal name coming in to be edited
        TABLE_NAME = extras.getString("TABLE");
        getData();
        viewItemsInMeal();
    }

    private void getData() {
        TextView text = (TextView) findViewById(R.id.foodName); //foodName is "Nutrition Information:"
        text.setText("Nutrition Information for:" + TABLE_NAME);
        text = (TextView) findViewById(R.id.calories);
        text.setText("calories :" + Integer.toString(mydb.getTotalCalories()));
        text = (TextView) findViewById(R.id.saturatedfat);
        text.setText("sat_fat :" + Integer.toString(mydb.getTotalSatFat()));
        text = (TextView) findViewById(R.id.transfat);
        text.setText("trans fat :" + Integer.toString(mydb.getTotalTransFat()));
        text = (TextView) findViewById(R.id.cholestrol);
        text.setText("cholesterol :" + Integer.toString(mydb.getTotalCholesterol()));
        text = (TextView) findViewById(R.id.sodium);
        text.setText("sodium :" + Integer.toString(mydb.getTotalSodium()));
        text = (TextView) findViewById(R.id.carbs);
        text.setText("carbohydrate :" + Integer.toString(mydb.getTotalCarbs()));
        text = (TextView) findViewById(R.id.fiber);
        text.setText("fiber :" + Integer.toString(mydb.getTotalFiber()));
        text = (TextView) findViewById(R.id.protein);
        text.setText("protein :" + Integer.toString(mydb.getTotalProtein()));
        text = (TextView) findViewById(R.id.sugar);
        text.setText("sugar :" + Integer.toString(mydb.getTotalSugar()));
        text = (TextView) findViewById(R.id.totalfat);
        text.setText("totalFat :" + Integer.toString(mydb.getTotalFat()));
        if (DEBUG) {
            System.out.println(mydb.getAllmacrosInfo());
        }
    }

    //displays food names along with some nutrients
    private void viewItemsInMeal() {
        foodNames.clear();
        foodNames = mydb.getAllmacrosInfo();
        System.out.println("food names: " + foodNames);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                foodNames);

        //puts the data into the listview
        listView.setAdapter(arrayAdapter);
    }
}
