package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class work_fitnessLog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_overview);

        //@TODO: display list of user's existing workouts

        Button new_work_btn = (Button) findViewById(R.id.new_workout);
        new_work_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_fitnessLog.this, work_createWorkout.class);
                startActivity(intent);
            }
        });

        Button existing_work_btn = (Button) findViewById(R.id.my_workouts);
        existing_work_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_fitnessLog.this, work_userWorkouts.class);
                startActivity(intent);
            }
        });

        //@TODO: add 'workout progress' button to analyze workout history
    }
}
