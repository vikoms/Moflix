package com.example.movieapplication.helper

import android.database.Cursor
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.BACKDROP
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.POSTER
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.RATING
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.RELEASE_DATE
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.TITLE
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion._ID
import com.example.movieapplication.entity.Favorite

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val title = getString(getColumnIndexOrThrow(TITLE))
                val type = getString(getColumnIndexOrThrow(TYPE))
                val date = getString(getColumnIndexOrThrow(RELEASE_DATE))
                val rating = getString(getColumnIndexOrThrow(RATING)).toDouble()
                val backdrop = getString(getColumnIndexOrThrow(BACKDROP))
                val poster = getString(getColumnIndexOrThrow(POSTER))

                favoriteList.add(Favorite(id, title, backdrop, poster, rating, type, date))
            }
        }
        return favoriteList
    }
}