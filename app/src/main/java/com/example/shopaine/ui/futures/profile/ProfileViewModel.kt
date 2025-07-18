package com.example.shopaine.ui.futures.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shopaine.model.repository.user.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val email = mutableStateOf("")
    val postalCode = mutableStateOf("")
    val address = mutableStateOf("")
    val loginTime = mutableStateOf("")

    val showLocationDialog = mutableStateOf(false)

    fun loadUserData(){

        email.value = userRepository.getUserName()!!
        loginTime.value = userRepository.getUserLoginTime()
        address.value = userRepository.getUserLocation().first
        postalCode.value = userRepository.getUserLocation().second

    }

    fun singOut(){
        userRepository.singOut()
    }

    fun setUserLocation( address :String , postalCode : String ){
        userRepository.saveUserLocation(address , postalCode)
    }


}