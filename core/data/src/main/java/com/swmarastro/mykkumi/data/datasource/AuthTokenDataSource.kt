package com.swmarastro.mykkumi.data.datasource

interface AuthTokenDataSource {
    fun saveAccessToken(accessToken: String)
    fun saveRefreshToken(refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
}