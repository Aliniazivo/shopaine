package com.example.shopaine.util

sealed class MyScreens(val route :String){
    object MainScreen : MyScreens("mainScreen")
    object ProductScreen : MyScreens("ProductScreen")
    object CategoryScreen : MyScreens("CategoryScreen")
    object ProfileScreen : MyScreens("ProfileScreen")
    object CartScreen : MyScreens("CartScreen")
    object SingUpScreen : MyScreens("SingUpScreen")
    object SingInScreen : MyScreens("SingInScreen")
    object IntroScreen : MyScreens("IntroScreen")
    object NoInternetScreen : MyScreens("NoInternetScreen")


}