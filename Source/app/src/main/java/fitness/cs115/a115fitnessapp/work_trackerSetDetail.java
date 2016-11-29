package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
    String exerciseName;
    int sessID;
    int setInd;
    work_DBHelper work_db;
    TextView onEachSideText;
    TextView plateResult;
    TextView targetRepsText;
    EditText totalWeightInput;
    EditText barWeightInput;
    EditText actualRepsInput;
    Button logButton;
    Button calcPlates;
    int totalWeight;
    int barWeight;
    int goalReps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tracker_setdetail);

        work_db = new work_DBHelper(this);
        //get extras
        Bundle intentExtras = getIntent().getExtras();
        sessID = intentExtras.getInt("sessID");
        setInd = intentExtras.getInt("setIndex");
        exerciseName = intentExtras.getString("eName");
        totalWeight = intentExtras.getInt("goalWeight");
        goalReps = intentExtras.getInt("goalReps");

        title = exerciseName + " - Set " + String.valueOf(setInd +1);
        setTitle(title);

        //Prevent keyboard from opening automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //this is the file that handles actual set logging
        actualRepsInput = (EditText) findViewById(R.id.et_reps);
        targetRepsText = (TextView) findViewById(R.id.tv_targetReps);
        onEachSideText = (TextView) findViewById(R.id.tv_oes);
        plateResult = (TextView) findViewById(R.id.tv_platesNeeded);
        totalWeightInput = (EditText) findViewById(R.id.et_totalWeight);
        barWeightInput = (EditText) findViewById(R.id.et_barWeight);
        logButton = (Button) findViewById(R.id.btn_log);
        calcPlates = (Button) findViewById(R.id.btn_calcPlate);

        //set values according to exercise details
        totalWeightInput.setText(String.valueOf(totalWeight));
        actualRepsInput.setText(String.valueOf(goalReps));
        targetRepsText.setText(String.valueOf(goalReps));
        barWeightInput.setText("45");


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


                int actualReps = Integer.parseInt(actualRepsInput.getText().toString());
                int actualWeight = Integer.parseInt(totalWeightInput.getText().toString());
                work_db.log_set(sessID, exerciseName, setInd, goalReps, actualReps, actualWeight);
                Toast.makeText(work_trackerSetDetail.this, "Good work. Rest until the timer says \"Ready\".", Toast.LENGTH_LONG).show();
                returnToSetList();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        //Calculate plates immediately.
        performPlateCalculation();
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
        String platesNeeded = "Put these plates on each side the bar:\n";
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
        totalWeight = Integer.parseInt(totalWeightInput.getText().toString());
        barWeight = Integer.parseInt(barWeightInput.getText().toString());

        if (barWeight > totalWeight){
            onEachSideText.setText("Invalid weight!");
            plateResult.setText("");
        } else if (barWeight == totalWeight) {
            onEachSideText.setText("Lift the empty bar.");
            plateResult.setText("");
        } else {
            onEachSideText.setText(eachSide(totalWeight, barWeight));
            plateResult.setText(plateCalculator(totalWeight, barWeight));
        }
    }

    private void returnToSetList () {

        Intent setListIntent = new Intent(work_trackerSetDetail.this, work_trackerSetList.class);
        setListIntent.putExtra("eName", exerciseName);
        setListIntent.putExtra("sessID", sessID);
        setListIntent.putExtra("isSetLogged", true);
        setListIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(setListIntent);
        finish();
    }

}
