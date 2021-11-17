package com.example.cat_house

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.cat_house.data.HotelContract
import com.example.cat_house.data.HotelDbHelper
import com.example.cat_house.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: HotelDbHelper
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, EditorActivity::class.java)
            startActivity(intent)
        }

        dbHelper = HotelDbHelper(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_insert_new_data) {
            insertGuest()
            displayDatabaseInfo()
        }
        if (item.itemId == R.id.action_insert_new_data_by_exec) {
            insertGuestByExec()
            displayDatabaseInfo()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        displayDatabaseInfo()
    }

    private fun insertGuest() {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.apply {
            put(HotelContract.GuestEntry.COLUMN_NAME, "Murzik")
            put(HotelContract.GuestEntry.COLUMN_CITY, "Murmansk")
            put(HotelContract.GuestEntry.COLUMN_GENDER, HotelContract.GuestEntry.GENDER_MALE)
            put(HotelContract.GuestEntry.COLUMN_AGE, 7)
        }
        db.insert(HotelContract.GuestEntry.TABLE_NAME, null, values)
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

    private fun displayDatabaseInfo() {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            HotelContract.GuestEntry._ID,
            HotelContract.GuestEntry.COLUMN_NAME,
            HotelContract.GuestEntry.COLUMN_CITY,
            HotelContract.GuestEntry.COLUMN_GENDER,
            HotelContract.GuestEntry.COLUMN_AGE
        )

        val selection = "${HotelContract.GuestEntry._ID} >?"
        val selectionArgs = arrayOf("1")

        val cursor = db.query(
            HotelContract.GuestEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "${HotelContract.GuestEntry._ID} DESC"
        )

        //use заменяте try{} - finally{cursor.close()}
        cursor.use {
            binding.displayTextView.text = "Таблица содержит ${it.count} гостей.\n\n"

            binding.displayTextView.append(
                "${HotelContract.GuestEntry._ID} -" +
                        "${HotelContract.GuestEntry.COLUMN_NAME} -" +
                        "${HotelContract.GuestEntry.COLUMN_CITY} - " +
                        "${HotelContract.GuestEntry.COLUMN_GENDER} - " +
                        "${HotelContract.GuestEntry.COLUMN_AGE} - "
            )

            binding.displayTextView.append("\n")

            val idColumnIndex = it.getColumnIndex(HotelContract.GuestEntry._ID)
            val nameColumnIndex = it.getColumnIndex(HotelContract.GuestEntry.COLUMN_NAME)
            val cityColumnIndex = it.getColumnIndex(HotelContract.GuestEntry.COLUMN_CITY)
            val genderColumnIndex = it.getColumnIndex(HotelContract.GuestEntry.COLUMN_GENDER)
            val ageColumnIndex = it.getColumnIndex(HotelContract.GuestEntry.COLUMN_AGE)

            while (it.moveToNext()) {
                val currentID = it.getInt(idColumnIndex)
                val currentName = it.getString(nameColumnIndex)
                val currentCity = it.getString(cityColumnIndex)
                val currentGender = it.getInt(genderColumnIndex)
                val currentAge = it.getInt(ageColumnIndex)

                binding.displayTextView.append(
                    ("\n" + currentID + " - " +
                            currentName + " - " +
                            currentCity + " - " +
                            currentGender + " - " +
                            currentAge)
                )
            }
        }

    }
}