package com.example.shopaine.ui.futures.main

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopaine.model.data.Ads
import com.example.shopaine.model.data.CheckOut
import com.example.shopaine.model.data.Product
import com.example.shopaine.model.repository.cart.CartRepository
import com.example.shopaine.model.repository.product.ProductRepository
import com.example.shopaine.util.NetworkChecker
import com.example.shopaine.util.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    isInternetConnected: Boolean
) : ViewModel() {
    val dataProduct = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgressBar = mutableStateOf(false)
    val badgeNumber = mutableIntStateOf(0)


    val showPaymentResultDialog = mutableStateOf(false)
    val checkoutData =  mutableStateOf(CheckOut(null,null))

    init {
        refreshAllDataFromNet(isInternetConnected)
    }


    fun getPaymentStatus() : Int {
        return cartRepository.getPurchaseStatus()
    }

    fun setPaymentStatus(status : Int){
        cartRepository.setPurchaseStatus(status)
    }


    fun getCheckOutData(){
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = cartRepository.checkOut(cartRepository.getOrderId())

            if (result.success!!){
                checkoutData.value = result
                showPaymentResultDialog.value = true
            }
        }


    }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (isInternetConnected)
                showProgressBar.value = true

            delay(1200)

            val newDataProducts = async { productRepository.getAllProducts(isInternetConnected) }
            val newDataAds = async { productRepository.getAllAds(isInternetConnected) }

            updateData(newDataProducts.await(), newDataAds.await())


            showProgressBar.value = false
        }


    }


    private fun updateData(products: List<Product>, ads: List<Ads>) {
        dataProduct.value = products
        dataAds.value = ads


    }


    fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.intValue = cartRepository.getCartSize()
        }
    }


}