package com.example.shopaine.ui.futures

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopaine.R
import com.example.shopaine.ui.theme.BackgroundMain
import com.example.shopaine.ui.theme.Blue
import com.example.shopaine.ui.theme.MyAppTheme
import com.example.shopaine.util.MyScreens
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController


@Preview(showBackground = true)
@Composable
fun IntroSceenPreview() {
    MyAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            IntroSceen()

        }
    }
}


@Composable
fun IntroSceen() {
    val UiController = rememberSystemUiController()
    SideEffect { UiController.setSystemBarsColor(Blue) }


val navigation = getNavController()


    Image(

        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.img_intro),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                navigation.navigate(MyScreens.SingUpScreen.route)
            },

            ) {

            Text(
                text = "Sing Up",
            )

        }

        Button(
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                navigation.navigate(MyScreens.SingInScreen.route)
            },

            ) {

            Text(
                text = "Sing In",
                color = Blue
            )

        }


    }

}