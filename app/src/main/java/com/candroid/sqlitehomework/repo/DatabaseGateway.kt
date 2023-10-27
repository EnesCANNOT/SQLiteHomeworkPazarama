package com.candroid.sqlitehomework.repo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseGateway(context: Context) :
    SQLiteOpenHelper(context, "SpareParts.db", null, 1) {
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("CREATE TABLE IF NOT EXISTS Categories (ID INTEGER PRIMARY KEY AUTOINCREMENT, Description TEXT)")
        database?.execSQL("CREATE TABLE IF NOT EXISTS SpareParts (ID INTEGER PRIMARY KEY AUTOINCREMENT, CategoryId INTEGER, Name TEXT, Stock INTEGER, Price INTEGER)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}