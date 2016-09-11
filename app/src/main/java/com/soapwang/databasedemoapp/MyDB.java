package com.soapwang.databasedemoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by Acer on 2016/6/28.
 */
public class MyDB {
    private SQLiteDatabase db;
    private final Context context;
    private final MyDBhelper myDBhelper;

    public MyDB(Context context) {
        this.context = context;
        myDBhelper = new MyDBhelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public void close() {
        db.close();
    }

    public void open() throws SQLiteException {
        try {
            db = myDBhelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.d("MyDB", "Open database exception caught "+e.getMessage());
            db = myDBhelper.getReadableDatabase();
        }
    }

    public  long insertNote(String title, String content) {
        try {
            ContentValues value = new ContentValues();
            value.put(Constants.TITLE_NAME, title);
            value.put(Constants.CONTENT_NAME, content);
            value.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());
            return db.insert(Constants.TABLE_NAME, null, value);
        } catch(SQLiteException e) {
            Log.d("MyDB", "Insert exception " + e.getMessage());
            return -1;
        }
    }

    public long updateNote(String id, String title, String content) {
        try {
            ContentValues value = new ContentValues();
            value.put(Constants.TITLE_NAME, title);
            value.put(Constants.CONTENT_NAME, content);
            value.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());
            return db.update(Constants.TABLE_NAME, value, "_id='" + id + "'", null);
        } catch (SQLiteException e) {
            Log.d("MyDB", "Update exception " + e.getMessage());
            return -1;
        }
    }

    public int deleteNote(String id) {
        try {
            return db.delete(Constants.TABLE_NAME, "_id='" + id + "'", null);
        } catch (SQLiteException e) {
            Log.d("MyDB", "Delete exception " + e.getMessage());
            return -1;
        }
    }

    public Cursor queryNote(String id) {
        try {
            Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME + " where _id='" + id + "'", null);
            Log.d("MyDB", "rawQuery result:" + c.getCount());
            if(c.getCount() > 0)
                return c;
            else
                return null;
        } catch(SQLiteException e) {

            return null;
        }
    }

    public Cursor getNote() {
        try {
            Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null,  Constants.DATE_NAME + " DESC");
            return c;
        } catch (SQLiteException e) {
            Log.d("MyDB", "getNote exception " + e.getMessage());
            return null;
        }
    }
}
