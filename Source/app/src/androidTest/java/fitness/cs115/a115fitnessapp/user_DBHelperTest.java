package fitness.cs115.a115fitnessapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * This class is a test harness for the user_DBHelper class.
 * Instrumentation test, which will execute on an Android device.
 *
 * @author James Kennedy
 * @since 10/17/16
 * @version 2.0
 */
@RunWith(AndroidJUnit4.class)
public class user_DBHelperTest {
    private work_DBHelper user_work_db;
    
    private ArrayList<String> TEST_PROG_LIST = new ArrayList<>(Arrays.asList(
            "testProgram one","_testProgram's\" 2_", "123testPrograms:;|3"));

    private ArrayList<String> TEST_WORK_LIST =new ArrayList<>(Arrays.asList(
            "Chest and Tri's" ,"Back;(and Biceps!)", "12_L3g_D4y_56","Easy Day",
            "EXXTREEME"));

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("fitness.cs115.a115fitnessapp", appContext.getPackageName());

        user_work_db = new work_DBHelper(appContext);
    }

    @Before
    public void pre_load_programs_and_workouts() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        user_work_db = new work_DBHelper(appContext);
        //load test programs
        for (String prog : TEST_PROG_LIST){
            user_work_db.create_program(prog);
        }
        //load test workouts
        for (String work : TEST_WORK_LIST){
            user_work_db.create_workout(work);
        }

    }

    @After
    public void clear_tables() {
        user_work_db.clear_all_tables();
    }


    @Test
    public void get_program_list__test() throws Exception{

        assertEquals("create_program__get_program_list failed",TEST_PROG_LIST,
                user_work_db.get_user_program_list());
    }

    @Test
    public void create_workout__add_work_to_prog__get_workouts__test() throws Exception{
        try {
            get_program_list__test();
            }
        catch (Exception e){
            throw new RuntimeException("aborting: error in method on which this test depends:\n" +
                    "\t create_program__get_program_list__tests(): "+e.getMessage());
        }



        user_work_db.add_work_to_prog("testProgram 1", "Chest and Tri's");
        user_work_db.add_work_to_prog("testProgram 1", "Back;(and Biceps!)");
        user_work_db.add_work_to_prog("testProgram 1", "12_L3g_D4y_56");

        user_work_db.add_work_to_prog("testProgram's 2", "Easy Day");
        user_work_db.add_work_to_prog("testProgram's 2", "EXXTREEME");

        user_work_db.add_work_to_prog("_test; Program\"s 3","12_L3g_D4y_56");

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.get_workouts_from_prog test");

        //print workouts in each program using get_workouts_from_prog
        for (int i = 0; i < TEST_PROG_LIST.size(); ++i) {
            TEST_WORK_LIST = user_work_db.get_workouts_from_prog(TEST_PROG_LIST.get(i));
            System.out.println(TEST_PROG_LIST.get(i) +": " + TEST_WORK_LIST.toString() );
        }
    }


    @Test
    public void remaining_tests() {

        user_work_db.create_program("testProgram 1");
        user_work_db.create_program("testProgram's 2");
        user_work_db.create_program("_test; Program\"s 3");

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.get_user_program_list test");

        TEST_PROG_LIST = user_work_db.get_user_program_list();
        System.out.println("Program List: " + TEST_PROG_LIST.toString() );

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.create_workout tests");

        user_work_db.create_workout("Chest and Tri's");
        user_work_db.create_workout("Back;(and Biceps!)");
        user_work_db.create_workout("12_L3g_D4y_56");
        user_work_db.create_workout("Easy Day");
        user_work_db.create_workout("EXXTREEME");

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.add_work_to_prog tests");

        user_work_db.add_work_to_prog("testProgram 1", "Chest and Tri's");
        user_work_db.add_work_to_prog("testProgram 1", "Back;(and Biceps!)");
        user_work_db.add_work_to_prog("testProgram 1", "12_L3g_D4y_56");

        user_work_db.add_work_to_prog("testProgram's 2", "Easy Day");
        user_work_db.add_work_to_prog("testProgram's 2", "EXXTREEME");

        user_work_db.add_work_to_prog("_test; Program\"s 3","12_L3g_D4y_56");

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.get_workouts_from_prog test");

        //print workouts in each program using get_workouts_from_prog
        for (int i = 0; i < TEST_PROG_LIST.size(); ++i) {
            TEST_WORK_LIST = user_work_db.get_workouts_from_prog(TEST_PROG_LIST.get(i));
            System.out.println(TEST_PROG_LIST.get(i) +": " + TEST_WORK_LIST.toString() );
        }

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.add_exer_to_work tests");


        user_work_db.add_exer_to_work("Chest and Tri's","Dumbell Press","rep",3,10,80,10,60);
        user_work_db.add_exer_to_work("Chest and Tri's","Pushups","rep",4,20,0,10,60);
        user_work_db.add_exer_to_work("Chest and Tri's","Dumbell Tricep Extension, Overhead",
                "rep",4,15,50,10,60);
        user_work_db.add_exer_to_work("Back;(and Biceps!)","Pullups","rep",4,10,0,10,60);
        user_work_db.add_exer_to_work("Back;(and Biceps!)","Dumbell Curls","rep",4,10,30,10,60);
        user_work_db.add_exer_to_work("Back;(and Biceps!)","Planks","timed",4,60,0,10,60);
        user_work_db.add_exer_to_work("12_L3g_D4y_56","Squats","rep",4,10,60,10,60);
        user_work_db.add_exer_to_work("12_L3g_D4y_56","Lunges","rep",4,10,30,10,60);
        user_work_db.add_exer_to_work("12_L3g_D4y_56","Planks","rep",4,60,0,10,60);
        user_work_db.add_exer_to_work("12_L3g_D4y_56","Run a mile","timed",1,10,0,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Bench Press","rep",3,10,100,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Dumbell Press","rep",3,10,80,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Pushups","rep",4,20,0,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Dumbell Tricep Extension, Overhead",
                "rep",4,15,50,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Pullups","rep",4,10,0,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Dumbell Curls","rep",4,10,30,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Planks","timed",4,60,0,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Squats","rep",4,10,60,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Lunges","rep",4,10,30,10,60);
        user_work_db.add_exer_to_work("EXXTREEME","Run a mile","timed",1,10,0,10,60);
        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.create_session tests");

        user_work_db.create_session("Chest and Tri's");
        user_work_db.create_session("Back;(and Biceps!)");
        user_work_db.create_session("Chest and Tri's");
        user_work_db.create_session("Back;(and Biceps!)");
        user_work_db.create_session("EXXTREEME");

        System.out.println(user_work_db);
        System.out.println("starting work_DBHelper.log_set tests");

        user_work_db.log_set(1, 1, 1, 10, 10, 100);
        user_work_db.log_set(2, 1, 2, 10, 9, 110);
        user_work_db.log_set(3, 1, 3, 8,  7, 100);
        user_work_db.log_set(4, 1, 1, 10, 8, 100);

        System.out.println(user_work_db);
        System.out.println("finished work_DBHelper tests");
    }
}
