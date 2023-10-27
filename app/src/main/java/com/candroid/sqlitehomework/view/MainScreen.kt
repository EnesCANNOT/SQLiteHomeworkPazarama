package com.candroid.sqlitehomework.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.candroid.sqlitehomework.model.Category
import com.candroid.sqlitehomework.repo.CategoryRepo
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import com.candroid.sqlitehomework.model.SparePart
import com.candroid.sqlitehomework.repo.SparePartRepo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(context: Context) {
    val categoryRepo = CategoryRepo(context)
    val sparePartRepo = SparePartRepo(context)
    val categories = categoryRepo.getCategories()
    val categoryList = categories.sortedBy {
        it.description
    }.toMutableList()
    categoryList.add(0, Category(0, "All Categories"))
    categoryList.add(Category(1, "Add New Category"))
    val isExpanded = remember { mutableStateOf(false) }
    val selectedCategory = remember { mutableStateOf(categoryList[0]) }

    val isAlertDialogForCategoryVisible = remember { mutableStateOf(false) }
    val newCategoryInput = remember { mutableStateOf("") }

    val isAlertDialogForSparePartVisible = remember { mutableStateOf(false) }
    val newSparePartName = remember { mutableStateOf("") }
    val newSparePartStock: MutableState<Int?> = remember { mutableStateOf(0) }
    val newSparePartPrice: MutableState<Int?> = remember { mutableStateOf(0) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            isAlertDialogForSparePartVisible.value = true
        }) {

            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Magenta)
                    .height(35.dp)
            ) {
                Row(modifier = Modifier
                    .clickable { isExpanded.value = !isExpanded.value }
                    .align(Alignment.TopStart)
                )
                {
                    Text(text = selectedCategory.value.description)
                    Icon(imageVector = Icons.Outlined.ArrowDropDown, null)
                }

                DropdownMenu(
                    expanded = isExpanded.value,
                    onDismissRequest = { isExpanded.value = false }
                )
                {
                    categoryList.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.description) },
                            onClick = {
                                selectedCategory.value = it
                                isExpanded.value = false

                                if (selectedCategory.value.description == "Add New Category") {
                                    isAlertDialogForCategoryVisible.value = true
                                }
                            }
                        )
                    }
                }
            }

            if (isAlertDialogForCategoryVisible.value) {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the AlertDialog
                        isAlertDialogForCategoryVisible.value = false
                    },
                    title = { Text("Add New Category") },
                    text = {
                        // TextField to get input for the new category
                        OutlinedTextField(
                            value = newCategoryInput.value,
                            onValueChange = {
                                newCategoryInput.value = it
                            },
                            label = { Text("Category Name") }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Add the new category and dismiss the AlertDialog
                                if (newCategoryInput.value.isNotEmpty()) {
                                    if (!categoryRepo.checkCategory(newCategoryInput.value)) {
                                        val newCategoryId = (categoryRepo.insertCategory(Category(0, newCategoryInput.value))!!)
                                        if (newCategoryId > 0) {
                                            val newCategory = Category(newCategoryId.toInt(), newCategoryInput.value)
                                            categoryList.add(categoryList.size-1, newCategory)
                                            selectedCategory.value = newCategory
                                            Toast.makeText(
                                                context,
                                                "Category added successfully.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Something went wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "The category already exists.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Category description can not be empty.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                isAlertDialogForCategoryVisible.value = false
                            }
                        ) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                isAlertDialogForCategoryVisible.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (isAlertDialogForSparePartVisible.value) {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the AlertDialog
                        isAlertDialogForSparePartVisible.value = false
                    },
                    title = { Text("Add New Spare Part") },
                    text = {
                        Column {

                            val (selectedOption, onOptionSelected) = remember {
                                mutableStateOf(
                                    categoryRepo.getCategories().toList().get(0).description
                                )
                            }

                            Column(modifier = Modifier.selectableGroup()) {
                                categories.forEach {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .selectable(
                                                selected = (it.description == selectedOption),
                                                onClick = { onOptionSelected(it.description) },
                                                role = Role.RadioButton
                                            )
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = (it.description == selectedOption),
                                            onClick = null // null recommended for accessibility with screenreaders
                                        )
                                        Text(
                                            text = it.description,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }

                                }
                            }

                            OutlinedTextField(
                                value = newSparePartName.value,
                                onValueChange = {
                                    newSparePartName.value = it
                                },
                                label = { Text("Spare Part Name") }
                            )
                            OutlinedTextField(
                                value = newSparePartStock.value.toString(),
                                onValueChange = {
                                    newSparePartStock.value = it.toIntOrNull()
                                },
                                label = { Text("Spare Part Stock") }
                            )
                            OutlinedTextField(
                                value = newSparePartPrice.value.toString(),
                                onValueChange = {
                                    newSparePartPrice.value = it.toIntOrNull()
                                },
                                label = { Text("Spare Part Price") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newSparePartName.value.isNotEmpty() && newSparePartStock.value != null && newSparePartPrice.value != null) {
                                    val result = sparePartRepo.insertSparePart(
                                        SparePart(
                                            categoryId = selectedCategory.value.categoryId,
                                            name = newSparePartName.value,
                                            stock = newSparePartStock.value!!.toInt(),
                                            price = newSparePartPrice.value!!.toInt()
                                        )
                                    )
                                    if (result != null) {
                                        if (result >= 0L) {
                                            Toast.makeText(
                                                context,
                                                "The spare part could be added!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        Toast.makeText(
                                            context,
                                            "The spare part could not be added!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Something went wrong",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Text fields can not be empty'",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                isAlertDialogForSparePartVisible.value = false
                            }
                        ) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                // Dismiss the AlertDialog
                                isAlertDialogForSparePartVisible.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (selectedCategory.value.description != "Add New Category" && selectedCategory.value.description == "All Categories") {
                ListSpareParts(spareParts = sparePartRepo.getSparePartsByCategory(Category(-1, "All Categories")))
            } else{
                ListSpareParts(spareParts = sparePartRepo.getSparePartsByCategory(selectedCategory.value))
            }
        }
    }
}

@Composable
fun ShowSparePart(sparePart: SparePart) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    )
    {
        Text(text = sparePart.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.7F)
                .background(color = Color.DarkGray)
                .height(1.dp)
        )
        Text(text = "Stock : ${sparePart.stock}", style = MaterialTheme.typography.bodyMedium)
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.7F)
                .background(color = Color.DarkGray)
                .height(1.dp)
        )
        Text(text = "Price : ${sparePart.price}", style = MaterialTheme.typography.bodyMedium)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
                .height(2.dp)
        )
    }
}

@Composable
fun ListSpareParts(spareParts: MutableList<SparePart>) {
    LazyColumn(
        modifier = Modifier.border(3.dp, Color.Blue),
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.Top,
        userScrollEnabled = true
    )
    {
        this.items(spareParts) {
            ShowSparePart(sparePart = it)
        }
    }
}