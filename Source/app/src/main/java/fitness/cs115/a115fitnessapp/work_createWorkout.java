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
/*
* Edited by James Kennedy on 10/18/2016
 */

public class work_createWorkout extends AppCompatActivity {

    private final boolean DEBUG = true;
    private work_DBHelper user_work_db;

    @Override
    protected void  onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_workout);

        user_work_db = new work_DBHelper(this);

        if (DEBUG) {
            user_work_db.create_program("testProgram 1");
            user_work_db.add_work_to_prog("testProgram 1", 400.87);//shouldn't be added since hotdog already in database
            user_work_db.insertFood("hotdog", 200.2);//shouldn't be added since hotdog already in database
            user_work_db.insertFood("cat", 300.0);
            user_work_db.insertFood("orange", 120.5);
            System.out.println(user_work_db.getAllFoods());
            System.out.println(user_work_db.getAllFoodInfo());
            System.out.println("number of rows/items is: " + user_work_db.getNumberOfRows());
            Cursor res = user_work_db.getData(2);
            System.out.println(res);

            Boolean x = user_work_db.deleteFood(1);
            System.out.println("x is " + x);
            Boolean y = user_work_db.deleteFood(0);
            System.out.println("y is " + y);
            Boolean z = user_work_db.deleteFood(1);
            System.out.println("z is " + z);

            System.out.println(user_work_db.getAllFoodInfo());
            user_work_db.insertFood("hotdog", 120.357897456);
            System.out.println(user_work_db.getAllFoodInfo());
            System.out.println(user_work_db.deleteFood("orange"));
            System.out.println(user_work_db.deleteFood("orange"));
            System.out.println(user_work_db.deleteFood("kiwi"));
            System.out.println(user_work_db.getAllFoodInfo());


            user_work_db.deleteEntireTable();
            System.out.println("After deleting entire table " + user_work_db.getAllFoodInfo());
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
                user_work_db.insertFood(name, numberOfCalories);//insert food to database
                if (DEBUG) {
                    // System.out.println(user_work_db.getAllFoods());
                    System.out.println(user_work_db.getAllFoodInfo());
                    System.out.println("number of rows/items is: " + user_work_db.getNumberOfRows());

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
