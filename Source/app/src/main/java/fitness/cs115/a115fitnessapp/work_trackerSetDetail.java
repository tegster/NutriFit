package fitness.cs115.a115fitnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Henry on 10/23/2016.
 */

public class work_trackerSetDetail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setdetail);


        //this is the file that handles actual set logging

        TextView onEachSideText = (TextView) findViewById(R.id.tv_oes);
        TextView plateResult = (TextView) findViewById(R.id.tv_platesNeeded);
        EditText totalWeightInput = (EditText) findViewById(R.id.et_totalWeight);
        EditText barWeightInput = (EditText) findViewById(R.id.et_barWeight);
        Button logButton = (Button) findViewById(R.id.btn_log);
        Button calcPlates = (Button) findViewById(R.id.btn_calcPlate);

        //
        //int totalWeight = totalWeightInput.getText().toString();
        //int barWeight = barWeightInput.getText().toString();

        onEachSideText.setText(eachSide(150,45));
        plateResult.setText(plateCalculator(150,45));


    }

    //Take weight from EditTexts and return a string to be used in a TextView.
    public String eachSide(int totalWeight, int barWeight){
        return "On each side: " + String.valueOf((totalWeight - barWeight) / 2.0);
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
        return platesNeeded;
    }

}
