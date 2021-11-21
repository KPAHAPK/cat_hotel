package com.example.cat_house.data

import android.net.Uri
import android.provider.BaseColumns

class HotelContract {

    companion object{
        const val CONTENT_AUTHORITY = "com.example.cat_house"
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")
        const val PATH_GUESTS = "guests"
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GUESTS)
    }

    open class GuestEntry : BaseColumns{
        companion object{
            const val TABLE_NAME = "guests"

            const val _ID = BaseColumns._ID
            const val COLUMN_NAME = "name"
            const val COLUMN_CITY = "city"
            const val COLUMN_GENDER = "gender"
            const val COLUMN_AGE = "age"

            const val GENDER_FEMALE = 0
            const val GENDER_MALE = 1
            const val GENDER_UNKNOWN = 2
        }

    }

}