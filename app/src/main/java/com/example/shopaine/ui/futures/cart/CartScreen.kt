package com.example.shopaine.ui.futures.cart

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.shopaine.R
import com.example.shopaine.model.data.Product
import com.example.shopaine.ui.futures.profile.AddUserLocationDataDialog
import com.example.shopaine.ui.theme.BackgroundMain
import com.example.shopaine.ui.theme.Blue
import com.example.shopaine.ui.theme.MyAppTheme
import com.example.shopaine.ui.theme.PriceBackground
import com.example.shopaine.ui.theme.Shapes
import com.example.shopaine.util.MyScreens
import com.example.shopaine.util.NetworkChecker
import com.example.shopaine.util.PAYMENT_PENDING
import com.example.shopaine.util.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import androidx.core.net.toUri


@Preview(showBackground = true)
@Composable
fun SingUpScreenPreview() {
    MyAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            CartScreen()
        }
    }
}


@Composable
fun CartScreen() {

    val getDataDialogState = remember{ mutableStateOf(false) }
    val context = LocalContext.current
    val navigation = getNavController()

    val viewModel = getNavViewModel<CartViewModel>()
    viewModel.loadCartData()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 74.dp)
        ){

            CartToolbar(
                onBackClicked = { navigation.popBackStack() },
                onProfileClicked = { navigation.navigate(MyScreens.ProfileScreen.route) }
            )

            if (viewModel.productList.value.isNotEmpty()){
                CartList(
                    data = viewModel.productList.value,
                    isChangingNumber = viewModel.isChangingNumber.value,
                    onAddItemClicked = { viewModel.addItem(it) },
                    onRemoveItemClicked = { viewModel.removeItem(it) },
                    onItemClicked = { navigation.navigate(MyScreens.ProductScreen.route + "/" + it) }
                )

            }else{
                NoDataAnimation()
            }
        }

        PurchaseAll(viewModel.totalPrice.value.toString()){

            if (viewModel.productList.value.isNotEmpty()){
                val locationData = viewModel.getUserLocation()
                if (locationData.first == "click to add" || locationData.second == "click to add" ){
                    getDataDialogState.value = true
                }else{
                    // pardakht ->
                    viewModel.purchaseAll( locationData.first , locationData.second ){success , link ->
                        if (success){
                            Toast.makeText(context, "pay using zarinPal...", Toast.LENGTH_SHORT).show()

                            viewModel.setPaymentStatus(PAYMENT_PENDING)

                            val intent = Intent(Intent.ACTION_VIEW , link.toUri())
                            context.startActivity(intent)

                        }else{
                            Toast.makeText(context, "problem in payment", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }else{
                Toast.makeText(context, "please add some products first", Toast.LENGTH_SHORT).show()
            }


        }

        if (getDataDialogState.value){

            AddUserLocationDataDialog(
                showSaveLocation = true,
                onDismiss = { getDataDialogState.value = false },
                onSubmitClicked = { address , postalCode , isChecked ->
                    if (NetworkChecker(context).isInternetConnected){

                        if (isChecked){
                            viewModel.setUserLocation(address , postalCode)
                        }

                        // pardakht ->
                        viewModel.purchaseAll( address, postalCode ){success , link ->
                            if (success){
                                Toast.makeText(context, "pay using zarinPal...", Toast.LENGTH_SHORT).show()

                                viewModel.setPaymentStatus(PAYMENT_PENDING)

                                val intent = Intent(Intent.ACTION_VIEW , link.toUri())
                                context.startActivity(intent)

                            }else{
                                Toast.makeText(context, "problem in payment", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }else{
                        Toast.makeText(context, "please connect to internet first", Toast.LENGTH_SHORT).show()
                    }

                }

            )

        }


    }




}


@Composable
fun PurchaseAll(
    totalPrice : String,
    onPurchaseClicked:()-> Unit
){

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val fraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.7f


    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
    ){

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp),
                onClick = {
                    if (NetworkChecker(context).isInternetConnected){
                        onPurchaseClicked.invoke()
                    }else{
                        Toast.makeText(context, "please connect to internet", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "Let's Purchase",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp , fontWeight = FontWeight.Medium)
                )
            }

            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(Shapes.large),
                color = PriceBackground
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                    text = "total :" + stylePrice( totalPrice ),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }


        }


    }
}



@Composable
fun NoDataAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.no_data)
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}


@Composable
fun CartList(
    data: List<Product>,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(data.size) {
            CartItem(
                data = data[it],
                isChangingNumber = isChangingNumber,
                onItemClicked = onItemClicked,
                onAddItemClicked = onAddItemClicked,
                onRemoveItemClicked = onRemoveItemClicked
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartToolbar(
    onBackClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {

    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked.invoke() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        expandedHeight = 2.dp,
        colors = TopAppBarColors(Color.White, Color.White, Color.Blue, Color.Black, Color.Black),
        modifier = Modifier.fillMaxWidth(),
        title = {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                text = "My Cart",
                textAlign = TextAlign.Center
            )
        },
        actions = {

            IconButton(
                modifier = Modifier.padding(end = 6.dp),
                onClick = { onProfileClicked.invoke() }
            ) {
                Icon(Icons.Default.Person, null)
            }
        }
    )

}


@Composable
fun CartItem(
    data: Product,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, start = 16.dp, top = 16.dp)
            .clickable { onItemClicked.invoke(data.productId) },
        shape = Shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column {


            AsyncImage(
                model = data.imgUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = data.name,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )


                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "From" + data.category + "Group",
                        style = TextStyle(fontSize = 14.sp)
                    )

                    Text(
                        modifier = Modifier.padding(top = 17.dp),
                        text = "Product authenticity guarantee",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Available in stock to ship",
                        style = TextStyle(fontSize = 14.sp)
                    )

                    Surface(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 6.dp)
                            .clip(Shapes.large),
                        color = PriceBackground
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp,
                                bottom = 6.dp,
                                end = 8.dp,
                                start = 8.dp
                            ),
                            text = stylePrice(
                                (data.price.toInt() * (data.quantity ?: "1").toInt()).toString()
                            ),
                            style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        )
                    }

                }

                Surface(
                    modifier = Modifier
                        .padding(bottom = 14.dp, end = 8.dp)
                        .align(Alignment.Bottom)
                ) {
                    Card(
                        border = BorderStroke(2.dp, Blue)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            if (data.quantity?.toInt() == 1) {
                                IconButton(onClick = { onRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                IconButton(onClick = { onRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        painter = painterResource(R.drawable.ic_minus),
                                        contentDescription = null
                                    )
                                }
                            }


                            // size of products
                            if (isChangingNumber.first == data.productId && isChangingNumber.second) {
                                Text(
                                    text = "...",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            } else {
                                Text(
                                    text = data.quantity ?: "1",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }


                            // Add button
                            IconButton(onClick = { onAddItemClicked.invoke(data.productId) }) {
                                Icon(
                                    modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }


                        }

                    }
                }
            }
        }
    }
}
