package com.example.cat_house.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

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

        val match = uriMatcher.match(uri)
        when (match) {
            GUESTS -> {
                cursor = database.query(HotelContract.GuestEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            GUEST_ID -> {
                val mSelection = "${HotelContract.GuestEntry._ID} =?"
                val mSelectionArgs = arrayOf((ContentUris.parseId(uri)).toString())

                cursor = database.query(HotelContract.GuestEntry.TABLE_NAME,
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
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (val match = uriMatcher.match(uri)) {
            GUESTS -> {
                HotelContract.GuestEntry.CONTENT_LIST_TYPE
            }
            GUEST_ID -> {
                HotelContract.GuestEntry.CONTENT_ITEM_TYPE
            }
            else -> {
                throw java.lang.IllegalArgumentException("Unknown URI $uri with match $match")
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}