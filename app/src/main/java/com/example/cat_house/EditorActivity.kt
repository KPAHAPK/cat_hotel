package com.example.cat_house

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cat_house.data.HotelContract
import com.example.cat_house.data.HotelDbHelper
import com.example.cat_house.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    companion object {
        var gender = 0
    }

    lateinit var binding: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner();

        binding.insertGuest.setOnClickListener {
            insertGuest()
            finish()
        }

        binding.updateGuest.setOnClickListener {
            updateGuest()
            finish()
        }

        binding.deleteGuest.setOnClickListener {
            deleteGuest()
            finish()
        }
    }

    private fun deleteGuest() {
        val dbHelper = HotelDbHelper(this)
        val db = dbHelper.writableDatabase

        db.delete(HotelContract.GuestEntry.TABLE_NAME, "${HotelContract.GuestEntry.COLUMN_NAME}= ?", arrayOf("wer"))
    }

    private fun updateGuest() {
        val dbHelper = HotelDbHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.apply {
            put(HotelContract.GuestEntry.COLUMN_NAME, "Mashka")
            put(HotelContract.GuestEntry.COLUMN_CITY, "NewYork")
        }

        val updatedRow = db.update(
            HotelContract.GuestEntry.TABLE_NAME,
            values,
            "${HotelContract.GuestEntry.COLUMN_NAME}= ?",
            arrayOf("Mashka")
        )
    }

    private fun insertGuest() {
        val name = binding.editGuestName.text.toString().trim()
        val city = binding.editGuestCity.text.toString().trim()
        val age = binding.editGuestAge.text.toString().trim().toInt()

        val dbHelper = HotelDbHelper(this)

        val values = ContentValues()
        values.apply {
            put(HotelContract.GuestEntry.COLUMN_NAME, name)
            put(HotelContract.GuestEntry.COLUMN_CITY, city)
            put(HotelContract.GuestEntry.COLUMN_GENDER, gender)
            put(HotelContract.GuestEntry.COLUMN_AGE, age)
        }

        val db = dbHelper.writableDatabase

        val newRowId = db.insert(HotelContract.GuestEntry.TABLE_NAME, null, values)

        when (newRowId) {
            -1L -> {
                Toast.makeText(this, "Ошбика регистрации гостя", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Гость зарегестрирован", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setupSpinner() {
        val genderSpinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_gender_options,
            android.R.layout.simple_spinner_item
        );

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerGender.apply {
            adapter = genderSpinnerAdapter
            setSelection(2)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selection = parent?.getItemAtPosition(position) as String
                    gender = if (!TextUtils.isEmpty(selection)) {
                        HotelContract.GuestEntry.GENDER_MALE
                    } else if (selection == getString(R.string.gender_female)) {
                        HotelContract.GuestEntry.GENDER_FEMALE
                    } else {
                        HotelContract.GuestEntry.GENDER_UNKNOWN
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    gender = 2
                }

            }
        }
    }
}