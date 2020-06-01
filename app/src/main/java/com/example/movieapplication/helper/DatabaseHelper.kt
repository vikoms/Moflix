package com.example.movieapplication.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.movieapplication.database.DatabaseContract
import com.example.movieapplication.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "dbmovieappliaction"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoriteColumns._ID} INTEGER PRIMARY KEY," +
                " ${DatabaseContract.FavoriteColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.POSTER} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.BACKDROP} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.RATING} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.RELEASE_DATE} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.TYPE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("create_database", SQL_CREATE_TABLE_FAVORITE)
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
        onCreate(db)
    }

}