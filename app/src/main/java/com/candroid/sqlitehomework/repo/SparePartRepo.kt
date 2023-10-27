package com.candroid.sqlitehomework.repo

import android.content.ContentValues
import android.content.Context
import com.candroid.sqlitehomework.model.Category
import com.candroid.sqlitehomework.model.SparePart

class SparePartRepo(context: Context) {
    private var context: Context? = null
    private var databaseGateway: DatabaseGateway? = null
    init {
        this.context = context
        this.databaseGateway = DatabaseGateway(context)
    }

    fun insertSparePart(sparePart: SparePart): Long?{
        val writableDatabase = databaseGateway?.writableDatabase
        val contentValue = ContentValues().apply {
            put("CategoryId", sparePart.categoryId)
            put("Name", sparePart.name)
            put("Stock", sparePart.stock)
            put("Price", sparePart.price)
        }

        val rowId = writableDatabase?.insert("SpareParts", null, contentValue)
        //writableDatabase?.close()
        return rowId
    }

    fun getSparePartsByCategory(category: Category): MutableList<SparePart>{
        val spareParts = mutableListOf<SparePart>()
        val readableDatabase = databaseGateway!!.readableDatabase
        var cursor = if (category.description == "All Categories") {
            readableDatabase.rawQuery("SELECT * FROM SpareParts", arrayOf())
        } else{
            readableDatabase.rawQuery("SELECT * FROM SpareParts WHERE CategoryId  = ?", arrayOf(category.categoryId.toString()))
        }

        while (cursor.moveToNext()){
            spareParts.add(
                SparePart(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
                )
            )
        }

        readableDatabase.close()

        return spareParts
    }
}