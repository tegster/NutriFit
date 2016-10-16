package fitness.cs115.a115fitnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Matthew on 10/13/16.
 */

public class meal_editMeal extends AppCompatActivity {
    private meal_mealDBHelper mydb;
    private boolean DEBUG = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("meal_editMeal: ", "error getting table name");
            return;
        }
        String table = extras.getString("TABLE"); //this is the name of the table that is being edited.
        mydb = new meal_mealDBHelper(this, table);
        if (DEBUG) {
            mydb.insertFood("chicken", 400.3);
            System.out.println(mydb.getAllFoodInfo());
        }


    }
}
