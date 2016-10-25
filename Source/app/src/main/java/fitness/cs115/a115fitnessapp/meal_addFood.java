package fitness.cs115.a115fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Matthew on 10/6/16.
 */

public class meal_addFood extends AppCompatActivity {
    private static final boolean DEBUG = true;
    private EditText foodName;
    private EditText calories;

    private meal_foodDBHelper mydb;
    private double no_value = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        foodName = (EditText) findViewById(R.id.foodName);
        calories = (EditText) findViewById(R.id.calories);
        //get data passed into intent? maybe which meal an item is being added to is passed in????
        Intent intent = getIntent();
        //item = intent.getStringExtra("stuffz");


        //not all meal_foodDBHelper functions tested yet
        //hardcoded stuff to test database
        mydb = new meal_foodDBHelper(this);
        if (DEBUG) {
            mydb.insertFood("hotdog", 200.2, no_value, no_value, no_value);
            mydb.insertFood("hotdog", 400.87, no_value, no_value, no_value);//shouldn't be added since hotdog already in database
            mydb.insertFood("hotdog", 200.2, no_value, no_value, no_value);//shouldn't be added since hotdog already in database
            mydb.insertFood("cat", 300.0, no_value, no_value, no_value);
            mydb.insertFood("orange", 120.5, no_value, no_value, no_value);
            System.out.println(mydb.getAllFoods());
            System.out.println(mydb.getAllFoodInfo());
            System.out.println("number of rows/items is: " + mydb.getNumberOfRows());
            Cursor res = mydb.getData(2);
            System.out.println(res);

            Boolean x = mydb.deleteFood(1);
            System.out.println("x is " + x);
            Boolean y = mydb.deleteFood(0);
            System.out.println("y is " + y);
            Boolean z = mydb.deleteFood(1);
            System.out.println("z is " + z);

            System.out.println(mydb.getAllFoodInfo());
            mydb.insertFood("hotdog", 120.357897456, no_value, no_value, no_value);
            System.out.println(mydb.getAllFoodInfo());
            System.out.println(mydb.deleteFood("orange"));
            System.out.println(mydb.deleteFood("orange"));
            System.out.println(mydb.deleteFood("kiwi"));
            System.out.println(mydb.getAllFoodInfo());


            mydb.deleteEntireTable();
            System.out.println("After deleting entire table " + mydb.getAllFoodInfo());
        }

        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check that both fields are filled in and display error message if not
                String name = foodName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    foodName.setError("Enter a food's name");
                    return;
                }
                String caloriesString = calories.getText().toString();
                if (TextUtils.isEmpty(caloriesString)) {
                    calories.setError("Enter the number of calories");
                    return;
                }

                //hide keyboard
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                double numberOfCalories = Double.parseDouble(caloriesString);
                double numberOfCarbs = -1;
                double amountOfFat = -1;
                double amountOfPotasium = -1;
                EditText carbs = (EditText) findViewById(R.id.carb);
                EditText fat = (EditText) findViewById(R.id.fat);
                EditText potasium = (EditText) findViewById(R.id.pot);
                //need to check if it is null or not
                if (!carbs.getText().toString().isEmpty()) {
                    numberOfCarbs = Double.parseDouble(carbs.getText().toString());
                    System.out.println("carbs: " + numberOfCarbs);
                }
                if (!fat.getText().toString().isEmpty()) {
                    amountOfFat = Double.parseDouble(fat.getText().toString());
                    System.out.println("fat: " + amountOfFat);
                }
                if (!potasium.getText().toString().isEmpty()) {
                    amountOfPotasium = Double.parseDouble(potasium.getText().toString());
                    System.out.println("potasium: " + amountOfPotasium);
                }
                mydb.insertFood(name, numberOfCalories, amountOfFat, numberOfCarbs, amountOfPotasium);//insert food to database
                if (DEBUG) {
                    // System.out.println(mydb.getAllFoods());
                    System.out.println(mydb.getAllFoodInfo());
                    System.out.println("number of rows/items is: " + mydb.getNumberOfRows());

/*
                    Intent intent = new Intent(meal_addFood.this, MainActivity.class);
                    startActivity(intent);
*/
                }

                //launch intent here
                Intent intent = new Intent(meal_addFood.this, meal_overview.class);
                startActivity(intent);


            }
        });

    }

}