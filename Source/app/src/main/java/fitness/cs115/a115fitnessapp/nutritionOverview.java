package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Matthew on 10/9/16.
 */

public class nutritionOverview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_overview);

        Button meal = (Button) findViewById(R.id.newMeal);
        meal.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nutritionOverview.this, CreateMeal.class);
                startActivity(intent);
            }
        });

        Button addFood = (Button) findViewById(R.id.addFood);
        addFood.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nutritionOverview.this, addItemToMeal.class);
                startActivity(intent);
            }
        });


        Button program_list = (Button) findViewById(R.id.proglist);
        program_list.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nutritionOverview.this, programList.class);
                startActivity(intent);
            }
        });
    }
}
