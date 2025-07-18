package com.example.shopaine.ui.futures.singUp

import android.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopaine.model.repository.user.UserRepository
import com.example.shopaine.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SingUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    fun SingUpUser( LoggingEvent : (String)-> Unit ){


        viewModelScope.launch(coroutineExceptionHandler){
            val result = userRepository.singUp(name.value!! , email.value!! ,password.value!!)
            LoggingEvent(result)
        }


    }

}