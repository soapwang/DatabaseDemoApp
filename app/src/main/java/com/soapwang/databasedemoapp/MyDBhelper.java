package com.soapwang.databasedemoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Acer on 2016/6/28.
 */
public class MyDBhelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "create table " + Constants.TABLE_NAME +" (" +
            Constants.KEY_ID + " integer primary key autoincrement, " +
            Constants.TITLE_NAME + " text not null, " +
            Constants.CONTENT_NAME + ", " +
            Constants.DATE_NAME + " long);";
    public MyDBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("MyDBhelper", "onCreate");
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLiteException e) {
            Log.d("MyDBhelper", "Create table exception " + e.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Constants.TABLE_NAME);
        onCreate(db);
    }
}