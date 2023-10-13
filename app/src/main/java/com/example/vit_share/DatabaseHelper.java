package com.example.vit_share;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "vit_share.db";

    // User table constants
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_PASSWORD = "password";

    // Request table constants
    public static final String REQUEST_TABLE_NAME = "requests";
    public static final String REQUEST_COLUMN_ID = "id";
    public static final String REQUEST_COLUMN_TITLE = "title";
    public static final String REQUEST_COLUMN_DESCRIPTION = "description";
    public static final String REQUEST_COLUMN_USER_ID = "user_id";
    public static final String REQUEST_COLUMN_ACCEPTED_BY = "accepted_by";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the user table
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_EMAIL + " TEXT UNIQUE, " +
                USER_COLUMN_PASSWORD + " TEXT)");

        // Create the request table
        db.execSQL("CREATE TABLE " + REQUEST_TABLE_NAME + "(" +
                REQUEST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REQUEST_COLUMN_TITLE + " TEXT, " +
                REQUEST_COLUMN_DESCRIPTION + " TEXT, " +
                REQUEST_COLUMN_USER_ID + " INTEGER, " +
                REQUEST_COLUMN_ACCEPTED_BY + " INTEGER DEFAULT -1, " +
                "FOREIGN KEY(" + REQUEST_COLUMN_USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REQUEST_TABLE_NAME);
        onCreate(db);
    }

    // User table methods

    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_PASSWORD, password);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean checkUserEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COLUMN_EMAIL + " = ? AND " + USER_COLUMN_PASSWORD + " = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + USER_COLUMN_ID + " FROM " + USER_TABLE_NAME + " WHERE " + USER_COLUMN_EMAIL + " = ?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(USER_COLUMN_ID);
            if (userIdIndex >= 0) {
                userId = cursor.getInt(userIdIndex);
            }
        }
        cursor.close();
        return userId;
    }

    // Request table methods

    public boolean insertRequest(String title, String description, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_COLUMN_TITLE, title);
        contentValues.put(REQUEST_COLUMN_DESCRIPTION, description);
        contentValues.put(REQUEST_COLUMN_USER_ID, userId);
        long result = db.insert(REQUEST_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public ArrayList<Request> getAllRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requestList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + REQUEST_TABLE_NAME, null);
        int idIndex = cursor.getColumnIndex(REQUEST_COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(REQUEST_COLUMN_TITLE);
        int descIndex = cursor.getColumnIndex(REQUEST_COLUMN_DESCRIPTION);
        int userIdIndex = cursor.getColumnIndex(REQUEST_COLUMN_USER_ID);
        int acceptedByIndex = cursor.getColumnIndex(REQUEST_COLUMN_ACCEPTED_BY);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String description = cursor.getString(descIndex);
            int userId = cursor.getInt(userIdIndex);
            int acceptedBy = cursor.getInt(acceptedByIndex);
            requestList.add(new Request(id, title, description, userId, acceptedBy));
        }
        cursor.close();
        return requestList;
    }




    public ArrayList<Request> getRequestsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Request> requestList = new ArrayList<>();
        String[] columns = {REQUEST_COLUMN_ID, REQUEST_COLUMN_TITLE, REQUEST_COLUMN_DESCRIPTION, REQUEST_COLUMN_ACCEPTED_BY};
        String selection = REQUEST_COLUMN_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(REQUEST_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(REQUEST_COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(REQUEST_COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(REQUEST_COLUMN_DESCRIPTION));
            int acceptedBy = cursor.getInt(cursor.getColumnIndexOrThrow(REQUEST_COLUMN_ACCEPTED_BY));
            requestList.add(new Request(id, title, description, userId, acceptedBy));
        }
        cursor.close();
        return requestList;
    }

    public boolean acceptRequest(int requestId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_COLUMN_ACCEPTED_BY, userId);
        String selection = REQUEST_COLUMN_ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(requestId) };
        int count = db.update(REQUEST_TABLE_NAME, contentValues, selection, selectionArgs);
        return count > 0;
    }



    public boolean updateRequestAcceptedBy(int requestId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_COLUMN_ACCEPTED_BY, userId);
        int rowsAffected = db.update(REQUEST_TABLE_NAME, contentValues, REQUEST_COLUMN_ID + " = ?", new String[]{String.valueOf(requestId)});
        return rowsAffected > 0;
    }
}
