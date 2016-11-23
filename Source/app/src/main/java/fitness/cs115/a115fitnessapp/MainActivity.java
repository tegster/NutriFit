package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView grid;
    ArrayList selectedCategories = new ArrayList<>();//used to store strings that contain user selected genre
    ArrayList selectedPositions = new ArrayList<>();//used to store actual positions that things in the grid are in
    meal_CustomGrid adapter;
    private boolean debug = false;
    String[] promptUserText = {
            "   Nutrition",
            "   Fitness",
    };
    int[] imageId = {
            R.drawable.restaurant48,
            R.drawable.dumbbell48,
    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_meal_onboarding);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            ); //makes sure keyboard is hidden on this view as it is not needed
            selectedCategories.clear();
            selectedPositions.clear();
            grid = (GridView) findViewById(R.id.grid);
            grid.setNumColumns(1);
            adapter = new meal_CustomGrid(MainActivity.this, promptUserText, imageId);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //    Toast.makeText(meal_Onboarding.this, "You Clicked at position"+position+" " + promptUserText[+position], Toast.LENGTH_SHORT).show(); //debug
                    //the position is the actual position that is pressed. but the getChildAt returns gets the relative position, ie what's being drawn
                    //on the grid.
                    //to fix this, the first visible position is obtained and the difference is subtracted
                    int firstPos = grid.getFirstVisiblePosition();
                    View tv = grid.getChildAt(position - firstPos);
                    if (debug) {
                        Toast.makeText(MainActivity.this, "You Clicked at position" + position + " " + promptUserText[+position] +
                                " firstpos is " + firstPos, Toast.LENGTH_SHORT).show();
                    }
                    if (tv == null) { //this shouldn't/doesn't happen
                        Log.e("OnBoarding.java", "Issue with null view at line " + Thread.currentThread().getStackTrace()[2].getLineNumber() +
                                "grid is having a problem");
                        return;
                    }
                    if (selectedCategories.contains(promptUserText[+position]) == false) {//add the item if it doesn't exist
                        selectedCategories.add(promptUserText[+position]);
                        selectedPositions.add(Integer.toString(position));
                        tv.setBackgroundColor(Color.parseColor("#FFE5CD"));
                        //now launch new activity here
                        // Toast.makeText(getApplicationContext(),selectedCategories.toString(),Toast.LENGTH_SHORT).show();
                        //  System.out.println(selectedCategories.toString());
                        if (selectedCategories.toString().equals("[   Nutrition]")) {
                            //Toast.makeText(getApplicationContext(), "Launching Graph now", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, meal_Onboarding.class);
                            startActivity(intent);
                        } else if (selectedCategories.toString().equals("[   Fitness]")) {
                            //  Toast.makeText(getApplicationContext(), "Launching Daily Food log now", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, work_programList.class);
                            startActivity(intent);
                        }
                    }
                }
            });

            grid.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    //Log.d("meal_Onboarding 7331", "onScroll: onScroll " + firstVisibleItem + " " + selectedPositions.toString()); //debug
                    for (int i = 0; i < totalItemCount; i++) {
                        View tv = grid.getChildAt(i - firstVisibleItem);
                        if (tv == null) {
                            continue;
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    //     Log.d("meal_Onboarding 7331", "onScroll: state changed "+selectedPositions.toString()); //debug
                }
            });

/*

        Button work_overview_btn = (Button) findViewById(R.id.btn_programList);
        work_overview_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, work_programList.class);
                startActivity(intent);
            }
        });

        Button work_db_test_btn = (Button) findViewById(R.id.btn_testWorkDB);
        work_db_test_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, work_createWorkout.class);
                startActivity(intent);
            }
        });
    }
*/
        }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //System.out.println("resume");
        selectedCategories.clear();
        selectedPositions.clear();
        View tv;
        for(int i=0; i<grid.getChildCount(); i++) {
            tv=grid.getChildAt(i);
            tv.setBackgroundColor(Color.TRANSPARENT);
        }

    }
}
