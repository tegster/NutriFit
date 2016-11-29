package fitness.cs115.a115fitnessapp;

/**
 * Created by James Kennedy on 10/17/16.
 *
 * parts of this file were adapted from:
 * https://www.tutorialspoint.com/android/android_sqlite_database.htm
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class serves as an interface for adding, modifying and retrieving data
 * from the user workout database.
 *
 * @author James Kennedy
 * @version %I%   %G%
 */
public class work_DBHelper extends SQLiteOpenHelper {

    /**
     * This inner class contains the exercise tracker data for a single exercise.
     *
     * @author James Kennedy
     * @version %I% %G%
     */
    public class ExerciseData {
        private String exercise_name;
        private int exer_id;
        private int target_num_sets;
        private int num_sets_completed;
        private int target_reps;
        private int target_rest_time;
        private int session_id;
        private int start_wt;
        private int incr_wt;
        private ArrayList<Integer> reps_completed;
        private ArrayList<Integer> weights_used;


        public ExerciseData() {
            this.exercise_name = "";
            this.exer_id = -1;
            this.session_id = -1;
            this.target_num_sets = -1;
            this.num_sets_completed = -1;
            this.target_reps = -1;
            this.target_rest_time = -1;
            this.start_wt = -1;
            this.incr_wt = -1;
            this.reps_completed = new ArrayList<>();
            this.weights_used = new ArrayList<>();
        }

        public ExerciseData (String exer_name, int session_ID) {
            load_session_and_exer(exer_name, session_ID);
        }

        public void load_session_and_exer(String exer_name, int session_ID){
            //invariant: exercise name must exist
            if (exer_name == null || !is_taken_exer_name(exer_name)){
                Log.e("ExerciseData","Exercise does not exist: "+exer_name);
                throw new IllegalArgumentException(exer_name);
            }

            //invariant: session must exist
            if ( !is_taken_session_id(session_ID)) {
                Log.e("ExerciseData","session does not exist: "+ session_id);
                throw new IllegalArgumentException("session does not exist: "+ session_ID);
            }

            exercise_name = exer_name;
            session_id = session_ID;


            //load exercise details from DB
            HashMap<String, Integer> exer_db_entry = get_exer_index_entry(exercise_name);
            exer_id = exer_db_entry.get(EXER_INDEX_EXER_ID);
            target_num_sets = exer_db_entry.get(EXER_INDEX_GOAL_SETS);
            target_reps = exer_db_entry.get(EXER_INDEX_REPS);
            target_rest_time = exer_db_entry.get(EXER_INDEX_REST_TIME);

            //TODO: weight adjustment
            start_wt= exer_db_entry.get(EXER_INDEX_START_WEIGHT);
            incr_wt = exer_db_entry.get(EXER_INDEX_INC_WEIGHT);

            //some reps and weights may already be logged. check logs first
            update_logged_entries();
        }

        /**
         * Updates the reps and weight for the sets that have already been logged in this session
         *
         */
        public void update_logged_entries () {

            //load logged set data, fill unlogged sets with 0
            HashMap<String, ArrayList<Integer>> logged_data_lists = get_session_logs_for_exer(exercise_name,session_id);
            ArrayList<Integer> logged_set_nums = logged_data_lists.get(WORK_LOG_SET_NUM);
            ArrayList<Integer> existing_reps =logged_data_lists.get(WORK_LOG_ACTUAL);
            ArrayList<Integer> existing_weights =logged_data_lists.get(WORK_LOG_WEIGHT);

            //TODO: check if logging set uses the set index or set number (ind + 1)
            //fill unlogged sets with exercise standard values
            num_sets_completed = 0;
            for (int set_ind = 0; set_ind < target_num_sets; ++set_ind){
                if (logged_set_nums.contains(set_ind)) {
                    ++num_sets_completed;
                } else {
                    //this set has not yet been logged. intialize reps done to 0.
                    existing_reps.add(set_ind, 0);
                    //increment weight for sets that havent been logged
                    existing_weights.add(set_ind, start_wt + incr_wt * set_ind);
                }
            }
            reps_completed = existing_reps;
            weights_used = existing_weights;
        }

        public int get_session_id() {
            return session_id;
        }

        public int get_start_wt() {
            return start_wt;
        }

        public int get_incr_wt() {
            return incr_wt;
        }

        public ArrayList<Integer> get_weights_used() {
            return weights_used;
        }

        public int get_exer_id() {
            return exer_id;
        }

        public String get_name() {
            return exercise_name;
        }

        public int get_target_sets() {
            return target_num_sets;
        }

        public int get_sets_completed() {
            return num_sets_completed;
        }

        public int get_target_reps() {
            return target_reps;
        }

        public int get_rest_time() {
            return target_rest_time;
        }

        public ArrayList<Integer> get_reps_completed() {
            return reps_completed;
        }

    }

    /**
     * inner class used to retrieve the workout tracker information
     * used to initiate a workout session.
     * @author James Kennedy
     * @version %I% %G%
     */
    public class WorkoutData {
        private String workout_name;
        //map of exercise names to their corresponding ExerciseData objects
        private HashMap<String, ExerciseData> exercises;
        private int exer_count;
        private int session_id;

        public WorkoutData(){
            session_id = -1;
            workout_name = "";
            exercises = new HashMap<String, ExerciseData>();
            exer_count = 0;
        }

        public void reload_session (){
            //invariant: session must exist
            if (!is_taken_session_id(session_id)){
                Log.e("workoutData", "bad session_id: "+session_id);
                throw new IllegalArgumentException();
            }
            if (!is_taken_work_name(workout_name)){
                Log.e("workoutData", "bad workout name: "+workout_name);
                throw new IllegalArgumentException();
            }
            //retrieve exercise names for this workout
            ArrayList<String> exers_in_work = get_exers_from_work(workout_name);

            exercises = new HashMap<String, ExerciseData>();
            exer_count = 0;

            for (String exname: exers_in_work){
                exercises.put(exname, new ExerciseData(exname,session_id));
                ++exer_count;
            }

        }

        public void load_work_session(String _workout_name, int _session_id){
            session_id = _session_id;
            workout_name = _workout_name;
            reload_session();
        }

        public ArrayList<String> get_exer_names(){
            Set<String> names = exercises.keySet();
            ArrayList<String> returnList = new ArrayList<>(names.size());
            for (String name: names) {
                returnList.add(name);
            }
            return returnList;
        }

        public void set_workout_name (String _workout_name){
            workout_name = _workout_name;
        }

        public String get_workout_name() {
            return workout_name;
        }

        public HashMap<String, ExerciseData> get_exercises() {
            return exercises;
        }

        public int get_session_id() {
            return session_id;
        }

        public void set_session_id (int _session_id){
            session_id = _session_id;
        }

        public void add_exercise(ExerciseData exercise) {
            exercises.put(exercise.exercise_name, exercise);
            ++exer_count;
        }

        public ExerciseData get_exercise_data(String exercise_name) {
            return exercises.get(exercise_name);
        }

        public int get_exer_count () {
            return exer_count;
        }
    };

    private static final Boolean DEBUG = true;
    public static final String DATABASE_NAME = "user_work.db";

    /**
    WORK_INDEX: This table will serve as an index of all of the user's created
     workouts. The fields are: work_id (integer unique to each workout);
     name(of the workout, given by user); created_on (datetime string);
     last_used (datetime string); disabled (false unless workout was deleted).
    */
    public static final String WORK_INDEX_TABLE_NAME = "work_index";
    public static final String WORK_INDEX_WORK_ID = "work_id";
    public static final String WORK_INDEX_WORK_NAME = "work_name";
    public static final String WORK_INDEX_CREATED_ON = "created_on";
    public static final String WORK_INDEX_LAST_USED = "last_used";
    public static final String WORK_INDEX_DISABLED = "disabled";
    /*
    WORK_DETAIL: this table will store the list of exercises that make up each
    of the user's workouts. Each row will represent one exercise. The fields
    are: work_id (corresponding to the id assigned to each workout in
    WORK_INDEX table); exercise_id (exercise id, to retrieve data from the
    master exercise database);  name (exercise name given by user); type
    ("rep" or "timed", repetition based vs. timed exercise); sets (goal # of
    sets that should be done for that exercise); reps(goal repetitions per set);
    weight.
    */
    public static final String WORK_DETAIL_TABLE_NAME = "work_detail";
    public static final String WORK_DETAIL_WORK_ID = "work_id";
    public static final String WORK_DETAIL_EXER_ID = "exer_id";
    public static final String WORK_DETAIL_EXER_NAME = "exer_name";
    public static final String WORK_DETAIL_TYPE = "exer_type";
    public static final String WORK_DETAIL_SETS = "sets";
    public static final String WORK_DETAIL_REPS = "reps";
    public static final String WORK_DETAIL_START_WEIGHT = "start_weight";
    public static final String WORK_DETAIL_INC_WEIGHT = "inc_weight";
    public static final String WORK_DETAIL_REST_TIME = "rest_time";
    /*
    WORK_LOG: This table stores the actual exercise log for each time the user
     chooses to do a workout. Each row corresponds to one set of an exercise in
     a workout. Fields: session_id(unique id assigned to each workout iteration.
     Rows have the same session_id if they belong to the same workout session);
     exercise_id (corresponds to exercise_id field of that exercise in
     work_detail table); set_num (current set number for the row); goal(goal
     time or goal number of reps, depending on exercise_type for the given
     exercise); actual (time or reps actually completed); weight.
     */
    public static final String WORK_LOG_TABLE_NAME = "work_log";
    public static final String WORK_LOG_SESSION_ID = "wl_session_id";
    public static final String WORK_LOG_EXER_NAME = "wl_exer_name";
    public static final String WORK_LOG_SET_NUM = "wl_set_num";
    public static final String WORK_LOG_GOAL = "wl_goal";
    public static final String WORK_LOG_ACTUAL = "wl_actual";
    public static final String WORK_LOG_WEIGHT = "wl_weight";
    public static final String WORK_LOG_TIMESTAMP = "wl_timestamp";
    /*
    WORK_SESSIONS: This table stores a log for each time the user chooses to
    begin a workout. The fields are: session_id (unique id assigned to each
    workout iteration); work_id (corresponding to work_id of the workout from
    work_index table); datetime (time session was initiated, stored as text
    in ISO-8601 date/time format).
     */
    public static final String WORK_SESSIONS_TABLE_NAME = "work_sessions";
    public static final String WORK_SESSIONS_SESSION_ID = "session_id";
    public static final String WORK_SESSIONS_WORK_ID = "work_id";
    public static final String WORK_SESSIONS_DATETIME = "date_time";
    /*
    PROG_INDEX: stores a list of the program(s) that have been saved.
     */
    public static final String PROG_INDEX_TABLE_NAME = "prog_index";
    public static final String PROG_INDEX_PROG_ID = "prog_id";
    public static final String PROG_INDEX_PROG_NAME = "prog_name";
    public static final String PROG_INDEX_CREATED_ON = "created_on";
    public static final String PROG_INDEX_LAST_USED = "last_used";
    public static final String PROG_INDEX_DISABLED = "disabled";
    /*
    PROG_DETAIL: stores a list of workouts according to the program(s) they
     belong to.
     */
    public static final String PROG_DETAIL_TABLE_NAME = "prog_detail";
    public static final String PROG_DETAIL_PROG_ID = "prog_id";
    public static final String PROG_DETAIL_WORK_ID = "work_id";
    public static final String PROG_DETAIL_LAST_USED = "last_used";

    /**
     EXER_INDEX: serves as an index of all of the user's exercises.
     The fields are: exer_id (integer unique to each exercise);
     name(of the exercise); created_on (datetime string);
     last_used (datetime string); disabled (false unless exercise was deleted).
     */
    public static final String EXER_INDEX_TABLE_NAME = "exer_index";
    public static final String EXER_INDEX_EXER_ID = "ei_exer_id";
    public static final String EXER_INDEX_EXER_NAME = "ei_exer_name";
    public static final String EXER_INDEX_GOAL_SETS = "ei_sets";
    public static final String EXER_INDEX_REPS = "ei_reps";
    public static final String EXER_INDEX_START_WEIGHT = "ei_start_weight";
    public static final String EXER_INDEX_INC_WEIGHT = "ei_incr_weight";
    public static final String EXER_INDEX_REST_TIME = "ei_rest_time";
    public static final String EXER_INDEX_CREATED_ON = "ei_created_on";
    public static final String EXER_INDEX_LAST_USED = "ei_last_used";
    public static final String EXER_INDEX_DISABLED = "ei_disabled";

    /**
     * List of all tables in the work DB
     */
    public static final String[] WORK_DB_TABLES = {WORK_INDEX_TABLE_NAME,
            WORK_DETAIL_TABLE_NAME, WORK_LOG_TABLE_NAME, WORK_SESSIONS_TABLE_NAME,
            PROG_DETAIL_TABLE_NAME, PROG_INDEX_TABLE_NAME, EXER_INDEX_TABLE_NAME};

    /**
     * formatting used when storing and retrieving dates from the database
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * class constructor
     *
     * @param context context of the workDBHelper.
     */
    public work_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            System.out.println("work_DBHelper: onCreate-ed");
        }
        //create tables if they don't yet exist
        db.execSQL("CREATE TABLE IF NOT EXISTS " + WORK_INDEX_TABLE_NAME + " ("
                + WORK_INDEX_WORK_ID + " integer primary key, "
                + WORK_INDEX_WORK_NAME + " text, "
                + WORK_INDEX_CREATED_ON + " text, "
                + WORK_INDEX_LAST_USED + " text , "
                + WORK_INDEX_DISABLED + " integer default 0)"
        );

        //create tables if they don't yet exist
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EXER_INDEX_TABLE_NAME + " ("
                + EXER_INDEX_EXER_ID + " integer primary key, "
                + EXER_INDEX_EXER_NAME + " text, "
                + EXER_INDEX_GOAL_SETS + " integer, "
                + EXER_INDEX_REPS + " integer, "
                + EXER_INDEX_START_WEIGHT + " integer, "
                + EXER_INDEX_INC_WEIGHT + " integer, "
                + EXER_INDEX_REST_TIME + " integer, "
                + EXER_INDEX_CREATED_ON + " text, "
                + EXER_INDEX_LAST_USED + " text , "
                + EXER_INDEX_DISABLED + " integer default 0)"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + WORK_DETAIL_TABLE_NAME + " "
                        + "(" + WORK_DETAIL_WORK_ID + " integer, "
                        + WORK_DETAIL_EXER_ID + " integer, "
                        + WORK_DETAIL_EXER_NAME + " text, "
                        + WORK_DETAIL_TYPE + " text, "
                        + WORK_DETAIL_SETS + " integer, "
                        + WORK_DETAIL_REPS + " integer, "
                        + WORK_DETAIL_START_WEIGHT + " integer, "
                        + WORK_DETAIL_INC_WEIGHT + " integer, "
                        + WORK_DETAIL_REST_TIME + " integer, "
                        + "primary key (" + WORK_DETAIL_WORK_ID + ", "
                        + WORK_DETAIL_EXER_ID
                        + ") )"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + WORK_LOG_TABLE_NAME + " "
                + "(" + WORK_LOG_SESSION_ID + " integer, "
                + WORK_LOG_EXER_NAME + " text, "
                + WORK_LOG_SET_NUM + " integer, "
                + WORK_LOG_GOAL + " integer, "
                + WORK_LOG_ACTUAL + " integer, "
                + WORK_LOG_WEIGHT + " integer, "
                + WORK_LOG_TIMESTAMP + " text, "
                + "primary key (" + WORK_LOG_SESSION_ID + ", "
                + WORK_LOG_EXER_NAME + ", " + WORK_LOG_SET_NUM
                + ") )"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + WORK_SESSIONS_TABLE_NAME
                + " (" + WORK_SESSIONS_SESSION_ID + " integer primary key, "
                + WORK_SESSIONS_WORK_ID + " integer, "
                + WORK_SESSIONS_DATETIME + " text "
                + ")"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PROG_INDEX_TABLE_NAME + " "
                + "(" + PROG_INDEX_PROG_ID + " integer primary key, "
                + PROG_INDEX_PROG_NAME + " text, "
                + PROG_INDEX_CREATED_ON + " text, "
                + PROG_INDEX_LAST_USED + " text, "
                + PROG_INDEX_DISABLED + " integer default 0 )"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PROG_DETAIL_TABLE_NAME + " "
                + "(" + PROG_DETAIL_PROG_ID + " integer, "
                + PROG_DETAIL_WORK_ID + " integer, "
                + PROG_DETAIL_LAST_USED + " text, "
                + "primary key (" + PROG_DETAIL_PROG_ID + ", "
                + PROG_DETAIL_WORK_ID
                + ") )"
        );
    }

    public void reset_default_values() {
        if (DEBUG) {
            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("starting work_DBHelper.reset_default_values()");
        }

        clear_all_tables();

        create_program("Default Program 1");
        create_program("Default Program 2");
        create_program("Default Program 3");

        create_workout("Chest and Tri's");
        create_workout("Back and Biceps");
        create_workout("Leg Day");
        create_workout("Easy Day");
        create_workout("EXXTREEME");

        add_work_to_prog("Default Program 1", "Chest and Tri's");
        add_work_to_prog("Default Program 1", "Back and Biceps");
        add_work_to_prog("Default Program 1", "Leg Day");
        add_work_to_prog("Default Program 2", "Easy Day");
        add_work_to_prog("Default Program 2", "EXXTREEME");
        add_work_to_prog("Default Program 3", "Leg Day");

        create_exercise("Bench Press", 3, 10, 100, 10, 60);
        create_exercise("Dumbell Press", 3, 10, 80, 10, 60);
        create_exercise("Pushups", 4, 20, 0, 10, 60);
        create_exercise("Dumbell Tricep Extension, Overhead",
                4, 15, 50, 10, 60);
        create_exercise("Pullups", 4, 10, 0, 10, 60);
        create_exercise("Dumbell Curls", 4, 10, 30, 10, 60);
        create_exercise("Planks", 4, 60, 0, 10, 60);
        create_exercise("Squats", 4, 10, 60, 10, 60);
        create_exercise("Lunges", 4, 10, 30, 10, 60);
        create_exercise("Planks", 4, 60, 0, 10, 60);
        create_exercise("Run a mile", 1, 10, 0, 10, 60);
        create_exercise("Bench Press", 3, 10, 100, 10, 60);
        create_exercise("Dumbell Press", 3, 10, 80, 10, 60);
        create_exercise("Pushups", 4, 20, 0, 10, 60);
        create_exercise("Dumbell Tricep Extension, Overhead",
                4, 15, 50, 10, 60);
        create_exercise("Pullups", 4, 10, 0, 10, 60);
        create_exercise("Dumbell Curls", 4, 10, 30, 10, 60);
        create_exercise("Planks", 4, 60, 0, 10, 60);
        create_exercise("Squats", 4, 10, 60, 10, 60);
        create_exercise("Lunges", 4, 10, 30, 10, 60);
        create_exercise("Run a mile", 1, 10, 0, 10, 60);

        add_exer_to_work("Chest and Tri's", "Bench Press");
        add_exer_to_work("Chest and Tri's", "Dumbell Press");
        add_exer_to_work("Chest and Tri's", "Pushups");
        add_exer_to_work("Chest and Tri's", "Dumbell Tricep Extension, Overhead");
        add_exer_to_work("Back and Biceps", "Pullups");
        add_exer_to_work("Back and Biceps", "Dumbell Curls");
        add_exer_to_work("Back and Biceps", "Planks");
        add_exer_to_work("Leg Day", "Squats");
        add_exer_to_work("Leg Day", "Lunges");
        add_exer_to_work("Leg Day", "Planks");
        add_exer_to_work("Leg Day", "Run a mile");
        add_exer_to_work("EXXTREEME", "Bench Press");
        add_exer_to_work("EXXTREEME", "Dumbell Press");
        add_exer_to_work("EXXTREEME", "Pushups");
        add_exer_to_work("EXXTREEME", "Dumbell Tricep Extension, Overhead");
        add_exer_to_work("EXXTREEME", "Pullups");
        add_exer_to_work("EXXTREEME", "Dumbell Curls");
        add_exer_to_work("EXXTREEME", "Planks");
        add_exer_to_work("EXXTREEME", "Squats");
        add_exer_to_work("EXXTREEME", "Lunges");
        add_exer_to_work("EXXTREEME", "Run a mile");
        if (DEBUG) {
            //mark current line num
            System.out.println("" + new Exception().getStackTrace()[0]);
            System.out.println("work_DBHelper.reset_default_values() finished");
            System.out.println(this);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop_all_tables();

        onCreate(db);
    }

    //returns true if programName is found in the prog_index
    public boolean is_taken_prog_name(String programName) {
        int found = get_prog_id_from_name(programName);

        if (found == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the workout already exists in the database.
     *
     * @param workName
     * @return true if workName is found in the work_index table, false otherwise.
     */
    public boolean is_taken_work_name(String workName) {
        int found = get_work_id_from_name(workName);
        if (found == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * checks if an exercise with the given name can be added to the given workout.
     * precondition:
     *
     * @param exercise name of the new exercise to be added to the workout
     * @param workout  name of the wrokout to add a new exercise to. must already
     *                 exist in the database. use is_taken_work_name to check.
     * @return true if a new exercise with the given name can be added to the
     * existing workout, false otherwise.
     * @throws IllegalArgumentException (unchecked) if the workout specified does
     *                                  not exist.
     */
    public boolean can_add_exer_to_work(String exercise, String workout) {
        //check precondition
        if (!is_taken_work_name(workout)) {
            throw new IllegalArgumentException("workout name " + workout
                    + " does not exist");
        }
        ArrayList<String> exers_in_work = this.get_exers_from_work(workout);

        if (exers_in_work.contains(exercise)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * creates a new entry in prog_index and returns it's prog_id.
     * <p>
     * Precondition: program_name must not match any existing program
     *
     * @see #is_taken_prog_name(String) to check that programName is not taken
     * before using this function. Throws unchecked
     * RuntimeException if the precondition is violated.
     */
    public int create_program(String program_name) {
        long new_prog_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROG_INDEX_PROG_NAME, program_name);
        contentValues.put(PROG_INDEX_CREATED_ON, current_time_ISO8601());
        contentValues.put(PROG_INDEX_LAST_USED, "never");

        //invariant: program_name doesn't already exist in prog_index
        if (is_taken_prog_name(program_name)) {
            throw new IllegalArgumentException("Error creating program: name \"" +
                    program_name + "\" is already taken.");
        }
        new_prog_id = db.insert(PROG_INDEX_TABLE_NAME, null, contentValues);

        if (DEBUG) {
            if (new_prog_id > 0) {
                System.out.println(program_name + " successfully inserted into "
                        +  PROG_INDEX_TABLE_NAME + " with prog_id: " + new_prog_id);
            } else {
                System.out.println(program_name + "create_program failed for: "
                        + PROG_INDEX_TABLE_NAME);
            }
        }
        db.close();

        return (int) new_prog_id;
    }

    /**
     * Disables the program in prog_index so that it doesnt show up in the
     * user's program list. Note that the program isn't removed from the index,
     * but the 'disabled' attribute of the program is set true so that it isn't
     * retrieved by a call to get_user_program_list().
     * <p>
     * Precondition: program_name must match an existing program
     *
     * @see #is_taken_prog_name(String) to check that programName is not taken
     * before using this function. Throws unchecked
     * RuntimeException if the precondition is violated.
     */
    public int delete_program(String program_name) {
        //TODO: AMEND DELETE_PROGRAM TO DO AS IT SHOULD
        long new_prog_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROG_INDEX_DISABLED, 1);

        //invariant: program_name doesn't already exist in prog_index
        if (!is_taken_prog_name(program_name)) {
            throw new IllegalArgumentException("Error deleting program: name \"" +
                    program_name + "\" is not taken.");
        }
        new_prog_id = db.update(PROG_INDEX_TABLE_NAME, contentValues,
                PROG_INDEX_PROG_NAME + " = ?", new String[]{program_name});

        if (DEBUG) {
            if (new_prog_id > 0) {
                System.out.println(program_name + " successfully deleted.");
            } else {
                System.out.println(program_name + " delete_program failed.");
            }
        }




    //invariant: program_name doesn't already exist in prog_index
        if (!is_taken_prog_name(program_name)) {
            throw new IllegalArgumentException("Error deleting program: program \"" +
                    program_name + "\" is not found.");
        }
        new_prog_id = db.update(PROG_INDEX_TABLE_NAME, contentValues,
                PROG_INDEX_PROG_NAME + " = ?",new String []{program_name});

        if (DEBUG) {
            if (new_prog_id > 0) {
                System.out.println(program_name + " successfully inserted into "
                        + PROG_INDEX_TABLE_NAME + " with prog_id: " + new_prog_id
                );
            } else {
                System.out.println(program_name + "create_program failed for: "
                        + PROG_INDEX_TABLE_NAME);
            }
        }
        db.close();

        return (int) new_prog_id;
    }

    /**
     * Adds a workout to an existing program.
     *
     * @param prog_name program to be added to. must already exist, use
     *                  is_taken_prog_name(String) to check.
     * @param work_name workout to be added. If it doesn't already exist, an
     *                  empty workout with this name will be created.
     * @return the work_id assigned to the workout. -1 for failure.
     */
    public int add_work_to_prog(String prog_name, String work_name) {
        long conf_prog_id = -1;
        int prog_id = get_prog_id_from_name(prog_name);
        int work_id = get_work_id_from_name(work_name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (DEBUG) {
            System.out.println("starting add_work_to_prog: "
                    + new Exception().getStackTrace()[0]);
        }
        //invariant: workout and program must exist
        if (!is_taken_prog_name(prog_name)) {
            throw new RuntimeException("Error adding work to prog: program\"" +
                    prog_name + "\" was not found.");
        }
        if (!is_taken_work_name(work_name)) {
            throw new RuntimeException("Error adding work to prog: workout \"" +
                    work_name + "\" was not found.");
        }
        //invariant: program must not already contain this workout
        ArrayList<String> prog_workouts = get_workouts_in_prog(prog_name);
        if (prog_workouts.contains(prog_name)) {
            throw new RuntimeException("Error adding work to prog: program\"" +
                    prog_name + "\" already contains workout \""+work_name+"\"");
        }


        contentValues.put(PROG_DETAIL_PROG_ID, prog_id);
        contentValues.put(PROG_DETAIL_WORK_ID, work_id);
        contentValues.put(PROG_DETAIL_LAST_USED, "never");

        conf_prog_id = db.insert(PROG_DETAIL_TABLE_NAME, null, contentValues);
        if (DEBUG) {
            if (conf_prog_id == -1)
                System.out.println(work_name + "failed to insert in  "
                        + prog_name);
            else
                System.out.println(work_name + " inserted in  " + prog_name);
        }
        db.close();
        return (int) conf_prog_id;
    }

    /**
     * This function returns an Arraylist containing the user's program names
     *
     * @return ArrayList of user's program names. returns empty list if the user
     * has no programs.
     */
    public ArrayList<String> get_user_program_list() {
        ArrayList<String> p_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor res = db.rawQuery(
//                "select " + PROG_INDEX_PROG_NAME + " from " +
//                        PROG_INDEX_TABLE_NAME, null);

        //retrieve the names of all programs in the prog index table
        Cursor res = db.query(PROG_INDEX_TABLE_NAME,
                new String[]{PROG_INDEX_PROG_NAME},
                PROG_INDEX_DISABLED + " = 0",
                null, null, null, null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            //add the current row in the table
            p_list.add(res.getString(res.getColumnIndex(
                    PROG_INDEX_PROG_NAME)));
            res.moveToNext();
        }
        res.close();

        return p_list;
    }

    /**
     * creates a new workout in work_index with the name passed to the function.
     * Workout name must not match an already existing workout. Use
     *
     * @param workout_name must not match the name of any existing workout.
     *                     Cannot be null.
     * @return work_id of inserted workout, or -1 if an error occurred
     * @see #is_taken_work_name(String) to check precondition.
     * note: defaults "never" for last_used field.
     */
    public int create_workout(String workout_name) {
        long inserted_id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //invariant: workout name must not be empty
        if (workout_name == null || workout_name.length() == 0) {
            throw new RuntimeException("Error creating workout: "
                    + "no workout name has been specified.");
        }
        contentValues.put(WORK_INDEX_WORK_NAME, workout_name);
        contentValues.put(WORK_INDEX_CREATED_ON, current_time_ISO8601());
        contentValues.put(WORK_INDEX_LAST_USED, "never");

        inserted_id = db.insert(WORK_INDEX_TABLE_NAME, null, contentValues);
        db.close();
        return (int) inserted_id;
    }

    /**
     * Adds an existing exercise to an existing workout.
     *
     * @param work_name        name of the workout to be added to.
     * @param exer_name        name of the exercise to be added.
     * @return inserted item's work_id, or -1 if insertion fails.
     * @see #is_taken_work_name(String) to check a precondition.
     * @see #is_taken_exer_name(String) to check a precondition.
     */
    public int add_exer_to_work(String work_name, String exer_name) {
        long confirm_row_id = -1;
        int work_id = get_work_id_from_name(work_name);
        int exer_id = get_exer_id_from_name(exer_name);
        //TODO: change exer_type to retrieve exercise type if timed exercises are allowed
        String exer_type = "reps";
        ArrayList<String> exers_in_work;

        //invariant: workout already exists
        if (!is_taken_work_name(work_name)) {
            throw new IllegalArgumentException("Error adding exercise \"" + exer_name +
                    "\" to workout \"" + work_name +
                    "\": workout does not exist.");
        }
        //invariant: exercise already exists
        if (!is_taken_exer_name(exer_name)) {
            throw new IllegalArgumentException("Error adding exercise \"" + exer_name +
                    "\" to workout \"" + work_name +
                    "\": exercise does not exist.");
        }
        //invariant: exercise is not already part of program
        exers_in_work = get_exers_from_work(work_name);
        if (exers_in_work.contains(exer_name)) {
            throw new IllegalArgumentException("Error adding exercise \"" + exer_name +
                    "\" to workout \"" + work_name + "\": " +
                    "this exercise is already part of this workout.");
        }

        HashMap<String, Integer> exer_details =get_exer_index_entry(exer_name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(WORK_DETAIL_WORK_ID, work_id);
        contentValues.put(WORK_DETAIL_EXER_ID, exer_id);
        contentValues.put(WORK_DETAIL_EXER_NAME, exer_name);
        contentValues.put(WORK_DETAIL_TYPE, exer_type);
        contentValues.put(WORK_DETAIL_SETS, exer_details.get(EXER_INDEX_GOAL_SETS));
        contentValues.put(WORK_DETAIL_REPS, exer_details.get(EXER_INDEX_REPS));
        contentValues.put(WORK_DETAIL_START_WEIGHT, exer_details.get(EXER_INDEX_START_WEIGHT));
        contentValues.put(WORK_DETAIL_INC_WEIGHT,exer_details.get(EXER_INDEX_INC_WEIGHT));
        contentValues.put(WORK_DETAIL_REST_TIME, exer_details.get(EXER_INDEX_REST_TIME));

        confirm_row_id = db.insert(WORK_DETAIL_TABLE_NAME, null, contentValues);
        db.close();
        return (int) confirm_row_id;
    }

    /**
     * Retrieves the names of the exercises assigned to the given workout.
     *
     * @param work_name the workout to retrieve. Must have already been created.
     * @return list of exercise names that have been assigned to work_name. Will
     * return an empty list if no exercises have been added yet.
     * @see #is_taken_work_name(String) to check that work_name has been created.
     */
    public ArrayList<String> get_exers_from_work(String work_name) {
        ArrayList<String> exer_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int work_id = get_work_id_from_name(work_name);
        String exer;
        //invariant: workout already exists
        if (!is_taken_work_name(work_name)) {
            throw new RuntimeException("Error getting exercises from workout \""
                    + work_name + "\": workout does not exist.");
        }

        //retrieve the names of all exercises from the desired workout
        Cursor res = db.rawQuery(
                "select " + WORK_DETAIL_EXER_NAME + " from "
                        + WORK_DETAIL_TABLE_NAME + " where "
                        + WORK_DETAIL_WORK_ID + " = ? ",
                new String[]{String.valueOf(work_id)});

        res.moveToFirst();
        while (!res.isAfterLast()) {
            //add the current workout name to the list of names
            exer = res.getString(res.getColumnIndex(WORK_DETAIL_EXER_NAME));
            exer_list.add(exer);
            res.moveToNext();
        }
        res.close();

        return exer_list;
    }


    /**
     * Retrieves the names of the exercise assigned to the given program.
     *
     * @param prog_name program to retrieve. Must have already been created.
     * @return list of workout names that have been assigned to the program.
     * Returns an empty list if no workouts have been added yet.
     * @see is_taken_prog_name(String) to check that prog_name has been created.
     */
    public ArrayList<String> get_workouts_in_prog(String prog_name) {
        ArrayList<String> w_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int prog_id = get_prog_id_from_name(prog_name);
        int work_id = -1;
        //retrieve the id's of all workouts from the desired program
        Cursor res = db.rawQuery(
                "select " + PROG_DETAIL_WORK_ID + " from "
                        + PROG_DETAIL_TABLE_NAME + " where "
                        + PROG_DETAIL_PROG_ID + " = ?",
                new String[]{String.valueOf(prog_id)}
        );

        res.moveToFirst();
        while (!res.isAfterLast()) {
            //add the current workout name to the list of names
            work_id = res.getInt(res.getColumnIndex(PROG_DETAIL_WORK_ID));
            w_list.add(get_work_name_from_id(work_id));
            res.moveToNext();
        }
        res.close();

        return w_list;
    }

    /*
    This function creates a new session entry in work_sessions for the workout
    specified and returns it's session_id.
     */
    public int create_session(String work_name) {
        long session_id = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int work_id = get_work_id_from_name(work_name);

        contentValues.put(WORK_SESSIONS_WORK_ID, work_id);
        contentValues.put(WORK_SESSIONS_DATETIME, current_time_ISO8601());
        //invariant:work_name exists in work_index
        if (!is_taken_work_name(work_name)) {
            throw new IllegalArgumentException("Error creating session: workout \""
                    + work_name + "\" does not exist.");
        }

        session_id = db.insert(WORK_SESSIONS_TABLE_NAME, null, contentValues);

        db.close();
        return (int) session_id;
    }

    /**
     * records the user's performance in a single set of an exercise. exercise_id
     * and session_id must already exist.
     *
     * @param session_id the current workout session ID. must already have been
     *                   created.
     * @param exer_name  the current exercise. the exercise must be a part
     *                   of the workout associated with the session_id.
     * @param set_num    the current set number for the current session and exercise
     * @param goal       the target number of repetitions for the set to be logged
     * @param actual     the number of repetitions the user actually achieved for
     *                   the set to be logged
     * @param weight     the amount of weight used for the set to be logged
     * @return the ID of the new set log entry, if creation was successful.
     * returns -1 if insertion fails.
     */
    public int log_set(int session_id, String exer_name, int set_num,
                       int goal, int actual, int weight) {

        if (DEBUG) {
            System.out.println("Starting: "+new Exception().getStackTrace()[0]);
        }
        long log_id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //invariant: session_id already exists
        if (!is_taken_session_id(session_id)) {
            throw new RuntimeException("Error logging set: session_id "
                    + session_id + " does not exist.");
        }

        contentValues.put(WORK_LOG_SESSION_ID, session_id);
        contentValues.put(WORK_LOG_EXER_NAME, exer_name);
        contentValues.put(WORK_LOG_SET_NUM, set_num);
        contentValues.put(WORK_LOG_GOAL, goal);
        contentValues.put(WORK_LOG_ACTUAL, actual);
        contentValues.put(WORK_LOG_WEIGHT, weight);
        contentValues.put(WORK_LOG_TIMESTAMP, current_time_ISO8601());

        log_id = db.insert(WORK_LOG_TABLE_NAME, null, contentValues);
        db.close();

        if (DEBUG) {
            System.out.println("log: " +contentValues+ " inserted with log id: "+log_id);
        }
        return (int) log_id;
    }

    /**
     * checks whether session_id has been assigned to an existing session.
     *
     * @param session_id the session ID to be checked
     * @return true if session_id has already been assigned to a session,
     * false if the session_id is not yet taken
     */
    public boolean is_taken_session_id(int session_id) {
        if (DEBUG) {
            System.out.println("starting: " +
                    new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        //retrieve row with desired work_id
        Cursor res = db.query(WORK_SESSIONS_TABLE_NAME,
                new String[]{WORK_SESSIONS_SESSION_ID},
                WORK_SESSIONS_SESSION_ID + " = ? ",
                new String[]{String.valueOf(session_id)},
                null, null, null);

        res.moveToFirst();
        boolean found = false;
        if (res.isAfterLast()) {
            if (DEBUG) {
                System.out.println("session_id: " + session_id + " not found");
            }
            found = false;
        } else {
            if (DEBUG) {
                System.out.println("session_id: " + session_id + "  found");
            }
            found = true;
        }
        res.close();
        return found;

    }

    //gets contents of the table specified as a String. table name must match
    //a table in WORK_DB_TABLES.
    private String get_table(String table_name) {
        //col_padding used to add empty space between columns
        int col_padding = 2;
        SQLiteDatabase db = this.getReadableDatabase();
        final List<String> TABLE_LIST = Arrays.asList(WORK_DB_TABLES);
        //invariant: table_name is a valid table
        if (!TABLE_LIST.contains(table_name)) {
            throw new RuntimeException("Error getting table: \""
                    + table_name + "\": not a valid table name.");
        }

        Cursor res = db.rawQuery("select * from " + table_name, null);
        String[] col_names = res.getColumnNames();
        String output = "";
        String row_str = "", col_entry = "";
        int num_cols = res.getColumnCount();
        int num_rows = res.getCount();
        int max_col_widths[] = new int[num_cols];
        //str_len_priorityQs used to find length of longest string in each column
        ArrayList<PriorityQueue<Integer>> str_len_priorityQs =
                new ArrayList<PriorityQueue<Integer>>(num_cols);
        //col_data contains one ArrayList to represent each column of data
        ArrayList<ArrayList<String>> col_data =
                new ArrayList<ArrayList<String>>(num_cols);

        //initialize column data list and priority Qs
        for (int col_i = 0; col_i < num_cols; ++col_i) {
            //size of each column List is num_rows + 1 for column names
            col_data.add(new ArrayList<String>(num_rows + 1));
            str_len_priorityQs.add(new PriorityQueue<Integer>(num_rows + 1,
                    Collections.reverseOrder()));
        }
        //load column names
        for (int col_j = 0; col_j < num_cols; ++col_j) {
            col_data.get(col_j).add(col_names[col_j]);
            str_len_priorityQs.get(col_j).add(Integer.valueOf(
                    col_names[col_j].length()));
        }

        res.moveToFirst();
        //retrieve all column entries to calc col_width for each column
        while (!res.isAfterLast()) {
            //retrieve the current row in the table
            for (int col_i = 0; col_i < num_cols; ++col_i) {
                //add data to its respective column List
                col_entry = res.getString(res.getColumnIndex(col_names[col_i]));
                col_data.get(col_i).add(col_entry);
                str_len_priorityQs.get(col_i).add(col_entry.length());
            }
            res.moveToNext();
        }
        res.close();

        //get max string length of each column from Priority Q if one exists
        for (int col_i = 0; col_i < num_cols; ++col_i) {
            max_col_widths[col_i] = str_len_priorityQs.get(col_i).size() > 0 ?
                    str_len_priorityQs.get(col_i).peek().intValue() + col_padding :
                    col_padding;
        }

        //print formatted columns to output String. +1 to print col headers
        for (int row_i = 0; row_i < num_rows + 1; ++row_i) {
            row_str = "";
            for (int col_j = 0; col_j < num_cols; ++col_j) {
                col_entry = col_data.get(col_j).get(row_i);
                //print entries with the width associated with their column
                row_str += String.format("%-" + max_col_widths[col_j] + "s",
                        col_entry);
            }
            output += row_str + "\n";
        }

        return output;

    }

    /**
     * gets number of rows in the table specified.
     *
     * @param table_name table to count rows from. must match a string in
     *                   WORK_DB_TABLES.
     * @return number of rows in table
     */
    public int get_num_rows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    /**
     * deletes all entries from each table in WORK_DB_TABLES. does not drop
     * the tables from the database, only empties them.
     */
    public void clear_all_tables() {
        SQLiteDatabase db = this.getWritableDatabase();

        if (DEBUG) {
            System.out.println("Clearing all tables.");
        }

        for (String t_name : WORK_DB_TABLES) {
            db.delete(t_name, null, null);
        }
        db.close();
    }

    /**
     * prints all tables in WORK_DB_TABLES
     */
    @Override
    public String toString() {
        String tables_output = new String();

        for (String t_name : WORK_DB_TABLES) {
            tables_output += t_name + ": \n" + this.get_table(t_name) + "\n";
        }

        return tables_output;
    }

    /*
    Returns current date and time as ISO 8601 formatted String
    Date conversion taken from here:
    http://beginnersbook.com/2013/05/current-date-time-in-java/
     */
    private String current_time_ISO8601() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date dateobj = new Date();
        String date_str = df.format(dateobj);

        return date_str;
    }

    /*
    Converts ISO 8601 formatted String date representation into a Date object
     */
    private Date string_ISO8601_to_Date(String date_str) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);

        Date date_obj;
        try {
            date_obj = df.parse(date_str);
        } catch (ParseException pe) {
            date_obj = null;
        }

        return date_obj;
    }

    //returns prog_id if found, -1 if not found
    private int get_prog_id_from_name(String prog_name) {

        if (DEBUG) {
            System.out.println("starting: " + new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        int prog_id = -1;

        Cursor res = db.query(PROG_INDEX_TABLE_NAME, new String[]{PROG_INDEX_PROG_ID},
                PROG_INDEX_PROG_NAME + " = ?", new String[]{prog_name},
                null, null, null);

        res.moveToFirst();

        if (!res.isAfterLast()) {
            prog_id = res.getInt(res.getColumnIndex(PROG_INDEX_PROG_ID));
        }
        if (DEBUG) {
            System.out.println("prog_id: " + prog_id + "  found for prog_name: "
                    + prog_name);
        }
        res.close();

        return prog_id;
    }

    //returns work_id if found, -1 if not found
    private int get_work_id_from_name(String work_name) {

        if (DEBUG) {
            System.out.println("starting: " +
                    new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        int work_id = -1;

        //retrieve the work ID if the workout name matches work_name for the row
        Cursor res = db.query(WORK_INDEX_TABLE_NAME,
                new String[]{WORK_INDEX_WORK_ID},
                WORK_INDEX_WORK_NAME + " = ?",
                new String[]{work_name}, null, null, null);

        res.moveToFirst();
        if (!res.isAfterLast()) {
            work_id = res.getInt(res.getColumnIndex(WORK_INDEX_WORK_ID));
        }
        if (DEBUG) {
            System.out.println("work_id: " + work_id + "  found for work_name: " + work_name);
        }
        res.close();

        return work_id;
    }


    //returns work_name if found, null if not found
    private String get_work_name_from_id(int work_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String work_name = null;

        //retrieve row with desired work_id
        Cursor res = db.query(WORK_INDEX_TABLE_NAME, new String[]{
                        WORK_INDEX_WORK_NAME}, WORK_INDEX_WORK_ID + " = ? ",
                new String[]{String.valueOf(work_id)},
                null, null, null);

        res.moveToFirst();
        if (!res.isAfterLast()) {
            work_name = res.getString(res.getColumnIndex(WORK_INDEX_WORK_NAME));
        }
        res.close();

        if (DEBUG) {
            System.out.println("work_name: " + work_name + "  found for work_id: " + work_id);
        }
        return work_name;
    }

    //returns work_id if found, -1 if not found
    private int get_exer_id_from_name(String exer_name) {
        if (DEBUG) {
            System.out.println("starting: " +
                    new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        int exer_id = -1;

        //retrieve the exer ID if the exercise name matches exer_name for the row
        Cursor res = db.query(EXER_INDEX_TABLE_NAME,
                new String[]{EXER_INDEX_EXER_ID},
                EXER_INDEX_EXER_NAME + " = ?",
                new String[]{exer_name}, null, null, null);

        res.moveToFirst();
        if (!res.isAfterLast()) {
            exer_id = res.getInt(res.getColumnIndex(EXER_INDEX_EXER_ID));
        }
        if (DEBUG) {
            System.out.println("exer_id: " + exer_id + "  found for exer_name: " + exer_name);
        }
        res.close();

        return exer_id;
    }

    private void drop_all_tables() {
        SQLiteDatabase db = this.getWritableDatabase();
        int num_tables = WORK_DB_TABLES.length;
        if (DEBUG) {
            System.out.println("all tables dropping like flies");
        }
        for (int ind = 0; ind < num_tables; ++ind) {
            db.execSQL("DROP TABLE IF EXISTS " + WORK_DB_TABLES[ind]);
        }
        db.close();
    }

    /**
     * retrieves the next workout in the sequence of workouts for the given
     * program.
     * @param program name of the program to determine next workout for
     * @return the name of the next workout to perform.
     */
    public String get_next_workout(String program) {
        ArrayList<String> workList = get_workouts_in_prog(program);

        //TODO: change get_next_workout to intelligently choose next workout
        return workList.get(0);
    }

    /**
     * This function returns an Arraylist containing the user's workout names
     *
     * @return ArrayList of all of user's workout names. returns empty list if the user
     * has no workouts.
     */
    public ArrayList<String> get_user_workout_list() {
        ArrayList<String> w_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //retrieve the names of all workouts in the work index table
        Cursor res = db.query(WORK_INDEX_TABLE_NAME,
                new String[]{WORK_INDEX_WORK_NAME},
                null, null, null, null, null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            //add the current row in the table
            w_list.add(res.getString(res.getColumnIndex(
                    WORK_INDEX_WORK_NAME)));
            res.moveToNext();
        }
        res.close();

        return w_list;
    }

    /**
     * This function returns an Arraylist containing the user's exercise names
     *
     * @return ArrayList of all of user's exercise names. returns empty list if the user
     * has no exercises.
     */
    public ArrayList<String> get_user_exercise_list() {
        ArrayList<String> exer_list = new ArrayList<>();
        String name_entry = "";
        SQLiteDatabase db = this.getReadableDatabase();

        //retrieve the names of all exercises in the exer index table
        Cursor res = db.query(EXER_INDEX_TABLE_NAME,
                new String[]{EXER_INDEX_EXER_NAME},
                null, null, null, null, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            //add the current row in the table
            name_entry = res.getString(res.getColumnIndex(EXER_INDEX_EXER_NAME));
            exer_list.add(name_entry);
            res.moveToNext();
        }
        res.close();
        return exer_list;
    }

    /**
     * checks if an exercise name is already taken.
     *
     * @param exercise_name name of the exercise. can case sensitive.
     * @return true if there is already an exercise with the given name, false
     *  otherwise.
     */
    public Boolean is_taken_exer_name(String exercise_name) {
        int found = get_exer_id_from_name(exercise_name);

        if (found == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * retrieves the weight used for all log entries for a given exercise,
     * sorted by timestamp.
     *
     * Usage:
     * TreeMap<Date, Integer> weight_log;
     * weight_log =  user_work_db.get_weight_logs_for_exer("Bench Press");
     * for (Map.Entry<Date, Integer> date_weight_entry : weight_log.entrySet()){
     *    Date date_entry = date_weight_entry.getKey();
     *    Integer weight_entry = date_weight_entry.getValue();
     * }
     * @param exercise_name the exercise to retrieve entries for
     * @return a map of Dates to weights. one entry in the map corresponds to
     *  the date and wieght used for one entry in the log for the given exercise_name.
     */
    public TreeMap<Date, Integer> get_weight_logs_for_exer(String exercise_name) {


        TreeMap<Date, Integer> set_logs = new TreeMap<>();
        Date date_entry = new Date();
        String date_str = "";
        String weight_str = "";
        Integer weight_entry;
        SQLiteDatabase db = this.getReadableDatabase();

        if (!is_taken_exer_name(exercise_name)) {
            throw new IllegalArgumentException("Exercise "+exercise_name+" does not exist!");
        }

        //query segments to retrieve weight log for the given exercise
        String select_cols[] = {WORK_LOG_TIMESTAMP, WORK_LOG_WEIGHT};
        String where_clause = WORK_LOG_EXER_NAME + " = ? ";
        String where_args [] = {exercise_name};
        //query the DB
        Cursor res = db.query(WORK_LOG_TABLE_NAME,
                select_cols, where_clause, where_args,
                null,null,WORK_LOG_TIMESTAMP);

        //populate HashMap with database data
        res.moveToFirst();
        while (!res.isAfterLast()) {
            date_str = res.getString(res.getColumnIndex(WORK_LOG_TIMESTAMP));
            date_entry = string_ISO8601_to_Date(date_str);

            weight_str = res.getString(res.getColumnIndex(WORK_LOG_WEIGHT));
            weight_entry = Integer.parseInt(weight_str);
            set_logs.put(date_entry, weight_entry);
            if (DEBUG)            {
                System.out.println(date_entry + ", " + weight_entry + " entered into set_logs.");
            }
            res.moveToNext();
        }
        res.close();
        if (DEBUG)        {
            System.out.println("all entries for exercise \""+exercise_name+"\" entered into set_logs.");
            System.out.println("The entries are: ");
            System.out.println(set_logs);
        }
        return set_logs;
    }

    /**
     * creates a new exercise in exer_index with the name passed to the function.
     * Precondition: Exercise name must not match an already existing exercise.
     *
     * @param exercise_name must not match the name of any existing exercise.
     *                     Cannot be null.
     * @param num_of_sets      goal number of sets. use 1 for timed exercise types.
     * @param reps_per_set     goal reps per set. used to populate work log targets.
     * @param start_weight     initial weight lifted, for weighted exercises.
     * @param increment_weight weight increment to add or remove between sets.
     * @param rest_time        time between exercise sets, in seconds.
     * @return exer_id of inserted exercise, or -1 if an error occurred
     * @throws #IllegalArgumentException if the exercise name is empty or the
     *      name is already taken.
     * @see #is_taken_exer_name(String) to check precondition.
     * note: defaults "never" for last_used field.
     */
    public int create_exercise(String exercise_name, int num_of_sets, int reps_per_set, int start_weight,
        int increment_weight, int rest_time) {
        long inserted_row_id = -1;
        //TODO: change exer_type to retrieve exercise type if timed exercises are allowed
        String exer_type = "reps";
        
        //invariant: exercise name must not be empty
        if (exercise_name == null || exercise_name.length() == 0) {
            throw new IllegalArgumentException("Error creating exercise: "
                    + "no exercise name has been specified.");
        }
        //invariant: exercise must not already exist
        if (is_taken_exer_name(exercise_name)) {
            throw new IllegalArgumentException("create_exercise failed: \"" + exercise_name + "\" already exists");
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EXER_INDEX_CREATED_ON, current_time_ISO8601());
        contentValues.put(EXER_INDEX_LAST_USED, "never");
        contentValues.put(EXER_INDEX_EXER_NAME, exercise_name);
        contentValues.put(EXER_INDEX_GOAL_SETS, num_of_sets);
        contentValues.put(EXER_INDEX_REPS, reps_per_set);
        contentValues.put(EXER_INDEX_START_WEIGHT, start_weight);
        contentValues.put(EXER_INDEX_INC_WEIGHT, increment_weight);
        contentValues.put(EXER_INDEX_REST_TIME, rest_time);

        inserted_row_id = db.insert(EXER_INDEX_TABLE_NAME, null, contentValues);
        db.close();

        return (int) inserted_row_id;
    }

    public HashMap<String, Integer> get_exer_index_entry(String exercise_name){
        if (DEBUG) {
            System.out.println("Starting "+ new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();

        //details are the keys returned in the hashmap. must be column names from EXER_INDEX table.
        String [] ex_details = {EXER_INDEX_EXER_ID, EXER_INDEX_GOAL_SETS, EXER_INDEX_REPS,
                EXER_INDEX_START_WEIGHT, EXER_INDEX_INC_WEIGHT, EXER_INDEX_REST_TIME};
        //fetch entry from DB
        Cursor res = db.query(EXER_INDEX_TABLE_NAME, ex_details,
                EXER_INDEX_EXER_NAME +" = ?", new String[]{exercise_name},
                null,null,null);
        //populate hashmap with DB row data for the exercise
        HashMap<String, Integer> returnVals = new HashMap<>(ex_details.length);
        res.moveToFirst();
        while ( !res.isAfterLast()) {
            for (String detail: ex_details) {
                returnVals.put(detail, res.getInt(res.getColumnIndex(detail)) );
            }
            res.moveToNext();
        }
        res.close();
        return returnVals;
    }


    public HashMap<String, ArrayList<Integer>> get_session_logs_for_exer(String exerciseName, int session_id){

        //these are the columns in work_log that are returned as keys in the hashmap
        String log_cols [] = {WORK_LOG_WEIGHT, WORK_LOG_SET_NUM, WORK_LOG_ACTUAL};
        if (DEBUG) {
            System.out.println("starting: " + new Exception().getStackTrace()[0]);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        //get logs for the current exercise and session
        Cursor res = db.query(WORK_LOG_TABLE_NAME, log_cols,
                WORK_LOG_SESSION_ID + " = ? AND "+ WORK_LOG_EXER_NAME + " = ?"
                , new String[]{String.valueOf(session_id), exerciseName},
                null, null, null
                );

        ArrayList<Integer> logged_reps = new ArrayList<Integer>(res.getCount());
        ArrayList<Integer> logged_weight = new ArrayList<Integer>(res.getCount());
        ArrayList<Integer> logged_set_num = new ArrayList<Integer>(res.getCount());

        //load ArrayList with logged reps from the DB
        res.moveToFirst();
        while (!res.isAfterLast()){
            logged_reps.add(res.getInt(res.getColumnIndex(WORK_LOG_ACTUAL)));
            logged_weight.add(res.getInt(res.getColumnIndex(WORK_LOG_WEIGHT)));
            logged_set_num.add(res.getInt(res.getColumnIndex(WORK_LOG_SET_NUM)));
            res.moveToNext();
        }
        res.close();

        //insert lists into return hashmap
        HashMap<String, ArrayList<Integer>> returnMap =
                new HashMap<String, ArrayList<Integer>> (log_cols.length);
        returnMap.put(WORK_LOG_SET_NUM, logged_set_num);
        returnMap.put(WORK_LOG_ACTUAL, logged_reps);
        returnMap.put(WORK_LOG_WEIGHT, logged_weight);

        if (DEBUG) {
            System.out.println("returnMap: " + returnMap);
        }

        return returnMap;
    }
}