package com.example.culturenearby;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table culturstable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "imageUrl text,"
                + "address text,"
                + "mapLink text,"
                + "isDefault boolean,"
                + "info text" + ");");

        db.execSQL("create table usertable ("
                + "id integer primary key,"
                + "email text,"
                + "login text,"
                + "pass text" + ");");


        db.execSQL("create table currentusertable ("
                + "id integer primary key,"
                + "email text,"
                + "login text,"
                + "pass text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}