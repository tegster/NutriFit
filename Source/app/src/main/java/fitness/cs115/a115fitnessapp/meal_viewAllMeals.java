package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Matthew on 10/24/16.
 */

public class meal_viewAllMeals extends AppCompatActivity {
    ListView listView;
    String[] dummyArray = {"Android", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X", "1", "2", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_meals);
        listView = (ListView) findViewById(R.id.meals);
        listView.setLongClickable(true);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, simple_list_item_1, dummyArray);
        listView.setAdapter(adapter);
        setUpClickListener();

    }

    private void setUpClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                Toast.makeText(getApplicationContext(), "long click: " + position + " " + dummyArray[position], Toast.LENGTH_LONG).show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), "click: " + position + " " + dummyArray[position], Toast.LENGTH_LONG).show();

            }
        });

    }

}
