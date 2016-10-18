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
    public static final String WORK_INDEX_COL_NAME = "name";
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
    public static final String WORK_DETAIL_COL_EXERCISE_ID = "exer_id";
    public static final String WORK_DETAIL_COL_NAME = "exer_name";
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
    public static final String WORK_LOG_COL_EXERCISE_ID = "exer_id";
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
    PROGRAMS: stores a list of workouts according to the program(s) they belong to.
     */
    public static final String PROGRAMS_TABLE_NAME = "programs";
    public static final String PROGRAMS_COL_PROGRAM_ID = "prog_id";
    public static final String PROGRAMS_COL_PROGRAM_NAME = "prog_name";
    public static final String PROGRAMS_COL_WORK_ID = "work_id";

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
                        "(id integer auto_increment primary key, work_id integer, date_time text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WORK_SESSIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WORK_INDEX_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WORK_LOG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WORK_DETAIL_TABLE_NAME);
        onCreate(db);
    }

    /*
    This function creates a new session entry in work_sessions and returns it's session_id.
    Date conversion found here: http://beginnersbook.com/2013/05/current-date-time-in-java/
     */
    public int create_session(int workout_id) {
        DateFormat df = new SimpleDateFormat("YYYY-MM-DD HH:mm");
        Date dateobj = new Date();
        String date_str = df.format(dateobj);
        long new_session_id = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("work_id", workout_id);
        contentValues.put("date_time", date_str);

        //TODO: include insert invariant that db is not full
        new_session_id = db.insert(WORK_SESSIONS_TABLE_NAME, null, contentValues);

        return (int) new_session_id;
    }

    public Cursor getAllSessions(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ WORK_SESSIONS_TABLE_NAME, null );
        return res;
    }


    //gets number of items (rows) in the table specified
    public int get_num_rows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    //TODO: left off here 10/17/16
    private long insert_work_index(String workout_name, String creation_date) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", workout_name);
        contentValues.put("created_on", creation_date);
        contentValues.put("last_used", "never");

        //TODO: include insert invariant that db is not full
        inserted_id = db.insert(WORK_INDEX_TABLE_NAME, null, contentValues);

        return inserted_id;
    }



/*
The following is an unedited part of Matts tutorial:


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