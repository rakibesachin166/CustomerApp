package com.dev.customerapp.models

data class ProductModel(
    val productId: Int ,
    val productName: String,
    val productTopCategoryId: Int,
    val productChild1CategoryId: Int,
    val productChild2CategoryId: Int,
    val productCodeSku: String,
    val productMrp: Int,
    val productSalePrice: Int,
    val productDiscount: Int,
    val productAvailableQuantity: Int,
    val productDescription: String,
    val createdBy: Int,
    val productCreateRoleLoginType:Int,
    val createdAt: String
)
