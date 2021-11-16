package com.example.cat_house

import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.cat_house.data.HotelContract
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