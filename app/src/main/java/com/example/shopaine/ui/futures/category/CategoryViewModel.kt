package com.example.shopaine.ui.futures.category

import android.R
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopaine.model.data.Ads
import com.example.shopaine.model.data.Product
import com.example.shopaine.model.repository.product.ProductRepository
import com.example.shopaine.model.repository.user.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val productRepository: ProductRepository,
) : ViewModel() {
    val dataProduct = mutableStateOf<List<Product>>(listOf())


    fun loadDataByCatgory(category: String) {
        viewModelScope.launch {

            val dataFromLocal = productRepository.getAllProductsByCategory(category)
            dataProduct.value = dataFromLocal

        }


    }


}