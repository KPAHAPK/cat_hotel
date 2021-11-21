package com.example.cat_house.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.cat_house.data.HotelContract.GuestEntry


class GuestProvider : ContentProvider() {

    lateinit var dbHelper: HotelDbHelper

    companion object {
        const val TAG = "GuestProvider"
        const val GUESTS = 100
        const val GUEST_ID = 101
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        uriMatcher.addURI(HotelContract.CONTENT_AUTHORITY, HotelContract.PATH_GUESTS, GUESTS)
        uriMatcher.addURI(HotelContract.CONTENT_AUTHORITY, "${HotelContract.PATH_GUESTS} /#", GUEST_ID)
    }

    override fun onCreate(): Boolean {
        dbHelper = HotelDbHelper(context!!)
        return true
    }

    override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ): Cursor? {
        val database = dbHelper.readableDatabase

        val cursor: Cursor

        when (uriMatcher.match(uri)) {
            GUESTS -> {
                cursor = database.query(GuestEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            GUEST_ID -> {
                val mSelection = "${GuestEntry._ID} =?"
                val mSelectionArgs = arrayOf((ContentUris.parseId(uri)).toString())

                cursor = database.query(GuestEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            else -> {
                throw IllegalArgumentException("Cannot query unknown URI $uri")
            }
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return "0"
//        when (val match = uriMatcher.match(uri)) {
//            GUESTS -> {
//                HotelContract.GuestEntry.CONTENT_LIST_TYPE
//            }
//            GUEST_ID -> {
//                HotelContract.GuestEntry.CONTENT_ITEM_TYPE
//            }
//            else -> {
//                throw java.lang.IllegalArgumentException("Unknown URI $uri with match $match")
//            }
//        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            GUESTS -> {
                insertGuest(uri, values)
            }
            else -> {
                throw java.lang.IllegalArgumentException("Insertion is not supported for $uri")
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val database = dbHelper.writableDatabase

        return when (uriMatcher.match(uri)) {
            GUESTS -> {
                database.delete(HotelContract.GuestEntry.TABLE_NAME, selection, selectionArgs)
            }
            GUEST_ID -> {
                val mSelection = "${HotelContract.GuestEntry._ID} =?"
                val mSelectionArgs = arrayOf((ContentUris.parseId(uri)).toString())
                database.delete(HotelContract.GuestEntry.TABLE_NAME, mSelection, mSelectionArgs)
            }
            else -> {
                throw java.lang.IllegalArgumentException("Deletion is not supported for $uri")
            }
        }
    }

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            GUESTS -> {
                updateGuests(uri, values, selection, selectionArgs)
            }
            GUEST_ID -> {
                val mSelection = "${HotelContract.GuestEntry._ID} =?"
                val mSelectionArgs = arrayOf((ContentUris.parseId(uri)).toString())
                updateGuests(uri, values, mSelection, mSelectionArgs)
            }
            else -> {
                throw IllegalArgumentException("Update is not supported for " + uri)
            }
        }
    }

    private fun insertGuest(uri: Uri, values: ContentValues?): Uri? {
        val name = values?.getAsString(GuestEntry.COLUMN_NAME)
                ?: throw IllegalArgumentException("Guest requires a name")

        val gender = values.getAsString(GuestEntry.COLUMN_GENDER)
        if (gender == null) {
            throw IllegalArgumentException("Guest requires valid gender")
        }

        val age = values.getAsInteger(GuestEntry.COLUMN_AGE)
        if (age != null && age < 0) {
            throw IllegalArgumentException("Guest requires valid gender")
        }

        val database = dbHelper.writableDatabase

        val id = database.insert(GuestEntry.TABLE_NAME, null, values)

        if (id == -1L) {
            Log.e(TAG, "Failed to insert row for $uri")
            return null
        }

        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }

    private fun updateGuests(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
    ): Int {

        GuestEntry.apply {
            COLUMN_NAME.also {
                if (values?.containsKey(it) == true) {
                    val name = values.getAsString(it)
                            ?: throw IllegalArgumentException("Guests requires a name")
                }
            }

            COLUMN_GENDER.also {
                if (values?.containsKey(it) == true) {
                    val gender = values.getAsString(it)
                    if (gender == null) {
                        throw IllegalArgumentException("Guest requires valid gender")
                    }
                }
            }

            COLUMN_AGE.also{
                if (values?.containsKey(it) == true) {
                    val age = values.getAsInteger(it)
                    if (age != null && age < 0) {
                        throw IllegalArgumentException("Guest requires valid gender")
                    }
                }
            }
        }

        if (values?.size() == 0){
            return 0
        }

        val database = dbHelper.writableDatabase

        return  database.update(GuestEntry.TABLE_NAME, values, selection, selectionArgs)




    }
}