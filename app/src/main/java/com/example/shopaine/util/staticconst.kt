package com.example.shopaine.util

import com.example.shopaine.R
import com.example.shopaine.model.data.Product

const val KEY_PRODUCT_ARG = "productId"
const val KEY_CATEGORY_ARG = "categoryName"
const val BASE_URL = "https://dunijet.ir/Projects/DuniBazaar/"
const val VALUE_SUCCESS = "success"
val EPMTY_PRODUCT = Product("","","","","","","","","" , "")

val CATEGORY = listOf(
    Pair("Backpack", R.drawable.ic_cat_backpack),
    Pair("Handbag", R.drawable.ic_cat_handbag),
    Pair("Shopping", R.drawable.ic_cat_shopping),
    Pair("Tote", R.drawable.ic_cat_tote),
    Pair("Satchel", R.drawable.ic_cat_satchel),
    Pair("Clutch", R.drawable.ic_cat_clutch),
    Pair("Wallet", R.drawable.ic_cat_wallet),
    Pair("Sling", R.drawable.ic_cat_sling),
    Pair("Bucket", R.drawable.ic_cat_bucket),
    Pair("Quilted", R.drawable.ic_cat_quilted)
)

val TAGS = listOf(
    "Best Sellers",
    "Newset",
    "Most Visited",
    "Highest Quality"
)

const val PAYMENT_SUCCESS = 1
const val PAYMENT_PENDING = 0
const val PAYMENT_FAIL = -1
const val NO_PAYMENT = -2