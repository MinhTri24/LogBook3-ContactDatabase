package com.example.logbook3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ContactDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "persons";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "demo_name";
    private static final String COLUMN_DOB = "demo_dob";
    private static final String COLUMN_EMAIL = "demo_email";
    private static final String COLUMN_IMAGE = "demo_image";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DOB + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_IMAGE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPerson(String name, String dob, String email, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DOB, dob);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_IMAGE, image);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Error saving contact")
                    .setPositiveButton("Back", null)
                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setTitle("Success")
                    .setMessage("New contact saved")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
