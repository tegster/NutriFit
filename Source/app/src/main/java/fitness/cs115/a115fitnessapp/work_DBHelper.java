package fitness.cs115.a115fitnessapp;

/**
 * Created by James Kennedy on 10/17/16.
 */
//this file was adapted from:
// https://www.tutorialspoint.com/android/android_sqlite_database.htm

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class work_DBHelper extends SQLiteOpenHelper {

    private static final Boolean DEBUG = false;

    public static final String DATABASE_NAME = "user_work.db";

    /*
    WORK_INDEX: This table will serve as an index of all of the user's created workouts.
    The fields are: work_id (integer unique to each workout); name(of the workout, given by user);
    created_on (datetime string); last_used (datetime string).
    */
    public static final String WORK_INDEX_TABLE_NAME = "work_index";
    public static final String WORK_INDEX_COL_WORK_ID = "work_id";
    public static final String WORK_INDEX_COL_WORK_NAME = "work_name";
    public static final String WORK_INDEX_COL_CREATED_ON = "created_on";
    public static final String WORK_INDEX_COL_LAST_USED = "last_used";

    /*
    WORK_DETAIL: this table will store the list of exercises that make up each of the user's
    workouts. Each row will represent one exercise. The fields are: work_id (corresponding to
    the id assigned to each workout in WORK_INDEX table); exercise_id (exercise id, to retrieve
    data from the master exercise database);  name (exercise name given by user);
    type ("rep" or "timed", repetition based vs. timed exercise); sets (goal # of sets that
     should be done for that exercise); reps(goal repetitions per set); weight.
    */
    public static final String WORK_DETAIL_TABLE_NAME = "work_detail";
    public static final String WORK_DETAIL_COL_WORK_ID = "work_id";
    public static final String WORK_DETAIL_COL_EXER_ID = "exer_id";
    public static final String WORK_DETAIL_COL_EXER_NAME = "exer_name";
    public static final String WORK_DETAIL_COL_TYPE = "exer_type";
    public static final String WORK_DETAIL_COL_SETS = "sets";
    public static final String WORK_DETAIL_COL_REPS = "reps";
    public static final String WORK_DETAIL_COL_WEIGHT = "weight";

    /*
    WORK_LOG: This table stores the actual exercise log for each time the user chooses to do a
    workout. Each row corresponds to one set of an exercise in a workout. Fields: session_id
    (unique id assigned to each workout iteration. Rows have the same session_id if they belong
    to the same workout session); exercise_id (corresponds to exercise_id field of that
    exercise in work_detail table); set_num (current set number for the row); goal(goal time
     or goal number of reps, depending on exercise_type for the given exercise); actual (time
     or reps actually completed); weight.
     */
    public static final String WORK_LOG_TABLE_NAME = "work_log";
    public static final String WORK_LOG_COL_SESSION_ID = "session_id";
    public static final String WORK_LOG_COL_EXER_ID = "exer_id";
    public static final String WORK_LOG_COL_SET_NUM = "set_num";
    public static final String WORK_LOG_COL_GOAL = "goal";
    public static final String WORK_LOG_COL_ACTUAL = "actual";
    public static final String WORK_LOG_COL_WEIGHT = "weight";

    /*
    WORK_SESSIONS: This table stores a log for each time the user chooses to begin a workout.
    The fields are: session_id (unique id assigned to each workout iteration);
    work_id (corresponding to work_id of the workout from work_index table);
    datetime (time session was initiated, stored as text in ISO-8601 date/time format).
     */
    public static final String WORK_SESSIONS_TABLE_NAME = "work_sessions";
    public static final String WORK_SESSIONS_COL_SESSION_ID = "session_id";
    public static final String WORK_SESSIONS_COL_WORK_ID = "work_id";
    public static final String WORK_SESSIONS_COL_DATETIME = "date_time";

    /*
    PROG_INDEX: stores a list of workouts according to the program(s) they belong to.
     */
    public static final String PROG_INDEX_TABLE_NAME = "prog_index";
    public static final String PROG_INDEX_COL_PROG_ID = "prog_id";
    public static final String PROG_INDEX_COL_PROG_NAME = "prog_name";
    public static final String PROG_INDEX_COL_CREATED_ON = "created_on";
    public static final String PROG_INDEX_COL_LAST_USED = "last_used";

    /*
    PROG_DETAIL: stores a list of workouts according to the program(s) they belong to.
     */
    public static final String PROG_DETAIL_TABLE_NAME = "prog_detail";
    public static final String PROG_DETAIL_COL_PROG_ID = "prog_id";
    public static final String PROG_DETAIL_COL_WORK_ID = "work_id";
    public static final String PROG_DETAIL_COL_LAST_USED = "last_used";

    public static final String[] WORK_DB_TABLES= {WORK_INDEX_TABLE_NAME, WORK_DETAIL_TABLE_NAME,
            WORK_LOG_TABLE_NAME, WORK_SESSIONS_TABLE_NAME,
            PROG_DETAIL_TABLE_NAME, PROG_INDEX_TABLE_NAME};


    public work_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            System.out.println("work_DBHelper: onCreate-ed");
        }
        //create tables if they don't yet exist
        db.execSQL( "CREATE TABLE IF NOT EXISTS " + WORK_INDEX_TABLE_NAME + " ("
                       + WORK_INDEX_COL_WORK_ID + " integer primary key autoincrement, "
                       + WORK_INDEX_COL_WORK_NAME + " text, "
                        + WORK_INDEX_COL_CREATED_ON + " text, "
                        + WORK_INDEX_COL_LAST_USED + " text )"
        );

        db.execSQL( "CREATE TABLE IF NOT EXISTS " + WORK_DETAIL_TABLE_NAME + " "
                        + "(" + WORK_DETAIL_COL_WORK_ID + " integer, "
                        + WORK_DETAIL_COL_EXER_ID + " integer, "
                        + WORK_DETAIL_COL_EXER_NAME + " text, "
                        + WORK_DETAIL_COL_TYPE + " text, "
                        + WORK_DETAIL_COL_SETS + " text, "
                        + WORK_DETAIL_COL_REPS + " text, "
                        + WORK_DETAIL_COL_WEIGHT + " text,"
                        + "primary key (" +WORK_DETAIL_COL_WORK_ID +", "+ WORK_DETAIL_COL_EXER_NAME
                        + ") )"
                //TODO: change primary key to EXER_ID once exercise db is implemented
        );

        db.execSQL( "CREATE TABLE IF NOT EXISTS " + WORK_LOG_TABLE_NAME + " "
                + "(" + WORK_LOG_COL_SESSION_ID + " integer, "
                + WORK_LOG_COL_EXER_ID + " integer, "
                + WORK_LOG_COL_SET_NUM + " integer, "
                + WORK_LOG_COL_GOAL + " integer, "
                + WORK_LOG_COL_ACTUAL + " integer, "
                + WORK_LOG_COL_WEIGHT + " integer, "
                + "primary key (" +WORK_LOG_COL_SESSION_ID +", "+ WORK_LOG_COL_EXER_ID
                + ") )"
        );

        db.execSQL( "CREATE TABLE IF NOT EXISTS " + WORK_SESSIONS_TABLE_NAME + " "
                + "(" + WORK_SESSIONS_COL_SESSION_ID + " integer primary key autoincrement, "
                + WORK_SESSIONS_COL_WORK_ID + " integer, "
                + WORK_SESSIONS_COL_DATETIME + " text "
                + ")"
        );

        db.execSQL( "CREATE TABLE IF NOT EXISTS " + PROG_INDEX_TABLE_NAME + " "
                + "(" + PROG_INDEX_COL_PROG_ID + " integer primary key, "
                + PROG_INDEX_COL_PROG_NAME + " text, "
                + PROG_INDEX_COL_CREATED_ON + " text, "
                + PROG_INDEX_COL_LAST_USED + " text "
                + ")"
        );

        db.execSQL( "CREATE TABLE IF NOT EXISTS " + PROG_DETAIL_TABLE_NAME + " "
                + "(" + PROG_DETAIL_COL_PROG_ID + " integer, "
                + PROG_DETAIL_COL_WORK_ID + " integer, "
                + PROG_DETAIL_COL_LAST_USED + " text, "
                + "primary key (" + PROG_DETAIL_COL_PROG_ID + ", " + PROG_DETAIL_COL_WORK_ID
                + ") )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop_all_tables();

        onCreate(db);
    }

    /*
    This function creates a new program entry in prog_index and returns it's prog_id.
     */
    public int create_program(String program_name) {

        long new_prog_id = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROG_INDEX_COL_PROG_NAME, program_name);
        contentValues.put(PROG_INDEX_COL_CREATED_ON, current_time_ISO8601());
        contentValues.put(PROG_INDEX_COL_LAST_USED, "never");

        //TODO: include insert invariant that db is not full
        //TODO: include insert invariant that program_name doesnt already exist in prog_index
        new_prog_id = db.insert(PROG_INDEX_TABLE_NAME, null, contentValues);

        if (DEBUG) {
            if (new_prog_id > 0) {
                System.out.println("create_program: " + program_name + " inserted into "
                        + PROG_INDEX_TABLE_NAME + " with prog_id = "+new_prog_id);
            }
        }
        db.close();

        return (int) new_prog_id;
    }

    /*
    This function adds an existing workout to an existing program and returns its prog_id or -1
    if insertion fails.
     */
    public int add_work_to_prog(String prog_name, String work_name)
    {
        //TODO: add invariant that workout and program must exist
        if (DEBUG) {
            System.out.println("starting: " + new Exception().getStackTrace()[0]);
        }
        long confirm_prog_id = -1;
        int prog_id = get_prog_id_from_name(prog_name);
        int work_id = get_work_id_from_name(work_name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROG_DETAIL_COL_PROG_ID, prog_id);
        contentValues.put(PROG_DETAIL_COL_WORK_ID, work_id);
        contentValues.put(PROG_DETAIL_COL_LAST_USED, "never");


        //TODO: include insert invariant that db is not full
        //TODO: include insert invariant that workout_id exists in work_index
        //TODO: include insert invariant that workout is not already part of program
        confirm_prog_id = db.insert(PROG_DETAIL_TABLE_NAME, null, contentValues);
        if (DEBUG) {
            System.out.println(work_name + " inserted in  " + prog_name);
        }
        db.close();
        return (int) confirm_prog_id;
    }

    /*
    This function returns a String ArrayList containing the user's program names
     */
    public ArrayList get_program_list () {
        ArrayList<String> p_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery(
                "select " + PROG_INDEX_COL_PROG_NAME + " from " + PROG_INDEX_TABLE_NAME, null);

        res.moveToFirst();
        while ( !res.isAfterLast() ) {
            //add the current row in the table
            p_list.add(res.getString(res.getColumnIndex(PROG_INDEX_COL_PROG_NAME) ) );
            res.moveToNext();
        }
        res.close();

        return p_list;
    }

    /*
    creates a new workout in work_index with the name passed to the function,
    using current time for the created_on field and "never" for last_used field.
     */
    public int create_workout(String workout_name) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WORK_INDEX_COL_WORK_NAME, workout_name);
        contentValues.put(WORK_INDEX_COL_CREATED_ON, current_time_ISO8601() );
        contentValues.put(WORK_INDEX_COL_LAST_USED, "never");

        //TODO: include insert invariant that db is not full
        inserted_id = db.insert(WORK_INDEX_TABLE_NAME, null, contentValues);
        db.close();
        return (int) inserted_id;
    }

    /*
    This function adds an existing exercise to an existing workout and returns its work_id or -1
    if insertion fails.
     */
    public int add_exer_to_work(String work_name, String exer_name, String exer_type,
                                int num_of_sets, int reps_per_set, int weight)
    {
        long confirm_row_id = -1;
        int work_id = get_work_id_from_name(work_name);
        int exer_id = get_exer_id_from_name(exer_name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(WORK_DETAIL_COL_WORK_ID, work_id);
        contentValues.put(WORK_DETAIL_COL_EXER_ID, exer_id);
        contentValues.put(WORK_DETAIL_COL_EXER_NAME, exer_name);
        contentValues.put(WORK_DETAIL_COL_TYPE, exer_type);
        contentValues.put(WORK_DETAIL_COL_SETS, num_of_sets);
        contentValues.put(WORK_DETAIL_COL_REPS, reps_per_set);
        contentValues.put(WORK_DETAIL_COL_WEIGHT, weight);
        //TODO: include insert invariant that workout_id exists in work_index
        //TODO: include insert invariant that exercise is not already part of program (if needed)
        confirm_row_id = db.insert(WORK_DETAIL_TABLE_NAME, null, contentValues);
        db.close();
        return (int) confirm_row_id;
    }
    /*
    This function returns a String ArrayList containing the workouts assigned to the given program
    */
    public ArrayList get_workouts_from_prog (String prog_name) {
        ArrayList<String> w_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int prog_id = get_prog_id_from_name(prog_name);
        int work_id = 0;
        //retrieve the id's of all workouts from the desired program
        Cursor res = db.rawQuery(
                "select " + PROG_DETAIL_COL_WORK_ID + " from " + PROG_DETAIL_TABLE_NAME +
                " where " + PROG_DETAIL_COL_PROG_ID + " = ?",
                new String[] {String.valueOf(prog_id)});

        res.moveToFirst();
        while ( !res.isAfterLast() ) {
            //add the current workout name to the list of names
            work_id = res.getInt(res.getColumnIndex(PROG_DETAIL_COL_WORK_ID));
            w_list.add(get_work_name_from_id(work_id));
            res.moveToNext();
        }
        res.close();

        return w_list;
    }

    /*
    This function creates a new session entry in work_sessions for the workout specified
    and returns it's session_id.
     */
    public int create_session(String work_name) {
        long new_session_id = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int work_id = get_work_id_from_name(work_name);

        contentValues.put("work_id", work_id);
        contentValues.put("date_time", current_time_ISO8601());

        //TODO: include insert invariant that db is not full
        //TODO: include insert invariant that workout_id exists in work_index
        
        new_session_id = db.insert(WORK_SESSIONS_TABLE_NAME, null, contentValues);

        db.close();
        return (int) new_session_id;
    }

    public int create_work_log (int session_id)
    {
        //TODO: fill this function
        return -1;
    }

    //gets contents of the table specified as a String
    public String dump_table(String table_name) {

        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        //TODO: add invariant that table_name is a valid table
        Cursor res = db.rawQuery("select * from " + table_name, null);
        String[] col_names = res.getColumnNames();
        String output="";
        String row="", col ="";
        res.moveToFirst();

        if (res.isAfterLast())
        {
            output = "\t<table empty>";
        }
        while ( !res.isAfterLast() ) {

            row="";
            //print the current row in the table
            for (int ind = 0; ind < col_names.length; ++ind)
            {
                if(ind == 0)
                {
                    row = "\t";
                }
                col = col_names[ind] + ": " +res.getString(res.getColumnIndex(col_names[ind]));
                row += String.format("%-10s ", col);
                //prints column name followed by value stored in table
            }
            array_list.add(row);
            res.moveToNext();
        }
        res.close();

        for (int argi = 0; argi <array_list.size(); ++argi)
        {
            output += array_list.get(argi) +"\n";
        }

        return output;
    }

    //gets number of items (rows) in the table specified
    public int get_num_rows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    public void clear_all_tables() {
        SQLiteDatabase db = this.getWritableDatabase();
        int num_tables = WORK_DB_TABLES.length;

        if (DEBUG) {
            System.out.println("Clearing all tables.");
        }

        for (int ind= 0; ind < num_tables; ++ind)
        {
            db.execSQL("delete from " + WORK_DB_TABLES[ind]);
        }
        db.close();
    }

    /*
    Returns current date and time as ISO 8601 formatted String
    Date conversion found here: http://beginnersbook.com/2013/05/current-date-time-in-java/
     */
    private String current_time_ISO8601() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-DD HH:mm");
        Date dateobj = new Date();
        String date_str = df.format(dateobj);

        return date_str;
    }

    //returns prog_id if found, -1 if not found
    private int get_prog_id_from_name(String prog_name) {

        if (DEBUG) {
            System.out.println("starting: " + new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        int prog_id = -1;

        Cursor res = db.query(PROG_INDEX_TABLE_NAME, new String[] {PROG_INDEX_COL_PROG_ID},
                PROG_INDEX_COL_PROG_NAME + " = ?", new String[] {prog_name},
                null, null, null);

        res.moveToFirst();

        if(!res.isAfterLast() ) {
            prog_id = res.getInt(res.getColumnIndex(PROG_INDEX_COL_PROG_ID));
        }

        if (DEBUG) {
            System.out.println("prog_id: " + prog_id + "  found for prog_name: " + prog_name);
        }
        res.close();

        return prog_id;
    }

    //returns work_id if found, -1 if not found
    private int get_work_id_from_name(String work_name) {

        if (DEBUG) {
            System.out.println("starting: " + new Exception().getStackTrace()[0]);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        int work_id = -1;

        Cursor res = db.rawQuery("select " + WORK_INDEX_COL_WORK_ID + " from " + WORK_INDEX_TABLE_NAME
       +" where " + WORK_INDEX_COL_WORK_NAME + " = ?", new String[] {work_name} );

        res.moveToFirst();
        if(!res.isAfterLast() ) {
            work_id = res.getInt(res.getColumnIndex(WORK_INDEX_COL_WORK_ID));
        }
        if (DEBUG) {
            System.out.println("work_id: " + work_id + "  found for work_name: " + work_name);
        }
        res.close();

        return work_id;
    }

    private String get_work_name_from_id(int work_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String work_name="";

        //retrieve row with desired work_id
        Cursor res = db.query(WORK_INDEX_TABLE_NAME, new String[] {WORK_INDEX_COL_WORK_NAME},
                WORK_INDEX_COL_WORK_ID + " =? ", new String[] {String.valueOf(work_id)},
                null, null, null);


        res.moveToFirst();
        if(!res.isAfterLast() ) {
            work_name = res.getString(res.getColumnIndex(WORK_INDEX_COL_WORK_NAME));
        }
        res.close();

        if (DEBUG) {
            System.out.println("work_name: " + work_name + "  found for work_id: " + work_id);
        }
        return work_name;
    }

    //returns work_id if found, -1 if not found
    private int get_exer_id_from_name(String exer_name) {

        //TODO: stub for now. retrieve exercise name from master exercise DB when implemented.
        return -1;
    }

    private void drop_all_tables() {
        SQLiteDatabase db = this.getWritableDatabase();
        int num_tables = WORK_DB_TABLES.length;
        if (DEBUG)
        {
            System.out.println("dropping tables like flies");
        }
        for (int ind= 0; ind < num_tables; ++ind)
        {
            db.execSQL("DROP TABLE IF EXISTS " + WORK_DB_TABLES[ind]);
        }
        db.close();
    }

}