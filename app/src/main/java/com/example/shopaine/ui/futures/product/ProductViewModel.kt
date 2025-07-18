package com.example.shopaine.ui.futures.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopaine.model.data.Comment
import com.example.shopaine.model.repository.cart.CartRepository
import com.example.shopaine.model.repository.comment.CommentRepository
import com.example.shopaine.model.repository.product.ProductRepository
import com.example.shopaine.util.EPMTY_PRODUCT
import com.example.shopaine.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val thisProduct = mutableStateOf(EPMTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val isAddingProduct = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)


    fun loadData(productId: String, isInternetConnected: Boolean) {



        loadProductFromCache(productId)

        if (isInternetConnected) {
            loadAllComments(productId)
            loadBadgeNumber()

        }

    }


    fun loadProductFromCache(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)
        }

    }


    private fun loadBadgeNumber(){
        viewModelScope.launch(coroutineExceptionHandler){
            badgeNumber.value = cartRepository.getCartSize()
        }
    }


    private fun loadAllComments(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            comments.value = commentRepository.getAllComments(productId)

        }
    }


    fun addNewComment(productId: String, text: String, isSuccess: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productId, text, isSuccess)
            delay(200)
            comments.value = commentRepository.getAllComments(productId)

        }

    }


    fun addProductToCart(productId: String , AddingToCartResult:(String)-> Unit ){
        viewModelScope.launch(coroutineExceptionHandler) {
            isAddingProduct.value = true
            val result = cartRepository.addToCart(productId)
            delay(500)
            isAddingProduct.value = false

            if (result){
                AddingToCartResult.invoke("product added to cart")
            }else{
                AddingToCartResult.invoke("product not added")
            }

        }
    }


}