package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



/**
 * Created by Henry on 10/17/2016.
 */



public class work_tracker_adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] exercises;
    private final String[] currSets;
    private final String[] targetSets;
    private final String[] weights;
    private final String[] statuses;

    public work_tracker_adapter(Activity context,
                         String[] exercises, String[] currSets, String[] targetSets, String[] weights, String[] statuses){
        super(context, R.layout.list_tracker_exer_single, exercises);
        this.context = context;
        this.exercises = exercises;
        this.currSets = currSets;
        this.targetSets = targetSets;
        this.weights = weights;
        this.statuses = statuses;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Create the list view.
        LayoutInflater exerciseListInflater = context.getLayoutInflater();
        View rowView = exerciseListInflater.inflate(R.layout.list_tracker_exer_single, null, true);

        //Declaration
        TextView exerciseNameText = (TextView) rowView.findViewById(R.id.tv_exerciseName);
        TextView setsText = (TextView) rowView.findViewById(R.id.tv_setsDone);
        TextView setsGoalText = (TextView) rowView.findViewById(R.id.tv_targetSets);
        TextView weightText = (TextView) rowView.findViewById(R.id.tv_weight);
        TextView statusText = (TextView) rowView.findViewById(R.id.tv_status);

        //Exercise Name
        exerciseNameText.setText(exercises[position]);

        //Current Set
        setsText.setText(currSets[position]);

        //Target Sets
        setsGoalText.setText(targetSets[position]);

        //Weight
        weightText.setText(weights[position]);


        //Status
        //TODO: Handle changing status depending on the sets completed, in work_tracker
        //Zero sets = Not Started, (Number <= Target) = In Progress, (Number == Target) = Complete
        if (statuses[position].equals("Complete")){
            statusText.setText("Complete");
            statusText.setTextColor(Color.parseColor("#ff669900")); //dark green - complete
        } else if (statuses[position].equals("In Progress")){
            statusText.setText("In Progress");
            statusText.setTextColor(Color.parseColor("#ff0099cc")); //dark blue - in progress
        } else {
            statusText.setText("Not Started");
            statusText.setTextColor(Color.parseColor("#ffcc0000")); //dark red - not started
        }



        return rowView;
    }


}
