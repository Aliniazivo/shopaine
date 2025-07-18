package com.example.shopaine.ui.futures.singIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopaine.model.repository.user.UserRepository
import com.example.shopaine.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SingInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")


    fun SingInUser(LoggingEvent : (String)-> Unit){

        viewModelScope.launch(coroutineExceptionHandler){
           val result = userRepository.singIn(email.value!! , password.value!!)
            LoggingEvent(result)
        }



    }

}