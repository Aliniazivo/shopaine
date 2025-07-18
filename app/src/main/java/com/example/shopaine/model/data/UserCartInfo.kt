package com.example.shopaine.model.data

data class UserCartInfo(
    val success : Boolean,
    val productList : List<Product>,
    val totalPrice : Int,
    val message : String
)
