package com.candroid.sqlitehomework.repo

import android.content.ContentValues
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.candroid.sqlitehomework.model.Category

class CategoryRepo(context: Context) {
    private var context: Context? = null
    private var databaseGateway: DatabaseGateway? = null

    init {
        this.context = context
        this.databaseGateway = DatabaseGateway(context)
        if (!checkCategory("Engine")) insertCategory(Category(0, "Engine"))
    }

    fun insertCategory(category: Category): Long? {
        val writableDatabase = databaseGateway?.writableDatabase
        val contentValue = ContentValues().apply {
            put("Description", category.description)
        }

        val rowId = writableDatabase?.insert("Categories", null, contentValue)
        //writableDatabase?.close()
        return rowId
    }

    fun getCategories(): MutableSet<Category> {
        val categorySet = mutableSetOf<Category>()
        val readableDatabase = databaseGateway!!.readableDatabase
        val cursor = readableDatabase.rawQuery("SELECT * FROM Categories", null)

        while (cursor.moveToNext()) {
            categorySet.add(Category(cursor.getInt(0), cursor.getString(1)))
        }
        //readableDatabase.close()
        return categorySet
    }

    fun checkCategory(description: String) = getCategories().any { it.description == description }
}