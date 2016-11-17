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


//Currently not needed. Keep in case; it will be used later as a template.



public class work_tracker_adapter extends ArrayAdapter<String>{

    private final Activity context;

    //Temporary constructor
    work_tracker_adapter(Activity context, String[] programs){
        super(context, R.layout.list_work_single, programs);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Create the list view.
        LayoutInflater programListInflator = context.getLayoutInflater();
        View rowView = programListInflator.inflate(R.layout.list_work_single, null, true);

        //Set program names.
        String programName = getItem(position);
        TextView programNameText = (TextView) rowView.findViewById(R.id.et_entryName);
        programNameText.setText(programName);


        return rowView;
    }


}
