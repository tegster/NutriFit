package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Henry on 10/17/2016.
 */



public class work_tracker_adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final work_DBHelper.Workout_data work_data;
//ArrayList<String> exercises, ArrayList<String> currSets, ArrayList<String> targetSets, ArrayList<String> weights, ArrayList<String> statuses){

    public work_tracker_adapter(Activity context, work_DBHelper.Workout_data workout_data){
        super(context, R.layout.list_tracker_exer_single, workout_data.get_exercises());
        this.context = context;
        this.work_data = workout_data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Create the list view.
        LayoutInflater exerciseListInflater = context.getLayoutInflater();
        View rowView = exerciseListInflater.inflate(R.layout.list_tracker_exer_single, null, true);

        //Declaration
        TextView exerciseNameText = (TextView) rowView.findViewById(R.id.tv_exerciseName);
        TextView setsDoneText = (TextView) rowView.findViewById(R.id.tv_setsDone);
        TextView setsGoalText = (TextView) rowView.findViewById(R.id.tv_targetSets);
        TextView weightText = (TextView) rowView.findViewById(R.id.tv_weight);
        TextView statusText = (TextView) rowView.findViewById(R.id.tv_status);

        //Exercise Name
        exerciseNameText.setText(work_data.get_exer_name_at(position));

        //Current Set
        setsDoneText.setText(work_data.get_current_set_at(position).toString());

        //Target Sets
        setsGoalText.setText(work_data.get_goal_set_at(position).toString());

        //Weight
        weightText.setText(work_data.get_goal_weight_at(position).toString());

        //Exercise Completion Status
        //Zero sets = Not Started, (Number <= Target) = In Progress, (Number == Target) = Complete
        if (work_data.get_current_set_at(position) == 0 ){
            statusText.setText("Not Started");
            statusText.setTextColor(Color.parseColor("#ffcc0000")); //dark red - not started
        } else if (work_data.get_current_set_at(position) < work_data.get_goal_set_at(position)) {
            statusText.setText("In Progress");
            statusText.setTextColor(Color.parseColor("#ff0099cc")); //dark blue - in progress
        } else {
            statusText.setText("Complete");
            statusText.setTextColor(Color.parseColor("#ff669900")); //dark green - complete
        }

        return rowView;
    }


}
