package com.dev.customerapp.models

 data class TopCategoryModel(
     val topCategoryId: Int,
     val topCategoryName: String,
     val topCategoryImage: String,
     //child 1 Category list available for only some api
     val child1CategoryList: List<Child1CategoryModel> = emptyList(),
     // for nested Scroll

     var isExpanded: Boolean = false
)