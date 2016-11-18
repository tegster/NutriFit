package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Henry on 10/23/2016.
 */

public class work_trackerSetDetail extends AppCompatActivity{

    String title = "";
    String sessID = "";
    work_DBHelper work_db;
    TextView onEachSideText;
    TextView plateResult;
    EditText totalWeightInput;
    EditText barWeightInput;
    EditText repsInput;
    Button logButton;
    Button calcPlates;
    int totalWeight;
    int barWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setdetail);

        Intent intent = getIntent();
        sessID = intent.getExtras().getString("sessID");
        title = intent.getExtras().getString("setName");
        setTitle(title);

        //Prevent keyboard from opening automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //this is the file that handles actual set logging



        repsInput = (EditText) findViewById(R.id.et_reps);
        onEachSideText = (TextView) findViewById(R.id.tv_oes);
        plateResult = (TextView) findViewById(R.id.tv_platesNeeded);
        totalWeightInput = (EditText) findViewById(R.id.et_totalWeight);
        barWeightInput = (EditText) findViewById(R.id.et_barWeight);
        logButton = (Button) findViewById(R.id.btn_log);
        calcPlates = (Button) findViewById(R.id.btn_calcPlate);



        //TODO: Grab weight and bar weight from database
        totalWeightInput.setText("300");
        barWeightInput.setText("45");

        //Calculate plates immediately.
        performPlateCalculation();


        //======================================================================================
        //  Buttons
        //======================================================================================
        calcPlates.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                performPlateCalculation();
            }
        });

        logButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Log the set and start the timer!
                Toast.makeText(work_trackerSetDetail.this, "This would have been logged.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    //Take weight from EditTexts and return a string to be used in a TextView.
    public String eachSide(int totalWeight, int barWeight){
        return "On each side: " + String.valueOf((totalWeight - barWeight) / 2.0) + " lbs";
    }

    //Take weight from EditTexts and return a string to be used in a TextView.
    //Precondition: totalWeight must be greater than barWeight.
    public String plateCalculator(int totalWeight, int barWeight) {
        final double[] plates =  {45, 35, 25, 10, 5, 2.5};
        final String[] plateStr = {"45, ", "35, ", "25, ", "10, ", "5, ", "2.5"};
        String platesNeeded = "Put these plates on the bar: ";
        double weight = (totalWeight - barWeight) / 2.0;

        for(int i = 0; i < plates.length; i++){
            double currentPlate = plates[i];
            while(weight - currentPlate >= 0){
                weight = weight - currentPlate;
                platesNeeded = platesNeeded + plateStr[i];
            }
        }

        if (platesNeeded.endsWith(", ")){
            platesNeeded = platesNeeded.substring(0, platesNeeded.length()-2);
        }

        return platesNeeded;
    }

    public void hideKeyboard(){
        if(getCurrentFocus()!= null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void performPlateCalculation(){
        //TODO: Prevent negatives
        totalWeight = Integer.parseInt(totalWeightInput.getText().toString());
        barWeight = Integer.parseInt(barWeightInput.getText().toString());
        onEachSideText.setText(eachSide(totalWeight,barWeight));
        plateResult.setText(plateCalculator(totalWeight,barWeight));
    }


}
