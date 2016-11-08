package fitness.cs115.a115fitnessapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


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

public class meal_barCodeScannerTest extends AppCompatActivity {

    // https://api.nutritionix.com/v1_1/search/cheddar%20cheese?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=[YOURID]&appKey=[YOURKEY]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        String url = "https://api.nutritionix.com/v1_1/item?upc=49000036756&appId=dc7f6afd&appKey=8976d7ae10363be41401e2419d2bddf3";
        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                //  System.out.println("1337: " + response);
                try {
                    JSONObject jObject = new JSONObject(response); // json
                    //    System.out.println(jObject.toString());
                    System.out.println(jObject.toString(2));
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


