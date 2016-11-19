package fitness.cs115.a115fitnessapp;

/**
 * Created by Matthew on 10/16/16.
 */
//took meal_foodDBHelper.java as a starting point for this

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import static android.R.attr.id;

public class meal_mealDBHelper extends SQLiteOpenHelper {
    private static final Boolean DEBUG = true;


    private static final String DATABASE_NAME = "meal.db";
    private static final int DATABASE_Version = 4;

    private String TABLE_NAME = "meal";

//    private static final String Col_1_id = "id";
    private static final String Col_2_name = "foodname";
    private static final String Col_3_cals = "calories";
    private static final String Col_4_fat = "totalfat";
    private static final String Col_5_transfat = "transfat";
    private static final String Col_6_satfat = "satfat";
    private static final String Col_7_cholestrol = "cholestrol";
    private static final String Col_8_sodium = "sodium";
    private static final String Col_9_carbs = "carbs";
    private static final String Col_10_fiber = "fiber";
    private static final String Col_11_sugar = "sugar";
    private static final String Col_12_protein = "protein";







    public meal_mealDBHelper(Context context, String table_name) {
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
            System.out.println("1337 onCreate " + TABLE_NAME);
        }
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                        "(id INTEGER PRIMARY KEY, foodname text, calories DECIMAL(5,1), totalfat DECIMAL(5,1), transfat DECIMAL(5,1)," +
                        "satfat DECIMAL(5,1), cholestrol DECIMAL(5,1), sodium DECIMAL(5,1), carbs DECIMAL(5,1)," +
                        "fiber DECIMAL(5,1), sugar DECIMAL(5,1), protein DECIMAL(5,1));"
        );
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add a new food
    public boolean insertFoodinMeal(String name, Double calories, Double fat, Double transfat, Double satfat,
                                    Double cholestrol, Double sodium, Double carbs, Double fiber, Double sugar,
                                    Double protein) {
        if (isFoodInMealtable(name)) { //don't insert same item twice
            if (DEBUG) {
                System.out.println(name + " is already in database");
            }
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("Displaying data to be inserted before inserion into meal");
        System.out.println("name:" + name);
        System.out.println("cals:" + calories);
        System.out.println("fat:" + fat);
        System.out.println("transfat:" +transfat);
        System.out.println("satfat:" +satfat);
        System.out.println("cholestrol:" + cholestrol);
        System.out.println("sodium:" +sodium);
        System.out.println("carbs:" +carbs);
        System.out.println("fiber:" +fiber);
        System.out.println("sugar:" +sugar);
        System.out.println("protein:" +protein);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2_name, name);
        contentValues.put(Col_3_cals, calories);
        contentValues.put(Col_4_fat, fat);
        contentValues.put(Col_5_transfat, transfat);
        contentValues.put(Col_6_satfat, satfat);
        contentValues.put(Col_7_cholestrol, cholestrol);
        contentValues.put(Col_8_sodium, sodium);
        contentValues.put(Col_9_carbs, carbs);
        contentValues.put(Col_10_fiber, fiber);
        contentValues.put(Col_11_sugar, sugar);
        contentValues.put(Col_12_protein, protein);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    //return all foods and their respective macros. name, cals, fat, carbs, protein
    public ArrayList<String> getAllmacrosInfo() {
        ArrayList<String> macros_array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            macros_array_list.add(res.getString(res.getColumnIndex(Col_2_name)) + "," +
                    "Cal: " + res.getString(res.getColumnIndex(Col_3_cals)) + "," +
                    "Fat: " + res.getString(res.getColumnIndex(Col_4_fat)) + "," +
                    "Carbs: " + res.getString(res.getColumnIndex(Col_9_carbs)) + "," +
                    "Protein: " + res.getString(res.getColumnIndex(Col_12_protein)));

            res.moveToNext();
        }
        res.close();
        return macros_array_list;
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
    public boolean updateFoodinMeal(Integer id, String name, Double calories, Double totalfat, Double transfat, Double satfat,
                                    Double cholestrol, Double sodium, Double carbs, Double fiber, Double sugar,
                                    Double protein)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2_name, name);
        contentValues.put(Col_3_cals, calories);
        contentValues.put(Col_4_fat, totalfat);
        contentValues.put(Col_5_transfat, transfat);
        contentValues.put(Col_6_satfat, satfat);
        contentValues.put(Col_7_cholestrol, cholestrol);
        contentValues.put(Col_8_sodium, sodium);
        contentValues.put(Col_9_carbs, carbs);
        contentValues.put(Col_10_fiber, fiber);
        contentValues.put(Col_11_sugar, sugar);
        contentValues.put(Col_12_protein, protein);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    //deletes the food with the given id
    public Boolean deleteFoodinMeal(Integer id) {
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
    public boolean deleteFoodinMeal(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_NAME, Col_2_name + " = ?", new String[]{name});
        if (DEBUG) {
            System.out.println("deleted x value is " + delete);
        }
        return (delete != 0);
    }


    //returns a List of all food names in Database
    public ArrayList<String> getAllFoodfromMeal() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(Col_2_name)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    //gets food and calories information
    public ArrayList<String> getAllFoodInfofromMeal() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME +" WHERE id='" + id + "'",null);
        res.moveToFirst();
        long count = 0;
        while (res.isAfterLast() == false) {
            //what this commented out stuff is doing, is making each field be a seperate thing in the arraylist, which maybe not be desired

            array_list.add("Item: " + count);
            array_list.add("name " + res.getString(res.getColumnIndex(Col_2_name)));
            array_list.add("calories " + res.getString(res.getColumnIndex(Col_3_cals)));
            array_list.add("fat " + res.getString(res.getColumnIndex(Col_4_fat)));
            array_list.add("transfat " + res.getString(res.getColumnIndex(Col_5_transfat)));
            array_list.add("satfat " + res.getString(res.getColumnIndex(Col_6_satfat)));
            array_list.add("cholesterol " + res.getString(res.getColumnIndex(Col_7_cholestrol)));
            array_list.add("sodium " + res.getString(res.getColumnIndex(Col_8_sodium)));
            array_list.add("carbs " + res.getString(res.getColumnIndex(Col_9_carbs)));
            array_list.add("fiber " + res.getString(res.getColumnIndex(Col_10_fiber)));
            array_list.add("sugar " + res.getString(res.getColumnIndex(Col_11_sugar)));
            array_list.add("Protein " + res.getString(res.getColumnIndex(Col_12_protein)));
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
    public boolean isFoodInMealtable(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2_name)).equals(foodName)) {
                return true; //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return false;

    }

    //returns all foods and their respective calories at the same time
    public ArrayList<String> getAllFoodsAndCaloriesfromMeal() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(Col_2_name)) + ", cal: " + res.getString(res.getColumnIndex(Col_3_cals)));
            res.moveToNext();
        }
        res.close();
        return array_list;
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

    //true means the data is in the databse
    //true means food name is already in database
    //false means food is not in database
    public boolean isFoodInDataBase(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2_name)).equals(foodName)) {
                return true; //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return false;
    }
}
