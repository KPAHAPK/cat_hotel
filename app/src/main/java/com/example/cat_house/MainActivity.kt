package com.example.cat_house

import android.content.Intent
import android.os.Bundle
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

    override fun onStart() {
        super.onStart()
        displayDatabaseInfo()
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

        val selection = "${HotelContract.GuestEntry._ID} + >?"
        val selectionArgs = arrayOf("1")

        val cursor = db.query(
            HotelContract.GuestEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "${HotelContract.GuestEntry.COLUMN_AGE} + DESC" )


        try {
            binding.displayTextView.text = "Таблица содержит ${cursor.count} гостей.\n\n"

            binding.displayTextView.append("${HotelContract.GuestEntry._ID} -" +
                    "${HotelContract.GuestEntry.COLUMN_NAME} -" +
                    "${HotelContract.GuestEntry.COLUMN_CITY} - " +
                    "${HotelContract.GuestEntry.COLUMN_GENDER} - " +
                    "${HotelContract.GuestEntry.COLUMN_AGE} - ")

            binding.displayTextView.append("\n")

            val idColumnIndex = cursor.getColumnIndex(HotelContract.GuestEntry._ID)
            val nameColumnIndex = cursor.getColumnIndex(HotelContract.GuestEntry.COLUMN_NAME)
            val cityColumnIndex = cursor.getColumnIndex(HotelContract.GuestEntry.COLUMN_CITY)
            val genderColumnIndex = cursor.getColumnIndex(HotelContract.GuestEntry.COLUMN_GENDER)
            val ageColumnIndex = cursor.getColumnIndex(HotelContract.GuestEntry.COLUMN_AGE)

            while (cursor.moveToNext()){
                val currentID = cursor.getInt(idColumnIndex)
                val currentName = cursor.getString(nameColumnIndex)
                val currentCity = cursor.getString(cityColumnIndex)
                val currentGender = cursor.getInt(genderColumnIndex)
                val currentAge = cursor.getInt(ageColumnIndex)

                binding.displayTextView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentCity + " - " +
                        currentGender + " - " +
                        currentAge))
            }
        } finally {
            cursor.close()
        }

    }
}