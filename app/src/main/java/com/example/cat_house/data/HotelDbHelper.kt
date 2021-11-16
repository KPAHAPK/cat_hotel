package com.example.cat_house.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HotelDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "hotel.db"
        const val LOG_TAG = "HotelDbHelper"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_GUESTS_TABLE = "CREATE TABLE ${HotelContract.GuestEntry.TABLE_NAME}" +
                "(${HotelContract.GuestEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${HotelContract.GuestEntry.COLUMN_NAME} TEXT NOT NULL," +
                "${HotelContract.GuestEntry.COLUMN_CITY} TEXT NOT NULL," +
                "${HotelContract.GuestEntry.COLUMN_GENDER} INTEGER NOT NULL DEFAULT 3," +
                "${HotelContract.GuestEntry.COLUMN_AGE} INTEGER NOT NULL DEFAULT 0);"

        db?.execSQL(SQL_CREATE_GUESTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}