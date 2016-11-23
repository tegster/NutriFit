package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Matthew on 10/9/16.
 */

//old code, used for displaying all of the nutrition stuff
    //replaced with meal_Onboarding
public class meal_overview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_overview);

        Button meal = (Button) findViewById(R.id.newMeal);
        meal.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_createMeal.class);
                startActivity(intent);
            }
        });

        Button eaten = (Button) findViewById(R.id.eaten);
        eaten.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_viewAllEatenMeals.class);
                startActivity(intent);
            }
        });

        Button addFood = (Button) findViewById(R.id.addFood);
        addFood.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_addFood.class);
                startActivity(intent);
            }
        });

        //view all meals
        Button viewMeals = (Button) findViewById(R.id.view_meals);
        viewMeals.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_viewAllMeals.class);
                startActivity(intent);
            }
        });


        //this is the actual scanner
        Button barcodeScan = (Button) findViewById(R.id.scan);
        barcodeScan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_SimpleScannerActivity.class);
                startActivity(intent);
            }
        });

        //this is the web query
        Button graph = (Button) findViewById(R.id.graphs);
        graph.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_graphs.class);
                startActivity(intent);
            }
        });

        // save meals to current day,
        Button createDayLog = (Button) findViewById(R.id.createdaylog);
        createDayLog.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, CalendarViewer.class);
                startActivity(intent);
            }
        });

        Button onboarding = (Button) findViewById(R.id.onboarding);
        onboarding.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_Onboarding.class);
                startActivity(intent);
            }
        });

    }
}
