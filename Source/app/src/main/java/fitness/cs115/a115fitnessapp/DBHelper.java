package fitness.cs115.a115fitnessapp;

/**
 * Created by Matthew on 10/7/16.
 */
//this was used as a starting point but heavily modified https://www.tutorialspoint.com/android/android_sqlite_database.htm

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {
    private static final Boolean DEBUG = true;


    private static final String DATABASE_NAME = "foods.db";
    private static final String TABLE_NAME = "food";

    private static final String CONTACTS_COLUMN_ID = "id";
    //public static final String CONTACTS_COLUMN_NAME = "name";

    private static final String FOOD_NAME = "name";
    private static final String CALORIES = "calories";

/*
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_STREET = "street";
    public static final String CONTACTS_COLUMN_CITY = "place";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    */
    //  private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        if (DEBUG) {
            System.out.println("1337 onCreate");
        }
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                        "(id integer primary key, name text,calories REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add a new food
    public boolean insertFood(String name, Double calories) {
        if (isFoodInDataBase(name)) { //don't insert same item twice
            if (DEBUG) {
                System.out.println(name + " is already in database");
            }
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOOD_NAME, name);
        contentValues.put(CALORIES, calories);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    //gets number of rows in database
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    //can change the foods name and number of calories
    public boolean updateFood(Integer id, String name, Double calories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOOD_NAME, name);
        contentValues.put(CALORIES, calories);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    //deletes the food with the given id
    public Integer deleteFood(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    //returns a List of all food names in Database
    public ArrayList<String> getAllFoods() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(FOOD_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    //gets food and calories information
    public ArrayList<String> getAllFoodInfo() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        long count = 0;
        while (res.isAfterLast() == false) {
            array_list.add("Item: " + count);
            array_list.add(res.getString(res.getColumnIndex(FOOD_NAME)));
            array_list.add(res.getString(res.getColumnIndex(CALORIES)));
            res.moveToNext();
            count++;
        }
        res.close();
        return array_list;
    }

    //true means the data is in the databse
    //true means food name is already in database
    //false means food is not in database
    public boolean isFoodInDataBase(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(FOOD_NAME)).equals(foodName)) {
                return true; //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return false;
    }
}
