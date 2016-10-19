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

    private static final Boolean DEBUG = true;

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


    private HashMap hp;


    public work_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            System.out.println("work_DBHelper: onCreate-ed");
        }

        //create tables if they don't yet exist
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + WORK_INDEX_TABLE_NAME + " " +
                        "(id integer auto_increment primary key, name text, created_on text, " +
                        "last_used text)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + WORK_DETAIL_TABLE_NAME + " " +
                        "(work_id integer, exer_id integer, exer_name text, exer_type text, " +
                        "sets integer, reps integer, weight integer, primary key (work_id, exer_id) )"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + WORK_LOG_TABLE_NAME + " " +
                        "(session_id integer, exer_id integer, set_num integer, goal integer, " +
                        "actual integer, weight integer, primary key(session_id, exer_id) )"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + WORK_SESSIONS_TABLE_NAME + " " +
                        "(session_id integer auto_increment primary key, work_id integer, date_time text)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + PROG_INDEX_TABLE_NAME + " " +
                        "(prog_id integer primary key, prog_name text, created_on text)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + PROG_DETAIL_TABLE_NAME + " " +
                        "(prog_id integer, work_id integer, primary key (prog_id, work_id)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tbl_name;
        int num_tables = WORK_DB_TABLES.length;

        //delete all tables if they have been created
        for (int ind= 0; ind < num_tables; ++ind)
        {
            db.execSQL("DROP TABLE IF EXISTS " + WORK_DB_TABLES[ind]);
        }
        onCreate(db);
    }

    /*
    This function creates a new program entry in prog_index and returns it's prog_id.
     */
    public int create_program(String program_name) {

        long new_prog_id = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("prog_name", program_name);
        contentValues.put("created_on", current_time_ISO8601());

        //TODO: include insert invariant that db is not full
        //TODO: include insert invariant that program_name doesnt already exist in prog_index

        new_prog_id = db.insert(PROG_INDEX_TABLE_NAME, null, contentValues);

        if (DEBUG) {
            if (new_prog_id > 0) {
                System.out.println("create_program: " + program_name + " inserted into "
                        + PROG_INDEX_TABLE_NAME);
            }
        }
        return (int) new_prog_id;
    }

    /*
    This function adds an existing workout to an existing program and returns its prog_id or -1
    if insertion fails.
     */
    public int add_work_to_prog(String prog_name, String work_name)
    {
        long confirm_prog_id = -1;
        int prog_id = get_prog_id_from_name(prog_name);
        int work_id = get_work_id_from_name(work_name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("prog_id", prog_id);
        contentValues.put("work_id", work_id);

        //TODO: include insert invariant that db is not full
        //TODO: include insert invariant that workout_id exists in work_index
        //TODO: include insert invariant that workout is not already part of program
        confirm_prog_id = db.insert(PROG_DETAIL_TABLE_NAME, null, contentValues);

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
        contentValues.put("name", workout_name);
        contentValues.put("created_on", current_time_ISO8601() );
        contentValues.put("last_used", "never");

        //TODO: include insert invariant that db is not full
        inserted_id = db.insert(WORK_INDEX_TABLE_NAME, null, contentValues);

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
                        " where " + PROG_DETAIL_COL_PROG_ID + " = " + prog_id, null);

        res.moveToFirst();
        while ( !res.isAfterLast() ) {
            //add the current workout name to the list of names
            work_id = res.getColumnIndex(PROG_DETAIL_COL_WORK_ID);
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

        return (int) new_session_id;
    }

    public int create_work_log ( )
    {
        //TODO: fill this function
        return -1;
    }

    //gets contents of the table specified as a String
    public String str_dump_table(String table_name) {

        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        //TODO: add invariant that table_name is a valid table
        Cursor res = db.rawQuery("select * from " + table_name, null);
        String[] col_names = res.getColumnNames();
        String output="";
        res.moveToFirst();
        long count = 0;
        while ( !res.isAfterLast() ) {

            //print the current row in the table
            array_list.add("count: " + count);
            for (int ind = 0; ind < col_names.length; ++ind)
            {
                //prints column name followed by value stored in table
                array_list.add( "\t"+ col_names[ind] + ": " +
                        res.getString(res.getColumnIndex(col_names[ind])) );
            }
            res.moveToNext();
            count++;
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

    /*
    Returns current date and time as ISO 8601 formatted String
    Date conversion found here: http://beginnersbook.com/2013/05/current-date-time-in-java/
     */
    private String current_time_ISO8601() {
        DateFormat df = new SimpleDateFormat("YYYY-MM-DD HH:mm");
        Date dateobj = new Date();
        String date_str = df.format(dateobj);

        return date_str;
    }

    //returns prog_id if found, -1 if not found
    private int get_prog_id_from_name(String prog_name) {

        SQLiteDatabase db = this.getReadableDatabase();
        int prog_id = -1;

        Cursor res = db.rawQuery("select prog_id from " + PROG_INDEX_TABLE_NAME +
                "where " + PROG_INDEX_COL_PROG_NAME + " = " + prog_name, null);

        res.moveToFirst();
        if(!res.isAfterLast() ) {
            prog_id = res.getColumnIndex(PROG_INDEX_COL_PROG_ID);
        }
        res.close();

        return prog_id;
    }

    //returns work_id if found, -1 if not found
    private int get_work_id_from_name(String work_name) {

        SQLiteDatabase db = this.getReadableDatabase();
        int work_id = -1;

        Cursor res = db.rawQuery("select work_id from " + WORK_INDEX_TABLE_NAME +
                "where " + WORK_INDEX_COL_WORK_NAME + " = " + work_name, null);

        res.moveToFirst();
        if(!res.isAfterLast() ) {
            work_id = res.getColumnIndex(WORK_INDEX_COL_WORK_ID);
        }
        res.close();

        return work_id;
    }

    private String get_work_name_from_id(int work_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String work_name="";

        Cursor res = db.rawQuery("select " + WORK_INDEX_COL_WORK_NAME + " from " +
            WORK_INDEX_TABLE_NAME + " where " + WORK_INDEX_COL_WORK_ID + " = " + work_id, null);

        res.moveToFirst();
        if(!res.isAfterLast() ) {
            work_name = res.getString(res.getColumnIndex(WORK_INDEX_COL_WORK_NAME));
        }
        res.close();

        return work_name;
    }

    //returns work_id if found, -1 if not found
    private int get_exer_id_from_name(String exer_name) {

        //TODO: stub for now. retrieve exercise name from master exercise DB when implemented.
        return -1;
    }



/*
The following is an unedited part of the tutorial Matt posted:


    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, WORKOUTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(WORKOUTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
*/
}