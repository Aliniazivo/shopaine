package com.example.shopaine.model.repository.user

interface UserRepository {

    // online
    suspend fun singUp(name :String ,username : String , password : String ) : String
    suspend fun singIn(username: String , password: String): String

    //offline
    fun singOut()
    fun loadToken()

    fun saveToken(newToken : String)
    fun getToken() : String?

    fun saveUserName(username: String)
    fun getUserName(): String?

    fun saveUserLocation(address : String , postalCode : String)
    fun getUserLocation() : Pair<String , String>

    fun saveUserLoginTime()
    fun getUserLoginTime(): String
}