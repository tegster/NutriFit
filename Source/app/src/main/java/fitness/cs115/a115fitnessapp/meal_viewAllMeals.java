package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Matthew on 10/24/16.
 */

public class meal_viewAllMeals extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrTblNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_meals);
        listView = (ListView) findViewById(R.id.meals);
        listView.setLongClickable(true);
        //   ArrayAdapter adapter = new ArrayAdapter<String>(this, simple_list_item_1, dummyArray);
        //  listView.setAdapter(adapter);

        SQLiteDatabase db = openOrCreateDatabase("meal.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        c.close();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrTblNames);

        listView.setAdapter(arrayAdapter);

        setUpClickListener();

    }

    private void setUpClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                Toast.makeText(getApplicationContext(), "long click: " + position + " " + arrTblNames.get(position), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), "click: " + position + " " + arrTblNames.get(position), Toast.LENGTH_LONG).show();

            }
        });
    }

}
