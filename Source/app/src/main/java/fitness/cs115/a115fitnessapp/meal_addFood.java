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
    private static final boolean DEBUG = false;
    private EditText foodName, calories, totalfat, cholestrol, sodium, carbs, protein;
    private EditText satfat, transfat, fiber, sugars;


    private meal_foodDBHelper mydb;
    private double no_value = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        foodName = (EditText) findViewById(R.id.foodName);
        calories = (EditText) findViewById(R.id.calories);
        totalfat = (EditText) findViewById(R.id.totalfat);
        transfat = (EditText) findViewById(R.id.transfat);
        satfat = (EditText) findViewById(R.id.saturatedfat);
        cholestrol = (EditText) findViewById(R.id.cholestrol);
        sodium = (EditText) findViewById(R.id.sodium);
        carbs = (EditText) findViewById(R.id.carbs);
        fiber = (EditText) findViewById(R.id.fiber);
        sugars = (EditText) findViewById(R.id.sugar);
        protein = (EditText) findViewById(R.id.protein);



        //get data passed into intent? maybe which meal an item is being added to is passed in????
        Intent intent = getIntent();
        //item = intent.getStringExtra("stuffz");


        //not all meal_foodDBHelper functions tested yet
        //hardcoded stuff to test database
        mydb = new meal_foodDBHelper(this);
        if (DEBUG) {

            /**
            mydb.insertFood("dummyhotdog", 176.2, 12.0, 3.0, 2.0, 7.0, 123.0, 4.0, 1235.3, 23.0, 4.0);
            mydb.insertFood("dummycorn", 22.3, 15.3, 5.0, 2.0, 9.0, 123.0, 4.0, 1235.3, 23.0, 4.0);
            mydb.insertFood("dummybun", 189.3, 12.0, 3.0, 2.0, 7.0, 123.0, 4.0, 1235.3, 23.0, 4.0);
            mydb.insertFood("dummypeas", 182.4, 15.3, 5.0, 2.0, 9.0, 123.0, 4.0, 1235.3, 23.0, 4.0);
            mydb.insertFood("dummychicken", 234.2, 12.0, 3.0, 2.0, 7.0, 123.0, 4.0, 1235.3, 23.0, 4.0);
            mydb.insertFood("dummyrice", 15.2, 15.3, 5.0, 2.0, 9.0, 123.0, 4.0, 1235.3, 23.0, 4.0);

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
            //mydb.insertFood("hotdog", 120.35);



            mydb.deleteEntireTable();
            System.out.println("After deleting entire table " + mydb.getAllFoodInfo());
        }

             */
        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all the info from the edittext's, and insert it into
                // the insetfood(), if nothing inputted, make it default 0
                //check that both fields are filled in and display error message if not, only name and cals
                String name, caloriestring, totalfatstring, cholestrolstring, sodiumstring, carbsstring, proteinstring;
                String transfatstring, satfatstring, fiberstring, sugarstring;

                // getting info from the edittext, once the confirm button is clicked
                name = foodName.getText().toString();
                caloriestring = calories.getText().toString();
                totalfatstring = totalfat.getText().toString();
                transfatstring = transfat.getText().toString();
                satfatstring = satfat.getText().toString();
                cholestrolstring = cholestrol.getText().toString();
                sodiumstring = sodium.getText().toString();
                carbsstring = carbs.getText().toString();
                fiberstring = fiber.getText().toString();
                sugarstring = sugars.getText().toString();
                proteinstring = protein.getText().toString();



                if (TextUtils.isEmpty(name)) {
                    foodName.setError("Enter the foods name, you'd like to enter");
                    return;
                }
                if (TextUtils.isEmpty(caloriestring)) {
                    calories.setError("Enter the number of calories");
                    return;
                }
                if (TextUtils.isEmpty(totalfatstring)) {
                    totalfatstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(transfatstring)) {
                    transfatstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(satfatstring)) {
                    satfatstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(cholestrolstring)) {
                    cholestrolstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(sodiumstring)) {
                    sodiumstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(carbsstring)) {
                    carbsstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(fiberstring)) {
                    fiberstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(sugarstring)) {
                    sugarstring = "0.0";
                    return;
                }
                if (TextUtils.isEmpty(proteinstring)) {
                    proteinstring = "0.0";
                    return;
                }



                //hide keyboard
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }


                double numberOfCalories = Double.parseDouble(caloriestring);
                double numberOftotalfat = Double.parseDouble(totalfatstring);
                double numberOftransfat = Double.parseDouble(transfatstring);
                double numberOfsatfat = Double.parseDouble(satfatstring);
                double numberOfCholestorl = Double.parseDouble(cholestrolstring);
                double numberOfSodium = Double.parseDouble(sodiumstring);
                double numberOfCarbs = Double.parseDouble(carbsstring);
                double numberOffiber = Double.parseDouble(fiberstring);
                double numberOfsugar = Double.parseDouble(sugarstring);
                double numberOfprotein = Double.parseDouble(proteinstring);
                    //insert food to database
                mydb.insertFood(name, numberOfCalories, numberOftotalfat, numberOftransfat,
                        numberOfsatfat, numberOfCholestorl, numberOfSodium, numberOfCarbs,
                        numberOffiber, numberOfsugar, numberOfprotein);

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