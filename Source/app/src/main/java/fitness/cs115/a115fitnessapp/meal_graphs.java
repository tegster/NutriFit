package fitness.cs115.a115fitnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Matthew on 11/19/16.
 */

public class meal_graphs extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_graphs);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        int x = 2;
        int y = 7;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                //     new DataPoint(0, 1),
                //   new DataPoint(1, 5),
                // new DataPoint(2, 3),
                //new DataPoint(3, 2),
                //new DataPoint(4, 6),
                new DataPoint(x, y),
                new DataPoint(24, 26)

        });
        graph.addSeries(series);

        // first series is a line
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
        }
        series = new LineGraphSeries<>(points);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);


    }
}
