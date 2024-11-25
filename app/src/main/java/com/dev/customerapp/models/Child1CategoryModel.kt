package com.dev.customerapp.models


data class Child1CategoryModel(
    val child1CategoryId: Int,
    val child1CategoryName: String,
    val topCategoryId: Int,
    //child 2 Category list available for only some api
    val child2CategoryList: List<Child2CategoryModel>,
    var isExpanded: Boolean = false
)