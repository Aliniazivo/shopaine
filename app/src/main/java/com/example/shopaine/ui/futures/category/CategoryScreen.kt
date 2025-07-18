package com.example.shopaine.ui.futures.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shopaine.model.data.Product
import com.example.shopaine.ui.theme.Blue
import com.example.shopaine.ui.theme.Shapes
import com.example.shopaine.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Composable
fun CategoryScreen(categoryName: String) {
    val viewModel = getNavViewModel<CategoryViewModel>()
    viewModel.loadDataByCatgory(categoryName)

    val navigation = getNavController()


    Column(modifier = Modifier.fillMaxSize()) {

        CategoryToolbar(categoryName)

        val data = viewModel.dataProduct
        CategoryList(data.value) {
            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }


    }

}


@Composable
fun CategoryItem( data : Product , onProductClicked: (String) -> Unit ){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, start = 16.dp, top = 16.dp)
            .clickable { onProductClicked.invoke(data.productId)},
        shape = Shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ){

        Column {


            AsyncImage(
                model = data.imgUrl,
                contentScale = ContentScale.Crop ,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){

                Column( modifier = Modifier.padding(10.dp)){
                    Text(
                        text = data.name,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )


                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = data.price + "Tomans",
                        style = TextStyle(fontSize = 14.sp,)
                    )

                }

                Surface(
                    color = Blue,
                    modifier = Modifier
                        .padding(bottom = 8.dp , end = 8.dp)
                        .align(Alignment.Bottom)
                        .clip(Shapes.large)
                ){
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = data.soldItem + "sold",
                        style = TextStyle(fontSize = 13.sp , fontWeight = FontWeight.Medium , color = Color.White)

                    )
                }

            }



        }

    }

}

@Composable
fun CategoryList(data: List<Product>, onProductClicked: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 15.dp)
    ){
        items(data.size){
            CategoryItem(data[it] , onProductClicked)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryToolbar(categoryName: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        title = { Text(
            text = categoryName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center

        ) },
    )
}