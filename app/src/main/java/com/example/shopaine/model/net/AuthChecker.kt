package com.example.shopaine.model.net

import com.example.shopaine.model.data.LoginResponse
import com.example.shopaine.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// This class for error 401 (for update token)

class AuthChecker : Authenticator, KoinComponent {
    private val apiService: ApiService by inject()

    override fun authenticate(route: Route?, response: Response): Request? {

        if (TokenInMemory.token != null
            && !response.request.url.pathSegments.last().equals("refreshToken" , false) ) {

            val result = refreshToken()
            if (result){
                return response.request
            }


        }
        return null

    }

    private fun refreshToken(): Boolean {

        val requset: retrofit2.Response<LoginResponse> = apiService.refreshToken().execute()
        if (requset.body() != null) {
            if (requset.body()!!.success) {

                return true

            }
        }

        return false

    }
}