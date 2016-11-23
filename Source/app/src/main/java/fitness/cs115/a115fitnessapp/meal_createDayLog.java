package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Teg on 11/18/2016.
 */
//for use with calendar. relates to meal consumption
public class meal_createDayLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_daylog);

        //dont think any buttons or input will be necessary, as the day to edit is coming in from
        //calendar viewer

        Bundle extras = getIntent().getExtras();    //date coming in to be edited

        if (extras == null) {
            Log.e("meal_createDayLog: ", "error getting the date m/d/year");
            Intent intent = new Intent(meal_createDayLog.this, MainActivity.class); //if weird error here, just go back to main activity
            startActivity(intent);
            return;
        }
        Integer extras_day = extras.getInt("Day"); //this is the name of the [meal] table that is being edited.
        Integer extras_month = extras.getInt("Month");
        Integer extras_year = extras.getInt("Year");

        //build the date into a string to pass into the table as the DayLog name

        StringBuilder sb = new StringBuilder();
        sb.append(extras_month);
        sb.append("/");
        sb.append(extras_day);
        sb.append("/");
        sb.append(extras_year);
        String date = sb.toString();
        date = "[" + date + "]";
        SQLiteDatabase mDatabase = openOrCreateDatabase("daylog.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + date + " " +
                        "(id INTEGER PRIMARY KEY, mealname text, servings DECIMAL(5,1), foodname text, calories DECIMAL(5,1), " +
                        "totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                        "satfat DECIMAL(5,1), cholestrol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                        "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
        );
        //now need to make the table so that it actually exists
        //i also need to update this code so that it uses the proper database and not "foods.db"
        Intent intent = new Intent(meal_createDayLog.this, meal_editMeal.class);
        intent.putExtra("Date", date); //send the meal name to edit_Meal as "TABLE"
        startActivity(intent);


    }
}