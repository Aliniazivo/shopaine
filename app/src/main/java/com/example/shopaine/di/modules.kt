package com.example.shopaine.di


import android.content.Context
import androidx.room.Room
import com.example.shopaine.model.db.AppDatabase
import com.example.shopaine.model.net.createApiService
import com.example.shopaine.model.repository.cart.CartRepository
import com.example.shopaine.model.repository.cart.CartRepositoryImpl
import com.example.shopaine.model.repository.comment.CommentRepository
import com.example.shopaine.model.repository.comment.CommentRepositoryImpl
import com.example.shopaine.model.repository.product.ProductRepository
import com.example.shopaine.model.repository.product.ProductRepositoryImpl
import com.example.shopaine.model.repository.user.UserRepository
import com.example.shopaine.model.repository.user.UserRepositoryIMPL
import com.example.shopaine.ui.futures.category.CategoryViewModel
import com.example.shopaine.ui.futures.main.MainViewModel
import com.example.shopaine.ui.futures.product.ProductViewModel
import com.example.shopaine.ui.futures.profile.ProfileViewModel
import com.example.shopaine.ui.futures.singIn.SingInViewModel
import com.example.shopaine.ui.futures.singUp.SingUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {


    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app.database.db").build()
    }

    single<UserRepository> { UserRepositoryIMPL(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }
    single<CommentRepository> { CommentRepositoryImpl(get())}
    single<CartRepository> { CartRepositoryImpl(get(),get())}


    viewModel { ProfileViewModel( get() ) }
    viewModel { ProductViewModel( get(), get(), get()) }
    viewModel { SingUpViewModel(get()) }
    viewModel { SingInViewModel(get()) }
    viewModel { (isConnected: Boolean)-> MainViewModel(  get(),  get() , isConnected)}
    viewModel { CategoryViewModel(get()) }

}