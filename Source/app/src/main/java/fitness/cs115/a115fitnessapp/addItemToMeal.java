package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_to_meal);

        foodName= (EditText) findViewById(R.id.foodName);
        calories= (EditText) findViewById(R.id.calories);
        //get data passed into intent? maybe which meal an item is being added to is passed in????
        Intent intent = getIntent();
        //item = intent.getStringExtra("stuffz");

        SQLiteDatabase mydatabase = openOrCreateDatabase("foods.db", MODE_MULTI_PROCESS, null);

        //  mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TutorialsPoint(Username VARCHAR,Password VARCHAR);");
        // mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin','admin');");
/*
        SQLiteDatabase mydatabase = openOrCreateDatabase("your database name",MODE_PRIVATE,null);

        mydatabase.execSQL("CREATE TABLE TutorialsPoint(Username VARCHAR,Password REAL);");
        mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin','2002.2');");
        Cursor resultSet = mydatabase.rawQuery("Select * from TutorialsPoint",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(0);
        String password = resultSet.getString(1);
System.out.println(username);
        System.out.println(password);
*/


        //not all DBHelper functions tested yet
        mydb = new DBHelper(this);
        System.out.println("1337 made new db");
        mydb.insertFood("hotdog", 200.2);
        mydb.insertFood("hotdog", 400.87);//shouldn't be added since hotdog already in database
        mydb.insertFood("cat", 300.0);
        mydb.insertFood("orange", 120.5);

        System.out.println(mydb.getAllFoods());
        System.out.println(mydb.getAllFoodInfo());

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