package com.soapwang.databasedemoapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Acer on 2016/7/4.
 */
public class NoteContentProvider extends ContentProvider {
    private MyDB myDB;
    private static final UriMatcher uriMatcher;
    private static final int NOTES = 1;
    public static final String AUTHORITY = "com.soapwang.databasedemoapp";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Constants.TABLE_NAME, NOTES);
    }

    public int update(Uri uri, ContentValues v, String selection, String[] selectionArgs) {
        return 0;
    }

    public Uri insert(Uri uri, ContentValues v) {
        return null;
    }

    public String getType(Uri uri) {
        return  null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public boolean onCreate() {
        myDB = new MyDB(this.getContext());
        myDB.open();
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                c = myDB.getNote();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "  + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return  c;
    }
}
