package com.example.shopaine.model.repository.cart

import com.example.shopaine.model.data.CartResponse
import com.example.shopaine.model.data.CheckOut
import com.example.shopaine.model.data.SubmitOrder
import com.example.shopaine.model.data.UserCartInfo


interface CartRepository {

    suspend fun addToCart(productId: String ): Boolean
    suspend fun getCartSize(): Int
    suspend fun removeFromCart(productId: String) : Boolean
    suspend fun getUserCartInfo() : UserCartInfo

    suspend fun submitOrder(address :String , postalCode: String) : SubmitOrder
    suspend fun checkOut(orderId : String) : CheckOut

     fun setOrderId(orderId: String)
     fun getOrderId() : String

     fun setPurchaseStatus(status : Int)
     fun getPurchaseStatus(): Int
}