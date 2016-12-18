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
 * Edited by James Kennedy on 11/20/2016.
 */



public class work_tracker_adapter extends ArrayAdapter<String>{

    private final Activity context;
    private static work_DBHelper.WorkoutData work_data;
    ArrayList<String> strExercises;
    ArrayList<String> strCurrSets;
    ArrayList<String> strTargetSets;
    ArrayList<String> strWeights;
    ArrayList<String> strStatuses;

    public work_tracker_adapter(Activity context, work_DBHelper.WorkoutData workout_data){
        super(context, R.layout.list_tracker_exer_single, workout_data.get_exer_names());
        this.context = context;
        this.work_data = workout_data;
        strExercises = new ArrayList<String>(workout_data.get_exer_count());
        strCurrSets= new ArrayList<String>(workout_data.get_exer_count());
        strTargetSets= new ArrayList<String>(workout_data.get_exer_count());
        strWeights= new ArrayList<String>(workout_data.get_exer_count());
        strStatuses= new ArrayList<String>(workout_data.get_exer_count());
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

        String exName = work_data.get_exer_names().get(position);
        work_DBHelper.ExerciseData currentEx = work_data.get_exercise_data(exName);

        //Exercise Name
        exerciseNameText.setText(currentEx.get_name());

        //Current Set
        setsDoneText.setText(String.valueOf(currentEx.get_sets_completed()));

        //Target Sets
        setsGoalText.setText(String.valueOf(currentEx.get_target_sets()));

        //Weight
        weightText.setText(String.valueOf(
                currentEx.get_weights_used()) );

        //Exercise Completion Status
        //Zero sets = Not Started, (Number <= Target) = In Progress, (Number == Target) = Complete
        if (currentEx.get_sets_completed() == 0 ){
            statusText.setText("Not Started");
            statusText.setTextColor(Color.parseColor("#ffcc0000")); //dark red - not started
        } else if (currentEx.get_sets_completed() < currentEx.get_target_sets()) {
            statusText.setText("In Progress");
            statusText.setTextColor(Color.parseColor("#ff0099cc")); //dark blue - in progress
        } else {
            statusText.setText("Complete");
            statusText.setTextColor(Color.parseColor("#ff669900")); //dark green - complete
        }

        return rowView;
    }


}
