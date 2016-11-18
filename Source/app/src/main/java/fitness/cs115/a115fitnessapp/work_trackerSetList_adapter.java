package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by Henry on 10/17/2016.
 */



public class work_trackerSetList_adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] setNumbers;
    private final String[] currReps;
    private final String[] targetReps;
    private final String[] weights;

    public work_trackerSetList_adapter(Activity context,
                                       String[] setNumbers, String[] currReps, String[] targetReps, String[] weights){
        super(context, R.layout.list_tracker_exer_single, setNumbers);
        this.context = context;
        this.setNumbers = setNumbers;
        this.currReps = currReps;
        this.targetReps = targetReps;
        this.weights = weights;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Create the list view.
        LayoutInflater exerciseListInflater = context.getLayoutInflater();
        View rowView = exerciseListInflater.inflate(R.layout.list_tracker_set_single, null, true);

        //Declaration
        TextView setsNumText = (TextView) rowView.findViewById(R.id.tv_setNumber);
        TextView repsText = (TextView) rowView.findViewById(R.id.tv_repsDone);
        TextView repsGoalText = (TextView) rowView.findViewById(R.id.tv_targetReps);
        TextView weightText = (TextView) rowView.findViewById(R.id.tv_weight);

        //Set Number
        setsNumText.setText(setNumbers[position]);

        //Current Reps
        repsText.setText(currReps[position]);

        //Target Reps
        repsGoalText.setText(targetReps[position]);

        //Weight
        weightText.setText(weights[position]);



        return rowView;
    }


}
