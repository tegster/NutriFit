package fitness.cs115.a115fitnessapp;

/**
 * Created by Matthew on 10/7/16.
 */
//this was used as a starting point but modified a bunch https://www.tutorialspoint.com/android/android_sqlite_database.htm

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Pair;

import static android.R.attr.id;
//helper class for the food database. it can return the total amount of each macronutrient consumed each day
public class meal_foodDBHelper extends SQLiteOpenHelper {
    private static final Boolean DEBUG = true;

    //intializing the database
    private static final String DATABASE_NAME = "foods.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "food";

    //declaring variables for the columns of our database
    public static final String Col_1 = "ID";
    public static final String Col_2 = "foodname";
    public static final String Col_3 = "calories";
    public static final String Col_4 = "totalfat";
    public static final String Col_5 = "transfat";
    public static final String Col_6 = "satfat";
    public static final String Col_7 = "cholestrol";
    public static final String Col_8 = "sodium";
    public static final String Col_9 = "carbs";
    public static final String Col_10 = "fiber";
    public static final String Col_11 = "sugar";
    public static final String Col_12 = "protein";


    public meal_foodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) {
            System.out.println("1337 onCreate");
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
    public boolean insertFood(String foodname, Double calories, Double totalfat, Double transfat, Double satfat,
                              Double cholestrol, Double sodium, Double carbs, Double fiber, Double sugar,
                              Double protein) {
        if (isFoodInDataBase(foodname)) { //don't insert same item twice

            if (DEBUG) {
                System.out.println(foodname + " is already in database");
            }
            return false;
        }
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
                    "Carbs: "+ res.getString(res.getColumnIndex(Col_9)) + "," +
                    "Protein: " + res.getString(res.getColumnIndex(Col_12)));

            res.moveToNext();
        }
        res.close();
        return macros_array_list;
    }


    //gets food and calories information
    public HashMap<String,HashMap<String, Double>> getAllFoodInfo() {
        HashMap<String ,HashMap<String ,Double>> Totalinfo = new HashMap<String, HashMap<String, Double>>();
        HashMap<String,Double> Totalfoodmacros = new HashMap<>();
      //  ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        Double count = 0.0;
       // while () {
            while (res.isAfterLast() == false) {
                Totalfoodmacros = new HashMap<>();
                Totalfoodmacros.put("Item: ", count );
                Double calories = res.getDouble(res.getColumnIndex(Col_3));
                Double totalfat = res.getDouble(res.getColumnIndex(Col_4));
                Double transfat = res.getDouble(res.getColumnIndex(Col_5));
                Double satfat = res.getDouble(res.getColumnIndex(Col_6));
                Double Cholestrol = res.getDouble(res.getColumnIndex(Col_7));
                Double sodium = res.getDouble(res.getColumnIndex(Col_8));
                Double carbs = res.getDouble(res.getColumnIndex(Col_9));
                Double fiber = res.getDouble(res.getColumnIndex(Col_10));
                Double sugar = res.getDouble(res.getColumnIndex(Col_11));
                Double protein = res.getDouble(res.getColumnIndex(Col_12));
                Totalfoodmacros.put("calories", calories);
                Totalfoodmacros.put("fat", totalfat);
                Totalfoodmacros.put("transfat", transfat);
                Totalfoodmacros.put("satfat", satfat);
                Totalfoodmacros.put("Cholestrol", Cholestrol);
                Totalfoodmacros.put("sodium", sodium);
                Totalfoodmacros.put("carbs", carbs);
                Totalfoodmacros.put("fiber", fiber);
                Totalfoodmacros.put("sugar", sugar);
                Totalfoodmacros.put("protein", protein);
                Totalinfo.put(res.getString(res.getColumnIndex(Col_2)), Totalfoodmacros);
                System.out.println("printing totalfoodmacros.entryset from fooddbhelper");
                System.out.println(Totalfoodmacros.entrySet());
                //System.out.println(Totalinfo.entrySet());

                //Totalfoodmacros.clear();
               // System.out.println("After Inserting    "+Totalinfo.entrySet());

                res.moveToNext();


            }
       // System.out.println(Totalfoodmacros.entrySet());

     //   Totalfoodmacros.clear();
        //Totalinfo.put(res.getString(res.getColumnIndex(Col_2)), Totalfoodmacros);
        System.out.println("Printing whats leeaving getallfoodinfo");
        System.out.println(Totalinfo.entrySet());
            res.close();

       // }


        return Totalinfo;
    }

    //get macros returned in a map
    //gets food and calories information
    public HashMap<String,HashMap<String, Double>> getfourfoodmacros() {
        HashMap<String ,HashMap<String ,Double>> Totalinfo = new HashMap<String, HashMap<String, Double>>();
        HashMap<String,Double> Totalfoodmacros = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        Double count = 0.0;
        while (res.isAfterLast() == false) {
            Totalfoodmacros = new HashMap<>();
            Totalfoodmacros.put("Item: ", count );
            Double calories = res.getDouble(res.getColumnIndex(Col_3));
            Double totalfat = res.getDouble(res.getColumnIndex(Col_4));
            Double carbs = res.getDouble(res.getColumnIndex(Col_9));
            Double protein = res.getDouble(res.getColumnIndex(Col_12));
            Totalfoodmacros.put("calories", calories);
            Totalfoodmacros.put("fat", totalfat);
            Totalfoodmacros.put("carbs", carbs);
            Totalfoodmacros.put("protein", protein);
            Totalinfo.put(res.getString(res.getColumnIndex(Col_2)), Totalfoodmacros);
           // System.out.println("printing totalfoodmacros.entryset from fooddbhelper");
           // System.out.println(Totalfoodmacros.entrySet());

            res.moveToNext();
        }

      //  System.out.println("Printing whats leeaving getallfoodinfo");
       // System.out.println(Totalinfo.entrySet());

        return Totalinfo;
    }

    //gets food and calories information
    public ArrayList<String> getAllFoodInfoList() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
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
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                return true; //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return false;
    }

    //returns the transfar associated with the foodname
    public double getTransFat(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                System.out.println("value of transfat is: " + res.getDouble(res.getColumnIndex(Col_5)));
                return res.getDouble(res.getColumnIndex(Col_5)); //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return 0;//couldn't find it
    }

    //returns the satFar associated with the foodname
    public double getSatFat(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                System.out.println("value of satfat is: " + res.getDouble(res.getColumnIndex(Col_6)));
                return res.getDouble(res.getColumnIndex(Col_6)); //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return 0;//couldn't find it
    }

    //returns the cholesterol associated with the foodname
    public double getCholesterol(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                System.out.println("value of cholesterol is: " + res.getDouble(res.getColumnIndex(Col_7)));
                return res.getDouble(res.getColumnIndex(Col_7)); //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return 0;//couldn't find it
    }

    //returns the sodium associated with the foodname
    public double getSodium(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                System.out.println("value of sodium is: " + res.getDouble(res.getColumnIndex(Col_8)));
                return res.getDouble(res.getColumnIndex(Col_8)); //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return 0;//couldn't find it
    }

    //returns the fiber associated with the foodname
    public double getFiber(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                System.out.println("value of fiber is: " + res.getDouble(res.getColumnIndex(Col_10)));
                return res.getDouble(res.getColumnIndex(Col_10)); //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return 0;//couldn't find it
    }

    //returns the sugar associated with the foodname
    public double getSugar(String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            if (res.getString(res.getColumnIndex(Col_2)).equals(foodName)) {
                System.out.println("value of sugar is: " + res.getDouble(res.getColumnIndex(Col_11)));
                return res.getDouble(res.getColumnIndex(Col_11)); //means food is in database
            }
            res.moveToNext();
        }
        res.close();
        return 0;//couldn't find it
    }
    //deletes all data stored in table
    public boolean deleteEntireTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        return true;
    }

}
