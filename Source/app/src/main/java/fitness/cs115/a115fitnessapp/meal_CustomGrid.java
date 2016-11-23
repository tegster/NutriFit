package fitness.cs115.a115fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Matthew on 11/22/16.
 */

public class meal_CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] genreString;
    private final int[] Imageid;

    public meal_CustomGrid(Context c, String[] genreString, int[] Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        this.genreString = genreString;
    }

    @Override
    public int getCount() {
        return genreString.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView;
        ImageView imageView;
        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_single, null);
        } else {
            grid = convertView;
        }
        textView  = (TextView) grid.findViewById(R.id.grid_text);
        imageView = (ImageView) grid.findViewById(R.id.grid_image);
        textView.setText(genreString[position]);
        imageView.setImageResource(Imageid[position]);

        return grid;
    }
}
