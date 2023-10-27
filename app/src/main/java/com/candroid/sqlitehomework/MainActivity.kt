package com.candroid.sqlitehomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.candroid.sqlitehomework.model.Category
import com.candroid.sqlitehomework.model.SparePart
import com.candroid.sqlitehomework.repo.SparePartRepo
import com.candroid.sqlitehomework.ui.theme.SQLiteHomeworkTheme
import com.candroid.sqlitehomework.view.MainScreen

class MainActivity : ComponentActivity() {
    lateinit var sparePartRepo: SparePartRepo
    lateinit var spareParts: MutableList<SparePart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sparePartRepo = SparePartRepo(this@MainActivity)
        setContent {
            spareParts = mutableListOf()
            SQLiteHomeworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    spareParts = sparePartRepo.getSparePartsByCategory(Category(-1, "All Categories"))
                    MainScreen(context = this@MainActivity)
                }
            }
        }
    }
}
