package com.example.simpleloginapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama database dan tabel
    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // SQL query untuk membuat tabel
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERNAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_PASSWORD + " TEXT NOT NULL"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);  // Membuat tabel saat pertama kali dijalankan
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);  // Memperbarui database jika versi berubah
    }

    // Menambahkan data pengguna baru
    public long addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result;
    }

    // Mengecek apakah username sudah ada
    public boolean checkUsernameExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Mengecek apakah email sudah ada
    public boolean checkEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Mengecek login berdasarkan username dan password
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        boolean isAuthenticated = false;
        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            isAuthenticated = storedPassword.equals(password);
        }
        cursor.close();
        db.close();
        return isAuthenticated;
    }

    // Mendapatkan email berdasarkan username
    public String getEmailByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_EMAIL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
        }
        cursor.close();
        db.close();
        return email;
    }
}
