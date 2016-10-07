package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Matthew on 10/6/16.
 */

public class addItemToMeal extends AppCompatActivity {
    private EditText foodName;
    private EditText calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_to_meal);

        foodName= (EditText) findViewById(R.id.foodName);
        calories= (EditText) findViewById(R.id.calories);
        //get data passed into intent? maybe which meal an item is being added to is passed in????
        Intent intent = getIntent();
        //item = intent.getStringExtra("stuffz");

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
                double numberOfCalories = Double.parseDouble(caloriesString);   //this will get a number
                //at this point, need to add to meal database database

            }
        });

    }

}