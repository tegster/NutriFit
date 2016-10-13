package fitness.cs115.a115fitnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Matthew on 10/9/16.
 */

public class meal_createMeal extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);
//have some sort of list of food items and another list of calories?
//then add these to database

        //items needed in database
        //suggested meal time
        //actual meal time
        //food items
            //includes name+ calories

        //flow
        //1. enter meal name (which will be the table name)
        //2.    check if meal name is has already been used or not (display error message and make user pick new name if it has)
        //3. create table
        //4. launch edit meal activity with the meal name being passed in
        //5. have button for add item to meal
        //6.
    }
}
