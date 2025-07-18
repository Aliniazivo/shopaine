package com.example.shopaine.ui.futures.singIn

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shopaine.R
import com.example.shopaine.ui.theme.BackgroundMain
import com.example.shopaine.ui.theme.Blue
import com.example.shopaine.ui.theme.MyAppTheme
import com.example.shopaine.ui.theme.Shapes
import com.example.shopaine.util.MyScreens
import com.example.shopaine.util.NetworkChecker
import com.example.shopaine.util.VALUE_SUCCESS
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Preview(showBackground = true)
@Composable
fun SingInScreenPreview() {
    MyAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            SingInScreen()
        }
    }
}

@Composable
fun SingInScreen() {
    val UiController = rememberSystemUiController()
    SideEffect { UiController.setSystemBarsColor(Blue) }

    val context = LocalContext.current

    val navigation = getNavController()
    val viewModel = getNavViewModel<SingInViewModel>()

    clearInputs(viewModel)


    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconApp()

            MainCard(navigation, viewModel) {
                viewModel.SingInUser(){
                    if(it == VALUE_SUCCESS){

                        navigation.navigate(MyScreens.MainScreen.route){
                            popUpTo(MyScreens.IntroScreen.route){
                                inclusive = true
                            }
                        }

                    }else{
                        Toast.makeText(context , it , Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }


    }

}

@Composable
fun IconApp() {

    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .size(64.dp)
    ) {

        Image(
            modifier = Modifier.padding(14.dp),
            painter = painterResource(R.drawable.ic_icon_app),
            contentDescription = null
        )
    }

}

@Composable
fun MainCard(navigation: NavController, viewModel: SingInViewModel, SingInEvent: () -> Unit) {

    val email = viewModel.email.observeAsState("")
    val Password = viewModel.password.observeAsState("")

    val context = LocalContext.current



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = Shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(8.dp)

    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                text = "Sing In",
                style = TextStyle(
                    color = Blue,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            MainTextField(
                edtValue = email.value,
                R.drawable.ic_email,
                "email"
            ) { viewModel.email.value = it }
            PrivateTextField(
                edtValue = Password.value,
                R.drawable.ic_password,
                "password"
            ) { viewModel.password.value = it }


            Button(
                onClick = {
                    if (email.value.isNotEmpty() && Password.value.isNotEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {

                            if (NetworkChecker(context).isInternetConnected){

                                SingInEvent.invoke()

                            }else{
                                Toast.makeText(context , "Please check your Internet" , Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            Toast.makeText(context, "your email's format is not true", Toast.LENGTH_SHORT).show()
                        }

                    } else { Toast.makeText(context, "Please write data first!", Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Register Account"
                )
            }

            Row(
                modifier = Modifier.padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don'n have an account?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    navigation.navigate(MyScreens.SingUpScreen.route) {
                        popUpTo(MyScreens.SingInScreen.route, { inclusive = true })
                    }
                }) {
                    Text(
                        text = "Register Here",
                        color = Blue
                    )
                }
            }


        }
    }

}


@Composable
fun MainTextField(
    edtValue: String,
    icon: Int,
    hint: String,
    onValueChanges: (String) -> Unit
) {


    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(text = hint) },
        singleLine = true,
        placeholder = { Text(text = hint) },
        value = edtValue,
        onValueChange = onValueChanges,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) }
    )


}


@Composable
fun PrivateTextField(
    edtValue: String,
    icon: Int,
    hint: String,
    onValueChanges: (String) -> Unit
) {

    val passwordvisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        label = { Text(text = hint) },
        singleLine = true,
        placeholder = { Text(text = hint) },
        value = edtValue,
        onValueChange = onValueChanges,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) },
        visualTransformation = if (passwordvisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordvisible.value) painterResource(R.drawable.ic_invisible)
            else painterResource(R.drawable.ic_visible)

            Icon(
                painter = image,
                contentDescription = null,
                modifier = Modifier.clickable {
                    passwordvisible.value = !passwordvisible.value
                }
            )
        }
    )


}



fun clearInputs(viewModel : SingInViewModel){
    viewModel.email.value = ""
    viewModel.password.value = ""
}