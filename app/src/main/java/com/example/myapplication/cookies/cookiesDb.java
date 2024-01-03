package com.example.myapplication.cookies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.model.User;

import java.util.ArrayList;

public class cookiesDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "BinusPark";

    private static final String USER_TABLE = "user";

    private static final String USERID_COLUMN = "id";

    private static final String USERNAME_COLUMN = "username";

    private static final String NAME_COLUMN = "name";

    private static final String EMAIL_COLUMN = "email";

    private static final String PASSWORD_COLUMN = "password";

    private static final String PHONE_COLUMN = "phone";

    private static final int DB_VERSION = 1;

    public cookiesDb(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String query = "CREATE TABLE " + USER_TABLE + " (" +
                USERID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                USERNAME_COLUMN + " TEXT NOT NULL, " +
                NAME_COLUMN + " TEXT NOT NULL, " +
                EMAIL_COLUMN + " TEXT NOT NULL, " +
                PASSWORD_COLUMN + " TEXT NOT NULL, " +
                PHONE_COLUMN + " INTEGER NOT NULL) ";

        db.execSQL(query);
    }

    public void createUserCookies(String username, String name, String email, String password, int phone){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues userCookiesCV = new ContentValues();

        userCookiesCV.put(USERNAME_COLUMN, username);

        userCookiesCV.put(NAME_COLUMN, name);

        userCookiesCV.put(EMAIL_COLUMN, email);

        userCookiesCV.put(PASSWORD_COLUMN, password);

        userCookiesCV.put(PHONE_COLUMN, phone);

        db.insert(USER_TABLE, null, userCookiesCV);

        db.close();
    }

    public ArrayList<User> getUserCookies(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE, null);

        ArrayList<User> userCookies = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                User user = new User(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)), cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COLUMN)), cursor.getString(cursor.getColumnIndexOrThrow(USERNAME_COLUMN)), cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_COLUMN)), Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(PHONE_COLUMN))));

                userCookies.add(user);
            }while(cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return userCookies;
    }

    public void deleteUserCookies(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(USER_TABLE, USERNAME_COLUMN + "=?", new String[]{username});

        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + USER_TABLE;

        db.execSQL(query);

        db.close();
    }

    public void updateUserCookies(String username, String name, String email, String password, int phone){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues userCookiesCV = new ContentValues();

        userCookiesCV.put(NAME_COLUMN, name);

        userCookiesCV.put(EMAIL_COLUMN, email);

        userCookiesCV.put(PASSWORD_COLUMN, password);

        userCookiesCV.put(PHONE_COLUMN, phone);

        db.update(USER_TABLE, userCookiesCV, USERNAME_COLUMN + "=?", new String[]{username});

        db.close();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "DROP TABLE IF EXISTS " + USER_TABLE;

        db.execSQL(query);

        onCreate(db);
    }
}
