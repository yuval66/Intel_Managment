package com.example.yuval.intel_managment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.R.attr.name;
import static android.provider.Telephony.Carriers.PASSWORD;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;

    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text,FNAME text, LNAME text); ";

    // Variable to hold the database instance
    public  SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    // Database open/upgrade helper
    private DataBaseHelper dbHelper;

    public  LoginDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public long insertEntry(String userName,String password, String FirstName, String LastName)
    {
        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD",MainActivity.md5(password));
        newValues.put("FNAME", FirstName);
        newValues.put("LNAME", LastName);

        long num = db.insert("LOGIN", null, newValues);
        return num;
    }
    public int deleteEntry(String UserName)
    {
        String where="USERNAME=?";
        int numberOFEntriesDeleted = db.delete("LOGIN", where, new String[]{UserName}) ;
        return numberOFEntriesDeleted;
    }

    public String getSinlgeEntry(String userName)
    {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));

        if(userName.equals("11410202"))
        {
                Log.d("11111111111", "here");
                return "Manager";
        }
        cursor.close();
        return password;
    }
    public void  updateEntry(String userName,String password)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD",password);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }

    ArrayList<String> getAll()
    {
        final ArrayList<String> Array = new ArrayList<String>();

        try {

            Cursor cursor = db.query(
                    "LOGIN",
                    new String[]{"USERNAME", "FNAME", "LNAME"},
                    null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    String s = cursor.getString(0) + " " +cursor.getString(1) + " " +cursor.getString(2);
                    Array.add(s);
                    cursor.moveToNext();
                }
                cursor.close();
                return Array;
            }
            return Array;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Array;
    }

    public long getCount() {
        String countQuery = "SELECT  * FROM " + "LOGIN";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public String getName(String userName)
    {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1)
        {
            cursor.close();
            return " ";
        }
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("FNAME")) + " "+ cursor.getString(cursor.getColumnIndex("LNAME"));

        return name;
    }


}


