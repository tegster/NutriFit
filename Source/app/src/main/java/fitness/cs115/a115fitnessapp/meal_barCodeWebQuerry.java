package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew on 11/8/16.
 */
//this class takes the scanned barcode and sends it to the nutritionix server using our api key
//the response to this contains all of the macronutrients that we need
public class meal_barCodeWebQuerry extends AppCompatActivity {
    private static final boolean DEBUG = false;
    private meal_foodDBHelper mydb;
    private String food_name = "";
    private double calories = 0;
    private double sat_fat = 0;
    private double tran_fat = 0;
    private double cholesterol = 0;
    private double sodium = 0;
    private double carbohydrate = 0;
    private double fiber = 0;
    private double sugars = 0;
    private double protein = 0;
    private double totalfat = 0;

    // https://api.nutritionix.com/v1_1/search/cheddar%20cheese?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=[YOURID]&appKey=[YOURKEY]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        RequestQueue FoodDeclaration = Volley.newRequestQueue(this);
        String upc = "49000036756"; //defaults to coke. use this in the event that something weird happens
        Intent intent = getIntent();
        try {
            upc = intent.getStringExtra("barcode");
        } catch (Exception e) {
            System.out.println(e);
        }


        Log.v("meal_barCodeWebQuerry", "upc is: " + upc);
        String url = "https://api.nutritionix.com/v1_1/item?upc=" + upc + "&appId=dc7f6afd&appKey=8976d7ae10363be41401e2419d2bddf3";
        StringRequest FoodStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                //  System.out.println("1337: " + response);
                try {
                    JSONObject jObject = new JSONObject(response); // json
                    //    System.out.println(jObject.toString());
                    if (DEBUG) {
                        System.out.println(jObject.toString(2));
                    }
                    //now extract all of the necessary information from the foods database.

                    if (!jObject.isNull("item_name")) {
                        food_name = jObject.getString("item_name");
                    } else {
                        food_name = "error";
                    }
                    if (!jObject.isNull("nf_calories")) {
                        calories = jObject.getDouble("nf_calories");
                    }
                    if (!jObject.isNull("nf_saturated_fat")) {
                        sat_fat = jObject.getDouble("nf_saturated_fat");
                    }
                    if (!jObject.isNull("nf_trans_fatty_acid")) {
                        tran_fat = jObject.getDouble("nf_trans_fatty_acid");
                    }
                    if (!jObject.isNull("nf_cholesterol")) {
                        cholesterol = jObject.getDouble("nf_cholesterol");
                    }
                    if (!jObject.isNull("nf_sodium")) {
                        sodium = jObject.getDouble("nf_sodium");
                    }
                    if (!jObject.isNull("nf_total_carbohydrate")) {
                        carbohydrate = jObject.getDouble("nf_total_carbohydrate");
                    }
                    if (!jObject.isNull("nf_dietary_fiber")) {
                        fiber = jObject.getDouble("nf_dietary_fiber");
                    }
                    if (!jObject.isNull("nf_sugars")) {
                        sugars = jObject.getDouble("nf_sugars");
                    }
                    if (!jObject.isNull("nf_protein")) {
                        protein = jObject.getDouble("nf_protein");
                    }
                    if (!jObject.isNull("nf_total_fat")) {
                        protein = jObject.getDouble("nf_total_fat");
                    }

                    if (DEBUG) {
                        System.out.println("food name: " + food_name);
                        System.out.println("calories :" + calories);
                        System.out.println("sat_fat :" + sat_fat);
                        System.out.println("trans fat :" + tran_fat);
                        System.out.println("cholesterol :" + cholesterol);
                        System.out.println("sodium :" + sodium);
                        System.out.println("carbohydrate :" + carbohydrate);
                        System.out.println("fiber :" + fiber);
                        System.out.println("sugars :" + sugars);
                        System.out.println("protein :" + protein);
                        System.out.println("total fat: " + totalfat);
                    }
                    //now displays values in textviews so user can see what they scanned
                    TextView text = (TextView) findViewById(R.id.foodName);
                    text.setText(food_name);
                    text = (TextView) findViewById(R.id.calories);
                    text.setText("calories :" + Double.toString(calories));
                    text = (TextView) findViewById(R.id.saturatedfat);
                    text.setText("sat_fat :" + Double.toString(sat_fat));
                    text = (TextView) findViewById(R.id.transfat);
                    text.setText("trans fat :" + Double.toString(tran_fat));
                    text = (TextView) findViewById(R.id.cholestrol);
                    text.setText("cholesterol :" + Double.toString(cholesterol));
                    text = (TextView) findViewById(R.id.sodium);
                    text.setText("sodium :" + Double.toString(sodium));
                    text = (TextView) findViewById(R.id.carbs);
                    text.setText("carbohydrate :" + Double.toString(carbohydrate));
                    text = (TextView) findViewById(R.id.fiber);
                    text.setText("fiber :" + Double.toString(fiber));
                    text = (TextView) findViewById(R.id.protein);
                    text.setText("protein :" + Double.toString(protein));
                    text = (TextView) findViewById(R.id.sugar);
                    text.setText("sugar :" + Double.toString(sugars));
                    text = (TextView) findViewById(R.id.totalfat);
                    text.setText("totalFat :" + Double.toString(totalfat));
                } catch (Exception e) {
                    System.out.println("error in barCodeWebQueery: " + e);
                    Toast.makeText(getApplicationContext(), "Sorry there was an error with this barcode. Please enter manually", Toast.LENGTH_SHORT).show();
                    launchNewIntent();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                System.out.println("error in barCodeWebQueery on ErrorResponse(): " + error);
                Toast.makeText(getApplicationContext(), "Sorry there was an error with this barcode. Please enter manually", Toast.LENGTH_SHORT).show();
                launchNewIntent();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                System.out.println("1337 map : " + MyData);
                return MyData;
            }
        };
        FoodDeclaration.add(FoodStringRequest); ///
        mydb = new meal_foodDBHelper(this);
        final Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check to see if the name is null, if it isn't, insert into the database.
                if (food_name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please wait for the lookup to occur", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (food_name.equals("error")) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    launchNewIntent();
                }
                //now can finally actually insert the food
                mydb.insertFood(food_name, calories, totalfat, tran_fat,
                        sat_fat, cholesterol, sodium, carbohydrate,
                        fiber, sugars, protein);
                //launch food activity activity
                launchNewIntent();
            }
        });

    }

    private void launchNewIntent() {
        Intent intent = new Intent(meal_barCodeWebQuerry.this, meal_Onboarding.class);
        startActivity(intent);
    }

}


