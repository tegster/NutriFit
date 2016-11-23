package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matthew on 11/22/16.
 */
//I adopted/modified this file from some android work I did over the summer. So there may be some leftover parts of the code that didn't get refactored out
public class meal_Onboarding extends Activity {
    GridView grid;
    ArrayList selectedCategories = new ArrayList<>();//used to store strings that contain user selected genre
    ArrayList selectedPositions = new ArrayList<>();//used to store actual positions that things in the grid are in
    meal_CustomGrid adapter;
    private boolean debug = false;
    String[] promptUserText = {
            "   Scan Bar Code",
            "   Manually Add Food",
            "   Meals",
            "   Add New Meal",
            "   Daily Food Log",
            "   Graphs",
    };
    int[] imageId = {
            R.drawable.barcodescanner48,
            R.drawable.vegetarianfoodfilled50,
            R.drawable.meal50,
            R.drawable.diningroom,
            R.drawable.addlist50,
            R.drawable.barchartfilled50,
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

        adapter = new meal_CustomGrid(meal_Onboarding.this, promptUserText, imageId);
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
                    Toast.makeText(meal_Onboarding.this, "You Clicked at position" + position + " " + promptUserText[+position] +
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
                    if (selectedCategories.toString().equals("[   Graphs]")) {
                        //Toast.makeText(getApplicationContext(), "Launching Graph now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(meal_Onboarding.this, meal_graphs.class);
                        startActivity(intent);
                    } else if (selectedCategories.toString().equals("[   Daily Food Log]")) {
                        //  Toast.makeText(getApplicationContext(), "Launching Daily Food log now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(meal_Onboarding.this, meal_viewAllEatenMeals.class);
                        startActivity(intent);
                    } else if (selectedCategories.toString().equals("[   Add New Meal]")) {
                 //       Toast.makeText(getApplicationContext(), "Launching Add New Meal now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(meal_Onboarding.this, meal_createMeal.class);
                        startActivity(intent);
                    } else if (selectedCategories.toString().equals("[   Meals]")) {
                      //  Toast.makeText(getApplicationContext(), "Launching Meals now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(meal_Onboarding.this, meal_viewAllMeals.class);
                        startActivity(intent);
                    } else if (selectedCategories.toString().equals("[   Manually Add Food]")) {
                       // Toast.makeText(getApplicationContext(), "Launching Manually Add Food now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(meal_Onboarding.this, meal_addFood.class);
                        startActivity(intent);
                    } else if (selectedCategories.toString().equals("[   Scan Bar Code]")) {
                        //Toast.makeText(getApplicationContext(), "Launching Scan Bar Code now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(meal_Onboarding.this, meal_SimpleScannerActivity.class);
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