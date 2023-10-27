package com.candroid.sqlitehomework.model

data class SparePart(
    var sparePartId: Int = 0,
    var categoryId: Int = 0,
    var name: String = "",
    var stock: Int = 0,
    var price: Int = 0
)