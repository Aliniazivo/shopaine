package com.example.shopaine.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shopaine.di.myModules
import com.example.shopaine.model.repository.TokenInMemory
import com.example.shopaine.model.repository.user.UserRepository
import com.example.shopaine.ui.futures.IntroSceen
import com.example.shopaine.ui.futures.cart.CartScreen
import com.example.shopaine.ui.futures.category.CategoryScreen
import com.example.shopaine.ui.futures.main.MainScreen
import com.example.shopaine.ui.futures.product.ProductScreen
import com.example.shopaine.ui.futures.profile.ProfileScreen
import com.example.shopaine.ui.futures.singIn.SingInScreen
import com.example.shopaine.ui.futures.singUp.SingUpScreen
import com.example.shopaine.ui.theme.BackgroundMain
import com.example.shopaine.ui.theme.MyAppTheme
import com.example.shopaine.util.KEY_CATEGORY_ARG
import com.example.shopaine.util.KEY_PRODUCT_ARG
import com.example.shopaine.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {

                MyAppTheme {
                    Surface(
                        color = BackgroundMain,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val repository: UserRepository = get()
                        repository.loadToken()

                        DuniBazaarUi()

                    }


                }
            }


        }
    }
}


@Composable
fun DuniBazaarUi() {
    val navController = rememberNavController()
    KoinNavHost(
        navController = navController,
        startDestination = MyScreens.IntroScreen.route,
    ) {

        composable(MyScreens.MainScreen.route) {

            if (TokenInMemory.token != null) {
                MainScreen()
            } else {
                IntroSceen()
            }

        }



        composable(
            route = MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) { type = NavType.StringType })
        ) {
            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, null))
        }



        composable(
            route = MyScreens.CategoryScreen.route + "{$KEY_CATEGORY_ARG}" ,
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) { type = NavType.StringType })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, null))
        }

        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }

        composable(MyScreens.SingUpScreen.route) {
            SingUpScreen()
        }

        composable(MyScreens.SingInScreen.route) {
            SingInScreen()
        }

        composable(MyScreens.IntroScreen.route) {
            IntroSceen()
        }

    }
}








@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            DuniBazaarUi()

        }
    }
}