package com.example.movieapplication.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.movieapplication"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "Favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val POSTER = "poster"
            const val BACKDROP = "backdrop"
            const val RELEASE_DATE = "release_date"
            const val RATING = "rating"
            const val TYPE = "type"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}

