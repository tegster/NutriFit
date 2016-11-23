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
    static work_DBHelper.ExerciseData exData;

    public work_trackerSetList_adapter(Activity context, work_DBHelper.ExerciseData exerData,
                                       ArrayList<String> setNumbers){
        super(context, R.layout.list_tracker_exer_single, setNumbers);
        this.context = context;
        this.exData = exerData;
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
        setsNumText.setText(String.valueOf(position +1));

        //Target Reps
        repsGoalText.setText(String.valueOf(exData.get_target_reps()) );

        //Current Reps
        repsText.setText(String.valueOf(exData.get_reps_completed().get(position)) );

        //Weight
        weightText.setText(String.valueOf(exData.get_weights_used().get(position)) );

        return rowView;
    }


}
