package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Matthew on 11/23/16.
 */

public class meal_Graph_Overview extends AppCompatActivity {
    ListView listView;
    ArrayList<String> variousGraphs = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_graph_overview);
        listView = (ListView) findViewById(R.id.macros);
        variousGraphs.add("Calories");
        variousGraphs.add("Carbs");
        variousGraphs.add("Fat");
        variousGraphs.add("Protein");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                variousGraphs);
        listView.setAdapter(arrayAdapter);
        setUpClickListener();
    }

    private void setUpClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                if (variousGraphs.get(position).equals("Calories")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_calories.class);
                    startActivity(intent);
                }
                if (variousGraphs.get(position).equals("Carbs")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_carbs.class);
                    startActivity(intent);
                }
                if (variousGraphs.get(position).equals("Fat")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_fat.class);
                    startActivity(intent);
                }
                if (variousGraphs.get(position).equals("Protein")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_protein.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                if (variousGraphs.get(position).equals("Calories")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_calories.class);
                    startActivity(intent);
                }
                if (variousGraphs.get(position).equals("Carbs")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_carbs.class);
                    startActivity(intent);
                }
                if (variousGraphs.get(position).equals("Fat")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_fat.class);
                    startActivity(intent);
                }
                if (variousGraphs.get(position).equals("Protein")) {
                    Intent intent = new Intent(meal_Graph_Overview.this, meal_graph_protein.class);
                    startActivity(intent);
                }
            }
        });
    }
}
