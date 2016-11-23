package fitness.cs115.a115fitnessapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



/**
 * Created by Teghpreet Singh on 10/17/2016.
 */



public class meal_editmeal_adapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] foodname;
    private final String[] calories;
    private final String[] fat;
    private final String[] carbs;
    private final String[] protein;

    public meal_editmeal_adapter(Activity context,
                                 String[] foodname, String[] calories, String[] fat, String[] carbs, String[] protein) {
        super(context, R.layout.meal_editmeal_listview, foodname);
        this.context = context;
        this.foodname = foodname;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
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

            LayoutInflater vi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.meal_editmeal_listview, parent, false);


            mViewHolder.foodname = (TextView) convertView.findViewById(R.id.tv_foodName); // title
            mViewHolder.calories = (TextView) convertView.findViewById(R.id.tv_totalcals); // thumb image
            mViewHolder.fat = (TextView) convertView.findViewById(R.id.tv_totalfat); // title
            mViewHolder.carbs = (TextView) convertView.findViewById(R.id.tv_totalcarbs); // thumb image
         //   mViewHolder.protein = (TextView) convertView.findViewById(R.id.tv_protein); // title


            convertView.setTag(mViewHolder);
            //  mViewHolder.cb.setTag(data.get(position));

           /* mViewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {

                    InviteFriends.isChecked[position] = buttonView.isChecked();

                }
            });
            */

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();

        }


        //Exercise Name
        mViewHolder.foodname.setText(foodname[position]);

        //Current Set
        mViewHolder.calories.setText(calories[position]);

        //Target Sets
        mViewHolder.fat.setText(fat[position]);

        //Weight
        mViewHolder.carbs.setText(carbs[position]);

        mViewHolder.protein.setText(protein[position]);


        return convertView;
    }


}
