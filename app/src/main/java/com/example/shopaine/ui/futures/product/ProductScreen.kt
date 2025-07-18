package com.example.shopaine.ui.futures.product

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.shopaine.R
import com.example.shopaine.model.data.Comment
import com.example.shopaine.model.data.Product
import com.example.shopaine.ui.theme.BackgroundMain
import com.example.shopaine.ui.theme.Blue
import com.example.shopaine.ui.theme.MyAppTheme
import com.example.shopaine.ui.theme.PriceBackground
import com.example.shopaine.ui.theme.Shapes
import com.example.shopaine.util.MyScreens
import com.example.shopaine.util.NetworkChecker
import com.example.shopaine.util.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun ProductScreensPreview() {

    MyAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            ProductScreen("")
        }
    }

}


@Composable
fun ProductScreen(Productid: String) {

    val context = LocalContext.current

    val viewModel = getNavViewModel<ProductViewModel>()
    viewModel.loadData(Productid, NetworkChecker(context).isInternetConnected)

    val navigation = getNavController()


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 58.dp)
        ) {

            ProductToolbar(
                productName = "Details",
                badgeNumber = viewModel.badgeNumber.value,
                onBackClicked = { navigation.popBackStack() },
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.route)
                    } else {
                        Toast.makeText(
                            context,
                            "please connect to internet first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            val comments = if (NetworkChecker(context).isInternetConnected) viewModel.comments.value else listOf()
            ProductItem(
                data = viewModel.thisProduct.value,
                comments = comments,
                onCategoryClicked = {
                    navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
                },
                onAddNewComment = {
                    viewModel.addNewComment(Productid, it) { payam ->
                        Toast.makeText(context, payam, Toast.LENGTH_SHORT).show()
                    }
                }
            )


        }


        AddToCard(
            viewModel.thisProduct.value.price,
            viewModel.isAddingProduct.value,
        ) {
            if (NetworkChecker(context).isInternetConnected) {
                viewModel.addProductToCart(Productid) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "please connect to internet...", Toast.LENGTH_SHORT).show()
            }

        }

    }


}


@Composable
fun ProductItem(
    comments: List<Comment>,
    data: Product,
    onCategoryClicked: (String) -> Unit,
    onAddNewComment: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        ProductDesing(data, onCategoryClicked)

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp)
        )

        ProductDetail(data, comments.size.toString())

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
        )

        ProductComments(comments, onAddNewComment)

    }


}


@Composable
fun CommentBody(comment: Comment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = Shapes.large
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = comment.userEmail,
                style = TextStyle(fontSize = 15.sp),
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = comment.text,
                style = TextStyle(fontSize = 13.sp),
            )

        }
    }

}


@Composable
fun ProductComments(comments: List<Comment>, AddNewComment: (String) -> Unit) {

    val showCommentDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (comments.isNotEmpty()) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                text = "Comments",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            TextButton(onClick = {
                if (NetworkChecker(context).isInternetConnected) {
                    showCommentDialog.value = true
                } else {
                    Toast.makeText(
                        context,
                        "connect to internet to add comment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text(
                    color = Color.Blue,
                    text = "Add New Comment",
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }


        }


        comments.forEach {
            CommentBody(it)
        }


    } else {

        TextButton(onClick = {
            if (NetworkChecker(context).isInternetConnected) {
                showCommentDialog.value = true
            } else {
                Toast.makeText(context, "connect to internet to add comment", Toast.LENGTH_SHORT)
                    .show()
            }

        }) {
            Text(
                text = "Add New Comment",
                style = TextStyle(fontSize = 13.sp)
            )

        }

    }

    if (showCommentDialog.value) {

        AddNewCommentDialog(
            onDismiss = { showCommentDialog.value = false },
            onPositiveClicked = { AddNewComment.invoke(it) }
        )

    }


}

@Composable
fun AddNewCommentDialog(
    onDismiss: () -> Unit,
    onPositiveClicked: (String) -> Unit
) {
    val context = LocalContext.current
    val userComment = remember { mutableStateOf("") }

    Dialog(onDismiss) {
        Card(
            modifier = Modifier.fillMaxHeight(0.53f),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = Shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {


                Text(
                    text = "Write Your Comment",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)

                )

                Spacer(modifier = Modifier.height(8.dp))

                //  enter data =>
                MainTextField2(
                    edtValue = userComment.value,
                    hint = "write something...",
                ) { userComment.value = it }


                // Buttons =>
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = { onDismiss.invoke() }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        onClick = {
                            if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {

                                if (NetworkChecker(context).isInternetConnected) {
                                    onPositiveClicked.invoke(userComment.value)
                                    onDismiss.invoke()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please Connect To Internet First!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            } else {
                                Toast.makeText(context, "Please Write First!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    ) { Text("Ok") }

                }

            }
        }
    }

}

@Composable
fun ProductDetail(data: Product, commentNumber: String) {


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_comment),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = commentNumber + "Comments",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp

                )

            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_material),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.material,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp

                )

            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.soldItem + "sold",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp

                )

            }


        }

        Surface(
            modifier = Modifier
                .clip(Shapes.large)
                .align(Alignment.Bottom),
            color = Blue
        ) {
            Text(
                text = data.tags,
                color = Color.White,
                modifier = Modifier.padding(6.dp),
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
            )
        }

    }


}


@Composable
fun ProductDesing(data: Product, onCategoryClicked: (String) -> Unit) {

    AsyncImage(
        model = data.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(Shapes.medium)
    )

    Text(
        modifier = Modifier.padding(top = 14.dp),
        text = data.name,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    )

    Text(
        modifier = Modifier.padding(top = 4.dp),
        text = data.detailText,
        style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Justify)
    )

    TextButton(onClick = { onCategoryClicked.invoke(data.category) }) {
        Text(
            text = data.category,
            style = TextStyle(fontSize = 13.sp)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductToolbar(
    productName: String,
    badgeNumber: Int,
    onBackClicked: () -> Unit,
    onCartClicked: () -> Unit
) {

    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
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
                text = productName,
                textAlign = TextAlign.Center
            )
        },
        actions = {

            IconButton(
                modifier = Modifier.padding(end = 6.dp),
                onClick = { onCartClicked.invoke() }
            ) {

                if (badgeNumber == 0) {
                    Icon(Icons.Default.ShoppingCart, null)
                } else {

                    BadgedBox(badge = { Badge { Text(badgeNumber.toString()) } }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }
            }
        }
    )

}

@Composable
fun AddToCard(
    price: String,
    isAddingProduct: Boolean,
    onCartClicked: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.9f

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Button(
                onClick = { onCartClicked.invoke() },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp)
            ) {

                if (isAddingProduct) {
                    DotsTyping()
                } else {
                    Text(
                        text = "Add Product To Cart ",
                        modifier = Modifier.padding(2.dp),
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)

                    )
                }

            }

            Surface(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(Shapes.large),
                color = PriceBackground,
            ) {

                Text(
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp,
                        end = 8.dp,
                        start = 8.dp
                    ),
                    text = stylePrice(price),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

            }

        }
    }

}


@Composable
fun MainTextField2(
    edtValue: String,
    hint: String,
    onValueChanges: (String) -> Unit
) {
    OutlinedTextField(
        label = { Text(text = hint) },
        singleLine = false,
        placeholder = { Text(text = hint) },
        value = edtValue,
        onValueChange = onValueChanges,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
    )


}



// Animation
@Composable
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}
