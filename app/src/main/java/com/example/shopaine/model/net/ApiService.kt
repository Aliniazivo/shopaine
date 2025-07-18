package com.example.shopaine.model.net

import com.example.shopaine.model.data.AddNewCommentResponse
import com.example.shopaine.model.data.AdsResponse
import com.example.shopaine.model.data.CartResponse
import com.example.shopaine.model.data.CheckOut
import com.example.shopaine.model.data.CommentResponse
import com.example.shopaine.model.data.LoginResponse
import com.example.shopaine.model.data.ProductResponse
import com.example.shopaine.model.data.SubmitOrder
import com.example.shopaine.model.data.UserCartInfo
import com.example.shopaine.model.repository.TokenInMemory
import com.example.shopaine.util.BASE_URL
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("singUp")
    suspend fun singUp(@Body jsonObject: JsonObject): LoginResponse

    @POST("singIn")
    suspend fun singIn(@Body jsonObject: JsonObject): LoginResponse

    @GET("refreshToken")
    fun refreshToken(): Call<LoginResponse>


    @GET("getProducts")
    suspend fun getAllProducts(): ProductResponse

    @GET("getSliderPics")
    suspend fun getAllAds(): AdsResponse

    @POST("getComments")
    suspend fun getAllComments(@Body jsonObject: JsonObject): CommentResponse

    @POST("addNewComment")
    suspend fun addNewComment(@Body jsonObject: JsonObject): AddNewCommentResponse

    @POST("addToCart")
    suspend fun addProductToCart(@Body jsonObject: JsonObject): CartResponse

    @GET("getUserCart")
    suspend fun getUserCart(): UserCartInfo

    @POST("removeFromCart")
    suspend fun removeFromCart(@Body jsonObject: JsonObject): CartResponse

    @POST("submitOrder")
    suspend fun submitOrder(@Body jsonObject: JsonObject): SubmitOrder

    @POST("checkout")
    suspend fun checkOut(@Body jsonObject: JsonObject): CheckOut

}


fun createApiService(): ApiService {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {

            val oldRequest = it.request()

            val newRequest = oldRequest.newBuilder()
            if (TokenInMemory.token != null) {
                newRequest.addHeader("Authorizitaon", TokenInMemory.token!!)
                newRequest.addHeader("Accept", "application/json")
                newRequest.method(oldRequest.method, oldRequest.body)
            }

            return@addInterceptor it.proceed(newRequest.build())

        }.build()


    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)

}