package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Henry on 10/17/2016.
 */



public class work_trackerSetList_adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> setNumbers;
    private final ArrayList<String> currReps;
    private final ArrayList<String> targetReps;
    private final ArrayList<String> weights;

    public work_trackerSetList_adapter(Activity context, ArrayList<String> setNumbers,
                                       ArrayList<String> currReps, ArrayList<String> targetReps,
                                       ArrayList<String> weights){
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
        setsNumText.setText(setNumbers.get(position));

        //Current Reps
        repsText.setText(currReps.get(position));

        //Target Reps
        repsGoalText.setText(targetReps.get(position));

        //Weight
        weightText.setText(weights.get(position));



        return rowView;
    }


}
