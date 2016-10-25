package fitness.cs115.a115fitnessapp;

/**
 * Created by Matthew on 10/7/16.
 */
//this was used as a starting point but modified a bunch https://www.tutorialspoint.com/android/android_sqlite_database.htm

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class meal_foodDBHelper extends SQLiteOpenHelper {
    private static final Boolean DEBUG = true;


    private static final String DATABASE_NAME = "foods.db";
    private static final String TABLE_NAME = "food";

    private static final String CONTACTS_COLUMN_ID = "id";

    private static final String FOOD_NAME = "name";
    private static final String CALORIES = "calories";
    private static final String FAT = "fat";
    private static final String CARBS = "carbs";
    private static final String POTASIUM = "potasium";

    public meal_foodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            System.out.println("1337 onCreate");
        }
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                        "(id integer primary key, name text,calories REAL,fat REAL,carbs REAL,potasium REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add a new food
    public boolean insertFood(String name, Double calories, Double fat, Double carbs, Double potasium) {
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
        if (fat != -1) {
            contentValues.put(FAT, fat);
        }
        if (carbs != -1) {
            contentValues.put(CARBS, carbs);
        }
        if (potasium != -1) {
            contentValues.put(POTASIUM, potasium);
        }
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    //gets number of items (rows) in database
    public int getNumberOfRows() {
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
    public Boolean deleteFood(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (DEBUG) {
            System.out.println("deleting food with id " + id);
        }
        int delete = db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
        return (delete != 0);
    }

    //true means delete successful
    //false means not successful, probably because the food doesn't exist in the database
    public boolean deleteFood(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_NAME, FOOD_NAME + " = ?", new String[]{name});
        if (DEBUG) {
            System.out.println("deleted x value is " + delete);
        }
        return (delete != 0);
    }


    //returns a List of all food names in Database
    public ArrayList<String> getAllFoods() {
        ArrayList<String> array_list = new ArrayList<String>();
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

    public ArrayList<String> getAllCalories() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CALORIES)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    //returns all foods and their respective calories at the same time
    public ArrayList<String> getAllFoodsAndCalories() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(FOOD_NAME)) + ", cal: " + res.getString(res.getColumnIndex(CALORIES)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
    //gets food and calories information
    public ArrayList<String> getAllFoodInfo() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        long count = 0;
        while (res.isAfterLast() == false) {
            array_list.add("Item: " + count);
            array_list.add("name " + res.getString(res.getColumnIndex(FOOD_NAME)));
            array_list.add("calories " + res.getString(res.getColumnIndex(CALORIES)));
            array_list.add("fat " + res.getString(res.getColumnIndex(FAT)));
            array_list.add("carbs " + res.getString(res.getColumnIndex(CARBS)));
            array_list.add("potassium " + res.getString(res.getColumnIndex(POTASIUM)));

            array_list.add("index " + res.getString(res.getColumnIndex("id")));
            res.moveToNext();
            count++;
        }
        res.close();
        return array_list;
    }


    /*
    //print out a specific item
    public void printCursorItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

    }

    //return itemID given food name
    public int getItemId(String name) {
        int id = 0;
        return id;
    }
*/

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

    //deletes all data stored in table
    public boolean deleteEntireTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        return true;
    }

}
