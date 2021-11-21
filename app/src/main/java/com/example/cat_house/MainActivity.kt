package com.example.cat_house

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.cat_house.data.HotelContract
import com.example.cat_house.data.HotelDbHelper
import com.example.cat_house.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var dbHelper: HotelDbHelper
    private lateinit var binding: ActivityMainBinding
    private lateinit var mCursorAdapter: GuestCursorAdapter

    companion object{
        const val GUEST_LOADER = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            val intent = Intent(
                this, EditorActivity::class.java
            )
            startActivity(intent)
        }

        val guestListView = findViewById<ListView>(R.id.list)

        val emptyView = findViewById<View>(R.id.empty_view)
        guestListView.emptyView = emptyView

        mCursorAdapter = GuestCursorAdapter(this, null)
        guestListView.adapter = mCursorAdapter

        guestListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, id ->
                val intent = Intent(this, EditorActivity::class.java)

                val currentGuestUri = ContentUris.withAppendedId(HotelContract.CONTENT_URI, id)
                intent.data = currentGuestUri
                startActivity(intent)
            }

        LoaderManager.getInstance(this).initLoader(GUEST_LOADER, null, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_insert_new_data) {
            insertGuest()
        }
        if (item.itemId == R.id.action_insert_new_data_by_exec) {
            insertGuestByExec()
        }
        return true
    }

    private fun insertGuest() {

        val values = ContentValues()
        values.apply {
            put(HotelContract.GuestEntry.COLUMN_NAME, "Murzik")
            put(HotelContract.GuestEntry.COLUMN_CITY, "Murmansk")
            put(HotelContract.GuestEntry.COLUMN_GENDER, HotelContract.GuestEntry.GENDER_MALE)
            put(HotelContract.GuestEntry.COLUMN_AGE, 7)
        }
        val newUri = contentResolver.insert(HotelContract.CONTENT_URI, values)
    }

    private fun insertGuestByExec() {
        val db = dbHelper.writableDatabase

        val insertQuery =
            "INSERT INTO ${HotelContract.GuestEntry.TABLE_NAME} " +
                    "(${HotelContract.GuestEntry.COLUMN_NAME}, " +
                    "${HotelContract.GuestEntry.COLUMN_CITY}) " +
                    "VALUES ('Васька','Питер')"
        db.execSQL(insertQuery)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            HotelContract.GuestEntry._ID,
            HotelContract.GuestEntry.COLUMN_NAME,
            HotelContract.GuestEntry.COLUMN_CITY
        )

        return CursorLoader(
            this, HotelContract.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mCursorAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mCursorAdapter.swapCursor(null)
    }
}