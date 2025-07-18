package com.example.shopaine.model.data

data class LoginResponse(
    val success : Boolean,
    val token : String,
    val expriesAt : Int,
    val message : String
)
