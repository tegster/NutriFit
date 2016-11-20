package fitness.cs115.a115fitnessapp;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;

/**
 * Created by Matthew on 11/19/16.
 */

public class meal_graphs extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_graphs);

        GraphView graph = (GraphView) findViewById(R.id.graph);


        String test1 = "10/19/2016";
        String test2 = "10/17/2016";
        java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
        long epoch = 0;
        long epoch2 = 0;
        Date date;
        try {
            date = df.parse(test1);
            epoch = date.getTime();
            System.out.println("10/19/2016: " + epoch);

            date = df.parse(test2);
            epoch2 = date.getTime();
            System.out.println("10/17/2016: " + epoch);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(epoch, 75),
                new DataPoint(epoch2, 80),
                //  new DataPoint(500, 3)
        });


        graph.addSeries(series);
// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        //    graph.getViewport().setMinX(epoch2);
        //   graph.getViewport().setMaxX(epoch);
        //  graph.getViewport().setXAxisBoundsManual(true);
        graph.setTitle("Calories");




/*Calendar calendar = Calendar.getInstance();
Date d1 = calendar.getTime();
calendar.add(Calendar.DATE, 1);
Date d2 = calendar.getTime();
calendar.add(Calendar.DATE, 1);
Date d3 = calendar.getTime();

GraphView graph = (GraphView) findViewById(R.id.graph);

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
    new DataPoint(d1, 1),
    new DataPoint(d2, 5),
    new DataPoint(d3, 3)
});

graph.addSeries(series);

// set date label formatter
graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
graph.getViewport().setMinX(d1.getTime());
graph.getViewport().setMaxX(d3.getTime());
graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
graph.getGridLabelRenderer().setHumanRounding(false);
*/
    }
}
