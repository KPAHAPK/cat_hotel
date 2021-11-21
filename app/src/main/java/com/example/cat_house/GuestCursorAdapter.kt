package com.example.cat_house

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.cat_house.data.HotelContract

class GuestCursorAdapter(context: Context, cursor: Cursor?) : CursorAdapter(context, cursor) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val nameTextView = view?.findViewById(R.id.name) as TextView
        val summaryTextView = view.findViewById(R.id.summary) as TextView

        val nameColumnIndex = cursor?.getColumnIndex(HotelContract.GuestEntry.COLUMN_NAME)
        val cityColumnIndex = cursor?.getColumnIndex(HotelContract.GuestEntry.COLUMN_CITY)

        val guestName = nameColumnIndex?.let { cursor.getString(it) }
        var guestCity = cityColumnIndex?.let { cursor.getString(it) }

        if (guestCity.isNullOrEmpty()){
            guestCity = "unknown"
        }

        nameTextView.text = guestName
        summaryTextView.text = guestCity
    }

}