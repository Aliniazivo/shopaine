package com.example.shopaine.ui.futures.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.shopaine.R
import com.example.shopaine.model.data.Ads
import com.example.shopaine.model.data.CheckOut
import com.example.shopaine.model.data.Product
import com.example.shopaine.model.repository.cart.CartRepository
import com.example.shopaine.model.repository.product.ProductRepository
import com.example.shopaine.ui.theme.BackgroundMain
import com.example.shopaine.ui.theme.Blue
import com.example.shopaine.ui.theme.CardViewBackground
import com.example.shopaine.ui.theme.MyAppTheme
import com.example.shopaine.ui.theme.Shapes
import com.example.shopaine.util.CATEGORY
import com.example.shopaine.util.MyScreens
import com.example.shopaine.util.NO_PAYMENT
import com.example.shopaine.util.NetworkChecker
import com.example.shopaine.util.PAYMENT_PENDING
import com.example.shopaine.util.PAYMENT_SUCCESS
import com.example.shopaine.util.TAGS
import com.example.shopaine.util.stylePrice
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun MyScreensPreview() {

    MyAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            MainScreen()
        }
    }

}


@Composable
fun MainScreen() {


    val context = LocalContext.current
    val viewModel =
        getNavViewModel<MainViewModel>(parameters = { parametersOf(NetworkChecker(context).isInternetConnected) })

    val navigation = getNavController()

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setSystemBarsColor(Color.White) }

    if (NetworkChecker(context).isInternetConnected) {
        viewModel.loadBadgeNumber()
    }


    if (viewModel.getPaymentStatus() == PAYMENT_PENDING){
        if (NetworkChecker(context).isInternetConnected){
            viewModel.getCheckOutData()
        }

    }


    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            if (viewModel.showProgressBar.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Blue)
            }

            TopToolbar(
                badgeNumber = viewModel.badgeNumber.value,
                onCardClicked = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.route)
                    } else {
                        Toast.makeText(context, "please connect to internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                onProfileClicked = {
                    navigation.navigate(MyScreens.ProfileScreen.route)
                })

            CategoryBar(CATEGORY) {
                navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
            }

            val productDataState = viewModel.dataProduct
            val adsDataState = viewModel.dataAds
            ProductSubjectList(TAGS, productDataState.value, adsDataState.value) {
                navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
            }


        }

        if (viewModel.showPaymentResultDialog.value){

            PaymentResultDialog(
                checkoutResult = viewModel.checkoutData.value,
                onDismiss = {
                    viewModel.showPaymentResultDialog.value = false
                    viewModel.setPaymentStatus(NO_PAYMENT)
                }
            )


        }

    }

}


// -------------------------------------------------------------------------------------------



@Composable
private fun PaymentResultDialog(
    checkoutResult: CheckOut,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = Shapes.medium
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Payment Result",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Data
                if (checkoutResult.order?.status?.toInt() == PAYMENT_SUCCESS) {

                    AsyncImage(
                        model = R.drawable.success_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(110.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order.amount).substring(0,
                                (checkoutResult.order.amount).length - 1
                            )
                        )
                    )

                } else {

                    AsyncImage(
                        model = R.drawable.fail_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was not successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        )
                    )

                }

                // Ok Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "ok")
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}





// -------------------------------------------------------------------------------------------


@Composable
fun ProductSubjectList(
    tags: List<String>,
    products: List<Product>,
    ads: List<Ads>,
    onProductClicked: (String) -> Unit
) {


    if (products.isNotEmpty()) {


        Column {


            tags.forEachIndexed { it, _ ->

                val withTagData = products.filter { product -> product.tags == tags[it] }
                ProductSubject(tags[it], withTagData.shuffled(), onProductClicked)

                if (ads.size >= 2)
                    if (it == 1 || it == 2)
                        BigPictureTablighat(ads[it - 1], onProductClicked)

            }


        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    badgeNumber: Int,
    onCardClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {

    TopAppBar(
        //elevation
        //background
        title = {
            Text(
                "Shopaine",
                style = TextStyle(fontStyle = FontStyle.Italic),
                textAlign = TextAlign.Center
            )
        },
        actions = {

            IconButton(
                onClick = { onCardClicked.invoke() }
            ) {

                if (badgeNumber == 0) {
                    Icon(Icons.Default.ShoppingCart, null)
                } else {

                    BadgedBox(badge = { Badge { Text(badgeNumber.toString()) } }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }
            }


            IconButton(onClick = { onProfileClicked.invoke() }) {
                Icon(Icons.Default.Person, null)
            }


        }
    )

}


// -------------------------------------------------------------------------------------------
@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(categoryList.size) {
            CategoryItems(categoryList[it], onCategoryClicked)
        }

    }

}

@Composable
fun CategoryItems(subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onCategoryClicked.invoke(subject.first) },
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {

        Surface(
            shape = Shapes.medium,
            color = CardViewBackground
        ) {
            Image(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(subject.second),
                contentDescription = null
            )
        }

        Text(
            text = subject.first,
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(color = Color.Gray)

        )

    }

}


// -------------------------------------------------------------------------------------------
@Composable
fun ProductSubject(subject: String, data: List<Product>, onProductClicked: (String) -> Unit) {

    Column(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Text(
            text = subject,
            modifier = Modifier.padding(start = 16.dp),
        )

        ProductBar(data, onProductClicked)

    }

}

@Composable
fun ProductBar(data: List<Product>, onProductClicked: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(data.size) {
            ProductItem(data[it], onProductClicked)
        }

    }
}

@Composable
fun ProductItem(product: Product, onProductClicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onProductClicked.invoke(product.productId) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = Shapes.medium

    ) {
        Column {
            AsyncImage(
                model = product.imgUrl,
                modifier = Modifier.size(200.dp),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )


            Column(
                modifier = Modifier.padding(10.dp),
            ) {
                Text(
                    text = product.name,
                    style = TextStyle(fontSize = 15.sp),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stylePrice(product.price),
                    style = TextStyle(fontSize = 14.sp),
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = product.soldItem + " sold",
                    style = TextStyle(fontSize = 13.sp, color = Color.Gray),
                )


            }


        }

    }
}

// -------------------------------------------------------------------------------------------
@Composable
fun BigPictureTablighat(ads: Ads, onProductClicked: (String) -> Unit) {

    AsyncImage(
        model = ads.imageURL,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 30.dp, start = 16.dp, end = 16.dp)
            .clip(Shapes.medium)
            .clickable { onProductClicked.invoke(ads.productId) },
        contentDescription = null,
        contentScale = ContentScale.Crop
    )


}
// -------------------------------------------------------------------------------------------