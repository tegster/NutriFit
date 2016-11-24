package fitness.cs115.a115fitnessapp;

/**
 * Created by Matthew on 11/6/16.
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

import static android.R.attr.id;

import android.database.sqlite.SQLiteDatabase;
//used to return values that are used for summing the amount of each nutrition consumed
public class meal_eatFoodDBHelper extends SQLiteOpenHelper {
    private static final Boolean DEBUG = false;

    //initializing the database
    private static final String DATABASE_NAME = "Eatfood.db";
    private String TABLE_NAME;

    //declaring variables for the columns of our database
    public static final String Col_1 = "ID";
    public static final String Col_2 = "foodname";
    public static final String Col_3 = "calories";
    public static final String Col_4 = "totalfat";
    public static final String Col_5 = "transfat";
    public static final String Col_6 = "satfat";
    public static final String Col_7 = "cholesterol";
    public static final String Col_8 = "sodium";
    public static final String Col_9 = "carbs";
    public static final String Col_10 = "fiber";
    public static final String Col_11 = "sugar";
    public static final String Col_12 = "protein";


    public meal_eatFoodDBHelper(Context context, String table_name) {
        super(context, DATABASE_NAME, null, 1);
        this.TABLE_NAME = table_name;
        if (DEBUG) {
            System.out.println("1337 Constructor called with: " + this.TABLE_NAME);
        }
        if (DEBUG) {
            System.out.println("1337 onCreate " + TABLE_NAME);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            System.out.println("1337 onCreate called");
        }
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                        "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                        "satfat DECIMAL(5,1), cholesterol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                        "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add a new food
    public boolean insertFood(String foodname, Double calories, Double totalfat, Double transfat, Double satfat,
                              Double cholestrol, Double sodium, Double carbs, Double fiber, Double sugar,
                              Double protein) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Col_2, foodname);
        contentValues.put(Col_3, calories);
        contentValues.put(Col_4, totalfat);
        contentValues.put(Col_5, transfat);
        contentValues.put(Col_6, satfat);
        contentValues.put(Col_7, cholestrol);
        contentValues.put(Col_8, sodium);
        contentValues.put(Col_9, carbs);
        contentValues.put(Col_10, fiber);
        contentValues.put(Col_11, sugar);
        contentValues.put(Col_12, protein);

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
    public boolean updateFood(String foodname, Double calories, Double totalfat, Double transfat, Double satfat,
                              Double cholestrol, Double sodium, Double carbs, Double fiber, Double sugar,
                              Double protein) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, foodname);
        contentValues.put(Col_3, calories);
        contentValues.put(Col_4, totalfat);
        contentValues.put(Col_5, transfat);
        contentValues.put(Col_6, satfat);
        contentValues.put(Col_7, cholestrol);
        contentValues.put(Col_8, sodium);
        contentValues.put(Col_9, carbs);
        contentValues.put(Col_10, fiber);
        contentValues.put(Col_11, sugar);
        contentValues.put(Col_12, protein);
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
        int delete = db.delete(TABLE_NAME, Col_2 + " = ?", new String[]{name});
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
            array_list.add(res.getString(res.getColumnIndex(Col_2)));
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
            array_list.add(res.getString(res.getColumnIndex(Col_3)));
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
            array_list.add(res.getString(res.getColumnIndex(Col_2)) + "," +
                    " cal: " + res.getString(res.getColumnIndex(Col_3)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    //return all foods and their respective macros. name, cals, fat, carbs, protein
    public ArrayList<String> getAllmacrosInfo() {
        ArrayList<String> macros_array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            macros_array_list.add(res.getString(res.getColumnIndex(Col_2)) + "," +
                    "Cal: " + res.getString(res.getColumnIndex(Col_3)) + "," +
                    "Fat: " + res.getString(res.getColumnIndex(Col_4)) + "," +
                    "Carbs: " + res.getString(res.getColumnIndex(Col_9)) + "," +
                    "Protein: " + res.getString(res.getColumnIndex(Col_12)));

            res.moveToNext();
        }
        res.close();
        return macros_array_list;
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
            //  array_list.add("index " + res.getString(res.getColumnIndex(Col_1)));
            array_list.add("foodname " + res.getString(res.getColumnIndex(Col_2)));
            array_list.add("calories " + res.getString(res.getColumnIndex(Col_3)));
            array_list.add("totalfat " + res.getString(res.getColumnIndex(Col_4)));
            array_list.add("transfat " + res.getString(res.getColumnIndex(Col_5)));
            array_list.add("satfat " + res.getString(res.getColumnIndex(Col_6)));
            array_list.add("Cholestrol " + res.getString(res.getColumnIndex(Col_7)));
            array_list.add("sodium " + res.getString(res.getColumnIndex(Col_8)));
            array_list.add("carbs " + res.getString(res.getColumnIndex(Col_9)));
            array_list.add("fiber " + res.getString(res.getColumnIndex(Col_10)));
            array_list.add("sugar " + res.getString(res.getColumnIndex(Col_11)));
            array_list.add("protein " + res.getString(res.getColumnIndex(Col_12)));

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
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
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

    //returns the total number of calories stored in this table
    public int getTotalCalories() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(calories) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of totalfat stored in this table
    public int getTotalFat() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(totalfat) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of transfat stored in this table
    public int getTotalTransFat() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(transfat) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of satfat stored in this table
    public int getTotalSatFat() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(satfat) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of cholesterol stored in this table
    public int getTotalCholesterol() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(cholesterol) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of sodium stored in this table
    public int getTotalSodium() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(sodium) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of carbs stored in this table
    public int getTotalCarbs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(carbs) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of fiber stored in this table
    public int getTotalFiber() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(fiber) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of sugar stored in this table
    public int getTotalSugar() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(sugar) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

    //returns the total number of protein stored in this table
    public int getTotalProtein() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(protein) FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return 0; //error
    }

}
