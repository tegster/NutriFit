package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Teghpreet Singh on 10/17/2016.
 */



public class meal_editmeal_adapter extends ArrayAdapter<String> {

    private final Activity context;
    private HashMap<String ,HashMap<String ,Double>> macrosmap;
    //private ArrayList<String> foodname = new ArrayList<>();

    //its a set, make an array list, pass thte set and put it in context

    public meal_editmeal_adapter(Activity context,
                                 HashMap<String ,HashMap<String ,Double>> mainmacrosmap, ArrayList<String> foodkeyset) {
       // ArrayList<String> macrolist = new ArrayList<String>(mainmacrosmap.keySet());
        super(context, R.layout.meal_editmeal_listview, foodkeyset);
        this.context = context;
        this.macrosmap = mainmacrosmap;

    }

    //
    public class ViewHolder {

        public TextView foodname;
        public TextView calories;
        public TextView fat;
        public TextView carbs;
        public TextView protein;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;

        if (convertView == null) {

            mViewHolder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.meal_editmeal_listview, parent, false);


            mViewHolder.foodname = (TextView) convertView.findViewById(R.id.tv_foodName); // title
            mViewHolder.calories = (TextView) convertView.findViewById(R.id.tv_totalcals); // thumb image
            mViewHolder.fat = (TextView) convertView.findViewById(R.id.tv_totalfat); // title
            mViewHolder.carbs = (TextView) convertView.findViewById(R.id.tv_totalcarbs); // thumb image
            mViewHolder.protein = (TextView) convertView.findViewById(R.id.tv_totalprotein); // title


            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();

        }

        String tempfoodname = macrosmap.keySet().toArray(new String [macrosmap.size()])[position];
        HashMap<String,Double> innermap = macrosmap.get(tempfoodname);

        //Exercise Name
        mViewHolder.foodname.setText(tempfoodname);

        //Current Set
        mViewHolder.calories.setText(innermap.get("calories").toString());

        //Target Sets
        mViewHolder.fat.setText(innermap.get("fat").toString());

        //Weight
        mViewHolder.carbs.setText(innermap.get("carbs").toString());

        mViewHolder.protein.setText(innermap.get("protein").toString());


        return convertView;
    }


}
