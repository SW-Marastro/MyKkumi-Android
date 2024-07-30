package com.swmarastro.mykkumi.data.interceptor

import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.swmarastro.mykkumi.domain.exception.ApiException
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val authTokenDataSource: AuthTokenDataStore,
) : Interceptor {
    private companion object {
        private const val AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            authTokenDataSource.getAccessToken()
        }

        if(accessToken.isNullOrEmpty()) {
            return chain.proceed(
                chain.request()
            )
        }

        return chain.proceed(
            chain.request().newBuilder()
                .header(AUTHORIZATION, accessToken)
                .build()
        )
    }
}