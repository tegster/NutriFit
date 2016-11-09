package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static fitness.cs115.a115fitnessapp.R.id.textView;

/**
 * Created by Matthew on 11/8/16.
 */

public class meal_barCodeWebQuerry extends AppCompatActivity {
    private static final boolean DEBUG = true;

    // https://api.nutritionix.com/v1_1/search/cheddar%20cheese?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=[YOURID]&appKey=[YOURKEY]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        String upc = "49000036756"; //defaults to coke. use this in the event that something weird happens
        Intent intent = getIntent();
        try {
            upc = intent.getStringExtra("barcode");
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.v("meal_barCodeWebQuerry", "upc is: " + upc);
        String url = "https://api.nutritionix.com/v1_1/item?upc=" + upc + "&appId=dc7f6afd&appKey=8976d7ae10363be41401e2419d2bddf3";
        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                    String food_name;
                    if (!jObject.isNull("item_name")) {
                        food_name = jObject.getString("item_name");
                    } else {
                        food_name = "error";
                    }
                    double calories = 0;
                    if (!jObject.isNull("nf_calories")) {
                        calories = jObject.getDouble("nf_calories");
                    }
                    double sat_fat = 0;
                    if (!jObject.isNull("nf_saturated_fat")) {
                        sat_fat = jObject.getDouble("nf_saturated_fat");
                    }
                    double tran_fat = 0;
                    if (!jObject.isNull("nf_trans_fatty_acid")) {
                        tran_fat = jObject.getDouble("nf_trans_fatty_acid");
                    }
                    double cholesterol = 0;
                    if (!jObject.isNull("nf_cholesterol")) {
                        cholesterol = jObject.getDouble("nf_cholesterol");
                    }
                    double sodium = 0;
                    if (!jObject.isNull("nf_sodium")) {
                        sodium = jObject.getDouble("nf_sodium");
                    }
                    double carbohydrate = 0;
                    if (!jObject.isNull("nf_total_carbohydrate")) {
                        carbohydrate = jObject.getDouble("nf_total_carbohydrate");
                    }
                    double fiber = 0;
                    if (!jObject.isNull("nf_dietary_fiber")) {
                        fiber = jObject.getDouble("nf_dietary_fiber");
                    }
                    double sugars = 0;
                    if (!jObject.isNull("nf_sugars")) {
                        sugars = jObject.getDouble("nf_sugars");
                    }
                    double protein = 0;
                    if (!jObject.isNull("nf_protein")) {
                        protein = jObject.getDouble("nf_protein");
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

                    }
                    //now displays values in textviews so user can see what they scanned


                } catch (Exception e) {
                    System.out.println(e);
                }
                //  textView.setText(jsonObject.toString(2));

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                System.out.println("error: " + error);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                System.out.println("1337 map : " + MyData);
                //     JSONObject jsonObject = (new JSONObject(MyData)).getJSONObject("");
                //  textView.setText(jsonObject.toString(2));

                return MyData;
            }
        };

        ExampleRequestQueue.add(ExampleStringRequest); ///

    }


}


