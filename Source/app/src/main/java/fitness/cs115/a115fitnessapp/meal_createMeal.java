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
 * Created by Matthew on 10/9/16.
 */

public class meal_createMeal extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        Button confirm_button = (Button) findViewById(R.id.confirm);
        confirm_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView meal_text_view = (TextView) findViewById(R.id.meal_name);

                String input = meal_text_view.getText().toString();
                if (input.isEmpty()) {
                    meal_text_view.setError("Please enter a meal name");
                    return;
                }
                if (checkIfTableExists(input)) {
                    meal_text_view.setError("There is already a meal with this name");
                    return;
                }
                input = "[" + input + "]";
                SQLiteDatabase mDatabase = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                mDatabase.execSQL(
                        "CREATE TABLE IF NOT EXISTS " + input + " " +
                                "(id integer primary key, name text,calories REAL, fat REAL, Carbs REAL, Protein REAL);");

                //now need to make the table so that it actually exists
                //i also need to update this code so that it uses the proper database and not "foods.db"
                Intent intent = new Intent(meal_createMeal.this, meal_editMeal.class);
                intent.putExtra("TABLE", input); //send the meal name to edit_Meal as "TABLE"
                startActivity(intent);

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //returns true if table already exists
    Boolean checkIfTableExists(String table_name) {
        /* open database, if doesn't exist, create it */
        SQLiteDatabase mDatabase = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor c = null;
        boolean tableExists = false;
/* get cursor on it */
        try {
            c = mDatabase.query(table_name, null,
                    null, null, null, null, null);
            tableExists = true;
        } catch (Exception e) {
    /* fail */
            Log.d("check if table exists: ", table_name + " doesn't exist :(((");
        }
//System.out.println("does the table exist? "+tableExists);
        return tableExists;

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("meal_createMeal Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
