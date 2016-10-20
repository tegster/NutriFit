package fitness.cs115.a115fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.ArrayList;
/*
* Edited by James Kennedy on 10/18/2016
 */

public class work_createWorkout extends AppCompatActivity {

    private final boolean DEBUG = true;
    private work_DBHelper user_work_db;
    private CharSequence[] prog_list;
    private CharSequence[] work_list;

    @Override
    protected void  onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_create_workout);

        user_work_db = new work_DBHelper(this);
        if (DEBUG) {

            System.out.println("starting work_DBHelper tests");
            user_work_db.clear_all_tables();

            dump_tables();

            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.create_program tests");

            user_work_db.create_program("testProgram 1");
            user_work_db.create_program("testProgram's 2");
            user_work_db.create_program("_test; Program\"s 3");

            dump_tables();
            System.out.println( "" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.get_program_list test");

            prog_list = user_work_db.get_program_list();

            System.out.print("Program List: [");

            for (int i = 0; i< prog_list.length; ++i){
                System.out.print(""+prog_list[i]);
                if (i+1 != prog_list.length) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");

            dump_tables();
            System.out.println( "" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.create_workout tests");

            user_work_db.create_workout("Chest and Tri's");
            user_work_db.create_workout("Back;(and Biceps!)");
            user_work_db.create_workout("12_L3g_D4y_56");
            user_work_db.create_workout("Easy Day");
            user_work_db.create_workout("EXXTREEME");

            dump_tables();
            System.out.println( ""+new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.add_work_to_prog tests");

            user_work_db.add_work_to_prog("testProgram 1", "Chest and Tri's");
            user_work_db.add_work_to_prog("testProgram 1", "Back;(and Biceps!)");
            user_work_db.add_work_to_prog("testProgram 1", "12_L3g_D4y_56");

            user_work_db.add_work_to_prog("testProgram's 2", "Easy Day");
            user_work_db.add_work_to_prog("testProgram's 2", "EXXTREEME");

            user_work_db.add_work_to_prog("_test; Program\"s 3","12_L3g_D4y_56");

            dump_tables();
            System.out.println( "" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.get_workouts_from_prog test");

            //print workouts in each program using get_workouts_from_prog
            for (int i = 0; i < prog_list.length; ++i) {

                System.out.print(prog_list[i] +": [" );

                work_list = user_work_db.get_workouts_from_prog(String.valueOf(prog_list[i]));

                //print workouts in each program
                for (int j = 0; j < work_list.length; ++j) {
                    System.out.print(work_list[j].toString());
                    if (j + 1 != work_list.length) {
                        System.out.print(", ");
                    }
                    System.out.println("]");
                }
            }



            dump_tables();
            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.add_exer_to_work tests");

            user_work_db.add_exer_to_work("Chest and Tri's","Bench Press","rep",3,10,100);
            user_work_db.add_exer_to_work("Chest and Tri's","Dumbell Press","rep",3,10,80);
            user_work_db.add_exer_to_work("Chest and Tri's","Pushups","rep",4,20,0);
            user_work_db.add_exer_to_work("Chest and Tri's","Dumbell Tricep Extension, Overhead",
                    "rep",4,15,50);

            dump_tables();
            System.out.println("" + new Exception().getStackTrace()[0]);

            user_work_db.add_exer_to_work("Back;(and Biceps!)","Pullups","rep",4,10,0);
            user_work_db.add_exer_to_work("Back;(and Biceps!)","Dumbell Curls","rep",4,10,30);
            user_work_db.add_exer_to_work("Back;(and Biceps!)","Planks","timed",4,60,0);

            user_work_db.add_exer_to_work("12_L3g_D4y_56","Squats","rep",4,10,60);
            user_work_db.add_exer_to_work("12_L3g_D4y_56","Lunges","rep",4,10,30);
            user_work_db.add_exer_to_work("12_L3g_D4y_56","Planks","rep",4,60,0);
            user_work_db.add_exer_to_work("12_L3g_D4y_56","Run a mile","timed",1,10,0);

            dump_tables();
            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.create_session tests");

            int sess_id = user_work_db.create_session("Chest and Tri's");

            dump_tables();
            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.create_work_log tests");

            //TODO: test user_work_db.create_work_log(sess_id);

            dump_tables();
            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("finished work_DBHelper tests");
        }

    }

    private void dump_tables(){

        System.out.println("prog_index: \n" +
                user_work_db.dump_table(work_DBHelper.PROG_INDEX_TABLE_NAME));
        System.out.println("prog_detail: \n" +
                user_work_db.dump_table(work_DBHelper.PROG_DETAIL_TABLE_NAME));
        System.out.println("work_index: \n" +
                user_work_db.dump_table(work_DBHelper.WORK_INDEX_TABLE_NAME));
        System.out.println("work_detail: \n" +
                user_work_db.dump_table(work_DBHelper.WORK_DETAIL_TABLE_NAME));
        System.out.println("work_sessions: \n" +
                user_work_db.dump_table(work_DBHelper.WORK_SESSIONS_TABLE_NAME));
        System.out.println("work_log: \n" +
                user_work_db.dump_table(work_DBHelper.WORK_LOG_TABLE_NAME));

    };
}
